package xyz.brettb.ac.commands;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.javatuples.Pair;
import xyz.brettb.ac.plugin.CorePlugin;
import xyz.brettb.ac.util.RunnableShorthand;

import java.util.*;

public abstract class CorePluginCommand implements CommandExecutor, TabCompleter {

    // name, Pair<Command,Alias?>
    private final Map<String, Pair<CorePluginCommand,Boolean>> subCommands = new HashMap<>();

    @Getter private final String name;
    @Setter(AccessLevel.PROTECTED) @Getter private CorePluginCommand superCommand = null;
    @Getter private CorePluginCommandMeta commandMeta = getClass().isAnnotationPresent(CorePluginCommandMeta.class)
            ? getClass().getAnnotation(CorePluginCommandMeta.class) : null;
    @Getter private CorePluginCommandPermission commandPermission = getClass().isAnnotationPresent(CorePluginCommandPermission.class)
            ? getClass().getAnnotation(CorePluginCommandPermission.class) : null;
    @Setter private CorePlugin plugin;

    protected CorePluginCommand(String name) {
        this.name = name;
    }

    protected CorePluginCommand(final String name, CorePluginCommand... subCommands) {
        this.name = name;
        registerSubCommand(subCommands);
    }

    public final void registerSubCommand(CorePluginCommand... subCommands) {
        for (CorePluginCommand subCommand : subCommands) {
            if (subCommand.getSuperCommand() != null) throw new IllegalArgumentException("The command you attempted to register has already been assigned a super command!");
            this.subCommands.put(subCommand.getName(), Pair.with(subCommand,false));
            CorePluginCommandMeta meta = subCommand.getCommandMeta();
            if (meta != null && !Arrays.equals(meta.aliases(), new String[]{}))
                for (String a : meta.aliases())
                    this.subCommands.put(a, Pair.with(subCommand,true));
            subCommand.setSuperCommand(this);
        }
        regenerateHelpCommand();
    }

    public void unregisterSubCommand(CorePluginCommand... subCommands) {
        for (CorePluginCommand subCommand : subCommands) {
            this.subCommands.remove(subCommand.getName());
            subCommand.setSuperCommand(null);
        }
        regenerateHelpCommand();
    }

    public final ImmutableList<CorePluginCommand> getSubCommands() {
        ArrayList<CorePluginCommand> subC = new ArrayList<>();
        for (Map.Entry<String, Pair<CorePluginCommand,Boolean>> entry : this.subCommands.entrySet()) {
            if (!entry.getValue().getValue1()) {
                subC.add(entry.getValue().getValue0());
            }
        }
        return ImmutableList.copyOf(subC);
    }

