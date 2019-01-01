package com.froobworld.froobprotect;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.froobworld.froobbasics.FroobBasics;
import com.froobworld.frooblib.FroobPlugin;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.froobpermissions.FroobPermissions;
import com.froobworld.froobpermissions.managers.GroupManager;
import com.froobworld.froobprotect.commands.AreaCommand;
import com.froobworld.froobprotect.commands.AreasCommand;
import com.froobworld.froobprotect.commands.HorseCommand;
import com.froobworld.froobprotect.commands.LockCommand;
import com.froobworld.froobprotect.commands.MyareasCommand;
import com.froobworld.froobprotect.commands.Position1Command;
import com.froobworld.froobprotect.commands.Position2Command;
import com.froobworld.froobprotect.commands.RedefineCommand;
import com.froobworld.froobprotect.commands.RemoveCommand;
import com.froobworld.froobprotect.commands.RequestareaCommand;
import com.froobworld.froobprotect.commands.SetCommand;
import com.froobworld.froobprotect.listeners.EntityListener;
import com.froobworld.froobprotect.listeners.PlayerListener;
import com.froobworld.froobprotect.listeners.WorldListener;
import com.froobworld.froobprotect.managers.AreaManager;
import com.froobworld.froobprotect.managers.HorseManager;
import com.froobworld.froobprotect.managers.LockManager;

import net.md_5.bungee.api.ChatColor;

public class FroobProtect extends FroobPlugin {
	private UUIDManager uuidManager;
	private AreaManager areaManager;
	private HorseManager horseManager;
	private GroupManager groupManager;
	private LockManager lockManager;
	private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;
	private com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager;
	
	public static final String AREA_PROTECTED = ChatColor.RED + "This area is protected.";
	public static final String AREA_NOT_FOUND = ChatColor.RED + "An area by that name could not be found.";
	public static final String AREA_ALREADY_EXISTS = ChatColor.RED + "An area by that name already exists.";
	
	public static final String REQUIRE_TWO_CORNERS = ChatColor.RED + "You need to define two corners.";
	public static final String REQUIRE_CORNERS_SAME_WORLD = ChatColor.RED + "The defined corners need to be in the same world.";
	public static final String PARENT_NOT_EXIST = ChatColor.RED + "The parent area does not exist.";
	public static final String AREA_NAME_NOT_VALID = ChatColor.RED + "That cannot be used as an area name.";
	
	public static final String HORSE_PROTECTED = ChatColor.RED + "This horse is protected.";
	
	public static final String PERM_EDIT_ALL_AREAS = "froobprotect.editallareas";
	public static final String PERM_BYPASS_LOCKS = "froobprotect.lock.bypass";
	public static final String PERM_WAND = "froobprotect.wand";
	
	public void onEnable() {
		iniManagers();
		registerEvents();
		registerCommands();
	}
	
	public void onDisable() {
		areaManager.task();
	}
	
	public void iniManagers() {
		new Config();
		
		PluginManager pm = Bukkit.getPluginManager();
		FroobBasics froobBasics = (FroobBasics) pm.getPlugin("FroobBasics");
		if(froobBasics != null) {
			fbPlayerManager = froobBasics.getPlayerManager();
		}
		
		FroobPermissions froobPermissions = (FroobPermissions) pm.getPlugin("FroobPermissions");
		if(froobPermissions != null) {
			fpPlayerManager = froobPermissions.getPlayerManager();
			groupManager = froobPermissions.getGroupManager();
		}
		uuidManager = uuidManager();
		areaManager = new AreaManager(groupManager, uuidManager, fpPlayerManager, fbPlayerManager);
		horseManager = new HorseManager(groupManager, uuidManager, fpPlayerManager, fbPlayerManager);
		lockManager = new LockManager(fbPlayerManager);
		
		registerManager(areaManager);
		registerManager(horseManager);
		registerManager(lockManager);
	}
	
	public void registerCommands() {
		registerCommand(new AreaCommand(areaManager, uuidManager(), groupManager, fpPlayerManager, fbPlayerManager));
		registerCommand(new SetCommand(areaManager));
		registerCommand(new Position1Command(areaManager));
		registerCommand(new Position2Command(areaManager));
		registerCommand(new AreasCommand(areaManager));
		registerCommand(new RemoveCommand(areaManager, fpPlayerManager, fbPlayerManager));
		registerCommand(new RequestareaCommand(areaManager, fpPlayerManager, fbPlayerManager));
		registerCommand(new RedefineCommand(areaManager));
		registerCommand(new HorseCommand(horseManager, uuidManager(), groupManager, fpPlayerManager, fbPlayerManager));
		registerCommand(new LockCommand(lockManager, uuidManager()));
		registerCommand(new MyareasCommand(areaManager, fpPlayerManager, fbPlayerManager));
	}
	
	public void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new PlayerListener(areaManager, horseManager, lockManager), this);
		pm.registerEvents(new EntityListener(areaManager, horseManager, lockManager), this);
		pm.registerEvents(new WorldListener(lockManager), this);
	}	
	
	public static Plugin getPlugin(){
		
		return getPlugin(FroobProtect.class);
	}
	
}
