package com.froobworld.froobprotect.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.froobworld.froobbasics.data.Playerdata;
import com.froobworld.froobbasics.managers.PlayerManager;
import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.TeleportUtils;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.managers.AreaManager;

import net.md_5.bungee.api.ChatColor;

public class Area_TeleportCommand extends PlayerCommandExecutor {
	private AreaManager areaManager;
	private PlayerManager playerManager;
	
	public Area_TeleportCommand(AreaManager areaManager, PlayerManager playerManager) {
		this.areaManager = areaManager;
		this.playerManager = playerManager;
	}

	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		Area area = areaManager.getArea(args[0]);
		if(area == null) {
			player.sendMessage(FroobProtect.AREA_NOT_FOUND);
			return false;
		}
		if(!area.isRequest()) {
			player.sendMessage(ChatColor.RED + "You can only teleport to area requests.");
			return false;
		}
		
		if(playerManager != null) {
			Playerdata data = playerManager.getPlayerdata(player);
			data.setBackLocation(player.getLocation(), false);
		}
		
		player.teleport(TeleportUtils.findSafeTeleportLocation(area.getLocation1()));
		player.sendMessage(ChatColor.YELLOW + "Teleported to requested area " + area.getNameDeep() + ".");
		return true;
	}

	@Override
	public List<String> tabCompletions(Player arg0, Command arg1, String arg2, String[] arg3) {
		
		return new ArrayList<String>();
	}

	@Override
	public String command() {
		
		return "area teleport";
	}

	@Override
	public String perm() {
		
		return "froobprotect.area.teleport";
	}

}
