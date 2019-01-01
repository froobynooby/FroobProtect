package com.froobworld.froobprotect.listeners;

import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.froobprotect.Config;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.data.Horse;
import com.froobworld.froobprotect.data.flag.Flag;
import com.froobworld.froobprotect.data.flag.OverrideFlag;
import com.froobworld.froobprotect.managers.AreaManager;
import com.froobworld.froobprotect.managers.HorseManager;
import com.froobworld.froobprotect.managers.LockManager;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlayerListener implements Listener {
    private AreaManager areaManager;
    private HorseManager horseManager;
    private LockManager lockManager;

    public PlayerListener(AreaManager areaManager, HorseManager horseManager, LockManager lockManager) {
        this.areaManager = areaManager;
        this.horseManager = horseManager;
        this.lockManager = lockManager;
    }


    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("froobprotect.area.listrequests")) {
            int requests = (int) areaManager.getAreas().stream().filter(a -> a.isRequest()).count();
            if (requests > 0) {
                e.getPlayer().sendMessage(ChatColor.YELLOW + "There " + (requests == 1 ?
                        "is one open area request." : "are " + requests + " area requests.") + " /area listrequests");
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block clicked = event.getClickedBlock();
        if (clicked != null) {
            if (event.getPlayer().hasPermission(FroobProtect.PERM_WAND) && event.getPlayer().getInventory().getItemInMainHand().getType() == Config.getAreasWandTool()) {
                ArrayList<Area> areas = areaManager.getTopAreas(clicked.getLocation());

                if (areas.size() == 0) {
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "There are no areas here.");
                    event.setCancelled(true);
                    return;
                }

                event.getPlayer().sendMessage(ChatColor.YELLOW + "There " + (areas.size() == 1 ? ("is one area ") : ("are " + areas.size() + " areas ")) + "here:");
                event.getPlayer().sendMessage(PageUtils.toString((ArrayList<String>) areas.stream().map(a -> a.getNameDeep()).collect(Collectors.toList())));
                event.setCancelled(true);
                return;
            }
            if (event.getPlayer().hasPermission(FroobProtect.PERM_WAND) && event.getPlayer().getInventory().getItemInMainHand().getType() == Config.getPositionSettingTool()) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    areaManager.location2.put(event.getPlayer(), clicked.getLocation());
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "Position two set. (" + clicked.getLocation().getBlockX() + "," + clicked.getLocation().getBlockY() + "," + clicked.getLocation().getBlockZ() + ")");
                } else {
                    areaManager.location1.put(event.getPlayer(), clicked.getLocation());
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "Position one set. (" + clicked.getLocation().getBlockX() + "," + clicked.getLocation().getBlockY() + "," + clicked.getLocation().getBlockZ() + ")");
                }
                event.setCancelled(true);
                return;
            }

            for (Area area : areaManager.getTopAreas(clicked.getLocation())) {
                if (event.isCancelled()) {
                    break;
                }
                for (Flag flag : area.getFlags()) {
                    if (event.isCancelled()) {
                        break;
                    }
                    if (flag instanceof OverrideFlag) {
                        continue;
                    }
                    flag.onPlayerInteract(event, area);
                }
            }
        }
        lockManager.onPlayerInteract(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract_Override(PlayerInteractEvent event) {
        Block clicked = event.getClickedBlock();
        if (clicked != null) {
            for (Area area : areaManager.getTopAreas(clicked.getLocation())) {
                for (Flag flag : area.getFlags()) {
                    if (!(flag instanceof OverrideFlag)) {
                        continue;
                    }
                    flag.onPlayerInteract(event, area);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getRightClicked().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onPlayerInteractEntity(event, area);
            }
        }
        Horse horse = horseManager.getHorse(event.getRightClicked().getUniqueId());
        if (horse != null) {
            horse.onPlayerInteractEntity(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEntity_Override(PlayerInteractEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getRightClicked().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onPlayerInteractEntity(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getRightClicked().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onPlayerInteractAtEntity(event, area);
            }
        }
        Horse horse = horseManager.getHorse(event.getRightClicked().getUniqueId());
        if (horse != null) {
            horse.onPlayerInteractAtEntity(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractAtEntity_Override(PlayerInteractAtEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getRightClicked().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onPlayerInteractAtEntity(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getEntity().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onPlayerLeashEntity(event, area);
            }
        }
        Horse horse = horseManager.getHorse(event.getEntity().getUniqueId());
        if (horse != null) {
            horse.onPlayerLeashEntity(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeashEntity_Override(PlayerLeashEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getEntity().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onPlayerLeashEntity(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getEntity().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onPlayerUnleashEntity(event, area);
            }
        }
        Horse horse = horseManager.getHorse(event.getEntity().getUniqueId());
        if (horse != null) {
            horse.onPlayerUnleashEntity(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerUnleashEntity_Override(PlayerUnleashEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getEntity().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onPlayerUnleashEntity(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlockClicked().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onPlayerBucketFill(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBucketFill_Override(PlayerBucketFillEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlockClicked().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onPlayerBucketFill(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlockClicked().getRelative(event.getBlockFace()).getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onPlayerBucketEmpty(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBucketEmpty_Override(PlayerBucketEmptyEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlockClicked().getRelative(event.getBlockFace()).getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onPlayerBucketEmpty(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getEntity().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onPlayerShearEntity(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerShearEntity_Override(PlayerShearEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getEntity().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onPlayerShearEntity(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onBlockBreak(event, area);
            }
        }
        lockManager.onBlockBreak(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak_Override(BlockBreakEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onBlockBreak(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onBlockPlace(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace_Override(BlockPlaceEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onBlockPlace(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onBlockBurn(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBurn_Override(BlockBurnEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onBlockBurn(event, area);
            }
        }
        lockManager.onBlockBurn(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onBlockDamage(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamage_Override(BlockDamageEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onBlockDamage(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onBlockExplode(event, area);
            }
        }
        lockManager.onBlockExplode(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockExplode_Override(BlockExplodeEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onBlockExplode(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onBlockIgnite(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgnite_Override(BlockIgniteEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onBlockIgnite(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onSignChange(event, area);
            }
        }
        lockManager.onSignChange(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChange_Override(SignChangeEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onSignChange(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onHangingPlace(HangingPlaceEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onHangingPlace(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingPlace_Override(HangingPlaceEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onHangingPlace(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getEntity().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onHangingBreakByEntity(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHangingBreakByEntity_Override(HangingBreakByEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getEntity().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onHangingBreakByEntity(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleCreate(VehicleCreateEvent event) {
        for (Area area : areaManager.getTopAreas(event.getVehicle().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onVehicleCreate(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleCreate_Override(VehicleCreateEvent event) {
        for (Area area : areaManager.getTopAreas(event.getVehicle().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onVehicleCreate(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        for (Area area : areaManager.getTopAreas(event.getVehicle().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onVehicleDestroy(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleDestroy_Override(VehicleDestroyEvent event) {
        for (Area area : areaManager.getTopAreas(event.getVehicle().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onVehicleDestroy(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleEnter(VehicleEnterEvent event) {
        for (Area area : areaManager.getTopAreas(event.getVehicle().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onVehicleEnter(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleEnter_Override(VehicleEnterEvent event) {
        for (Area area : areaManager.getTopAreas(event.getVehicle().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onVehicleEnter(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleDamage(VehicleDamageEvent event) {
        for (Area area : areaManager.getTopAreas(event.getVehicle().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                if (flag instanceof OverrideFlag) {
                    continue;
                }
                flag.onVehicleDamage(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleDamage_Override(VehicleDamageEvent event) {
        for (Area area : areaManager.getTopAreas(event.getVehicle().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onVehicleDamage(event, area);
            }
        }
    }

}
