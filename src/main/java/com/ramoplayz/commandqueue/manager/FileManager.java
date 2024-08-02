package com.ramoplayz.commandqueue.manager;

import com.ramoplayz.commandqueue.CommandQueuePlugin;
import dev.dejvokep.boostedyaml.YamlDocument;

import java.io.File;
import java.io.IOException;

public class FileManager {

	private CommandQueuePlugin CQP;

	private static YamlDocument config;

	public FileManager(CommandQueuePlugin CQP) {
		this.CQP = CQP;
	}

	public void setupFiles() {
		try {
			config = YamlDocument.create(new File(CQP.getDataFolder(), "config.yml"), CQP.getResource("config.yml"));
		} catch (IOException x) {
			x.printStackTrace();
		}

	}

	public static YamlDocument getConfig() {
		return config;
	}

	public static String getMessage(String messagePath) {
		String message = config.getString("messages." + messagePath);

		return message;
	}

	public static void reloadConfig() {
		try {
			config.reload();
		} catch (IOException x) {
			x.printStackTrace();
		}
	}

}
