package com.froobworld.froobprotect.data;

import com.froobworld.frooblib.data.Storage;
import com.froobworld.frooblib.utils.LocationUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.froobpermissions.managers.GroupManager;
import com.froobworld.froobprotect.data.flag.Flag;
import com.froobworld.froobprotect.data.flag.UnknownFlag;
import com.froobworld.froobprotect.managers.AreaManager;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Area {
    private Storage file;
    private AreaManager areaManager;
    private GroupManager groupManager;
    private UUIDManager uuidManager;

    private String name;
    private boolean request;

    private Location location1;
    private Location location2;

    private Area parent;
    private ArrayList<Area> children;

    private ArrayList<Flag> flags;

    private ArrayList<User> owners;
    private ArrayList<User> managers;
    private ArrayList<User> users;

    public Area(Storage file, Area parent, AreaManager areaManager, GroupManager groupManager, UUIDManager uuidManager) {
        this.file = file;
        this.parent = parent;
        this.areaManager = areaManager;
        this.groupManager = groupManager;
        this.uuidManager = uuidManager;
        load();
    }

    public Area(Storage file, Area parent, AreaManager areaManager, GroupManager groupManager, UUIDManager uuidManager, String name, boolean request, Location location1, Location location2, UUID owner, ArrayList<Flag> flags) {
        this.file = file;
        this.areaManager = areaManager;
        this.groupManager = groupManager;
        this.uuidManager = uuidManager;

        this.name = name;
        this.request = request;

        this.location1 = location1;
        this.location2 = location2;

        this.parent = parent;
        this.children = new ArrayList<Area>();

        this.flags = flags;

        this.owners = new ArrayList<User>();
        owners.add(User.Builder.createPlayerUser(uuidManager.getUUIDData(owner)));
        this.managers = new ArrayList<User>();
        this.users = new ArrayList<User>();

        save();
    }


    public void load() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file.getFile("area.yml"));
        name = config.getString("name");
        request = config.getBoolean("request");
        location1 = LocationUtils.deserialiseLocation(config.getString("location.1"));
        location2 = LocationUtils.deserialiseLocation(config.getString("location.2"));

        flags = new ArrayList<Flag>();
        for (String string : config.getStringList("flags")) {
            Flag flag = new UnknownFlag(string);
            for (Flag f : areaManager.getFlags()) {
                if (f.name().equalsIgnoreCase(string)) {
                    flag = f;
                }
            }

            flags.add(flag);
        }

        owners = new ArrayList<User>();
        for (String string : config.getStringList("owners")) {
            owners.add(User.deserialise(string, groupManager, uuidManager));
        }

        managers = new ArrayList<User>();
        for (String string : config.getStringList("managers")) {
            managers.add(User.deserialise(string, groupManager, uuidManager));
        }
        users = new ArrayList<User>();
        for (String string : config.getStringList("users")) {
            users.add(User.deserialise(string, groupManager, uuidManager));
        }

        children = new ArrayList<Area>();
        if (file.getDirectory("sub-areas").exists()) {
            for (File file : file.getDirectory("sub-areas").listFiles()) {
                addChild(new Area(new Storage(file.getPath()), this, areaManager, groupManager, uuidManager));
            }
        }
    }

    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("name", name);
        config.set("request", request);

        config.set("location.1", LocationUtils.serialiseLocation(location1));
        config.set("location.2", LocationUtils.serialiseLocation(location2));

        ArrayList<String> flagsString = new ArrayList<String>();
        for (Flag flag : flags) {
            flagsString.add(flag.name());
        }
        config.set("flags", flagsString);

        ArrayList<String> ownersString = new ArrayList<String>();
        for (User user : owners) {
            ownersString.add(user.serialise());
        }
        config.set("owners", ownersString);

        ArrayList<String> managersString = new ArrayList<String>();
        for (User user : managers) {
            managersString.add(user.serialise());
        }
        config.set("managers", managersString);

        ArrayList<String> usersString = new ArrayList<String>();
        for (User user : users) {
            usersString.add(user.serialise());
        }
        config.set("users", usersString);

        try {
            config.save(file.getFile("area.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Storage getStorage() {

        return file;
    }

    public void delete() {
        try {
            FileUtils.deleteDirectory(file.getTopFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Area area : children) {
            area.delete();
        }
    }

    public String getName() {

        return name;
    }

    public String getNameDeep() {
        if (parent == null) {
            return getName();
        }

        return parent.getNameDeep() + ":" + getName();
    }

    public Location getLocation1() {

        return location1;
    }

    public void setLocation1(Location location) {
        location1 = location;
        areaManager.addToSaveQueue(this);
    }

    public Location getLocation2() {

        return location2;
    }

    public void setLocation2(Location location) {
        location2 = location;
        areaManager.addToSaveQueue(this);
    }

    public ArrayList<User> getOwners() {

        return owners;
    }

    public ArrayList<User> getManagers() {

        return managers;
    }

    public ArrayList<User> getUsers() {

        return users;
    }

    public ArrayList<Flag> getFlags() {

        return flags;
    }

    public Area getParent() {

        return parent;
    }

    public ArrayList<Area> getChildren(boolean deep) {
        if (deep) {
            ArrayList<Area> areas = new ArrayList<Area>();
            areas.addAll(children);
            for (Area area : children) {
                areas.addAll(area.getChildren(true));
            }

            return areas;
        }

        return children;
    }

    public boolean isRequest() {

        return request;
    }

    public void setRequest(boolean bool) {
        request = bool;
        areaManager.addToSaveQueue(this);
    }

    public void addChild(Area area) {
        if (!children.contains(area)) {
            children.add(area);
        }
    }

    public void removeChild(Area area) {
        if (children.contains(area)) {
            children.remove(area);
        }
    }

    public void addOwner(User user) {
        if (!owners.contains(user)) {
            owners.add(user);
            areaManager.addToSaveQueue(this);
        }
    }

    public void removeOwner(User user) {
        if (owners.contains(user)) {
            owners.remove(user);
            areaManager.addToSaveQueue(this);
        }
    }

    public void addManager(User user) {
        if (!managers.contains(user)) {
            managers.add(user);
            areaManager.addToSaveQueue(this);
        }
    }

    public void removeManager(User user) {
        if (managers.contains(user)) {
            managers.remove(user);
            areaManager.addToSaveQueue(this);
        }
    }

    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            areaManager.addToSaveQueue(this);
        }
    }

    public void removeUser(User user) {
        if (users.contains(user)) {
            users.remove(user);
            areaManager.addToSaveQueue(this);
        }
    }

    public void addFlag(Flag flag) {
        if (!flags.contains(flag)) {
            flags.add(flag);
            areaManager.addToSaveQueue(this);
        }
    }

    public void removeFlag(Flag flag) {
        if (flags.contains(flag)) {
            flags.remove(flag);
            areaManager.addToSaveQueue(this);
        }
    }

    public boolean inBounds(Location location) {
        return location.getWorld() == location1.getWorld() &&
                Math.max(location1.getBlockX(), location2.getBlockX()) >= location.getX() &&
                Math.min(location1.getBlockX(), location2.getBlockX()) <= location.getX() &&
                Math.max(location1.getBlockY(), location2.getBlockY()) >= location.getY() &&
                Math.min(location1.getBlockY(), location2.getBlockY()) <= location.getY() &&
                Math.max(location1.getBlockZ(), location2.getBlockZ()) >= location.getZ() &&
                Math.min(location1.getBlockZ(), location2.getBlockZ()) <= location.getZ();
    }

    public ArrayList<Area> getTopAreas(Location location) {
        ArrayList<Area> topAreas = new ArrayList<Area>();
        for (Area child : children) {
            if (child.inBounds(location)) {
                topAreas.addAll(child.getTopAreas(location));
            }
        }
        if (topAreas.isEmpty()) {
            topAreas.add(this);
        }

        return topAreas;
    }

    @Override
    public String toString() {

        return name;
    }

}
