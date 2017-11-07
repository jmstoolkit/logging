/*
 * Copyright 2011, Scott Douglass <scott@swdouglass.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * on the World Wide Web for more details:
 * http://www.fsf.org/licensing/licenses/gpl.txt
 */
package com.jmstoolkit.logging;

import com.jmstoolkit.JTKException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author scott
 */
public class JTKConsoleFormatter extends SimpleFormatter {

  /** */
  private static final SimpleDateFormat RFC822 =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

  @Override
  public final String format(final LogRecord record) {
    final StringBuilder message = new StringBuilder();
    message.append("#######################################");
    message.append("#######################################\n");
    synchronized (this) {
      message.append(RFC822.format(record.getMillis()));
    }
    message.append(" ");
    message.append(record.getLoggerName());
    message.append(" ");
    message.append(record.getLevel().getName());
    message.append("\n");
    message.append(record.getSourceClassName());
    message.append(" ");
    message.append(record.getSourceMethodName());
    message.append(" ");
    message.append(record.getThreadID());
    message.append("\n");
    message.append(record.getMessage());
    message.append("\n");
    final Throwable exception = record.getThrown();
    if (exception != null) {
      message.append(JTKException.formatException(exception));
    }
    return message.toString();
  }

}
