package com.ramoplayz.commandqueue.util;

import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MessageUtil {

	private static final Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");

	public static String translate(String input) {
		if (input == null) {
			return null;
		}

		Matcher match = pattern.matcher(input);
		while (match.find()) {
			String color = input.substring(match.start() + 1, match.end());
			ChatColor chatColor = ChatColor.of(color);

			if (chatColor != null) {
				input = input.replace("&" + color, chatColor.toString());
			}

			match = pattern.matcher(input);
		}
		return input.replace("&", "\u00a7");
	}

	public static List<String> translate(List<String> input) {
		if (input == null) {
			return null;
		}

		return input.stream().map(MessageUtil::translate).collect(Collectors.toList());
	}
}
