package com.froobworld.froobprotect.commands;

import com.froobworld.frooblib.Messages;
import com.froobworld.frooblib.command.CommandExecutor;
import com.froobworld.frooblib.utils.PageUtils;
import com.froobworld.froobprotect.managers.AreaManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AreasCommand extends CommandExecutor {
    private static final int PAGE_LENGTH = 20;
    private AreaManager areaManager;

    public AreasCommand(AreaManager areaManager) {
        this.areaManager = areaManager;
    }


    @Override
    public boolean onCommandProcess(CommandSender sender, Command command, String cl, String[] args) {
        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(Messages.PAGE_NOT_A_NUMBER);
                return false;
            }
        }
        ArrayList<String> areas = PageUtils.toStringList(areaManager.getAreas());
        areas.sort(String::compareToIgnoreCase);
        int pages = PageUtils.pages(areas, PAGE_LENGTH);
        int total = areas.size();
        if (pages == 0) {
            sender.sendMessage(ChatColor.YELLOW + "There are no areas.");
            return true;
        }
        areas = PageUtils.page(areas, PAGE_LENGTH, page);

        if (areas == null) {
            sender.sendMessage(Messages.PAGE_NOT_EXIST);
            return false;
        }

        sender.sendMessage(ChatColor.YELLOW + "There " + (total == 1 ? "is one area." : ("are " + total + " areas."))
                + " Showing page " + page + " of " + pages);
        sender.sendMessage(PageUtils.toString(areas));
        return true;
    }

    @Override
    public String command() {

        return "areas";
    }

    @Override
    public String perm() {

        return "froobprotect.areas";
    }

    @Override
    public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            for (int i = 1; i <= PageUtils.pages(areaManager.getAreas(), PAGE_LENGTH); i++) {
                completions.add(i + "");
            }
            completions = StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<String>(completions.size()));
        }

        return completions;
    }


}
