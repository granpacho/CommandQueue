package com.ramoplayz.commandqueue.listener;

import com.ramoplayz.commandqueue.manager.QueueManager;
import com.ramoplayz.commandqueue.object.Command;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class ConnectionListener implements Listener {

	private QueueManager queueManager;

	public ConnectionListener(QueueManager queueManager) {
		this.queueManager = queueManager;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		List<Command> commands = queueManager.getQueuedCMDS(player);

		for (Command command : commands) {

			if (command.getOnce() == true) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.getCommand());

				queueManager.insertHistory(command, true, "Console - Auto Removed");
				queueManager.deleteData(command);

			} else {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.getCommand());

				queueManager.addRunTime(command);
			}
		}
	}

}
