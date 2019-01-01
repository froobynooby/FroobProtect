package com.froobworld.froobprotect.data.flag;

import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class NoPvpFlag implements Flag {
    public static final String AREA_NO_PVP = ChatColor.RED + "PvP is disabled in this area.";

    @Override
    public String name() {

        return "no-pvp";
    }

    @Override
    public String description() {

        return "Prevents players from attacking each other.";
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Area area) {
    }

    @Override
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event, Area area) {
    }

    @Override
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event, Area area) {
    }

    @Override
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event, Area area) {
    }

    @Override
    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event, Area area) {
    }

    @Override
    public void onPlayerBucketFill(PlayerBucketFillEvent event, Area area) {
    }

    @Override
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event, Area area) {
    }

    @Override
    public void onPlayerShearEntity(PlayerShearEntityEvent event, Area area) {
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event, Area area) {
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event, Area area) {
    }

    @Override
    public void onBlockBurn(BlockBurnEvent event, Area area) {
    }

    @Override
    public void onBlockDamage(BlockDamageEvent event, Area area) {
    }

    @Override
    public void onBlockExplode(BlockExplodeEvent event, Area area) {
    }

    @Override
    public void onBlockIgnite(BlockIgniteEvent event, Area area) {
    }

    @Override
    public void onSignChange(SignChangeEvent event, Area area) {
    }

    @Override
    public void onEntitySpawn(EntitySpawnEvent event, Area area) {
    }

    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event, Area area) {
    }

    @Override
    public void onEntityInteract(EntityInteractEvent event, Area area) {
    }

    @Override
    public void onEntityCombustByEntity(EntityCombustByEntityEvent event, Area area) {
        Player damager = Utils.getPlayerCauser(event.getCombuster());
        if (damager == null) {
            return;
        }
        if (damager.equals(event.getEntity())) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        event.setCancelled(true);
        damager.sendMessage(AREA_NO_PVP);
    }

    @Override
    public void onEntityChangeBlock(EntityChangeBlockEvent event, Area area) {
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event, Area area) {
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event, Area area) {
        Player damager = Utils.getPlayerCauser(event.getDamager());
        if (damager == null) {
            return;
        }
        if (damager.equals(event.getEntity())) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        event.setCancelled(true);
        damager.sendMessage(AREA_NO_PVP);
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event, Area area) {
    }

    @Override
    public void onEntityTarget(EntityTargetEvent event, Area area) {
    }

    @Override
    public void onExplosionPrime(ExplosionPrimeEvent event, Area area) {
    }

    @Override
    public void onHangingPlace(HangingPlaceEvent event, Area area) {
    }

    @Override
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event, Area area) {
    }

    @Override
    public void onVehicleCreate(VehicleCreateEvent event, Area area) {
    }

    @Override
    public void onVehicleDestroy(VehicleDestroyEvent event, Area area) {
    }

    @Override
    public void onVehicleEnter(VehicleEnterEvent event, Area area) {
    }

    @Override
    public void onVehicleDamage(VehicleDamageEvent event, Area area) {
    }

}
