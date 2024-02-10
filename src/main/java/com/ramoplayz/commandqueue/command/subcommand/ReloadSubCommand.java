package com.ramoplayz.commandqueue.command.subcommand;

import com.ramoplayz.commandqueue.Messages;
import com.ramoplayz.commandqueue.command.SubCommand;
import com.ramoplayz.commandqueue.manager.FileManager;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadSubCommand extends SubCommand {

	public ReloadSubCommand() {
		super("reload");
	}

	@Override
	public void execute(Player sender, String[] args) {
		FileManager.reloadConfig();

		sender.sendMessage(Messages.RELOAD.getMessage());
	}

	@Override
	public List<String> tabComplete(String[] args) {
		return null;
	}
}
