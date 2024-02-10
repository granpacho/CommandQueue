package com.ramoplayz.commandqueue.listener;

import com.ramoplayz.commandqueue.manager.QueueManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class ConnectionListener implements Listener {

	private QueueManager queueManager;

	private List<String> commands = new ArrayList<>();

	public ConnectionListener(QueueManager queueManager) {
		this.queueManager = queueManager;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		commands = queueManager.getQueuedCMDS(player);

		for (String command : commands) {

			if (command.split(";")[2].equalsIgnoreCase("true")) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.split(";")[1]);

				queueManager.insertHistory(command, "Console - Auto Remove");
				queueManager.deleteData(command);

			} else {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.split(";")[1]);

				queueManager.addRunTime(command);
			}
		}
		commands.clear();
	}
}
