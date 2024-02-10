package com.ramoplayz.commandqueue.command.subcommand;

import com.ramoplayz.commandqueue.command.SubCommand;
import com.ramoplayz.commandqueue.manager.FileManager;
import com.ramoplayz.commandqueue.util.MessageUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpSubCommand extends SubCommand {

	public HelpSubCommand() {
		super("help");
	}

	@Override
	public void execute(Player sender, String[] args) {
		for (String message : MessageUtil.translate(FileManager.getConfig().getStringList("messages.commands.help"))) {
			sender.sendMessage(message);
		}
	}

	@Override
	public List<String> tabComplete(String[] args) {
		return null;
	}
}
