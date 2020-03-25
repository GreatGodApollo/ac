package xyz.brettb.core.plugin;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.brettb.core.commands.CorePluginCommand;
import xyz.brettb.core.commands.CorePluginCommandMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class CorePlugin extends JavaPlugin {

    // Abstracted methods
    protected void onModuleEnable() throws Exception {}
    protected void onModuleDisable() throws Exception {}
    private String chatPrefix;

    @Override
    public final void onEnable() {
        try {
            onModuleEnable();
        } catch (Throwable ex) {
            getLogger().severe("Unable to enable the plugin!");
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public final void onDisable() {
        try {
            onModuleDisable();
        } catch (Throwable ex) {
            getLogger().severe("Unable to properly disable this plugin!");
            ex.printStackTrace();
        }
    }

    public final <T extends CorePluginCommand> T registerCommand(@NonNull T command) {
        PluginCommand pluginCommand = getCommand(command.getName());
        if (pluginCommand == null) {
            try {
                Constructor commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
                commandConstructor.setAccessible(true);
                pluginCommand = (PluginCommand) commandConstructor.newInstance(command.getName(), this);
            } catch (Exception e) {
                throw new IllegalStateException("Could not register command " + command.getName());
            }

            CommandMap commandMap;
            try {
                PluginManager pluginManager = Bukkit.getPluginManager();
                Field commandMapField = pluginManager.getClass().getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                commandMap = (CommandMap) commandMapField.get(pluginManager);
            } catch (Exception e) {
                throw new IllegalStateException("Could not register command " + command.getName());
            }

            CorePluginCommandMeta annotation = command.getClass().getAnnotation(CorePluginCommandMeta.class);
            if (annotation != null) {
                pluginCommand.setAliases(Arrays.asList(annotation.aliases()));
                pluginCommand.setDescription(annotation.description());
                pluginCommand.setUsage(annotation.usage());
            }
            commandMap.register(this.getDescription().getName(), pluginCommand);
        }
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);

        if (command.getPlugin() == null)
            command.setPlugin(this);
        else
            command.setPlugin(null);

        return command;
    }

    public final void registerCommand(CorePluginCommand... commands) {
        for (CorePluginCommand command : commands) registerCommand(command);
    }

    public <T extends Listener> T registerListener(@NonNull T listener) {
        getServer().getPluginManager().registerEvents(listener, this);
        return listener;
    }

    public String getChatPrefix() {
        CorePluginMeta annotation = this.getClass().getAnnotation(CorePluginMeta.class);
        if (annotation != null) {
            return annotation.chatPrefixColor() + annotation.chatPrefix();
        }
        return ChatColor.AQUA + "[PL]";
    }
}
