package com.froobworld.froobprotect.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.managers.AreaManager;
import com.froobworld.froobprotect.utils.Utils;

import net.md_5.bungee.api.ChatColor;

public class RemoveCommand extends CommandExecutor {
	private AreaManager areaManager;
	private com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager;
	private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;

	public RemoveCommand(AreaManager areaManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
		this.areaManager = areaManager;
		this.fpPlayerManager = fpPlayerManager;
		this.fbPlayerManager = fbPlayerManager;
	}


	@Override
	public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
		if(args.length == 0) {
			sender.sendMessage("/" + cl + " <area>");
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
				sender.sendMessage(ChatColor.RED + "You must be an owner of that area to remove it.");
				return false;
			}
		}
		int children = area.getChildren(true).size();
		
		areaManager.deleteArea(area);
		sender.sendMessage(ChatColor.YELLOW + "Area removed" + (children == 0 ? ".":(" (and " + children + " sub-areas).")));
		return true;
	}

	@Override
	public String command() {
		
		return "remove";
	}
	
	@Override
	public String perm() {
		
		return "froobprotect.remove";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length == 1) {
			if(sender instanceof Player && !sender.hasPermission(FroobProtect.PERM_EDIT_ALL_AREAS)) {
				Player player = (Player) sender;
				completions.addAll(Utils.getAreaNamesTabCompletion(args[0], areaManager, a -> a.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))));
			}else {
				completions.addAll(Utils.getAreaNamesTabCompletion(args[0], areaManager));
			}
		}
		
		return completions;
	}

}
