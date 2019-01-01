package com.froobworld.froobprotect.commands;

import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.managers.AreaManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Area_ListrequestsCommand extends CommandExecutor {
    private AreaManager areaManager;

    public Area_ListrequestsCommand(AreaManager areaManager) {
        this.areaManager = areaManager;
    }

    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<Area> requests = (ArrayList<Area>) areaManager.getAreas().stream().filter(a -> a.isRequest()).collect(Collectors.toList());

        if (requests.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "There are no requested areas.");
            return true;
        }
        sender.sendMessage(ChatColor.YELLOW + "There " + (requests.size() == 1 ? ("is one area request") : ("are " + requests.size() + " area requests")) + ":");
        ArrayList<String> areas = PageUtils.toStringList((ArrayList<Area>) areaManager.getAreas().stream().filter(a -> a.isRequest()).collect(Collectors.toList()));
        areas.sort(String::compareToIgnoreCase);
        sender.sendMessage(PageUtils.toString(areas));
        return false;
    }

    @Override
    public String command() {

        return "area listrequests";
    }

    @Override
    public String perm() {

        return "froobprotect.area.listrequests";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {

        return new ArrayList<String>();
    }

}
