package xyz.brettb.ac.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;

public class PermissionException extends CommandException implements FriendlyException {
    public PermissionException(String message) {
        super(message);
    }

    @Override
    public String getFriendlyMessage(CorePluginCommand command) {
        return ChatColor.RED + "You don't have permissions for this!";
    }
}
