package com.froobworld.froobprotect.managers;

import com.froobworld.frooblib.data.Storage;
import com.froobworld.frooblib.data.TaskManager;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.froobpermissions.managers.GroupManager;
import com.froobworld.froobprotect.Config;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.data.flag.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class AreaManager extends TaskManager {
    public HashMap<Player, Location> location1;
    public HashMap<Player, Location> location2;
    private Storage storage;
    private GroupManager groupManager;
    private com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager;
    private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;
    private UUIDManager uuidManager;
    private ArrayList<Area> areas;
    private ArrayList<Flag> flags;
    private ArrayList<Area> saveQueue;

    public AreaManager(GroupManager groupManager, UUIDManager uuidManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
        super(FroobProtect.getPlugin());
        this.groupManager = groupManager;
        this.fpPlayerManager = fpPlayerManager;
        this.fbPlayerManager = fbPlayerManager;
        this.uuidManager = uuidManager;
    }

    @Override
    public void ini() {
        storage = new Storage(FroobProtect.getPlugin().getDataFolder() + "/areas");

        flags = new ArrayList<Flag>();
        addFlag(new NoBuildFlag(fpPlayerManager, fbPlayerManager));
        addFlag(new NoExplodeFlag());
        addFlag(new NoFireDestroyFlag());
        addFlag(new NoFireSpreadFlag());
        addFlag(new NoInteractFlag(fpPlayerManager, fbPlayerManager));
        addFlag(new NoMobDamageFlag());
        addFlag(new NoMobSpawnFlag());
        addFlag(new NoMobTargetFlag());
        addFlag(new NoPvpFlag());
        addFlag(new AllowExplodeOverrideFlag());
        addFlag(new AllowFireDestroyOverrideFlag());
        addFlag(new AllowFireSpreadOverrideFlag());

        areas = new ArrayList<Area>();
        for (File file : storage.listFiles()) {
            areas.add(new Area(new Storage(file.getPath()), null, this, groupManager, uuidManager));
        }

        location1 = new HashMap<Player, Location>();
        location2 = new HashMap<Player, Location>();

        saveQueue = new ArrayList<Area>();

        addTask(0, 600, new Runnable() {

            @Override
            public void run() {
                task();
            }
        });
    }

    public ArrayList<Area> getAreas() {

        return areas;
    }

    public ArrayList<Area> getTopAreas(Location location) {
        ArrayList<Area> topAreas = new ArrayList<Area>();
        for (Area area : areas) {
            if (area.isRequest()) {
                continue;
            }
            if (area.inBounds(location)) {
                topAreas.addAll(area.getTopAreas(location));
            }
        }

        return topAreas;
    }

    public Area getArea(String name) {
        Area area = null;
        for (String string : name.split(":")) {
            boolean nextFound = false;
            if (area == null) {
                for (Area a : areas) {
                    if (a.getName().equalsIgnoreCase(string)) {
                        area = a;
                        nextFound = true;
                        break;
                    }
                }
            } else {
                for (Area a : area.getChildren(false)) {
                    if (a.getName().equalsIgnoreCase(string)) {
                        area = a;
                        nextFound = true;
                        break;
                    }
                }
            }
            if (area == null || nextFound == false) {
                return null;
            }
        }

        return area;
    }

    public void addFlag(Flag flag) {
        if (!flags.contains(flag)) {
            flags.add(flag);
        }
    }

    public ArrayList<Flag> getFlags() {

        return flags;
    }

    public Flag getFlag(String name) {
        for (Flag flag : flags) {
            if (flag.name().equalsIgnoreCase(name)) {
                return flag;
            }
        }

        return null;
    }

    public ArrayList<Flag> getDefaultFlags() {
        ArrayList<Flag> defaultFlags = new ArrayList<Flag>();
        for (String string : Config.getDefaultFlags()) {
            Flag defFlag = null;
            for (Flag flag : flags) {
                if (flag.name() == string) {
                    defFlag = flag;
                    break;
                }
            }
            if (defFlag == null) {
                defFlag = new UnknownFlag(string);
            }
            defaultFlags.add(defFlag);
        }

        return defaultFlags;
    }

    public void addToSaveQueue(Area area) {
        if (!saveQueue.contains(area)) {
            saveQueue.add(area);
        }
    }

    public boolean createArea(UUID owner, String name, boolean request, Location location1, Location location2) {
        String[] split = name.split(":");
        Area parent = null;
        if (split.length > 1) {
            parent = getArea(StringUtils.join(Arrays.copyOfRange(split, 0, split.length - 1), ":"));
            if (parent == null) {
                return false;
            }
        }
        if (parent == null) {
            Area area = new Area(new Storage(storage.getDirectory(name).getPath()), parent, this, groupManager, uuidManager, name, request, location1, location2, owner, getDefaultFlags());
            areas.add(area);
        } else {
            Area area = new Area(new Storage(parent.getStorage().getDirectory("sub-areas/" + split[split.length - 1]).getPath()), parent, this, groupManager, uuidManager, split[split.length - 1], request, location1, location2, owner, getDefaultFlags());
            parent.addChild(area);
        }

        return true;
    }

    public void deleteArea(Area area) {
        if (areas.contains(area)) {
            areas.remove(area);
        }
        area.delete();
        Area parent = area.getParent();
        if (parent != null) {
            parent.removeChild(area);
        }
    }

    public void task() {
        for (Area area : saveQueue) {
            area.save();
        }

        saveQueue.clear();
    }

}
