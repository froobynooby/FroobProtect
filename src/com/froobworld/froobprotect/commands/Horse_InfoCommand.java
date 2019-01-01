package com.froobworld.froobprotect.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.froobprotect.managers.HorseManager;

import net.md_5.bungee.api.ChatColor;

public class Horse_InfoCommand extends PlayerCommandExecutor {
	private HorseManager horseManager;
	
	public Horse_InfoCommand(HorseManager horseManager) {
		this.horseManager = horseManager;
	}
	
	
	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		horseManager.horseInspect.put(player, horseManager.horseInspect.containsKey(player) ? !horseManager.horseInspect.get(player):true);
		
		if(horseManager.horseInspect.get(player)) {
			player.sendMessage(ChatColor.YELLOW + "Horse info toggled. Hit the mount you want to check. Don't worry, it won't hurt it.");
		}else {
			player.sendMessage(ChatColor.YELLOW + "Horse info detoggled.");
		}
		return true;
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		
		return new ArrayList<String>();
	}

	@Override
	public String command() {
		
		return "horse info";
	}

	@Override
	public String perm() {
		
		return "froobprotect.horse";
	}

}
