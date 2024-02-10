package com.ramoplayz.commandqueue.command.subcommand;

import com.ramoplayz.commandqueue.Messages;
import com.ramoplayz.commandqueue.command.SubCommand;
import com.ramoplayz.commandqueue.manager.FileManager;
import com.ramoplayz.commandqueue.manager.QueueManager;
import com.ramoplayz.commandqueue.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HistorySubCommand extends SubCommand {

	private QueueManager queueManager;

	private List<String> commands = new ArrayList<>();

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

		commands = queueManager.getHistoryCMDS(target);

		sender.sendMessage(Messages.HISTORY_SPACER.getMessage());
		sender.sendMessage(Messages.HISTORY_HEADER.getMessage()
				.replace("%player%", target.getName()));
		sender.sendMessage(Messages.HISTORY_SPACER.getMessage());

		int i = 0;
		for (String command : commands) {
			for (String message : MessageUtil.translate(FileManager.getConfig().getStringList("messages.commands.history.history-commands"))) {
				sender.sendMessage(message
						.replace("%number%", Integer.toString(i))
						.replace("%command%", command.split(";")[1])
						.replace("%once%", command.split(";")[2])
						.replace("%run_times%", command.split(";")[3])
						.replace("%added_by%", Bukkit.getOfflinePlayer(UUID.fromString(command.split(";")[4])).getName())
						.replace("%date_added%", command.split(";")[5] + "/" + command.split(";")[6] + "/" + command.split(";")[7])
						.replace("%ran_command%", command.split(";")[8])
						.replace("%removed_by%", Bukkit.getOfflinePlayer(UUID.fromString(command.split(";")[9])).getName())
						.replace("%date_removed%", command.split(";")[10] + "/" + command.split(";")[11] + "/" + command.split(";")[12]));
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
