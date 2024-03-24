package com.ramoplayz.commandqueue.command.subcommand;

import com.ramoplayz.commandqueue.Messages;
import com.ramoplayz.commandqueue.command.SubCommand;
import com.ramoplayz.commandqueue.manager.QueueManager;
import com.ramoplayz.commandqueue.object.Command;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoveSubCommand extends SubCommand {

	private QueueManager queueManager;

	public RemoveSubCommand(QueueManager queueManager) {
		super("remove");

		this.queueManager = queueManager;

	}

	@Override
	public void execute(Player sender, String[] args) {

		if (args.length < 1) {
			sender.sendMessage(Messages.REMOVE_INVALID_COMMAND.getMessage());
			return;
		}

		try {
			Bukkit.getOfflinePlayer(args[0]);
		} catch (ArrayIndexOutOfBoundsException x) {
			sender.sendMessage(Messages.REMOVE_INVALID_COMMAND.getMessage());
			return;
		}

		if (Bukkit.getOfflinePlayer(args[0]) == null) {
			sender.sendMessage(Messages.INVALID_PLAYER.getMessage());
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

		List<Command> commands = queueManager.getQueuedCMDS(target);

		try {
			Command command = commands.get(Integer.valueOf(args[1]));

			queueManager.insertHistory(command, false, sender.getDisplayName());
			queueManager.deleteData(command);

			sender.sendMessage(Messages.REMOVED_COMMAND.getMessage()
					.replace("%command%", command.getCommand())
					.replace("%player%", target.getName()));
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException x) {
			sender.sendMessage(Messages.INVALID_NUMBER.getMessage());
			return;
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

		if (args.length == 2) {
			return StringUtil.copyPartialMatches(args[1], Arrays.asList("[number]"), new ArrayList<>());
		}

		return null;
	}
}
