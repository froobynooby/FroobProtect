package com.froobworld.froobprotect.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.froobprotect.data.Horse;
import com.froobworld.froobprotect.managers.HorseManager;

import net.md_5.bungee.api.ChatColor;

public class Horse_ProtectCommand extends PlayerCommandExecutor {
	private HorseManager horseManager;
	
	public Horse_ProtectCommand(HorseManager horseManager) {
		this.horseManager = horseManager;
	}
	
	
	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		Entity mount = player.getVehicle();
		if(mount == null) {
			player.sendMessage(ChatColor.RED + "You must be riding what you wish to protect.");
			return false;
		}
		if(!horseManager.isProtectable(mount)) {
			player.sendMessage(ChatColor.RED + "This cannot be protected.");
			return false;
		}
		Horse horse = horseManager.getHorse(mount.getUniqueId());
		if(horse != null) {
			player.sendMessage(ChatColor.RED + "This is already protected.");
			return false;
		}
		
		horseManager.addHorse(mount.getUniqueId(), player.getUniqueId());
		player.sendMessage(ChatColor.YELLOW + "Horse protected.");
		return true;
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		
		return new ArrayList<String>();
	}

	@Override
	public String command() {
		
		return "horse protect";
	}

	@Override
	public String perm() {
		
		return "froobprotect.horse";
	}

}
