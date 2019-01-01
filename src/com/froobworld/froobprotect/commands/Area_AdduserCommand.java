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
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Area_AdduserCommand extends CommandExecutor {
    private UUIDManager uuidManager;
    private AreaManager areaManager;
    private GroupManager groupManager;
    private com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager;
    private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;

    public Area_AdduserCommand(UUIDManager uuidManager, AreaManager areaManager, GroupManager groupManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
        this.uuidManager = uuidManager;
        this.areaManager = areaManager;
        this.groupManager = groupManager;
        this.fpPlayerManager = fpPlayerManager;
        this.fbPlayerManager = fbPlayerManager;
    }

    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("/" + cl + " <area> adduser <user>");
            return false;
        }
        Area area = areaManager.getArea(args[0]);
        if (area == null) {
            sender.sendMessage(FroobProtect.AREA_NOT_FOUND);
            return false;
        }
        if (sender instanceof Player && !sender.hasPermission(FroobProtect.PERM_EDIT_ALL_AREAS)) {
            Player player = (Player) sender;
            if (!area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getManagers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
                sender.sendMessage(ChatColor.RED + "You must be an owner or manager of that area to add users.");
                return false;
            }
        }
        User user = User.deserialise_command(sender, args[2], groupManager, uuidManager);

        if (user == null) {
            return false;
        }

        if (user.getType() == UserType.UNKNOWN) {
            sender.sendMessage(ChatColor.RED + "A user by that name could not be found.");
            return false;
        }

        if (area.getUsers().contains(user)) {
            sender.sendMessage(ChatColor.RED + "That user is already a user of the area.");
            return false;
        }

        area.addUser(user);
        sender.sendMessage(ChatColor.YELLOW + "Added user " + user.toString() + " to the area.");
        return true;
    }

    @Override
    public String command() {

        return "area adduser";
    }

    @Override
    public String perm() {

        return "froobprotect.area.adduser";
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
