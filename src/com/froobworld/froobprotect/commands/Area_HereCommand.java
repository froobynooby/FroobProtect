package com.froobworld.froobprotect.commands;

import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.managers.AreaManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Area_HereCommand extends PlayerCommandExecutor {
    private AreaManager areaManager;

    public Area_HereCommand(AreaManager areaManager) {
        this.areaManager = areaManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        ArrayList<Area> areas = areaManager.getTopAreas(player.getLocation());

        if (areas.size() == 0) {
            player.sendMessage(ChatColor.YELLOW + "There are no areas here.");
            return true;
        }

        player.sendMessage(ChatColor.YELLOW + "There " + (areas.size() == 1 ? ("is one area ") : ("are " + areas.size() + " areas ")) + "here:");
        player.sendMessage(PageUtils.toString((ArrayList<String>) areas.stream().map(a -> a.getNameDeep()).collect(Collectors.toList())));
        return true;
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {

        return new ArrayList<String>();
    }

    @Override
    public String command() {

        return "area here";
    }

    @Override
    public String perm() {

        return "froobprotect.area.here";
    }

}
