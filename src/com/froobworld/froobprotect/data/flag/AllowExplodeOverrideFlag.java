package com.froobworld.froobprotect.data.flag;

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

public class AllowExplodeOverrideFlag implements OverrideFlag {

    @Override
    public String name() {

        return "allow-explode-override";
    }

    @Override
    public String description() {

        return "Overrides everything to allow explosions.";
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
        event.setCancelled(false);
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
    }

    @Override
    public void onEntityChangeBlock(EntityChangeBlockEvent event, Area area) {
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event, Area area) {
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event, Area area) {
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event, Area area) {
        event.setCancelled(false);
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
