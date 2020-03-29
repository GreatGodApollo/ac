package xyz.brettb.ac.commands;

import lombok.Getter;
import org.bukkit.command.CommandSender;

public class CommandContext {
    @Getter private final CommandSender sender;
    @Getter private final String alias;
    @Getter private final String[] args;

    private CommandContext(CommandSender sender, String alias, String[] args) {
        this.sender = sender;
        this.alias = alias;
        this.args = args;
    }

    public static CommandContext with(CommandSender sender, String alias, String[] args) {
        return new CommandContext(sender, alias, args);
    }

    public void reply(String... message) {
        sender.sendMessage(message);
    }

}
