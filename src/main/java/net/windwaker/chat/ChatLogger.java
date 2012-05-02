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

import org.spout.api.Spout;

import java.util.logging.Level;

/**
 * Logger for WindChat plugin
 *
 * @author Windwaker
 */
public class ChatLogger {
	private static final ChatLogger instance = new ChatLogger();

	private ChatLogger() {

	}

	public static ChatLogger getInstance() {
		return instance;
	}

	public void log(Level level, Object obj) {
		Spout.getLogger().log(level, "[WindChat] " + obj);
	}

	public void info(Object obj) {
		log(Level.INFO, obj);
	}

	public void warn(Object obj) {
		log(Level.WARNING, obj);
	}

	public void severe(Object obj) {
		log(Level.SEVERE, obj);
	}
}