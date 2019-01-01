package com.froobworld.froobprotect.commands;

import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.froobpermissions.managers.GroupManager;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.data.User;
import com.froobworld.froobprotect.managers.AreaManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Area_RemmanagerCommand extends CommandExecutor {
    private UUIDManager uuidManager;
    private AreaManager areaManager;
    private GroupManager groupManager;
    private com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager;
    private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;

    public Area_RemmanagerCommand(UUIDManager uuidManager, AreaManager areaManager, GroupManager groupManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
        this.uuidManager = uuidManager;
        this.areaManager = areaManager;
        this.groupManager = groupManager;
        this.fpPlayerManager = fpPlayerManager;
        this.fbPlayerManager = fbPlayerManager;
    }

    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("/" + cl + " <area> remmanager <user>");
            return false;
        }
        Area area = areaManager.getArea(args[0]);
        if (area == null) {
            sender.sendMessage(FroobProtect.AREA_NOT_FOUND);
            return false;
        }
        if (sender instanceof Player && !sender.hasPermission(FroobProtect.PERM_EDIT_ALL_AREAS)) {
            Player player = (Player) sender;
            if (!area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
                sender.sendMessage(ChatColor.RED + "You must be an owner of that area to add managers.");
                return false;
            }
        }
        User user = User.deserialise_command(sender, args[2], groupManager, uuidManager);

        if (user == null) {
            return false;
        }

        if (!area.getManagers().contains(user)) {
            sender.sendMessage(ChatColor.RED + "That user is not a manager of this area.");
            return false;
        }

        area.removeManager(user);
        sender.sendMessage(ChatColor.YELLOW + "Removed manager " + user.toString() + " from the area.");
        return true;
    }

    @Override
    public String command() {

        return "area remmanager";
    }

    @Override
    public String perm() {

        return "froobprotect.area.remmanager";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 3) {
            Area area = areaManager.getArea(args[0]);
            if (area != null) {
                for (User user : area.getManagers()) {
                    completions.add(user.serialiseCommand());
                }
            }
        }

        return org.bukkit.util.StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));
    }

}
