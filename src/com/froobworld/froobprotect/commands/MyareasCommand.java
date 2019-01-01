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

public class MyareasCommand extends PlayerCommandExecutor {
    private AreaManager areaManager;
    private com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager;
    private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;

    public MyareasCommand(AreaManager areaManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
        this.areaManager = areaManager;
        this.fpPlayerManager = fpPlayerManager;
        this.fbPlayerManager = fbPlayerManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        ArrayList<Area> areas = (ArrayList<Area>) areaManager.getAreas().stream().filter(a -> a.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))).collect(Collectors.toList());
        ArrayList<String> areasNames = PageUtils.toStringList(areas);
        areasNames.sort(String::compareToIgnoreCase);

        if (areasNames.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "You don't own any areas.");
            return true;
        }

        player.sendMessage(ChatColor.YELLOW + "You own " + (areasNames.size() == 1 ? "one area." : (areasNames.size() + " areas.")));
        player.sendMessage(PageUtils.toString(areasNames));
        return true;
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {

        return new ArrayList<String>();
    }

    @Override
    public String command() {

        return "myareas";
    }

    @Override
    public String perm() {

        return "froobprotect.myareas";
    }

}
