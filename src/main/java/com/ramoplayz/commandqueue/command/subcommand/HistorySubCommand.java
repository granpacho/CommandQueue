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

public class HistorySubCommand extends SubCommand {

	private QueueManager queueManager;

	public HistorySubCommand(QueueManager queueManager) {
		super("history");

		this.queueManager = queueManager;
	}

	@Override
	public void execute(Player sender, String[] args) {

		if (args.length < 0) {
			sender.sendMessage(Messages.HISTORY_INVALID_COMMAND.getMessage());
			return;
		}

		try {
			Bukkit.getOfflinePlayer(args[0]);
		} catch (ArrayIndexOutOfBoundsException x) {
			sender.sendMessage(Messages.HISTORY_INVALID_COMMAND.getMessage());
			return;
		}

		if (Bukkit.getOfflinePlayer(args[0]) == null) {
			sender.sendMessage(Messages.INVALID_PLAYER.getMessage());
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

		List<Command> commands = queueManager.getHistoryCMDS(target);

		sender.sendMessage(Messages.HISTORY_SPACER.getMessage());
		sender.sendMessage(Messages.HISTORY_HEADER.getMessage()
				.replace("%player%", target.getName()));
		sender.sendMessage(Messages.HISTORY_SPACER.getMessage());

		int i = 0;
		for (Command command : commands) {
			for (String message : MessageUtil.translate(FileManager.getConfig().getStringList("messages.commands.history.history-commands"))) {
				sender.sendMessage(message
						.replace("%number%", Integer.toString(i))
						.replace("%command%", command.getCommand())
						.replace("%once%", Boolean.toString(command.getOnce()))
						.replace("%run_times%", Integer.toString(command.getRunTimes()))
						.replace("%added_by%", Bukkit.getOfflinePlayer(command.getAddedBy()).getName())
						.replace("%date_added%", (command.getDateAdded().get(Calendar.MONTH) + 1) + "/" + command.getDateAdded().get(Calendar.DAY_OF_MONTH) + "/" + command.getDateAdded().get(Calendar.YEAR))
						.replace("%ran_command%", Boolean.toString(command.getRanCommand()))
						.replace("%removed_by%", command.getRemovedBy())
						.replace("%date_removed%", (command.getDateRemoved().get(Calendar.MONTH) + 1) + "/" + command.getDateRemoved().get(Calendar.DAY_OF_MONTH) + "/" + command.getDateRemoved().get(Calendar.YEAR)));
			}
			sender.sendMessage(Messages.HISTORY_SPACER.getMessage());
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
