package com.froobworld.froobprotect.data.flag;

import com.froobworld.frooblib.data.Listable;
import com.froobworld.froobprotect.data.Area;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public interface Flag extends Listable {

    public String name();

    public String description();

    public void onPlayerInteract(PlayerInteractEvent event, Area area);

    public void onPlayerInteractEntity(PlayerInteractEntityEvent event, Area area);

    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event, Area area);

    public void onPlayerLeashEntity(PlayerLeashEntityEvent event, Area area);

    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event, Area area);

    public void onPlayerBucketFill(PlayerBucketFillEvent event, Area area);

    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event, Area area);

    public void onPlayerShearEntity(PlayerShearEntityEvent event, Area area);

    public void onBlockBreak(BlockBreakEvent event, Area area);

    public void onBlockPlace(BlockPlaceEvent event, Area area);

    public void onBlockBurn(BlockBurnEvent event, Area area);

    public void onBlockDamage(BlockDamageEvent event, Area area);

    public void onBlockExplode(BlockExplodeEvent event, Area area);

    public void onBlockIgnite(BlockIgniteEvent event, Area area);

    public void onSignChange(SignChangeEvent event, Area area);

    public void onEntitySpawn(EntitySpawnEvent event, Area area);

    public void onCreatureSpawn(CreatureSpawnEvent event, Area area);

    public void onEntityInteract(EntityInteractEvent event, Area area);

    public void onEntityCombustByEntity(EntityCombustByEntityEvent event, Area area);

    public void onEntityChangeBlock(EntityChangeBlockEvent event, Area area);

    public void onEntityDamage(EntityDamageEvent event, Area area);

    public void onEntityDamageByEntity(EntityDamageByEntityEvent event, Area area);

    public void onEntityExplode(EntityExplodeEvent event, Area area);

    public void onEntityTarget(EntityTargetEvent event, Area area);

    public void onExplosionPrime(ExplosionPrimeEvent event, Area area);

    public void onHangingPlace(HangingPlaceEvent event, Area area);

    public void onHangingBreakByEntity(HangingBreakByEntityEvent event, Area area);

    public void onVehicleCreate(VehicleCreateEvent event, Area area);

    public void onVehicleDestroy(VehicleDestroyEvent event, Area area);

    public void onVehicleEnter(VehicleEnterEvent event, Area area);

    public void onVehicleDamage(VehicleDamageEvent event, Area area);

    public default String listName() {

        return name();
    }
}
