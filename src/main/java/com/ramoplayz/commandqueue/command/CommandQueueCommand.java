package com.ramoplayz.commandqueue.command;

import com.ramoplayz.commandqueue.Messages;
import com.ramoplayz.commandqueue.command.subcommand.RemoveSubCommand;
import com.ramoplayz.commandqueue.manager.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandQueueCommand implements CommandExecutor, TabCompleter {

	private HashMap<String, SubCommand> subCommands = new HashMap<>();

	private List<UUID> vanished = new ArrayList<>();


	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(Messages.CONSOLE.getMessage());
			return true;
		}

		Player player = (Player) sender;

		if (!sender.hasPermission(FileManager.getConfig().getString("permission"))) {
			sender.sendMessage(Messages.NO_PERMISSION.getMessage());
			return true;
		}

		if (args.length <= 0) {
			player.sendMessage(Messages.INVALID_COMMAND.getMessage());
			return true;
		}

		SubCommand subCommand = getSubCommand(args[0]);

		if (subCommand == null) {
			player.sendMessage(Messages.INVALID_COMMAND.getMessage());
			return true;
		}

		subCommand.execute(player, Arrays.copyOfRange(args, 1, args.length));

		return false;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (args.length == 1) {
			return StringUtil.copyPartialMatches(args[0], Arrays.asList("help", "history", "list", "queue", "reload", "remove"), new ArrayList<>());
		}
		

		try {
			SubCommand subCommand = getSubCommand(args[0]);

			return subCommand.tabComplete(Arrays.copyOfRange(args, 1, args.length));
		} catch (NullPointerException x) {
			return null;
		}
	}

	public void addSubComamnd(SubCommand subCommand) {
		this.subCommands.put(subCommand.getSubCommand(), subCommand);
	}

	public SubCommand getSubCommand(String subCommand) {
		return subCommands.get(subCommand);
	}

	public Map<String, SubCommand> getSubCommands() {
		return subCommands;
	}
}
