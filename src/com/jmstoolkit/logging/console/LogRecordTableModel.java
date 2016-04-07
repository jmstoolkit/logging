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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import com.jmstoolkit.logging.JTKLogRecord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Scott Douglass
 */
public class LogRecordTableModel extends AbstractTableModel {

  private static final SimpleDateFormat TIMESTAMP =
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private List<JTKLogRecord> data = new ArrayList<JTKLogRecord>();
  private PropertyChangeSupport propertySupport;
  private static final String PROP_DATA = "data";

  private final static String[] columnName = new String[] {
    "date",
    "host",
    "level",
    "message",
    "class",
    "method",
    "thread",
    "millis",
    "sequence",
    "logger"
  };

  public final void addPropertyChangeListener(
    final PropertyChangeListener inListener) {
    getPropertySupport().addPropertyChangeListener(inListener);
  }

  public final void removePropertyChangeListener(
    final PropertyChangeListener inListener) {
    getPropertySupport().removePropertyChangeListener(inListener);
  }

  public final int getRowCount() {
    return getData().size();
  }

  public final int getColumnCount() {
    return columnName.length;
  }

  public final Object getValueAt(final int rowIndex, final int columnIndex) {
    final Object[] record = data.toArray();
    final JTKLogRecord logrec = (JTKLogRecord) record[rowIndex];
    // arbitrary assignment
    String result = "";
    switch (columnIndex) {
      case 0:
        result = TIMESTAMP.format(logrec.getDate()); break;
      case 1:
        result = logrec.getHostname(); break;
      case 2:
        result = logrec.getLevel().getName(); break;
      case 3:
        result = logrec.getMessage(); break;
      case 4:
        result = logrec.getSourceClassName(); break;
      case 5:
        result = logrec.getSourceMethodName(); break;
      case 6:
        result = String.valueOf(logrec.getThreadID()); break;
      case 7:
        result = String.valueOf(logrec.getMillis()); break;
      case 8:
        result = String.valueOf(logrec.getSequenceNumber()); break;
      case 9:
        result = logrec.getLoggerName(); break;
    }

    return result;
  }

  @Override
  public final String getColumnName(int column) {
    return columnName[column];
  }

  /**
   * @return the data
   */
  public final List<JTKLogRecord> getData() {
    return data;
  }

  /**
   * @param data the data to set
   */
  public final void setData(List<JTKLogRecord> newData) {
    List<JTKLogRecord> oldData = data;
    data = newData;
    System.out.println("logRecordTableModel data changed: firePropertyChange");
    getPropertySupport().firePropertyChange(PROP_DATA, oldData, newData);
  }

  /**
   * @return the propertySupport
   */
  public final PropertyChangeSupport getPropertySupport() {
    return propertySupport;
  }

  /**
   * @param propertySupport the propertySupport to set
   */
  public final void setPropertySupport(
    final PropertyChangeSupport inPropertySupport) {
    this.propertySupport = inPropertySupport;
  }

}
