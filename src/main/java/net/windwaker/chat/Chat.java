/* Copyright (c) 2012 Walker Crouse, http://windwaker.net/
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.windwaker.chat;

import java.util.Map;

import net.windwaker.chat.channel.Channel;
import net.windwaker.chat.channel.Chatter;

import org.spout.api.data.ValueHolder;
import org.spout.api.player.Player;

public class Chat {
	private static WindChat plugin;

	private Chat() {
	}

	public static void setPlugin(WindChat instance) {
		plugin = instance;
	}

	public static WindChat getPlugin() {
		return plugin;
	}

	public static void onLogin(Player player) {
		plugin.onLogin(player);
	}

	public static Channel getChannel(String name) {
		return plugin.getChannel(name);
	}

	public static Chatter getChatter(String name) {
		return plugin.getChatter(name);
	}

	public static String color(String s) {
		return s.replaceAll("&", "§");
	}

	public static String getData(Player player, Format format) {
		String def = format.getDefault();
		ValueHolder data = player.getData(format.toString());
		if (data != null && data.getString() != null) {
			def = data.getString();
		}
		return def;
	}

	public static String tagSwap(Map<String, String> tagMap, String message) {
		for (Map.Entry<String, String> entry : tagMap.entrySet()) {
			message = message.replaceAll("%" + entry.getKey() + "%", entry.getValue());
		}
		return color(message);
	}

	public static String dataSplit(Player player, String message) {
		for (String variable : message.split("%")) {
			ValueHolder value = player.getData(variable);
			if (value == null || value.getString() == null) {
				continue;
			}
			message = message.replaceAll("%" + variable + "%", value.getString());
		}
		return color(message);
	}

	public static String format(Player player, Format format, Map<String, String> tagMap) {
		return color(dataSplit(player, tagSwap(tagMap, getData(player, format))));
	}
}
