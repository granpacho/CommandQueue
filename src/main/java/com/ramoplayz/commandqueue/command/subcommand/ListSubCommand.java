package com.ramoplayz.commandqueue.command.subcommand;

import com.ramoplayz.commandqueue.Messages;
import com.ramoplayz.commandqueue.command.SubCommand;
import com.ramoplayz.commandqueue.manager.FileManager;
import com.ramoplayz.commandqueue.manager.QueueManager;
import com.ramoplayz.commandqueue.object.Command;
import com.ramoplayz.commandqueue.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListSubCommand extends SubCommand {

	private QueueManager queueManager;

	public ListSubCommand(QueueManager queueManager) {
		super("list");

		this.queueManager = queueManager;
	}

	@Override
	public void execute(Player sender, String[] args) {

		if (args.length < 0) {
			sender.sendMessage(Messages.LIST_INVALID_COMMAND.getMessage());
			return;
		}

		try {
			Bukkit.getOfflinePlayer(args[0]);
		} catch (ArrayIndexOutOfBoundsException x) {
			sender.sendMessage(Messages.LIST_INVALID_COMMAND.getMessage());
			return;
		}

		if (Bukkit.getOfflinePlayer(args[0]) == null) {
			sender.sendMessage(Messages.INVALID_PLAYER.getMessage());
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

		List<Command> commands = queueManager.getQueuedCMDS(target);

		sender.sendMessage(Messages.LIST_SPACER.getMessage());
		sender.sendMessage(Messages.LIST_HEADER.getMessage()
				.replace("%player%", target.getName()));
		sender.sendMessage(Messages.LIST_SPACER.getMessage());

		int i = 0;
		for (Command command : commands) {
			for (String message : MessageUtil.translate(FileManager.getConfig().getStringList("messages.commands.list.list-commands"))) {
				sender.sendMessage(message
						.replace("%number%", Integer.toString(i))
						.replace("%command%", command.getCommand())
						.replace("%once%", Boolean.toString(command.getOnce()))
						.replace("%run_times%", Integer.toString(command.getRunTimes()))
						.replace("%added_by%", Bukkit.getOfflinePlayer(command.getAddedBy()).getName())
						.replace("%date_added%", command.getDateAdded().get(Calendar.MONTH) + "/" + command.getDateAdded().get(Calendar.DAY_OF_MONTH) + "/" + command.getDateAdded().get(Calendar.YEAR)));
			}
			sender.sendMessage(Messages.LIST_SPACER.getMessage());
			i++;
		}

		commands.clear();
	}

	@Override
	public List<String> tabComplete(String[] args) {

		if (args.length == 1) {
			List<String> players = new ArrayList<>();

			for (Player player : Bukkit.getOnlinePlayers()) {
				players.add(player.getName());
			}

			return StringUtil.copyPartialMatches(args[0], players, new ArrayList<>());
		}

		return null;
	}
}
