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
package net.windwaker.chat.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.windwaker.chat.WindChat;
import net.windwaker.chat.handler.DateHandler;

public class ChatLogger {
	private final WindChat plugin;
	private final ChatLevel CHAT = new ChatLevel();
	private final DateHandler dateHandler;
	private final List<String> messages = new ArrayList<String>();
	private File file;

	/**
	 * Constructs a new ChatLogger of the specified plugin.
	 * @param plugin
	 */
	public ChatLogger(WindChat plugin) {
		this.plugin = plugin;
		dateHandler = plugin.getDateHandler();
	}

	/**
	 * Caches a chat message to be saved and prints to console.
	 * @param message to cache
	 */
	public void log(String message) {
		messages.add("[" + dateHandler.getFormattedTime() + "] " + message);
		plugin.getLogger().log(CHAT, message);
	}

	/**
	 * Starts the ChatLogger and creates a new file at 'DATA_FOLDER/logs/DATE_TIME.txt
	 */
	public void start() {
		try {
			String fileName = dateHandler.getFormattedDate() + "_" + dateHandler.getFormattedTime();
			fileName = fileName.replaceAll("[\\\\/]", "-").replaceAll(":", "\\.");
			file = new File(plugin.getDataFolder(), "logs/" + fileName + ".txt");
			if (!file.exists()) {
				System.out.println("Creating new file at: " + fileName);
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stops and the logger and writes all cached messages to disk.
	 */
	public void stop() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			for (String message : messages) {
				out.write(message);
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
