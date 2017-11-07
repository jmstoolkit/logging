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
package com.jmstoolkit.logging.log4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 * @author scott
 */
public class JTKLayout extends Layout {

  private static final SimpleDateFormat DATE =
    new SimpleDateFormat("yyyy-MM-dd", Locale.US);

  @Override
  public final String format(final LoggingEvent event) {
    final StringBuilder xml = new StringBuilder();
    xml.append("<log>\n");
    xml.append("<record>\n");
    xml.append("<hostname>");
    String hostname = "unknown";
    try {
      hostname = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException ex) {
      //System.out.println("Couldn't get hostname: " + ex.getMessage());
    }
    xml.append(hostname);
    xml.append("</hostname>");
    xml.append("<date>");
    synchronized (DATE) {
      xml.append(DATE.format(new Date(event.getTimeStamp())));
    }
    xml.append("<date>\n");
    xml.append("<millis>");
    xml.append(String.valueOf(event.getTimeStamp()));
    xml.append("</millis>\n");

    xml.append("<level>");
    xml.append(event.getLevel().toString());
    xml.append("</level>\n");

    xml.append("<class>");
    xml.append(event.getClass().getName());
    xml.append("</class>\n");

    xml.append("<thread>");
    xml.append(event.getThreadName());
    xml.append("</thread>\n");

    xml.append("<message>");
    xml.append(event.getMessage().toString());
    xml.append("</message>\n");

    xml.append("<line>");
    xml.append(event.getLocationInformation().getLineNumber());
    xml.append("</line>\n");

    xml.append("<method>");
    xml.append(event.getLocationInformation().getMethodName());
    xml.append("</method>\n");


    xml.append("</record>\n");
    xml.append("</log>\n");

    return xml.toString();
  }

  @Override
  public boolean ignoresThrowable() {
    return true;
  }

  public void activateOptions() {
    //
  }

}
