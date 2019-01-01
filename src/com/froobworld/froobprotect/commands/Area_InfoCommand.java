package com.froobworld.froobprotect.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.managers.AreaManager;

import net.md_5.bungee.api.ChatColor;

public class Area_InfoCommand extends CommandExecutor {
	private AreaManager areaManager;
	
	public Area_InfoCommand(AreaManager areaManager) {
		this.areaManager = areaManager;
	}

	@Override
	public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
		Area area = areaManager.getArea(args[0]);
		if(area == null) {
			sender.sendMessage(FroobProtect.AREA_NOT_FOUND);
			return false;
		}
		
		sender.sendMessage(ChatColor.YELLOW + "Information for " + (area.isRequest() ? "requested ":"") + "area " + area.getName() + ":");
		sender.sendMessage(ChatColor.YELLOW + "World: " + ChatColor.WHITE + area.getLocation1().getWorld().getName());
		sender.sendMessage(ChatColor.YELLOW + "Bounds: " + ChatColor.WHITE + "(" + area.getLocation1().getBlockX() + "," + area.getLocation1().getBlockY() + "," + area.getLocation1().getBlockZ() + "), (" + area.getLocation2().getBlockX() + "," + area.getLocation2().getBlockY() + "," + area.getLocation2().getBlockZ() + ")");
		
		sender.sendMessage(ChatColor.YELLOW + "Owners: " + ChatColor.WHITE + PageUtils.toString(area.getOwners()));
		sender.sendMessage(ChatColor.YELLOW + "Managers: " + ChatColor.WHITE + PageUtils.toString(area.getManagers()));
		sender.sendMessage(ChatColor.YELLOW + "Users: " + ChatColor.WHITE + PageUtils.toString(area.getUsers()));
		
		sender.sendMessage(ChatColor.YELLOW + "Flags: " + ChatColor.WHITE + PageUtils.toString(area.getFlags()));
		sender.sendMessage(ChatColor.YELLOW + "Children: " + ChatColor.WHITE + PageUtils.toString(area.getChildren(false)));
		return true;
	}
	
	@Override
	public String command() {
		
		return "area info";
	}

	@Override
	public String perm() {
		
		return "froobprotect.area";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length == 2) {
			completions.add("info");
		}
		
		return completions;
	}

}
