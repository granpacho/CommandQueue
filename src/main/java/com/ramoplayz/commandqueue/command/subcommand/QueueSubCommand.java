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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class QueueSubCommand extends SubCommand {

	private QueueManager queueManager;

	public QueueSubCommand(QueueManager queueManager) {
		super("queue");

		this.queueManager = queueManager;
	}

	@Override
	public void execute(Player sender, String[] args) {

		if (args.length < 1) {
			sender.sendMessage(Messages.QUEUE_INVALID_COMMAND.getMessage());
			return;
		}

		try {
			Bukkit.getOfflinePlayer(args[0]);

			if (Bukkit.getOfflinePlayer(args[0]) == null) {
				sender.sendMessage(Messages.INVALID_PLAYER.getMessage());
			}

			if (args[2] == null || args[2].isBlank() || args[2].isEmpty()) {
				sender.sendMessage(Messages.QUEUE_INVALID_COMMAND.getMessage());
				return;
			}

		} catch (ArrayIndexOutOfBoundsException x) {
			sender.sendMessage(Messages.QUEUE_INVALID_COMMAND.getMessage());
			return;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

		StringBuilder command = new StringBuilder();


		Calendar cal = Calendar.getInstance();

		if (FileManager.getConfig().getBoolean("always-once") == true || args[1].equalsIgnoreCase("once")) {

			for (int i = 2; i < args.length; i++) {
				command.append(args[i] + " ");
			}

			queueManager.insertCommand(new Command(target.getUniqueId(), command.toString().trim(), true, 0, sender.getUniqueId(), cal));

			sender.sendMessage(MessageUtil.translate(FileManager.getMessage("commands.queue.added-command-once"))
					.replace("%command%", command.toString().trim())
					.replace("%player%", target.getName()));
			return;
		}

		for (int i = 1; i < args.length; i++) {
			command.append(args[i] + " ");
		}

		queueManager.insertCommand(new Command(target.getUniqueId(), command.toString().trim(), false, 0, sender.getUniqueId(), cal));

		sender.sendMessage(MessageUtil.translate(FileManager.getMessage("commands.queue.added-command"))
				.replace("%command%", command.toString().trim())
				.replace("%player%", target.getName()));
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

		if (args.length == 2) {
			return StringUtil.copyPartialMatches(args[1], Arrays.asList("once", "[command]"), new ArrayList<>());
		}

		if (args.length == 3) {
			return StringUtil.copyPartialMatches(args[2], Arrays.asList("[command]"), new ArrayList<>());
		}

		return null;
	}

}
