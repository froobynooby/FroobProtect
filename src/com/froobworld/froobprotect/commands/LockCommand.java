package com.froobworld.froobprotect.commands;

import com.froobworld.frooblib.command.PlayerCommandExecutor;
import com.froobworld.frooblib.utils.CommandUtils;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.frooblib.uuid.UUIDManager.UUIDData;
import com.froobworld.froobprotect.FroobProtect;
import com.froobworld.froobprotect.managers.LockManager;
import com.froobworld.froobprotect.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LockCommand extends PlayerCommandExecutor {
    private LockManager lockManager;
    private UUIDManager uuidManager;

    public LockCommand(LockManager lockManager, UUIDManager uuidManager) {
        this.lockManager = lockManager;
        this.uuidManager = uuidManager;
    }

    @Override
    public boolean onPlayerCommandProcess(Player player, Command command, String cl, String[] args) {
        if (args.length < 1) {
            player.sendMessage("/" + cl + " <line> <name>");
            return false;
        }
        Sign sign = null;
        if (lockManager.selectedSigns.get(player) != null && lockManager.selectedSigns.get(player).getBlock().getType() == Material.WALL_SIGN) {
            sign = (Sign) lockManager.selectedSigns.get(player).getBlock().getState();
        }
        if (sign == null) {
            player.sendMessage(ChatColor.RED + "You must right-click the sign you wish to edit first.");
            return false;
        }
        int line;
        try {
            line = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "The line number must be an integer.");
            return false;
        }
        if (line < 2 || line > 4) {
            player.sendMessage(ChatColor.RED + "Line number must be between 2 and 4.");
            return false;
        }
        if (line == 2 && sign.getLine(0).equals(LockManager.LOCK_HEADER) && !player.hasPermission(FroobProtect.PERM_BYPASS_LOCKS)) {
            return false;
        }
        boolean owner = false;
        for (Block b : lockManager.getAttachedLockables(Utils.getAttachedBlock(sign.getBlock()))) {
            if (player.getUniqueId().equals(lockManager.getOwner(b.getLocation(), false))) {
                owner = true;
            }
        }
        if (!owner && !player.hasPermission(FroobProtect.PERM_BYPASS_LOCKS)) {
            player.sendMessage(ChatColor.RED + "You can only edit signs for protections you own.");
            return false;
        }
        String string = "";
        if (args.length > 1) {
            string = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        }

        if (uuidManager.getUUIDData(string).isEmpty() && !(line == 2 && sign.getLine(0).equals(LockManager.LOCK_HEADER))) {
            sign.setLine(line - 1, string);
            sign.update();
        } else {
            UUIDData data = Utils.commandSearchForPlayer(string, player, uuidManager);
            if (data == null) {
                return false;
            }
            sign.setLine(line - 1, Utils.encodeName(string, data.getUUID()));
            sign.update();
        }
        return true;
    }

    @Override
    public List<String> tabCompletions(Player player, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions.add(2 + "");
            completions.add(3 + "");
            completions.add(4 + "");
            completions = StringUtil.copyPartialMatches(args[0], completions, new ArrayList<String>(completions.size()));
        }
        if (args.length == 2) {
            completions.add("[Friends]");
            completions = StringUtil.copyPartialMatches(args[1], completions, new ArrayList<String>(completions.size()));
            completions.addAll(CommandUtils.tabCompletePlayerList(args[1], true, true, uuidManager));
        }

        return completions;
    }

    @Override
    public String command() {

        return "lock";
    }

    @Override
    public String perm() {

        return "froobprotect.lock";
    }

}
