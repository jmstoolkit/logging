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

package com.jmstoolkit.logging.console;

import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.logging.Level;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.SortOrder;
import com.jmstoolkit.logging.JTKLogRecord;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Scott Douglass
 */
public class JTKPieChartDataset implements Serializable, MessageListener {

  private static final Logger LOGGER = Logger.getLogger("com.jmstoolkit");

  public static final String PROP_PIE_DATASET = "pieDataset";
  private DefaultPieDataset pieDataset = new DefaultPieDataset();
  private PropertyChangeSupport propertySupport;
  /** Store count of types of messages. When handling logger output, key = "log level". */
  private Map<String, Integer> typeCount = new HashMap<>();
  private JTKLogRecord record;

  public JTKPieChartDataset() {
    propertySupport = new PropertyChangeSupport(this);
  }

  public PieDataset getPieDataset() {
    return pieDataset;
  }

  public void setPieDataset(final DefaultPieDataset value) {
    final DefaultPieDataset oldValue = pieDataset;
    pieDataset = value;
    //System.out.println("pieDataset changed: firePropertyChange");
    getPropertySupport().firePropertyChange(
      PROP_PIE_DATASET, oldValue, pieDataset);
  }

  public void addPropertyChangeListener(
    final PropertyChangeListener inListener) {
    getPropertySupport().addPropertyChangeListener(inListener);
  }

  public final void removePropertyChangeListener(
    final PropertyChangeListener inListener) {
    getPropertySupport().removePropertyChangeListener(inListener);
  }

  public final Integer getTotal() {
    Integer total = 0;
    for (String level : this.getTypeCount().keySet()) {
      total = total + this.getTypeCount().get(level);
    }
    return total;
  }

  /**
   * Handle received messages.
   * @param inMessage the message
   */
  @Override
  public final void onMessage(final Message inMessage) {
    //System.out.println("Received message...");
    if (inMessage instanceof TextMessage) {
      final DefaultPieDataset pd = (DefaultPieDataset) this.getPieDataset();
      final DefaultPieDataset newpd = new DefaultPieDataset();

      // clone/copy to new DefaultPieDataset
      for (Object level : pd.getKeys()) {
        newpd.setValue((String) level, pd.getValue((String) level));
      }

      try {
        final String rawMessage = ((TextMessage) inMessage).getText();
        Document recordDoc;
        final SAXReader saxReader = new SAXReader();
        recordDoc = saxReader.read(new StringReader(rawMessage));
        final String level = recordDoc.valueOf("//record/level");
        final JTKLogRecord newrecord = new JTKLogRecord(
                Level.parse(level),
                recordDoc.valueOf("//record/message"));
        newrecord.setMillis(
          Long.parseLong(recordDoc.valueOf("//record/millis")));
        newrecord.setLoggerName(recordDoc.valueOf("//record/logger"));
        newrecord.setSequenceNumber(
          Long.parseLong(recordDoc.valueOf("//record/sequence")));
        newrecord.setSourceClassName(recordDoc.valueOf("//record/class"));
        newrecord.setSourceMethodName(recordDoc.valueOf("//record/method"));
        newrecord.setThreadID(
          Integer.parseInt(recordDoc.valueOf("//record/thread")));
        newrecord.setHostname(recordDoc.valueOf("//record/hostname"));

        this.setRecord(newrecord);
        //System.out.println("Received message: " + rawMessage);

        if (this.getTypeCount().containsKey(level)) {
          final Integer count = this.getTypeCount().get(level);
          this.getTypeCount().put(level, count + 1);
          newpd.setValue(level, count + 1);
          //System.out.println("adding to level: " +
          //  level + " count: " + this.typeCount.get(level));
        } else {  // new key
          this.getTypeCount().put(level, 1);
          newpd.setValue(level, 1);
          newpd.sortByKeys(SortOrder.ASCENDING);
          //System.out.println("new level: " + level);
        }
        this.setPieDataset(newpd); // should fire an event
      } catch (JMSException ex) {
        LOGGER.log(Level.SEVERE, "Failed to get JMS message.", ex);
      } catch (DocumentException ex) {
        LOGGER.log(Level.SEVERE, "Could not parse XML log record.", ex);
      }
    } else {
      throw new IllegalArgumentException("Message must be of type TextMessage");
    }
  }

  public final synchronized void setRecord(final JTKLogRecord inRecord) {
    this.record = inRecord;
  }

  public final JTKLogRecord getRecord() {
    return this.record;
  }

  /**
   * @return the typeCount
   */
  public final Map<String, Integer> getTypeCount() {
    return typeCount;
  }

  /**
   * @param inTypeCount the typeCount to set
   */
  public final void setTypeCount(final Map<String, Integer> inTypeCount) {
    this.typeCount = inTypeCount;
  }

  /**
   * @return the propertySupport
   */
  public final PropertyChangeSupport getPropertySupport() {
    return propertySupport;
  }

  /**
   * @param inPropertySupport the propertySupport to set
   */
  public final void setPropertySupport(
    final PropertyChangeSupport inPropertySupport) {
    this.propertySupport = inPropertySupport;
  }
}
