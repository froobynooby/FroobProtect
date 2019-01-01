package com.froobworld.froobprotect;

import org.bukkit.Material;

import java.util.ArrayList;

public class Config {
    private static ArrayList<String> defaultFlags;

    public Config() {
        defaultFlags = new ArrayList<String>();
        defaultFlags.add("no-build");
        defaultFlags.add("no-interact");
        defaultFlags.add("no-explode");
    }

    public static ArrayList<String> getDefaultFlags() {

        return defaultFlags;
    }

    public static Material getAreasWandTool() {

        return Material.ROTTEN_FLESH;
    }

    public static Material getPositionSettingTool() {

        return Material.CLAY_BALL;
    }

}
