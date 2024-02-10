package com.ramoplayz.commandqueue.manager;

import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class QueueManager {

	private Connection connection;

	public QueueManager(Connection connection) {
		this.connection = connection;
	}

	public List<String> getQueuedCMDS(OfflinePlayer target) {
		List<String> commands = new ArrayList<>();

		try {
			PreparedStatement statement = connection.prepareStatement("SELECT UUID, Command, Once, RunTimes, AddedBy, DateAdded FROM CQDB WHERE UUID = ?;");
			statement.setString(1, target.getUniqueId().toString());

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("UUID"));
				String command = rs.getString("Command");
				boolean once = rs.getBoolean("Once");
				int runTimes = rs.getInt("RunTimes");
				String addedBy = rs.getString("AddedBy");
				String dateAdded = rs.getString("DateAdded");

				commands.add(uuid + ";" + command + ";" + once + ";" + runTimes + ";" + addedBy + ";" + dateAdded);
			}

		} catch (SQLException x) {
			x.printStackTrace();
		}

		return commands;
	}

	public List<String> getHistoryCMDS(OfflinePlayer target) {
		List<String> commands = new ArrayList<>();

		try {
			PreparedStatement statement = connection.prepareStatement("SELECT UUID, Command, Once, RunTimes, AddedBy, DateAdded, RanCommand, RemovedBy, DateRemoved FROM CQHistoryDB WHERE UUID = ?;");
			statement.setString(1, target.getUniqueId().toString());

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("UUID"));
				String command = rs.getString("Command");
				boolean once = rs.getBoolean("Once");
				int runTimes = rs.getInt("RunTimes");
				String addedBy = rs.getString("AddedBy");
				String dateAdded = rs.getString("DateAdded");
				String ranCommand = rs.getString("RanCommand");
				String removedBy = rs.getString("RemovedBy");
				String dateRemoved = rs.getString("DateRemoved");

				commands.add(uuid + ";" + command + ";" + once + ";" + runTimes + ";" + addedBy + ";" + dateAdded + ";" + ranCommand + ";" + removedBy + ";" + dateRemoved);
			}

		} catch (SQLException x) {
			x.printStackTrace();
		}

		return commands;
	}


	public void insertCommand(OfflinePlayer target, String command, boolean once, OfflinePlayer sender) {
		Calendar cal = Calendar.getInstance();

		try {

			PreparedStatement statement = connection.prepareStatement("INSERT INTO CQDB (UUID, COMMAND, ONCE, RunTimes, ADDEDBY, DATEADDED) VALUES (?, ?, ?, ?, ?, ?);");
			statement.setString(1, target.getUniqueId().toString());
			statement.setString(2, command);
			statement.setBoolean(3, once);
			statement.setInt(4, 0);
			statement.setString(5, sender.getUniqueId().toString());
			statement.setString(6, cal.get(Calendar.MONTH) + ";" + cal.get(Calendar.DAY_OF_MONTH) + ";" + cal.get(Calendar.YEAR));

			statement.executeUpdate();

		} catch (SQLException x) {
			x.printStackTrace();
		}

	}

	public void insertHistory(String command, String sender) {
		Calendar cal = Calendar.getInstance();

		try {

			PreparedStatement statement = connection.prepareStatement("INSERT INTO CQHistoryDB (UUID, Command, Once, RunTimes, AddedBy, DateAdded, RanCommand, RemovedBy, DateRemoved) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
			statement.setString(1, command.split(";")[0]);
			statement.setString(2, command.split(";")[1]);
			statement.setBoolean(3, Boolean.parseBoolean(command.split(";")[2]));
			statement.setInt(4, Integer.valueOf(command.split(";")[3]) + 1);
			statement.setString(5, command.split(";")[4]);
			statement.setString(6, command.split(";")[5] + ";" + command.split(";")[6] + ";" + command.split(";")[7]);
			statement.setBoolean(7, Boolean.parseBoolean(command.split(";")[3]));
			statement.setString(8, sender);
			statement.setString(9, cal.get(Calendar.MONTH) + ";" + cal.get(Calendar.DAY_OF_MONTH) + ";" + cal.get(Calendar.YEAR));

			statement.executeUpdate();

		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public void addRunTime(String command) {
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE CQDB SET RunTimes = ? WHERE UUID = ? AND Command = ? AND Once = ?;");

			statement.setInt(1, Integer.valueOf(command.split(";")[3] + 1));
			statement.setString(2, command.split(";")[0]);
			statement.setString(3, command.split(";")[1]);
			statement.setBoolean(4, Boolean.parseBoolean(command.split(";")[2]));

		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public void deleteData(String command) {

		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM CQDB WHERE UUID = ? AND Command = ? AND Once = ? AND RunTimes = ? AND AddedBy = ? AND DateAdded = ?;");
			statement.setString(1, command.split(";")[0]);
			statement.setString(2, command.split(";")[1]);
			statement.setBoolean(3, Boolean.parseBoolean(command.split(";")[2]));
			statement.setInt(4, Integer.valueOf(command.split(";")[3]));
			statement.setString(5, command.split(";")[4]);
			statement.setString(6, command.split(";")[5] + ";" + command.split(";")[6] + ";" + command.split(";")[7]);

			statement.executeUpdate();

		} catch (SQLException x) {
			x.printStackTrace();
		}
	}
}
