package com.ramoplayz.commandqueue;

import com.ramoplayz.commandqueue.command.CommandQueueCommand;
import com.ramoplayz.commandqueue.command.subcommand.*;
import com.ramoplayz.commandqueue.listener.ConnectionListener;
import com.ramoplayz.commandqueue.manager.DatabaseManager;
import com.ramoplayz.commandqueue.manager.FileManager;
import com.ramoplayz.commandqueue.manager.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;

public final class CommandQueuePlugin extends JavaPlugin {

	private DatabaseManager databaseManager;
	private FileManager fileManager;
	private QueueManager queueManager;

	private CommandQueueCommand commandQueueCommand;

	private Connection connection;

	@Override
	public void onEnable() {
		registerClasses();

		fileManager.setupFiles();
		databaseManager.setupDatabase();

		connection = databaseManager.getConnection();
		queueManager = new QueueManager(this, connection);

		Bukkit.getPluginManager().registerEvents(new ConnectionListener(queueManager), this);

		enbaleCommand();

		getLogger().info("CommandQueue has been enabled!");
	}

	@Override
	public void onDisable() {
		commandQueueCommand.getSubCommands().clear();

		getLogger().info("CommandQueue has been disabled!");
	}

	private void registerClasses() {
		databaseManager = new DatabaseManager(this);
		fileManager = new FileManager(this);
		commandQueueCommand = new CommandQueueCommand();
	}

	private void enbaleCommand() {
		getCommand("commandqueue").setExecutor(commandQueueCommand);

		commandQueueCommand.addSubComamnd(new HelpSubCommand());
		commandQueueCommand.addSubComamnd(new HistorySubCommand(queueManager));
		commandQueueCommand.addSubComamnd(new ListSubCommand(queueManager));
		commandQueueCommand.addSubComamnd(new QueueSubCommand(queueManager));
		commandQueueCommand.addSubComamnd(new ReloadSubCommand());
		commandQueueCommand.addSubComamnd(new RemoveSubCommand(queueManager));
	}
}
