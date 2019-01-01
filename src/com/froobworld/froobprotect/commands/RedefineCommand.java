package com.froobworld.froobprotect.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.managers.AreaManager;
import com.froobworld.froobprotect.utils.Utils;

import net.md_5.bungee.api.ChatColor;

public class RedefineCommand extends PlayerCommandExecutor {
	private AreaManager areaManager;
	
	public RedefineCommand(AreaManager areaManager) {
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
		Area area = areaManager.getArea(args[0]);
		if(area == null) {
			player.sendMessage(FroobProtect.AREA_NOT_FOUND);
			return false;
		}
		
		area.setLocation1(location1);
		area.setLocation2(location2);
		player.sendMessage(ChatColor.YELLOW + "Area redefined.");
		return true;
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length == 1) {
			completions.addAll(Utils.getAreaNamesTabCompletion(args[0], areaManager));
		}
		
		return completions;
	}

	@Override
	public String command() {
		
		return "redefine";
	}

	@Override
	public String perm() {
		
		return "froobprotect.redefine";
	}

}
