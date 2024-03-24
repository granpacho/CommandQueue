package com.ramoplayz.commandqueue.object;

import java.util.Calendar;
import java.util.UUID;

public class Command {

	/* Queued Commands & History Commands */
	private UUID player;
	private String command;
	private boolean once;
	private int runTimes;
	private UUID addedBy;
	private Calendar dateAdded;

	/* History Commands */
	private boolean ranCommand;
	private String removedBy;
	private Calendar dateRemoved;

	/* Queued Commands */

	public Command(UUID player, String command, boolean once, int runTimes, UUID addedBy, Calendar dateAdded) {
		this.player = player;
		this.command = command;
		this.once = once;
		this.runTimes = runTimes;
		this.addedBy = addedBy;
		this.dateAdded = dateAdded;
	}

	/* History Commands */
	
	public Command(UUID player, String command, boolean once, int runTimes, UUID addedBy, Calendar dateAdded, boolean ranCommand, String removedBy, Calendar dateRemoved) {
		this.player = player;
		this.command = command;
		this.once = once;
		this.runTimes = runTimes;
		this.addedBy = addedBy;
		this.dateAdded = dateAdded;
		this.ranCommand = ranCommand;
		this.removedBy = removedBy;
		this.dateRemoved = dateRemoved;
	}

	public UUID getPlayer() {
		return player;
	}

	public String getCommand() {
		return command;
	}

	public boolean getOnce() {
		return once;
	}

	public int getRunTimes() {
		return runTimes;
	}

	public UUID getAddedBy() {
		return addedBy;
	}

	public Calendar getDateAdded() {
		return dateAdded;
	}

	public boolean getRanCommand() {
		return ranCommand;
	}

	public String getRemovedBy() {
		return removedBy;
	}

	public Calendar getDateRemoved() {
		return dateRemoved;
	}


}
