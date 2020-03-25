package xyz.brettb.ac;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import xyz.brettb.ac.commands.CorePluginCommand;
import xyz.brettb.ac.commands.CorePluginCommandMeta;
import xyz.brettb.ac.plugin.CorePlugin;

public class ac extends CorePlugin {

    @Override
    public void onModuleEnable() {
        registerCommand(new ApolloCoreCommand());
    }

    @CorePluginCommandMeta(description = "ApolloCore", aliases = {"ac"})
    private static class ApolloCoreCommand extends CorePluginCommand {

        public ApolloCoreCommand() {
            super("apollocore");
        }

        @Override
        public void handleCommandUnspecific(CommandSender sender, String[] args) {
            sender.sendMessage(ChatColor.AQUA + "[AC] " + ChatColor.DARK_AQUA + "This server is using ApolloCore" +
                    ChatColor.GREEN + " v" + getPlugin().getDescription().getVersion());
        }

    }

}
