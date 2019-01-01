package com.froobworld.froobprotect.commands;

import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Horse;
import com.froobworld.froobprotect.managers.HorseManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Horse_UnprotectCommand extends PlayerCommandExecutor {
    private HorseManager horseManager;
    private com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager;
    private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;

    public Horse_UnprotectCommand(HorseManager horseManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
        this.horseManager = horseManager;
        this.fpPlayerManager = fpPlayerManager;
        this.fbPlayerManager = fbPlayerManager;
    }


    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        Entity mount = player.getVehicle();
        if (mount == null) {
            player.sendMessage(ChatColor.RED + "You must be riding what you wish to unprotect.");
            return false;
        }
        Horse horse = horseManager.getHorse(mount.getUniqueId());
        if (horse == null) {
            player.sendMessage(ChatColor.RED + "This is not protected.");
            return false;
        }

        if (!player.hasPermission(FroobProtect.PERM_EDIT_ALL_AREAS) && !horse.getOwner().isUser(player, fpPlayerManager, fbPlayerManager)) {
            player.sendMessage(ChatColor.RED + "You are not the owner of this horse.");
            return false;
        }

        horseManager.removeHorse(horse);
        player.sendMessage(ChatColor.YELLOW + "Horse unprotected.");
        return true;
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {

        return new ArrayList<String>();
    }

    @Override
    public String command() {

        return "horse unprotect";
    }

    @Override
    public String perm() {

        return "froobprotect.horse";
    }

}
