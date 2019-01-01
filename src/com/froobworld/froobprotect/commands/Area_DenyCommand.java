package com.froobworld.froobprotect.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.managers.AreaManager;

import net.md_5.bungee.api.ChatColor;

public class Area_DenyCommand extends CommandExecutor {
	private AreaManager areaManager;

	public Area_DenyCommand(AreaManager areaManager) {
		this.areaManager = areaManager;
	}
	
	@Override
	public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
		Area area = areaManager.getArea(args[0]);
		if(area == null) {
			sender.sendMessage(FroobProtect.AREA_NOT_FOUND);
			return false;
		}
		if(!area.isRequest()) {
			sender.sendMessage(ChatColor.RED + "That area is not a request.");
			return false;
		}
		
		area.delete();
		sender.sendMessage(ChatColor.YELLOW + "Area denied.");
		return true;
	}
	
	@Override
	public String command() {
		
		return "area deny";
	}

	@Override
	public String perm() {
		
		return "froobprotect.area.deny";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		
		return completions;
	}

}
