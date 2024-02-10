package com.ramoplayz.commandqueue.command;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand {

	private String subCommand;

	public SubCommand(String subCommand) {
		this.subCommand = subCommand;
	}

	protected String getSubCommand() {
		return subCommand;
	}

	public abstract void execute(Player sender, String args[]);

	public abstract List<String> tabComplete(String args[]);

}
