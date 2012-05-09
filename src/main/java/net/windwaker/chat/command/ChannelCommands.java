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
package net.windwaker.chat.command;

import net.windwaker.chat.channel.Channel;
import net.windwaker.chat.Chat;
import net.windwaker.chat.channel.Chatter;
import net.windwaker.chat.WindChat;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.exception.CommandException;
import org.spout.api.player.Player;

public class ChannelCommands {

	@Command(aliases = {"-join", "-j"}, desc = "Join a channel", min = 1, max = 1)
	@CommandPermissions("windchat.command.channel.join")
	public void joinChannel(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to perform this command!");
		}

		Player player = (Player) source;
		Chat chat = WindChat.getChat();
		Chatter chatter = chat.getChatter(player.getName());
		if (chatter == null) {
			player.kick("Error: Chatter was null");
			throw new CommandException("Chatter was null");
		}

		Channel channel = chat.getChannel(args.getString(0));
		if (channel == null) {
			throw new CommandException("Channel doesn't exist!");
		}

		if (channel.equals(chatter.getActiveChannel())) {
			throw new CommandException("You are already in " + channel.getName() + "!");
		}

		chatter.join(channel);
	}

	@Command(aliases = {"-leave", "-l"}, desc = "Leave a channel", min = 1, max = 1)
	@CommandPermissions("windchat.command.channel.leave")
	public void leaveChannel(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to perform this command!");
		}
		
		Player player = (Player) source;
		Chat chat = WindChat.getChat();
		Chatter chatter = chat.getChatter(player.getName());
		if (chatter == null) {
			player.kick("Error: Chatter was null");
			throw new CommandException("Chatter was null");
		}
		
		Channel channel = chat.getChannel(args.getString(0));
		if (channel == null) {
			throw new CommandException("Channel doesn't exist!");
		}

		if (channel.equals(chatter.getActiveChannel())) {
			throw new CommandException("You may not leave the channel you are active in!");
		}

		if (!chatter.isIn(channel)) {
			throw new CommandException("You are not in " + channel.getName() + "!");
		}

		chatter.leave(channel);
	}
}
