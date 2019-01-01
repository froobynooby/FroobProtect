package com.froobworld.froobprotect.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.froobworld.frooblib.command.ContainerCommandExecutor;
import com.froobworld.frooblib.uuid.UUIDManager;
import com.froobworld.froobpermissions.managers.GroupManager;
import com.froobworld.froobprotect.managers.AreaManager;
import com.froobworld.froobprotect.utils.Utils;

public class AreaCommand extends ContainerCommandExecutor {
	private AreaManager areaManager;
	
	public AreaCommand(AreaManager areaManager, UUIDManager uuidManager, GroupManager groupManager, com.froobworld.froobpermissions.managers.PlayerManager fpPlayerManager, com.froobworld.froobbasics.managers.PlayerManager fbPlayerManager) {
		this.areaManager = areaManager;
		
		addSubCommand(new Area_HereCommand(areaManager), 0, "here", "/area here");
		addSubCommand(new Area_InfoCommand(areaManager), 1, "info", "/area <area> info");
		addSubCommand(new Area_AddownerCommand(uuidManager, areaManager, groupManager, fbPlayerManager), 1, "addowner", "/area <area> addowner <user>");
		addSubCommand(new Area_RemownerCommand(uuidManager, areaManager, groupManager), 1, "remowner", "/area <area> remowner <user>");
		addSubCommand(new Area_AddmanagerCommand(uuidManager, areaManager, groupManager, fpPlayerManager, fbPlayerManager), 1, "addmanager", "/area <area> addmanager <user>");
		addSubCommand(new Area_RemmanagerCommand(uuidManager, areaManager, groupManager, fpPlayerManager, fbPlayerManager), 1, "remmanager", "/area <area> remmanager <user>");
		addSubCommand(new Area_AdduserCommand(uuidManager, areaManager, groupManager, fpPlayerManager, fbPlayerManager), 1, "adduser", "/area <area> adduser <user>");
		addSubCommand(new Area_RemuserCommand(uuidManager, areaManager, groupManager, fpPlayerManager, fbPlayerManager), 1, "remuser", "/area <area> remuser <user>");
		addSubCommand(new Area_ListrequestsCommand(areaManager), 0, "listrequests", "/area listrequests");
		addSubCommand(new Area_TeleportCommand(areaManager, fbPlayerManager), 1, "teleport", "/area <area> teleport");
		addSubCommand(new Area_AcceptCommand(areaManager), 1, "accept", "/area <area> accept");
		addSubCommand(new Area_DenyCommand(areaManager), 1, "deny", "/area <area> deny");
		addSubCommand(new Area_AddflagCommand(areaManager, fpPlayerManager, fbPlayerManager), 1, "addflag", "/area <area> addflag <flag>");
		addSubCommand(new Area_RemflagCommand(areaManager, fpPlayerManager, fbPlayerManager), 1, "remflag", "/area <area> remflag <flag>");
		addSubCommand(new Area_ExtendverticalCommand(areaManager, fpPlayerManager, fbPlayerManager), 1, new String[] {"extendvertical", "ev"}, "/area <area> extendvertical [<y1> <y2>]");
	}

	@Override
	public String command() {
		
		return "area";
	}

	@Override
	public String perm() {
		
		return "froobprotect.area";
	}

	@Override
	public List<String> tabCompletions(CommandSender sender, Command command, String cl, String[] args) {
		ArrayList<String> completions = new ArrayList<String>();
		for(SubCommand subCommand : getSubCommands()) {
			if(!sender.hasPermission(subCommand.getExecutor().perm())) {
				continue;
			}
			if(args.length == subCommand.getArgIndex() + 1) {
				completions.add(subCommand.getArg());
			}
		}
		completions = StringUtil.copyPartialMatches(args[args.length-1], completions, new ArrayList<String>(completions.size()));
		
		if(args.length == 1 && completions.isEmpty()) {
			completions.addAll(Utils.getAreaNamesTabCompletion(args[0], areaManager));
		}
		
		if(args.length > 2){
			for(SubCommand subCommand : getSubCommands()) {
				if(!sender.hasPermission(subCommand.getExecutor().perm())) {
					continue;
				}
				if(args.length > subCommand.getArgIndex() + 1) {
					if(subCommand.getArg().equalsIgnoreCase(args[subCommand.getArgIndex()])) {
						return subCommand.getExecutor().tabCompletions(sender, command, cl, args);
					}
				}
			}
		}
		
		return StringUtil.copyPartialMatches(args[args.length-1], completions, new ArrayList<String>(completions.size()));
	}

}
