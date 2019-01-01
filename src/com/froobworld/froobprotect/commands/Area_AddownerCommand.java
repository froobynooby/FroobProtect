package com.froobworld.froobprotect.commands;

import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.CommandUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.froobpermissions.data.Group;
import com.froobworld.froobpermissions.managers.GroupManager;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.data.User;
import com.froobworld.froobprotect.data.User.UserType;
import com.froobworld.froobprotect.managers.AreaManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Area_AddownerCommand extends CommandExecutor {
    private UUIDManager uuidManager;
    private AreaManager areaManager;
    private GroupManager groupManager;
    private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;

    public Area_AddownerCommand(UUIDManager uuidManager, AreaManager areaManager, GroupManager groupManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
        this.uuidManager = uuidManager;
        this.areaManager = areaManager;
        this.groupManager = groupManager;
        this.fbPlayerManager = fbPlayerManager;
    }

    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("/" + cl + " <area> addowner <user>");
            return false;
        }
        Area area = areaManager.getArea(args[0]);
        if (area == null) {
            sender.sendMessage(FroobProtect.AREA_NOT_FOUND);
            return false;
        }
        User user = User.deserialise_command(sender, args[2], groupManager, uuidManager);

        if (user == null) {
            return false;
        }

        if (user.getType() == UserType.UNKNOWN) {
            sender.sendMessage(ChatColor.RED + "A user by that name could not be found.");
            return false;
        }

        if (area.getOwners().contains(user)) {
            sender.sendMessage(ChatColor.RED + "That user is already an owner of the area.");
            return false;
        }

        area.addOwner(user);
        sender.sendMessage(ChatColor.YELLOW + "Added owner " + user.toString() + " to the area.");
        return true;
    }

    @Override
    public String command() {

        return "area addowner";
    }

    @Override
    public String perm() {

        return "froobprotect.area.addowner";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 3) {
            completions.addAll(CommandUtils.tabCompletePlayerList(args[2], true, true, uuidManager));
            completions.add("friends:");
            completions.add("group:");
            String[] split = args[2].split(":", -1);
            if (split.length > 1) {
                if (fbPlayerManager != null) {
                    if (split[0].equalsIgnoreCase("friends")) {
                        for (String string : CommandUtils.tabCompletePlayerList(split[1], true, true, uuidManager)) {
                            completions.add("friends:" + string);
                        }
                    }
                }
                if (groupManager != null) {
                    if (split[0].equalsIgnoreCase("group")) {
                        for (Group group : groupManager.getGroups()) {
                            completions.add("group:" + group.getName());
                        }
                    }
                }
            }
        }

        return org.bukkit.util.StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));
    }

}
