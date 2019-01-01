package com.froobworld.froobprotect.listeners;

import com.froobworld.froobprotect.data.Area;
import com.froobworld.froobprotect.data.Horse;
import com.froobworld.froobprotect.data.flag.Flag;
import com.froobworld.froobprotect.data.flag.OverrideFlag;
import com.froobworld.froobprotect.managers.AreaManager;
import com.froobworld.froobprotect.managers.HorseManager;
import com.froobworld.froobprotect.managers.LockManager;
import com.froobworld.froobprotect.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class EntityListener implements Listener {
    private AreaManager areaManager;
    private HorseManager horseManager;
    private LockManager lockManager;

    public EntityListener(AreaManager areaManager, HorseManager horseManager, LockManager lockManager) {
        this.areaManager = areaManager;
        this.horseManager = horseManager;
        this.lockManager = lockManager;
    }


    @EventHandler(ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent event) {
        for (Area area : areaManager.getTopAreas(event.getLocation())) {
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
                flag.onEntitySpawn(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn_Override(EntitySpawnEvent event) {
        for (Area area : areaManager.getTopAreas(event.getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onEntitySpawn(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        for (Area area : areaManager.getTopAreas(event.getLocation())) {
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
                flag.onCreatureSpawn(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn_Override(CreatureSpawnEvent event) {
        for (Area area : areaManager.getTopAreas(event.getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onCreatureSpawn(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityInteract(EntityInteractEvent event) {
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
                flag.onEntityInteract(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityInteract_Override(EntityInteractEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onEntityInteract(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
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
                flag.onEntityCombustByEntity(event, area);
            }
        }
        for (Area area : areaManager.getTopAreas(event.getCombuster().getLocation())) {
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
                flag.onEntityCombustByEntity(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityCombustByEntity_Override(EntityCombustByEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getEntity().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onEntityCombustByEntity(event, area);
            }
        }
        for (Area area : areaManager.getTopAreas(event.getCombuster().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onEntityCombustByEntity(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
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
                flag.onEntityChangeBlock(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityChangeBlock_Override(EntityChangeBlockEvent event) {
        for (Area area : areaManager.getTopAreas(event.getBlock().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onEntityChangeBlock(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
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
                flag.onEntityDamage(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage_Override(EntityDamageEvent event) {
        for (Area area : areaManager.getTopAreas(event.getEntity().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onEntityDamage(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
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
                flag.onEntityDamageByEntity(event, area);
            }
        }
        for (Area area : areaManager.getTopAreas(event.getDamager().getLocation())) {
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
                flag.onEntityDamageByEntity(event, area);
            }
        }
        Horse horse = horseManager.getHorse(event.getEntity().getUniqueId());
        if (horse != null) {
            horse.onEntityDamageByEntity(event);
        } else {
            Player player = Utils.getPlayerCauser(event.getDamager());
            if (player != null) {
                if (horseManager.isProtectable(event.getEntity())) {
                    if (horseManager.horseInspect.containsKey(player) && horseManager.horseInspect.get(player)) {
                        player.sendMessage(ChatColor.YELLOW + "This horse is not protected.");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity_Override(EntityDamageByEntityEvent event) {
        for (Area area : areaManager.getTopAreas(event.getEntity().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onEntityDamageByEntity(event, area);
            }
        }
        for (Area area : areaManager.getTopAreas(event.getDamager().getLocation())) {
            if (event.isCancelled()) {
                break;
            }
            for (Flag flag : area.getFlags()) {
                if (event.isCancelled()) {
                    break;
                }
                flag.onEntityDamageByEntity(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Area area : areaManager.getTopAreas(event.getLocation())) {
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
                flag.onEntityExplode(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode_Override(EntityExplodeEvent event) {
        for (Area area : areaManager.getTopAreas(event.getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onEntityExplode(event, area);
            }
        }

        lockManager.onEntityExplode(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() == null) {
            return;
        }
        for (Area area : areaManager.getTopAreas(event.getTarget().getLocation())) {
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
                flag.onEntityTarget(event, area);
            }
        }
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
                flag.onEntityTarget(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTarget_Override(EntityTargetEvent event) {
        if (event.getTarget() == null) {
            return;
        }
        for (Area area : areaManager.getTopAreas(event.getTarget().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onEntityTarget(event, area);
            }
        }
        for (Area area : areaManager.getTopAreas(event.getEntity().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onEntityTarget(event, area);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
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
                flag.onExplosionPrime(event, area);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosionPrime_Override(ExplosionPrimeEvent event) {
        for (Area area : areaManager.getTopAreas(event.getEntity().getLocation())) {
            for (Flag flag : area.getFlags()) {
                if (!(flag instanceof OverrideFlag)) {
                    continue;
                }
                flag.onExplosionPrime(event, area);
            }
        }
    }

}
