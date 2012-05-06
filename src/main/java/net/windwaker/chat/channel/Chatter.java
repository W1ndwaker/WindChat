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

import org.spout.api.Spout;
import org.spout.api.player.Player;

import static java.util.regex.Matcher.quoteReplacement;

public class Chatter {
	private final String name;
	private Channel channel;
	private String format;

	public Chatter(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}

	public boolean setChannel(Channel channel) {
		this.channel = channel;
		return channel.addChatter(this);
	}
	
	public void chat(String message) {
		channel.broadcast(format(format, name, message));
	}
	
	public void send(String message) {
		Player player = Spout.getEngine().getPlayer(name, true);
		if (player == null) {
			return;
		}

		player.sendMessage(message);
	}

	public static String format(String format, String name, String message) {
		format = format.replaceAll("%chatter%", quoteReplacement("%1$s")).replaceAll("%message%", quoteReplacement("%2$s").replaceAll("&", "§"));
		try {
			return String.format(format, name, message);
		} catch (Throwable t) {
			return null;
		}
	}
}