    private void regenerateHelpCommand() {
        if (!shouldGenerateHelpCommand()) return;
        final Map<String, Pair<CorePluginCommand,Boolean>> subCommands = this.subCommands;
        final TreeMap<String, CorePluginCommand> sortedSubCommands = new TreeMap<>();
        for (Map.Entry<String, Pair<CorePluginCommand,Boolean>> entry : this.subCommands.entrySet()) {
            if (!entry.getValue().getValue1()) {
                sortedSubCommands.put(entry.getKey(),entry.getValue().getValue0());
            }
        }
        final CorePluginCommand superHelpCommand = this;
        this.subCommands.put("help", Pair.with(new CorePluginCommand("help") {
            @Override
            public void handleCommandUnspecific(CommandSender sender, String[] args) {
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.GOLD).append(" Help for ").append(ChatColor.GREEN).append("/")
                        .append(superHelpCommand.getFormattedName()).append("\n");
                for (Map.Entry<String, CorePluginCommand> commandEntry : sortedSubCommands.entrySet()) {
                    builder.append(ChatColor.DARK_AQUA).append("> ").append(ChatColor.GREEN).append("/")
                            .append(commandEntry.getValue().getFormattedName()).append("\n");
                }
                String s = builder.toString();
                sender.sendMessage(plugin.getChatPrefix() + s);
            }
        }, false));
    }

    public final boolean onCommand(final CommandSender sender, Command command, String s, final String[] args) {
        try {
            CorePluginCommand subCommand = null;
            if (commandPermission != null) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (!player.hasPermission(commandPermission.value())
                            && !(player.isOp() && commandPermission.isOpExempt())
                            && !(Arrays.asList(commandPermission.userOverrides()).contains(player.getUniqueId().toString())))
                        throw new PermissionException("You do not have permission for this command!");
                } else if (!sender.hasPermission(commandPermission.value())
                        && !(sender.isOp() && commandPermission.isOpExempt()))
                    throw new PermissionException("You do not have permission for this command!");
            }

            if (isUsingSubCommandsOnly()) {
                if (args.length < 1)
                    throw new ArgumentRequirementException("You must specify a sub-command for this command!");
                if ((subCommand = getSubCommandFor(args[0])) == null)
                    throw new ArgumentRequirementException("The sub-command you have specified is invalid!");
            }
            if (subCommand == null && args.length > 0) subCommand = getSubCommandFor(args[0]);
            if (subCommand != null) {
                String[] choppedArgs = args.length < 2 ? new String[0] : Arrays.copyOfRange(args, 1, args.length);
                preSubCommandDispatch(sender, choppedArgs, subCommand);
                subCommand.onCommand(sender, command, s, choppedArgs);
                try {
                    handlePostSubCommand(sender, args);
                } catch (EmptyHandlerException ignored) {}
                return true;
            }


            if (getClass().isAnnotationPresent(AsyncCommand.class))
                RunnableShorthand.forPlugin(plugin).async().with(() -> {
                    try {
                        actualDispatch(sender, args);
                    } catch (CommandException e) {
                        handleCommandException(e, args, sender);
                    } catch (Exception e) {
                        handleCommandException(new UnhandledCommandException(e), args, sender);
                    }
                }).go();
            else
                actualDispatch(sender, args);
        }
        catch (CommandException ex) {
            handleCommandException(ex, args, sender);
        } catch (Exception e) {
            handleCommandException(new UnhandledCommandException(e), args, sender);
        }
        return true;
    }

    private void actualDispatch(CommandSender sender, String[] args) throws CommandException {
        try {
            if (sender instanceof Player) handleCommand(((Player) sender), args);
            else if (sender instanceof ConsoleCommandSender) handleCommand((ConsoleCommandSender)sender, args);
            else if (sender instanceof BlockCommandSender)  handleCommand((BlockCommandSender)sender, args);
        } catch (EmptyHandlerException e) {
            handleCommandUnspecific(sender, args);
        }
    }

    public final List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (getClass().isAnnotationPresent(CorePluginCommandPermission.class)) {
            CorePluginCommandPermission annotation = getClass().getAnnotation(CorePluginCommandPermission.class);
            if (!sender.hasPermission(annotation.value()) && !(sender.isOp() && annotation.isOpExempt())) return Collections.emptyList();
        }
        if (args.length > 1) {
            CorePluginCommand possibleHigherLevelSubCommand;
            if ((possibleHigherLevelSubCommand = getSubCommandFor(args[0])) != null)
                return possibleHigherLevelSubCommand.onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
        } else if (args.length == 1) {
            List<String> subCommandsForPartial = getSubCommandsForPartial(args[0]);
            if (subCommandsForPartial.size() != 0) {
                subCommandsForPartial.addAll(handleTabComplete(sender, command, alias, args));
                return subCommandsForPartial;
            }
        }
        return handleTabComplete(sender, command, alias, args);
    }

    protected void handleCommandException(CommandException ex, String[] args, CommandSender sender) {
        //Get the friendly message if supported
        if (ex instanceof FriendlyException) sender.sendMessage(((FriendlyException) ex).getFriendlyMessage(this));
        else sender.sendMessage(ChatColor.RED + ex.getClass().getSimpleName() + ": " + ex.getMessage() + "!");
        if (ex instanceof UnhandledCommandException) ((UnhandledCommandException) ex).getCausingException().printStackTrace();
    }

    protected void preSubCommandDispatch(CommandSender sender, String[] args, CorePluginCommand subCommand) {}

    public final CorePluginCommand getSubCommandFor(String s) {
        if (subCommands.containsKey(s)) return subCommands.get(s).getValue0();
        for (String s1 : subCommands.keySet()) {
            if (s1.equalsIgnoreCase(s)) return subCommands.get(s1).getValue0();
        }
        return null;
    }

    public List<String> getSubCommandsForPartial(String s) {
        List<String> commands = new ArrayList<>();
        CorePluginCommand subCommand;
        if ((subCommand = getSubCommandFor(s)) != null) {
            commands.add(subCommand.getName());
            return commands;
        }
        String s2 = s.toUpperCase();
        for (String s1 : subCommands.keySet()) {
            if (s1.toUpperCase().startsWith(s2))
                commands.add(s1);
        }
        return commands;
    }

    public CorePlugin getPlugin() {
        if (getSuperCommand() != null)
            return getSuperCommand().getPlugin();
        return plugin;
    }

    protected void handleCommand(Player player, String[] args) throws CommandException {throw new EmptyHandlerException();}
    protected void handleCommand(ConsoleCommandSender commandSender, String[] args) throws CommandException {throw new EmptyHandlerException();}
    protected void handleCommand(BlockCommandSender commandSender, String[] args) throws CommandException {throw new EmptyHandlerException();}

    protected void handleCommandUnspecific(CommandSender sender, String[] args) throws CommandException {throw new EmptyHandlerException();}
    protected void handlePostSubCommand(CommandSender sender, String[] args) throws CommandException {throw new EmptyHandlerException();}

    protected List<String> handleTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (isUsingSubCommandsOnly() || subCommands.size() > 0) return Collections.emptyList();
        List<String> ss = new ArrayList<>();
        String arg = args.length > 0 ? args[args.length - 1].toLowerCase() : "";
        for (Player player : Bukkit.getOnlinePlayers()) {
            String name1 = player.getName();
            if (name1.toLowerCase().startsWith(arg)) ss.add(name1);
        }
        return ss;
    }

    protected boolean isUsingSubCommandsOnly() {return false;}
    protected boolean shouldGenerateHelpCommand() {return true;}

    protected String getFormattedName() {return superCommand == null ? name : superCommand.getFormattedName() + " " + name;}


    @Override
    public String toString() {
        return "CorePluginCommand -> " + getFormattedName();
    }

}
