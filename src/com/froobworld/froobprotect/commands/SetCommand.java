package com.froobworld.froobprotect.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.managers.AreaManager;

import net.md_5.bungee.api.ChatColor;

public class SetCommand extends PlayerCommandExecutor {
	private AreaManager areaManager;
	
	public SetCommand(AreaManager areaManager) {
		this.areaManager = areaManager;
	}

	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		if(args.length == 0) {
			player.sendMessage("/" + cl + " <area>");
			return false;
		}
		Location location1 = areaManager.location1.get(player);
		Location location2 = areaManager.location2.get(player);
		if(location1 == null || location2 == null) {
			player.sendMessage(FroobProtect.REQUIRE_TWO_CORNERS);
			return false;
		}
		if(location1.getWorld() != location2.getWorld()) {
			player.sendMessage(FroobProtect.REQUIRE_CORNERS_SAME_WORLD);
			return false;
		}
		if(areaManager.getArea(args[0]) != null) {
			player.sendMessage(FroobProtect.AREA_ALREADY_EXISTS);
			return false;
		}
		if(args[0].equalsIgnoreCase("here")) {
			player.sendMessage(FroobProtect.AREA_NAME_NOT_VALID);
			return false;
		}
		
		if(areaManager.createArea(player.getUniqueId(), args[0], false, location1, location2)) {
			player.sendMessage(ChatColor.YELLOW + "Area set.");
		}else {
			player.sendMessage(FroobProtect.PARENT_NOT_EXIST);
		}
		return true;
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		
		return new ArrayList<String>();
	}

	@Override
	public String command() {
		
		return "set";
	}

	@Override
	public String perm() {
		
		return "froobprotect.set";
	}

}
