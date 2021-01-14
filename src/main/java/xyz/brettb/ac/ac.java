package xyz.brettb.ac;

import org.bukkit.ChatColor;
import xyz.brettb.ac.commands.CommandContext;
import xyz.brettb.ac.commands.CorePluginCommand;
import xyz.brettb.ac.commands.CorePluginCommandMeta;
import xyz.brettb.ac.gui.CoreGUIListener;
import xyz.brettb.ac.plugin.CorePlugin;
import xyz.brettb.ac.plugin.CorePluginMeta;

@CorePluginMeta(chatPrefix = "&l&8[&bAC&8]&r")
public class ac extends CorePlugin {

    @Override
    public void onModuleEnable() {
        registerListener(new CoreGUIListener(this));
        registerCommand(new ApolloCoreCommand());
    }

    @CorePluginCommandMeta(description = "ApolloCore", aliases = {"ac"})
    private static class ApolloCoreCommand extends CorePluginCommand {
        public ApolloCoreCommand() {
            super("apollocore");
        }

        @Override
        public void handleCommandUnspecific(CommandContext ctx) {
            ctx.reply(ChatColor.translateAlternateColorCodes('&', getPlugin().getChatPrefix()) +
                    ChatColor.DARK_AQUA + " This server is using ApolloCore" +
                    ChatColor.GREEN + " v" + getPlugin().getDescription().getVersion());
        }

    }

}
