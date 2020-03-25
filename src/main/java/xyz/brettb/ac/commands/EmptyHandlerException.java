package xyz.brettb.ac.commands;

import org.bukkit.command.CommandException;

public final class EmptyHandlerException extends CommandException implements FriendlyException {
    public EmptyHandlerException() {
        super("There was no handler found for this command!");
    }

    @Override
    public String getFriendlyMessage(CorePluginCommand command) {
        return getMessage();
    }
}