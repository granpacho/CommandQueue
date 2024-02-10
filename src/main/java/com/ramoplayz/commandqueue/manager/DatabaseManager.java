package com.ramoplayz.commandqueue.manager;

import com.ramoplayz.commandqueue.CommandQueuePlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {


	private CommandQueuePlugin CQP;

	private Connection connection;

	public DatabaseManager(CommandQueuePlugin CQP) {
		this.CQP = CQP;
	}

	public void setupDatabase() {

		if (!CQP.getDataFolder().exists()) {
			CQP.getDataFolder().mkdir();
		}

		try (Statement statement = getConnection().createStatement()) {
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS CQDB (ID int AUTO_INCREMENT, UUID varchar(36), Command varchar, Once boolean, RunTimes varchar, AddedBy varchar(36), DateAdded varchar, PRIMARY KEY(ID));");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS CQHistoryDB (ID int AUTO_INCREMENT, UUID varchar(36), Command varchar, Once boolean, RunTimes int, AddedBy varchar(36), DateAdded varchar, RanCommand boolean, RemovedBy varchar, DateRemoved varchar, PRIMARY KEY(ID));");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:"
						+ CQP.getDataFolder().getAbsolutePath() + "/database.db");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return connection;
	}

}
