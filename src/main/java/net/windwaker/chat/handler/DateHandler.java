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
package net.windwaker.chat.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import net.windwaker.chat.io.yaml.ChatConfiguration;

public class DateHandler {
	private DateFormat dateFormat, timeFormat;

	/**
	 * Initializes the DateHandler to the current set time formats
	 */
	public void init() {
		dateFormat = new SimpleDateFormat(ChatConfiguration.DATE_FORMAT.getString("MM/dd/yyyy"));
		timeFormat = new SimpleDateFormat(ChatConfiguration.TIME_FORMAT.getString("HH:mm:ss"));
	}

	/**
	 * Gets the formatted date as configured in {@link net.windwaker.chat.io.yaml.ChatConfiguration}.
	 * @return configured formatted date
	 */
	public String getFormattedDate() {
		return dateFormat.format(getTime());
	}

	/**
	 * Gets the formatted time as configured in {@link net.windwaker.chat.io.yaml.ChatConfiguration}.
	 * @return configured formatted time
	 */
	public String getFormattedTime() {
		return timeFormat.format(getTime());
	}

	/**
	 * Gets the configured {@link java.util.TimeZone} of the plugin.
	 * Returns the time zone of the location of the server if the zone is not set or is set to "default"
	 * @return time zone
	 */
	public TimeZone getTimeZone() {
		String timeZone = ChatConfiguration.TIME_ZONE.getString("default");
		if (timeZone.equalsIgnoreCase("default")) {
			return TimeZone.getDefault();
		}
		return TimeZone.getTimeZone(timeZone);
	}

	/**
	 * Gets an instance of a calendar from the configured {@link TimeZone}
	 * @return calendar from time zone
	 */
	public Calendar getCalendar() {
		return Calendar.getInstance(getTimeZone());
	}

	/**
	 * Gets the current time of the configured {@link TimeZone}.
	 * @return
	 */
	public Date getTime() {
		return getCalendar().getTime();
	}
}
