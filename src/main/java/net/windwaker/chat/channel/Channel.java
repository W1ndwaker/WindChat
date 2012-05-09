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
package net.windwaker.chat.channel;

import java.util.HashSet;
import java.util.Set;
import net.windwaker.chat.ChatLogger;
import net.windwaker.chat.WindChat;
import org.spout.api.ChatColor;

public class Channel {
	private final String name;
	private final Set<String> listeners = new HashSet<String>();
	private final ChatLogger logger = ChatLogger.getInstance();
	private String format = "[%channel%] %message%", password, joinMessage, leaveMessage;

	public Channel(String name) {
		this.name = name;
		joinMessage = ChatColor.BRIGHT_GREEN + "You have joined " + name + "!";
		leaveMessage = ChatColor.RED + "You have left " + name + "!";
	}
	
	public String getName() {
		return name;
	}
	
	public String getJoinMessage() {
		return joinMessage;
	}
	
	public void setJoinMessage(String joinMessage) {
		this.joinMessage = joinMessage;
	}
	
	public String getLeaveMessage() {
		return leaveMessage;
	}
	
	public void setLeaveMessage(String leaveMessage) {
		this.leaveMessage = leaveMessage;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean hasPassword() {
		return password != null;
	}
	
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public boolean addListener(Chatter chatter) {
		return listeners.add(chatter.getName());
	}
	
	public boolean removeListener(Chatter chatter) {
		return listeners.remove(chatter.getName());
	}
	
	public void broadcast(String message) {
		message = format(format, name, message);
		for (String n : listeners) {
			Chatter chatter = WindChat.getChat().getChatter(name);
			if (chatter != null) {
				chatter.send(message);
			}
		}

		logger.info(message);
	}
	
	// TODO: Extra variables
	public static String format(String format, String channelName, String message) {
		return format.replaceAll("%channel%", channelName).replaceAll("%message%", message).replaceAll("&", "§");
	}
}
