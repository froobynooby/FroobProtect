package com.froobworld.froobprotect.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.froobprotect.managers.AreaManager;

import net.md_5.bungee.api.ChatColor;

public class Position1Command extends PlayerCommandExecutor {
	private AreaManager areaManager;
	
	public Position1Command(AreaManager areaManager) {
		this.areaManager = areaManager;
	}

	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		areaManager.location1.put(player, player.getLocation());
		player.sendMessage(ChatColor.YELLOW + "Position one set. (" + player.getLocation().getBlockX() + "," + player.getLocation().getBlockY() + "," + player.getLocation().getBlockZ() + ")");
		return true;
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {

		return new ArrayList<String>();
	}

	@Override
	public String command() {
		
		return "position1";
	}

	@Override
	public String perm() {
		
		return "froobprotect.position";
	}

}
