/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package net.windwaker.chat.event.chatter;

import net.windwaker.chat.chan.Channel;
import net.windwaker.chat.chan.Chatter;

import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;

public class ChatterInviteChangeEvent extends ChatterEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled, invited;
	private Channel channel;

	public ChatterInviteChangeEvent(Chatter chatter, Channel channel, boolean invited) {
		super(chatter);
		this.channel = channel;
		this.invited = invited;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void setInvited(boolean invited) {
		this.invited = invited;
	}

	public boolean isInvited() {
		return invited;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
