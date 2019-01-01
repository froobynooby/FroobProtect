package com.froobworld.froobprotect.commands;

import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.managers.AreaManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Area_AcceptCommand extends CommandExecutor {
    private AreaManager areaManager;

    public Area_AcceptCommand(AreaManager areaManager) {
        this.areaManager = areaManager;
    }

    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        Area area = areaManager.getArea(args[0]);
        if (area == null) {
            sender.sendMessage(FroobProtect.AREA_NOT_FOUND);
            return false;
        }
        if (!area.isRequest()) {
            sender.sendMessage(ChatColor.RED + "That area is not a request.");
            return false;
        }

        area.setRequest(false);
        sender.sendMessage(ChatColor.YELLOW + "Area accepted.");
        return true;
    }

    @Override
    public String command() {

        return "area accept";
    }

    @Override
    public String perm() {

        return "froobprotect.area.accept";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();

        return completions;
    }

}
