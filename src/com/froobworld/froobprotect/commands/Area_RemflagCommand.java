package com.froobworld.froobprotect.commands;

import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.data.flag.Flag;
import com.froobworld.froobprotect.managers.AreaManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Area_RemflagCommand extends CommandExecutor {
    private AreaManager areaManager;
    private com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager;
    private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;

    public Area_RemflagCommand(AreaManager areaManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
        this.areaManager = areaManager;
        this.fpPlayerManager = fpPlayerManager;
        this.fbPlayerManager = fbPlayerManager;
    }

    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("/" + cl + " <area> remflag <flag>");
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
                sender.sendMessage(ChatColor.RED + "You must be an owner of that area to remove flags.");
                return false;
            }
        }
        Flag flag = areaManager.getFlag(args[2]);
        if (flag == null) {
            sender.sendMessage(ChatColor.RED + "That is not a valid flag. Possible flags are: " + ChatColor.WHITE + PageUtils.toString(areaManager.getFlags()));
            return false;
        }
        if (!sender.hasPermission("froobprotect.flag." + flag.name())) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use that flag.");
            return false;
        }
        if (!area.getFlags().contains(flag)) {
            sender.sendMessage(ChatColor.RED + "That area does not have that flag.");
            return false;
        }

        area.removeFlag(flag);
        sender.sendMessage(ChatColor.YELLOW + "Flag removed.");
        return true;
    }

    @Override
    public String command() {

        return "area remflag";
    }

    @Override
    public String perm() {

        return "froobprotect.area.remflag";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 3) {
            Area area = areaManager.getArea(args[0]);
            if (area != null) {
                for (Flag flag : area.getFlags()) {
                    if (!sender.hasPermission("froobprotect.flag." + flag.name())) {
                        continue;
                    }
                    completions.add(flag.name());
                }
            }
        }

        return org.bukkit.util.StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));
    }

}
