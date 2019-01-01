package com.froobworld.froobprotect.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.froobpermissions.managers.GroupManager;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.data.User;
import com.froobworld.froobprotect.managers.AreaManager;

import net.md_5.bungee.api.ChatColor;

public class Area_RemownerCommand extends CommandExecutor {
	private UUIDManager uuidManager;
	private AreaManager areaManager;
	private GroupManager groupManager;

	public Area_RemownerCommand(UUIDManager uuidManager, AreaManager areaManager, GroupManager groupManager) {
		this.uuidManager = uuidManager;
		this.areaManager = areaManager;
		this.groupManager = groupManager;
	}
	
	@Override
	public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
		if(args.length < 3) {
			sender.sendMessage("/" + cl + " <area> remowner <user>");
			return false;
		}
		Area area = areaManager.getArea(args[0]);
		if(area == null) {
			sender.sendMessage(FroobProtect.AREA_NOT_FOUND);
			return false;
		}
		User user = User.deserialise_command(sender, args[2], groupManager, uuidManager);
		
		if(user == null) {
			return false;
		}
		
		if(!area.getOwners().contains(user)) {
			sender.sendMessage(ChatColor.RED + "That user is not an owner of this area.");
			return false;
		}
		
		area.removeOwner(user);
		sender.sendMessage(ChatColor.YELLOW + "Removed owner " + user.toString() + " from the area.");
		return true;
	}
	
	@Override
	public String command() {
		
		return "area remowner";
	}

	@Override
	public String perm() {
		
		return "froobprotect.area.remowner";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length == 3) {
			Area area = areaManager.getArea(args[0]);
			if(area != null) {
				for(User user : area.getOwners()) {
					completions.add(user.serialiseCommand());
				}
			}
		}
		
		return org.bukkit.util.StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));
	}

}
