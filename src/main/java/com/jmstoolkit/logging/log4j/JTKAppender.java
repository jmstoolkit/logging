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

import com.jmstoolkit.JTKException;
import com.jmstoolkit.Settings;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 * @author scott
 */
public class JTKAppender extends AppenderSkeleton {

  private JTKLayout layout = new JTKLayout();
  private MessageProducer messageProducer;
  private Session session;

  public JTKAppender() {
    try {
      // JNDI properties should include at least:
      //  java.naming.factory.initial
      //  java.naming.provider.url
      // And optionally:
      //  java.naming.security.principal
      //  java.naming.security.credentials
      // App properties must include the connection factory and destination
      //  jmstoolkit.cf
      //  jmstoolkit.destination
      // And optionally:
      //  jmstoolkit.username
      //  jmstoolkit.password
      Settings.loadSystemSettings(Settings.JNDI_PROPERTIES);
      Settings.loadSystemSettings(Settings.APP_PROPERTIES);

      final Context jndi = new InitialContext(System.getProperties());
      final ConnectionFactory connectionFactory =
        (ConnectionFactory) jndi.lookup(System.getProperty("jmstoolkit.cf"));
      final Connection connection = connectionFactory.createConnection(
        System.getProperty("jmstoolkit.username"),
        System.getProperty("jmstoolkit.password"));
      session = connection.createSession(false,
        Session.AUTO_ACKNOWLEDGE);
      messageProducer = session.createProducer(
        (Destination) jndi.lookup(
          System.getProperty("jmstoolkit.destination")));


    } catch (JTKException | NamingException | JMSException e) {

    }
  }

  @Override
  protected final void append(final LoggingEvent event) {
    try {
      final TextMessage message = session.createTextMessage();
      message.setText(layout.format(event));
      messageProducer.send(message);
    } catch (JMSException e) {

    }
  }

  @Override
  public final void close() {
    try {
      session.close();
    } catch (JMSException ex) {

    }
  }

  /**
   * This appender uses the JTKLayout exclusively.
   * @return false
   */
  @Override
  public final boolean requiresLayout() {
    return false;
  }

}
