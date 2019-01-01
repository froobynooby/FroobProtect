package com.froobworld.froobprotect.data;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.frooblib.uuid.UUIDManager.UUIDData;
import com.froobworld.froobpermissions.data.Group;
import com.froobworld.froobpermissions.managers.GroupManager;
import com.froobworld.froobprotect.utils.Utils;

import net.md_5.bungee.api.ChatColor;

public class User {
	private UserType type;
	private UUIDData uuidData;
	private Group group;
	private String serialised;
	
	private User(UserType type, UUIDData uuidData, Group group, String serialised) {
		this.type = type;
		this.uuidData = uuidData;
		this.group = group;
		this.serialised = serialised;
	}
	
	
	public UserType getType() {
		
		return type;
	}
	
	protected UUID getUUID() {
		if(uuidData == null) {
			return null;
		}
		
		return uuidData.getUUID();
	}
	
	protected Group getGroup() {
		
		return group;
	}
	
	protected String getSerialisedUnknown() {
		
		return serialised;
	}
	
	public boolean isUser(Player player, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
		switch(type) {
		case FRIENDS:
			return (fbPlayerManager != null) && (uuidData.getUUID().equals(player.getUniqueId()) || fbPlayerManager.getPlayerdata(player).getFriends().contains(uuidData.getUUID()));
		case GROUP:
			if(fpPlayerManager == null) {
				return false;
			}
			Group playerGroup = fpPlayerManager.getPlayerdata(player).getGroup();
			return playerGroup.doesInherit(group) || group.equals(playerGroup);
		case PLAYER:
			return uuidData.getUUID().equals(player.getUniqueId());
		default:
			return false;
		}
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof User) {
			User user = (User) object;
			
			return (type == user.getType()) && (getUUID() == user.getUUID()) && (group == user.getGroup()) && (serialised == user.getSerialisedUnknown());
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		switch(type) {
		case FRIENDS:
			return "Friends:" + uuidData.getLastName();
		case GROUP:
			return "Group:" + group.getName();
		case PLAYER:
			return uuidData.getLastName();
		default:
			return "";
		
		}
	}
	
	public enum UserType {
		PLAYER,
		FRIENDS,
		GROUP,
		UNKNOWN;
	}
	
	public String serialise() {
		switch(type) {
		case FRIENDS:
			return type.toString() + ";" + uuidData.getUUID().toString();
		case GROUP:
			return type.toString() + ";" + group.getName();
		case PLAYER:
			return type.toString() + ";" + uuidData.getUUID().toString();
		case UNKNOWN:
			return serialised;
		default:
			return "";
		}
	}
	
	public static User deserialise(String string, GroupManager groupManager, UUIDManager uuidManager) {
		String[] split = string.split(";");
		
		switch(UserType.valueOf(split[0])) {
			case FRIENDS:
				return Builder.createFriendsUser(uuidManager.getUUIDData(UUID.fromString(split[1])));
			case GROUP:
				if(groupManager == null) {
					return Builder.createUnknownUser(string);
				}
				return Builder.createGroupUser(groupManager.getGroup(split[1]));
			case PLAYER:
				return Builder.createPlayerUser(uuidManager.getUUIDData(UUID.fromString(split[1])));
			default:
				return null;
		}
	}
	
	public static User deserialise_command(CommandSender sender, String string, GroupManager groupManager, UUIDManager uuidManager) {
		User user = null;
		String[] split = string.split(":");
		if(split.length == 1) {
			UUIDData uuidData = Utils.commandSearchForPlayer(split[0], sender, uuidManager);
			if(uuidData == null) {
				return null;
			}
			
			user = Builder.createPlayerUser(uuidData);
		}else {
			if(split[0].equalsIgnoreCase("friends")) {
				UUIDData uuidData = Utils.commandSearchForPlayer(split[1], sender, uuidManager);
				if(uuidData == null) {
					return null;
				}
				
				user = Builder.createFriendsUser(uuidData);
			}
			if(split[0].equalsIgnoreCase("group")) {
				if(groupManager == null) {
					return Builder.createUnknownUser(string);
				}
				Group group = groupManager.getGroup(split[1]);
				if(group == null) {
					sender.sendMessage(ChatColor.RED + "A group by that name could not be found.");
					return null;
				}
				
				user = Builder.createGroupUser(group);
			}
		}
		
		return user;
	}
	
	public String serialiseCommand() {
		switch(type) {
		case FRIENDS:
			return "Friends:" + uuidData.getLastName();
		case GROUP:
			return "Group:" + group.getName();
		case PLAYER:
			return uuidData.getLastName();
		case UNKNOWN:
			return serialised;
		default:
			return "";
		}
	}
	
	public static class Builder {
		public static User createPlayerUser(UUIDData uuidData) {
			
			return new User(UserType.PLAYER, uuidData, null, null);
		}
		public static User createFriendsUser(UUIDData uuidData) {
			
			return new User(UserType.FRIENDS, uuidData, null, null);
		}
		public static User createGroupUser(Group group) {
			
			return new User(UserType.GROUP, null, group, null);
		}
		public static User createUnknownUser(String serialised) {
			
			return new User(UserType.UNKNOWN, null, null, serialised);
		}
	}
}
