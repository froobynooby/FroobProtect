package com.froobworld.froobprotect.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.managers.AreaManager;

import net.md_5.bungee.api.ChatColor;

public class Area_ExtendverticalCommand extends CommandExecutor {
	private AreaManager areaManager;
	private com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager;
	private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;

	public Area_ExtendverticalCommand(AreaManager areaManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
		this.areaManager = areaManager;
		this.fpPlayerManager = fpPlayerManager;
		this.fbPlayerManager = fbPlayerManager;
	}
	
	@Override
	public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
		if(args.length == 3) {
			sender.sendMessage("/" + cl + " <area> extendvertical [<y1> <y2>]");
			return false;
		}
		Area area = areaManager.getArea(args[0]);
		if(area == null) {
			sender.sendMessage(FroobProtect.AREA_NOT_FOUND);
			return false;
		}
		if(sender instanceof Player && !sender.hasPermission(FroobProtect.PERM_EDIT_ALL_AREAS)) {
			Player player = (Player) sender;
			if(!area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
				sender.sendMessage(ChatColor.RED + "You must be an owner of that area to extend it.");
				return false;
			}
		}
		int y1 = 0;
		int y2 = area.getLocation1().getWorld().getMaxHeight();
		if(args.length > 2) {
			try {
				y1 = Integer.valueOf(args[2]);
				y2 = Integer.valueOf(args[3]);
			}catch(NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "The y-coordinates must be integers.");
				return false;
			}
		}
		
		Location location1 = area.getLocation1();
		Location location2 = area.getLocation2();
		location1.setY(y1);
		location2.setY(y2);
		
		area.setLocation1(location1);
		area.setLocation2(location2);
		sender.sendMessage(ChatColor.YELLOW + "Area now goes from " + y1 + " to " + y2 + ".");
		return true;
	}
	
	@Override
	public String command() {
		
		return "area extendvertical";
	}

	@Override
	public String perm() {
		
		return "froobprotect.area.extendvertical";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		
		return new ArrayList<String>();
	}

}
