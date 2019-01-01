package com.froobworld.froobprotect.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;

import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.froobpermissions.managers.GroupManager;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.managers.HorseManager;
import com.froobworld.froobprotect.utils.Utils;

import net.md_5.bungee.api.ChatColor;

public class Horse {
	private File file;
	
	private GroupManager groupManager;
	private UUIDManager uuidManager;
	private HorseManager horseManager;
	private com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager;
	private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;
	
	private UUID uuid;
	private User owner;
	private ArrayList<User> users;

	public Horse(File file, HorseManager horseManager, GroupManager groupManager, UUIDManager uuidManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager){
		this.file = file;
		this.horseManager = horseManager;
		this.groupManager = groupManager;
		this.uuidManager = uuidManager;
		this.fpPlayerManager = fpPlayerManager;
		this.fbPlayerManager = fbPlayerManager;
		
		load();
	}
	
	public Horse(File file, UUID uuid, User owner, HorseManager horseManager, GroupManager groupManager, UUIDManager uuidManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager){
		this.file = file;
		this.uuid = uuid;
		this.owner = owner;
		this.users = new ArrayList<User>();
		this.horseManager = horseManager;
		this.groupManager = groupManager;
		this.uuidManager = uuidManager;
		this.fpPlayerManager = fpPlayerManager;
		this.fbPlayerManager = fbPlayerManager;
		
		save();
	}
	
	
	public void load(){
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		uuid = UUID.fromString(config.getString("uuid"));
		owner = User.deserialise(config.getString("owner"), groupManager, uuidManager);
		users = new ArrayList<User>();
		for(String string : config.getStringList("users")) {
			users.add(User.deserialise(string, groupManager, uuidManager));
		}
	}

	public void save(){
		YamlConfiguration config = new YamlConfiguration();
		config.set("uuid", uuid.toString());
		config.set("owner", owner.serialise());
		ArrayList<String> stringUsers = new ArrayList<String>();
		for(User user : users) {
			stringUsers.add(user.serialise());
		}
		config.set("users", stringUsers);
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public UUID getUUID() {
		
		return uuid;
	}
	
	public User getOwner() {
		
		return owner;
	}
	
	public ArrayList<User> getUsers() {
		
		return users;
	}
	
	public void addUser(User user) {
		if(!users.contains(user)) {
			users.add(user);
			horseManager.addToSaveQueue(this);
		}
	}
	
	public void removeUser(User user) {
		if(!users.contains(user)) {
			users.remove(user);
			horseManager.addToSaveQueue(this);
		}
	}
	
	public void delete() {
		file.delete();
	}
	
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Player player = Utils.getPlayerCauser(event.getDamager());
		if(player == null) {
			return;
		}
		if(horseManager.horseInspect.get(player) != null && horseManager.horseInspect.get(player)) {
			player.sendMessage(ChatColor.YELLOW + "This horse is protected.");
			player.sendMessage(ChatColor.YELLOW + "Owner: " + ChatColor.WHITE + owner);
			player.sendMessage(ChatColor.YELLOW + "Users: " + ChatColor.WHITE + PageUtils.toString(users));
			event.setCancelled(true);
			return;
		}
		if(!player.hasPermission(FroobProtect.PERM_EDIT_ALL_AREAS) && !owner.isUser(player, fpPlayerManager, fbPlayerManager) && !users.stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
			event.setCancelled(true);
			player.sendMessage(FroobProtect.HORSE_PROTECTED);
		}
	}
	
	public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event) {
		Player player = event.getPlayer();
		if(player == null) {
			return;
		}
		if(!player.hasPermission(FroobProtect.PERM_EDIT_ALL_AREAS) && !owner.isUser(player, fpPlayerManager, fbPlayerManager) && !users.stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
			event.setCancelled(true);
			player.sendMessage(FroobProtect.HORSE_PROTECTED);
		}
	}
	
	public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
		Player player = event.getPlayer();
		if(player == null) {
			return;
		}
		if(!player.hasPermission(FroobProtect.PERM_EDIT_ALL_AREAS) && !owner.isUser(player, fpPlayerManager, fbPlayerManager) && !users.stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
			event.setCancelled(true);
			player.sendMessage(FroobProtect.HORSE_PROTECTED);
		}
	}
	
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();
		if(player == null) {
			return;
		}
		if(!player.hasPermission(FroobProtect.PERM_EDIT_ALL_AREAS) && !owner.isUser(player, fpPlayerManager, fbPlayerManager) && !users.stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
			event.setCancelled(true);
			player.sendMessage(FroobProtect.HORSE_PROTECTED);
		}
	}
	
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if(player == null) {
			return;
		}
		if(!player.hasPermission(FroobProtect.PERM_EDIT_ALL_AREAS) && !owner.isUser(player, fpPlayerManager, fbPlayerManager) && !users.stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
			event.setCancelled(true);
			player.sendMessage(FroobProtect.HORSE_PROTECTED);
		}
	}

}
