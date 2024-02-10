package com.ramoplayz.commandqueue;

import com.ramoplayz.commandqueue.manager.FileManager;
import com.ramoplayz.commandqueue.util.MessageUtil;

public enum Messages {

	HISTORY_HEADER(MessageUtil.translate(FileManager.getMessage("commands.history.header"))),
	HISTORY_SPACER(MessageUtil.translate(FileManager.getMessage("commands.history.spacer"))),
	HISTORY_INVALID_COMMAND(MessageUtil.translate(FileManager.getMessage("commands.history.invalid-command"))),
	LIST_HEADER(MessageUtil.translate(FileManager.getMessage("commands.list.header"))),
	LIST_SPACER(MessageUtil.translate(FileManager.getMessage("commands.list.spacer"))),
	LIST_INVALID_COMMAND(MessageUtil.translate(FileManager.getMessage("commands.list.invalid-command"))),
	ADDED_COMMAND(MessageUtil.translate(FileManager.getMessage("commands.queue.add-command"))),
	ADDED_COMMAND_ONCE(MessageUtil.translate(FileManager.getMessage("commands.queue.add-command-once"))),
	QUEUE_INVALID_COMMAND(MessageUtil.translate(FileManager.getMessage("commands.queue.invalid-command"))),
	RELOAD(MessageUtil.translate(FileManager.getMessage("commands.reload"))),
	REMOVED_COMMAND(MessageUtil.translate(FileManager.getMessage("commands.remove.removed-command"))),
	REMOVE_INVALID_COMMAND(MessageUtil.translate(FileManager.getMessage("commands.remove.invalid-command"))),
	CONSOLE(MessageUtil.translate(FileManager.getMessage("console"))),
	INVALID_COMMAND(MessageUtil.translate(FileManager.getMessage("invalid-command"))),
	INVALID_PLAYER(MessageUtil.translate(FileManager.getMessage("invalid-player"))),
	INVALID_NUMBER(MessageUtil.translate(FileManager.getMessage("invalid-number"))),
	NO_PERMISSION(MessageUtil.translate(FileManager.getMessage("no-permission")));

	private String message;

	Messages(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
