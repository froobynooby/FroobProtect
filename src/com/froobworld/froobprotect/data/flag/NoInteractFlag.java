package com.froobworld.froobprotect.data.flag;

import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.utils.Utils;
import org.bukkit.entity.Monster;
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

public class NoInteractFlag implements Flag {
    private com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager;
    private com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager;

    public NoInteractFlag(com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
        this.fpPlayerManager = fpPlayerManager;
        this.fbPlayerManager = fbPlayerManager;
    }

    @Override
    public String name() {

        return "no-interact";
    }

    @Override
    public String description() {

        return "Prevents players from interacting with blocks and items.";
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event, Area area) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) {
            return;
        }
        Player player = event.getPlayer();
        if (!area.getUsers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getManagers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
            event.setCancelled(true);
            if (event.getAction() != Action.PHYSICAL) {
                player.sendMessage(FroobProtect.AREA_PROTECTED);
            }
        }
    }

    @Override
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event, Area area) {
        Player player = event.getPlayer();
        if (!area.getUsers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getManagers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
            event.setCancelled(true);
            player.sendMessage(FroobProtect.AREA_PROTECTED);
        }
    }

    @Override
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event, Area area) {
        Player player = event.getPlayer();
        if (!area.getUsers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getManagers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
            event.setCancelled(true);
            player.sendMessage(FroobProtect.AREA_PROTECTED);
        }
    }

    @Override
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event, Area area) {
        Player player = event.getPlayer();
        if (!area.getUsers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getManagers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
            event.setCancelled(true);
            player.sendMessage(FroobProtect.AREA_PROTECTED);
        }
    }

    @Override
    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event, Area area) {
        Player player = event.getPlayer();
        if (!area.getUsers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getManagers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
            event.setCancelled(true);
            player.sendMessage(FroobProtect.AREA_PROTECTED);
        }
    }

    @Override
    public void onPlayerBucketFill(PlayerBucketFillEvent event, Area area) {
    }

    @Override
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event, Area area) {
    }

    @Override
    public void onPlayerShearEntity(PlayerShearEntityEvent event, Area area) {
        Player player = event.getPlayer();
        if (!area.getUsers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getManagers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
            event.setCancelled(true);
            player.sendMessage(FroobProtect.AREA_PROTECTED);
        }
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
        Player player = Utils.getPlayerCauser(event.getCombuster());
        if (player == null) {
            return;
        }
        if ((event.getEntity() instanceof Monster && event.getEntity().getCustomName() == null) || event.getEntity() instanceof Player) {
            return;
        }
        if (!area.getUsers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getManagers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
            event.setCancelled(true);
            player.sendMessage(FroobProtect.AREA_PROTECTED);
        }
    }

    @Override
    public void onEntityChangeBlock(EntityChangeBlockEvent event, Area area) {
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event, Area area) {
    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event, Area area) {
        Player player = Utils.getPlayerCauser(event.getDamager());
        if (player == null) {
            return;
        }
        if ((event.getEntity() instanceof Monster && event.getEntity().getCustomName() == null) || event.getEntity() instanceof Player) {
            return;
        }
        if (!area.getUsers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getManagers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
            event.setCancelled(true);
            player.sendMessage(FroobProtect.AREA_PROTECTED);
        }
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event, Area area) {
    }

    @Override
    public void onEntityTarget(EntityTargetEvent event, Area area) {
    }

    @Override
    public void onExplosionPrime(ExplosionPrimeEvent event, Area area) {
        Player player = Utils.getPlayerCauser(event.getEntity());
        if (player == null) {
            return;
        }
        if (!area.getUsers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getManagers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
            event.setCancelled(true);
            player.sendMessage(FroobProtect.AREA_PROTECTED);
        }
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
        Player player = Utils.getPlayerCauser(event.getEntered());
        if (player == null) {
            return;
        }
        if (!area.getUsers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getManagers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
            event.setCancelled(true);
            player.sendMessage(FroobProtect.AREA_PROTECTED);
        }
    }

    @Override
    public void onVehicleDamage(VehicleDamageEvent event, Area area) {
        Player player = Utils.getPlayerCauser(event.getAttacker());
        if (player == null) {
            return;
        }
        if (!area.getUsers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getManagers().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager)) && !area.getOwners().stream().anyMatch(u -> u.isUser(player, fpPlayerManager, fbPlayerManager))) {
            event.setCancelled(true);
            player.sendMessage(FroobProtect.AREA_PROTECTED);
        }
    }

}
