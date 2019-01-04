package com.froobworld.froobprotect.utils;

import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.frooblib.uuid.UUIDManager.UUIDData;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.managers.AreaManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Predicate;

public class Utils {

    public static Player getPlayerCauser(Entity entity) {
        if (entity instanceof Projectile) {
            ProjectileSource source = ((Projectile) entity).getShooter();
            if (source instanceof Entity) {
                entity = (Entity) source;
            }
        }
        if (entity instanceof Monster) {
            entity = ((Monster) entity).getTarget();
        }
        if (entity instanceof Player) {
            return (Player) entity;
        }

        return null;
    }

    public static UUIDData commandSearchForPlayer(String name, CommandSender sender, UUIDManager uuidManager) {
        UUIDData data = null;

        ArrayList<UUIDData> uuids = uuidManager.getUUIDData(name);
        if (uuids.size() > 1) {
            sender.sendMessage(ChatColor.RED + "There are multiple people who last played with that name:");
            sender.sendMessage(PageUtils.toString(uuids));
            sender.sendMessage(ChatColor.RED + "Please use their UUID in place of their name.");
            return null;
        }
        if (uuids.size() == 1) {
            return uuids.get(0);
        }
        UUID uuid = null;
        try {
            uuid = UUID.fromString(name);
        } catch (IllegalArgumentException ex) {
            sender.sendMessage(ChatColor.RED + "A player by that name could not be found.");
            return null;
        }
        if (uuid != null) {
            data = uuidManager.getUUIDData(uuid);
        } else {
            sender.sendMessage(ChatColor.RED + "A player with that UUID could not be found.");
            return null;
        }
        if (data == null) {
            sender.sendMessage(ChatColor.RED + "A player by that name could not be found.");
            return null;
        }

        return data;
    }

    public static ArrayList<String> getAreaNamesTabCompletion(String partial, AreaManager areaManager) {
        ArrayList<String> completions = new ArrayList<String>();
        String[] split = partial.split(":", -1);

        if (split.length == 1) {
            for (Area area : areaManager.getAreas()) {
                completions.add(area.getName());
            }
        } else {
            Area subArea = areaManager.getArea(String.join(":", Arrays.copyOfRange(split, 0, split.length - 1)));
            if (subArea != null) {
                for (Area area : subArea.getChildren(false)) {
                    completions.add(area.getNameDeep());
                }
            }
        }

        return org.bukkit.util.StringUtil.copyPartialMatches(partial, completions, new ArrayList<String>(completions.size()));
    }

    public static ArrayList<String> getAreaNamesTabCompletion(String partial, AreaManager areaManager, Predicate<Area> predicate) {
        ArrayList<String> completions = new ArrayList<String>();
        String[] split = partial.split(":", -1);

        if (split.length == 1) {
            for (Area area : areaManager.getAreas()) {
                if (predicate.test(area)) {
                    completions.add(area.getName());
                }
            }
        } else {
            Area subArea = areaManager.getArea(String.join(":", Arrays.copyOfRange(split, 0, split.length - 1)));
            if (subArea != null) {
                for (Area area : subArea.getChildren(false)) {
                    if (predicate.test(area)) {
                        completions.add(area.getNameDeep());
                    }
                }
            }
        }

        return org.bukkit.util.StringUtil.copyPartialMatches(partial, completions, new ArrayList<String>(completions.size()));
    }

    public static Block[] getAdjacentBlocks(Block block) {
        Block[] faces = {
                block.getRelative(1, 0, 0),
                block.getRelative(0, 0, 1),
                block.getRelative(-1, 0, 0),
                block.getRelative(0, 0, -1)
        };

        return faces;
    }

    public static Block getAttachedBlock(Block sb) {
        if (sb.getType() == Material.WALL_SIGN || sb.getType() == Material.SIGN) {
            org.bukkit.material.Sign s = (org.bukkit.material.Sign) sb.getState().getData();
            return sb.getRelative(s.getAttachedFace());
        } else {
            return null;
        }
    }

    public static Block getLR(Block block, BlockFace facing, boolean left) {
        if (facing == BlockFace.NORTH) {
            return left ? block.getRelative(BlockFace.WEST) : block.getRelative(BlockFace.EAST);
        }
        if (facing == BlockFace.SOUTH) {
            return left ? block.getRelative(BlockFace.EAST) : block.getRelative(BlockFace.WEST);
        }
        if (facing == BlockFace.WEST) {
            return left ? block.getRelative(BlockFace.SOUTH) : block.getRelative(BlockFace.NORTH);
        }
        if (facing == BlockFace.EAST) {
            return left ? block.getRelative(BlockFace.NORTH) : block.getRelative(BlockFace.SOUTH);
        }

        return null;
    }

    public static String encodeUUID(UUID uuid) {
        String str = uuid.toString();
        String concat = "";
        for (char c : str.toCharArray()) {
            concat += "§" + getEncoding(c);
        }

        return concat;
    }

    public static UUID decodeUUID(String line) {
        String[] split = line.split("§=");

        if (split.length < 2) {
            return null;
        }
        String decoded = "";
        for (String s : split[1].replaceFirst("§", "").split("§")) {
            decoded += getDecoded(s);
        }

        return UUID.fromString(decoded);
    }

    public static String decodeName(String line) {
        String[] split = line.split("§=");

        return split[0];
    }

    public static String encodeName(String name, UUID uuid) {

        return name + "§=" + encodeUUID(uuid);
    }

    private static char getEncoding(char c) {
        switch (c) {
            case '0':
                return ')';
            case '1':
                return '!';
            case '2':
                return '@';
            case '3':
                return '#';
            case '4':
                return '$';
            case '5':
                return '%';
            case '6':
                return '^';
            case '7':
                return '&';
            case '8':
                return '*';
            case '9':
                return '(';
            case 'a':
                return '_';
            case 'b':
                return '+';
            case 'c':
                return '[';
            case 'd':
                return ']';
            case 'e':
                return '{';
            case 'f':
                return '}';
            case '-':
                return '-';
        }

        return c;
    }

    private static char getDecoded(String s) {
        switch (s) {
            case ")":
                return '0';
            case "!":
                return '1';
            case "@":
                return '2';
            case "#":
                return '3';
            case "$":
                return '4';
            case "%":
                return '5';
            case "^":
                return '6';
            case "&":
                return '7';
            case "*":
                return '8';
            case "(":
                return '9';
            case "_":
                return 'a';
            case "+":
                return 'b';
            case "[":
                return 'c';
            case "]":
                return 'd';
            case "{":
                return 'e';
            case "}":
                return 'f';
            case "-":
                return '-';
        }

        return 'x';
    }

}
