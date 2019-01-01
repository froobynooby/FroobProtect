package com.froobworld.froobprotect.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.froobpermissions.managers.GroupManager;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Horse;
import com.froobworld.froobprotect.data.User;
import com.froobworld.froobprotect.data.User.UserType;
import com.froobworld.froobprotect.managers.HorseManager;

import net.md_5.bungee.api.ChatColor;

public class Horse_RemuserCommand extends PlayerCommandExecutor {
	private HorseManager horseManager;
	private GroupManager groupManager;
	private UUIDManager uuidManager;
	private com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager;
	private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;
	
	public Horse_RemuserCommand(HorseManager horseManager, GroupManager groupManager, UUIDManager uuidManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
		this.horseManager = horseManager;
		this.groupManager = groupManager;
		this.uuidManager = uuidManager;
		this.fpPlayerManager = fpPlayerManager;
		this.fbPlayerManager = fbPlayerManager;
	}
	
	
	@Override
	public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
		if(args.length < 2) {
			player.sendMessage("/" + cl + " remuser <user>");
			return false;
		}
		Entity mount = player.getVehicle();
		if(mount == null) {
			player.sendMessage(ChatColor.RED + "You must be riding what you wish to remove a user from.");
			return false;
		}
		Horse horse = horseManager.getHorse(mount.getUniqueId());
		if(horse == null) {
			player.sendMessage(ChatColor.RED + "This is not protected.");
			return false;
		}
		if(!player.hasPermission(FroobProtect.PERM_EDIT_ALL_AREAS) && !horse.getOwner().isUser(player, fpPlayerManager, fbPlayerManager)) {
			player.sendMessage(ChatColor.RED + "You are not the owner of this horse.");
			return false;
		}
		
		User user = User.deserialise_command(player, args[1], groupManager, uuidManager);
		
		if(user == null) {
			return false;
		}
		
		if(user.getType() == UserType.UNKNOWN) {
			player.sendMessage(ChatColor.RED + "A user by that name could not be found.");
			return false;
		}
		
		if(!horse.getUsers().contains(user)) {
			player.sendMessage(ChatColor.RED + "That user is not a user of the horse.");
			return false;
		}
		
		horse.removeUser(user);
		player.sendMessage(ChatColor.YELLOW + "Removed user " + user.toString() + " from the horse.");
		return true;
	}

	@Override
	public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		if(args.length == 2) {
			Entity mount = player.getVehicle();
			if(mount != null) {
				Horse horse = horseManager.getHorse(mount.getUniqueId());
				if(horse != null) {
					for(User user : horse.getUsers()) {
						completions.add(user.serialiseCommand());
					}
				}
			}
		}
		
		return org.bukkit.util.StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));
	}

	@Override
	public String command() {
		
		return "horse remuser";
	}

	@Override
	public String perm() {
		
		return "froobprotect.horse";
	}

}
