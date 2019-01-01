package com.froobworld.froobprotect.commands;

import com.froobworld.frooblib.command.ContainerCommandExecutor;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.froobpermissions.managers.GroupManager;
import com.froobworld.froobprotect.managers.HorseManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class HorseCommand extends ContainerCommandExecutor {

    public HorseCommand(HorseManager horseManager, UUIDManager uuidManager, GroupManager groupManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
        addSubCommand(new Horse_ProtectCommand(horseManager), 0, "protect", "/horse protect");
        addSubCommand(new Horse_UnprotectCommand(horseManager, fpPlayerManager, fbPlayerManager), 0, "unprotect", "/horse unprotect");
        addSubCommand(new Horse_AdduserCommand(horseManager, groupManager, uuidManager, fpPlayerManager, fbPlayerManager), 0, "adduser", "/horse adduser <user>");
        addSubCommand(new Horse_RemuserCommand(horseManager, groupManager, uuidManager, fpPlayerManager, fbPlayerManager), 0, "remuser", "/horse remuser <user>");
        addSubCommand(new Horse_InfoCommand(horseManager), 0, "info", "/horse info");
    }


    @Override
    public String command() {

        return "horse";
    }

    @Override
    public String perm() {

        return "froobprotect.horse";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        for (SubCommand subCommand : getSubCommands()) {
            if (!sender.hasPermission(subCommand.getExecutor().perm())) {
                continue;
            }
            if (args.length == subCommand.getArgIndex() + 1) {
                completions.add(subCommand.getArg());
            }
        }
        if (args.length > 1) {
            for (SubCommand subCommand : getSubCommands()) {
                if (!sender.hasPermission(subCommand.getExecutor().perm())) {
                    continue;
                }
                if (args.length > subCommand.getArgIndex() + 1) {
                    if (subCommand.getArg().equalsIgnoreCase(args[subCommand.getArgIndex()])) {
                        return subCommand.getExecutor().tabCompletions(sender, command, cl, args);
                    }
                }
            }
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));
    }

}
