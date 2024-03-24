package com.ramoplayz.commandqueue.manager;

import com.ramoplayz.commandqueue.CommandQueuePlugin;
import com.ramoplayz.commandqueue.object.Command;
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

	private CommandQueuePlugin CQP;

	private Connection connection;

	public QueueManager(CommandQueuePlugin CQP, Connection connection) {
		this.CQP = CQP;
		this.connection = connection;
	}

	public List<Command> getQueuedCMDS(OfflinePlayer target) {
		List<Command> commands = new ArrayList<>();

		Calendar calDateAdded = Calendar.getInstance();

		try {
			PreparedStatement statement = connection.prepareStatement("SELECT UUID, Command, Once, RunTimes, AddedBy, DateAdded FROM CQDB WHERE UUID = ?;");
			statement.setString(1, target.getUniqueId().toString());

			ResultSet rs = statement.executeQuery();


			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("UUID"));
				String command = rs.getString("Command");
				boolean once = rs.getBoolean("Once");
				int runTimes = rs.getInt("RunTimes");
				UUID addedBy = UUID.fromString(rs.getString("AddedBy"));
				String dateAdded = rs.getString("DateAdded");

				calDateAdded.set(Integer.valueOf(dateAdded.split(";")[0]), Integer.valueOf(dateAdded.split(";")[1]), Integer.valueOf(dateAdded.split(";")[2]));

				commands.add(new Command(uuid, command, once, runTimes, addedBy, calDateAdded));

			}

		} catch (SQLException x) {
			x.printStackTrace();
		}

		return commands;
	}

	public List<Command> getHistoryCMDS(OfflinePlayer target) {
		List<Command> commands = new ArrayList<>();

		Calendar calDateAdded = Calendar.getInstance();
		Calendar calDateRemoved = Calendar.getInstance();

		try {
			PreparedStatement statement = connection.prepareStatement("SELECT UUID, Command, Once, RunTimes, AddedBy, DateAdded, RanCommand, RemovedBy, DateRemoved FROM CQHistoryDB WHERE UUID = ?;");
			statement.setString(1, target.getUniqueId().toString());

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("UUID"));
				String command = rs.getString("Command");
				boolean once = rs.getBoolean("Once");
				int runTimes = rs.getInt("RunTimes");
				UUID addedBy = UUID.fromString(rs.getString("AddedBy"));
				String dateAdded = rs.getString("DateAdded");
				boolean ranCommand = Boolean.parseBoolean(rs.getString("RanCommand"));
				String removedBy = rs.getString("RemovedBy");
				String dateRemoved = rs.getString("DateRemoved");

				calDateAdded.set(Integer.valueOf(dateAdded.split(";")[0]), Integer.valueOf(dateAdded.split(";")[1]), Integer.valueOf(dateAdded.split(";")[2]));
				calDateRemoved.set(Integer.valueOf(dateRemoved.split(";")[0]), Integer.valueOf(dateRemoved.split(";")[1]), Integer.valueOf(dateRemoved.split(";")[2]));

				commands.add(new Command(uuid, command, once, runTimes, addedBy, calDateAdded, ranCommand, removedBy, calDateRemoved));
			}

		} catch (SQLException x) {
			x.printStackTrace();
		}

		return commands;
	}


	public void insertCommand(Command command) {
		Calendar calDateAdded = command.getDateAdded();

		try {

			PreparedStatement statement = connection.prepareStatement("INSERT INTO CQDB (UUID, COMMAND, ONCE, RunTimes, ADDEDBY, DATEADDED) VALUES (?, ?, ?, ?, ?, ?);");
			statement.setString(1, command.getPlayer().toString());
			statement.setString(2, command.getCommand());
			statement.setBoolean(3, command.getOnce());
			statement.setInt(4, command.getRunTimes());
			statement.setString(5, command.getAddedBy().toString());
			statement.setString(6, calDateAdded.get(Calendar.YEAR) + ";" + calDateAdded.get(Calendar.MONTH) + ";" + calDateAdded.get(Calendar.DAY_OF_MONTH));

			statement.executeUpdate();
		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public void insertHistory(Command command, boolean ranCommand, String removedBy) {
		Calendar calDateAdded = command.getDateAdded();

		Calendar calDateRemoved = Calendar.getInstance();

		try {

			PreparedStatement statement = connection.prepareStatement("INSERT INTO CQHistoryDB (UUID, Command, Once, RunTimes, AddedBy, DateAdded, RanCommand, RemovedBy, DateRemoved) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
			statement.setString(1, command.getPlayer().toString());
			statement.setString(2, command.getCommand());
			statement.setBoolean(3, command.getOnce());
			statement.setInt(4, command.getRunTimes());
			statement.setString(5, command.getAddedBy().toString());
			statement.setString(6, calDateAdded.get(Calendar.YEAR) + ";" + calDateAdded.get(Calendar.MONTH) + ";" + calDateAdded.get(Calendar.DAY_OF_MONTH));
			statement.setBoolean(7, ranCommand);
			statement.setString(8, removedBy);
			statement.setString(9, calDateRemoved.get(Calendar.YEAR) + ";" + calDateRemoved.get(Calendar.MONTH) + ";" + calDateRemoved.get(Calendar.DAY_OF_MONTH));

			statement.executeUpdate();

		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public void addRunTime(Command command) {
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE CQDB SET RunTimes = ? WHERE UUID = ? AND Command = ? AND Once = ?;");

			statement.setInt(1, command.getRunTimes() + 1);
			statement.setString(2, command.getPlayer().toString());
			statement.setString(3, command.getCommand());
			statement.setBoolean(4, command.getOnce());

		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public void deleteData(Command command) {
		Calendar calDateAdded = command.getDateAdded();

		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM CQDB WHERE UUID = ? AND Command = ? AND Once = ? AND RunTimes = ? AND AddedBy = ? AND DateAdded = ?;");
			statement.setString(1, command.getPlayer().toString());
			statement.setString(2, command.getCommand());
			statement.setBoolean(3, command.getOnce());
			statement.setInt(4, command.getRunTimes());
			statement.setString(5, command.getAddedBy().toString());
			statement.setString(6, calDateAdded.get(Calendar.YEAR) + ";" + calDateAdded.get(Calendar.MONTH) + ";" + calDateAdded.get(Calendar.DAY_OF_MONTH));

			statement.executeUpdate();

		} catch (SQLException x) {
			x.printStackTrace();
		}
	}
}
