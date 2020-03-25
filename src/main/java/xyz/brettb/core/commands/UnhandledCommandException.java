package xyz.brettb.core.commands;

import lombok.Getter;
import org.bukkit.command.CommandException;

public class UnhandledCommandException extends CommandException {
    @Getter private final Exception causingException;
    public UnhandledCommandException(Exception e) {
        super("Unhandled exception " + e.getMessage());
        this.causingException = e;
    }
}
