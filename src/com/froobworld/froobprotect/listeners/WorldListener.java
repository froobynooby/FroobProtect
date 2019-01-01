package com.froobworld.froobprotect.listeners;

import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import com.froobworld.froobprotect.managers.LockManager;

public class WorldListener implements Listener {
	private LockManager lockManager;
	
	public WorldListener(LockManager lockManager) {
		this.lockManager = lockManager;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPistonRetract(BlockPistonRetractEvent event){
		for(Block block : event.getBlocks()) {
			for(Block b : lockManager.getAttachedLockables(block)) {
				if(lockManager.getOwner(b.getLocation(), true) != null) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPistonExtend(BlockPistonExtendEvent event){
		for(Block block : event.getBlocks()) {
			for(Block b : lockManager.getAttachedLockables(block)) {
				if(lockManager.getOwner(b.getLocation(), true) != null) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onInventoryMoveItem(InventoryMoveItemEvent event){
		if(event.getSource().getHolder() instanceof BlockState || event.getSource().getHolder() instanceof DoubleChest){
			if(event.getDestination().getHolder() instanceof HopperMinecart){
				if(lockManager.getOwner(event.getSource().getLocation(), true) != null) {
					event.setCancelled(true);
					return;
				}
			}
			if(event.getDestination().getHolder()  instanceof BlockState){
				UUID ownerDestination = lockManager.getOwner(event.getDestination().getLocation(), true);
				UUID ownerSource = lockManager.getOwner(event.getSource().getLocation(), true);
				if(ownerSource != null && !ownerSource.equals(ownerDestination)) {
					event.setCancelled(true);
					return;
				}
			}
			
		}
	}

}
