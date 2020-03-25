package xyz.brettb.ac.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;

public final class ArgumentRequirementException extends CommandException implements FriendlyException {
    public ArgumentRequirementException(String message) {
        super(message);
    }

    @Override
    public String getFriendlyMessage(CorePluginCommand command) {
        return ChatColor.RED + this.getMessage();
    }
}