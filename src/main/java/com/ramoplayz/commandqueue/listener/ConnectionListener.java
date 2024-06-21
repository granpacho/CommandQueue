package com.ramoplayz.commandqueue.listener;

import com.ramoplayz.commandqueue.manager.QueueManager;
import com.ramoplayz.commandqueue.object.PlayerCommand;
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

		List<PlayerCommand> playerCommands = queueManager.getQueuedCMDS(player);

		for (PlayerCommand playerCommand : playerCommands) {

			if (playerCommand.getOnce() == true) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), playerCommand.getCommand());

				queueManager.insertHistory(playerCommand, true, "Console - Auto Removed");
				queueManager.deleteData(playerCommand);

			} else {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), playerCommand.getCommand());

				queueManager.addRunTime(playerCommand);
			}
		}
	}

}
