package com.froobworld.froobprotect.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.froobworld.frooblib.data.Storage;
import com.froobworld.frooblib.data.TaskManager;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.froobpermissions.managers.GroupManager;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Horse;
import com.froobworld.froobprotect.data.User;

public class HorseManager extends TaskManager {
	private Storage storage;
	
	private GroupManager groupManager;
	private UUIDManager uuidManager;
	private com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager;
	private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;
	
	private ArrayList<Horse> horses;
	private ArrayList<Horse> saveQueue;
	
	public HashMap<Player, Boolean> horseInspect;
	
	private EntityType[] protectable;

	public HorseManager(GroupManager groupManager, UUIDManager uuidManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
		super(FroobProtect.getPlugin());
		this.groupManager = groupManager;
		this.uuidManager = uuidManager;
		this.fpPlayerManager = fpPlayerManager;
		this.fbPlayerManager = fbPlayerManager;
	}
	
	
	@Override
	public void ini() {
		storage = new Storage(FroobProtect.getPlugin().getDataFolder() + "/horses");
		
		horses = new ArrayList<Horse>();
		saveQueue = new ArrayList<Horse>();
		horseInspect = new HashMap<Player, Boolean>();
		
		for(File file : storage.listFiles()) {
			horses.add(new Horse(file, this, groupManager, uuidManager, fpPlayerManager, fbPlayerManager));
		}
		
		protectable = new EntityType[] {
				EntityType.DONKEY,
				EntityType.HORSE,
				EntityType.LLAMA,
				EntityType.MULE,
				EntityType.PIG,
				EntityType.SKELETON_HORSE,
				EntityType.ZOMBIE_HORSE
		};
		
		addTask(0, 600, new Runnable() {

			@Override
			public void run() {
				task();
			}
		});
	}
	
	public Horse getHorse(UUID uuid) {
		for(Horse horse : horses) {
			if(horse.getUUID().equals(uuid)) {
				return horse;
			}
		}
		
		return null;
	}
	
	public void addHorse(UUID uuid, UUID owner) {
		File file = storage.getFile(uuid.toString() + ".yml");
		Horse horse = new Horse(file, uuid, User.Builder.createPlayerUser(uuidManager.getUUIDData(owner)), this, groupManager, uuidManager, fpPlayerManager, fbPlayerManager);
		horses.add(horse);
	}
	
	public void removeHorse(Horse horse) {
		horse.delete();
		horses.remove(horse);
	}
	
	public void addToSaveQueue(Horse horse) {
		if(!saveQueue.contains(horse)) {
			saveQueue.add(horse);
		}
	}
	
	public boolean isProtectable(Entity entity) {
		for(EntityType type : protectable) {
			if(entity.getType() == type) {
				return true;
			}
		}
		
		return false;
	}

	public void task() {
		for(Horse horse : saveQueue) {
			horse.save();
		}
		saveQueue.clear();
	}

}
