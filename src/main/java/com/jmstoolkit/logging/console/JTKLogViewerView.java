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

import com.jmstoolkit.JTKException;
import com.jmstoolkit.Settings;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.jdesktop.application.Application;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The application's main frame.
 */
public class JTKLogViewerView extends FrameView {

  /**
   * Logger for this class.
   */
  private static final Logger LOGGER = Logger.getLogger("com.jmstoolkit");
  /**
   * Spring application context.
   */
  private ClassPathXmlApplicationContext applicationContext;

  /**
   * Constructor.
   *
   * @param app the application
   */
  public JTKLogViewerView(final SingleFrameApplication app) {
    super(app);

    _init();
    initComponents();

    // status bar initialization - message timeout,
    // idle icon and busy animation, etc
    final ResourceMap resourceMap = getResourceMap();
    final int messageTimeout
      = resourceMap.getInteger("StatusBar.messageTimeout");
    messageTimer = new Timer(messageTimeout, new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent evt) {
        statusMessageLabel.setText("");
      }
    });
    messageTimer.setRepeats(false);
    final int busyAnimationRate
      = resourceMap.getInteger("StatusBar.busyAnimationRate");
    for (int i = 0; i < busyIcons.length; i++) {
      busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
    }
    busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent evt) {
        busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
        statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
      }
    });
    idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
    statusAnimationLabel.setIcon(idleIcon);
    progressBar.setVisible(false);

    // connecting action tasks to status bar via TaskMonitor
    final TaskMonitor taskMonitor
      = new TaskMonitor(getApplication().getContext());
    taskMonitor.addPropertyChangeListener(new PropertyChangeListener() {

      @Override
      public void propertyChange(final PropertyChangeEvent evt) {
        final String propertyName = evt.getPropertyName();
        if (null != propertyName) {
          switch (propertyName) {
            case "started":
              if (!busyIconTimer.isRunning()) {
                statusAnimationLabel.setIcon(busyIcons[0]);
                busyIconIndex = 0;
                busyIconTimer.start();
              }
              progressBar.setVisible(true);
              progressBar.setIndeterminate(true);
              break;
            case "done":
              busyIconTimer.stop();
              statusAnimationLabel.setIcon(idleIcon);
              progressBar.setVisible(false);
              progressBar.setValue(0);
              break;
            case "message":
              final String text = (String) (evt.getNewValue());
              statusMessageLabel.setText((text == null) ? "" : text);
              messageTimer.restart();
              break;
            case "progress":
              final int value = (Integer) (evt.getNewValue());
              progressBar.setVisible(true);
              progressBar.setIndeterminate(false);
              progressBar.setValue(value);
              break;
            default:
              break;
          }
        }
      }
    });
  }

  /**
   * Show the about box.
   */
  @Action
  public final void showAboutBox() {
    if (aboutBox == null) {
      final JFrame mainFrame = JTKLogViewerApp.getApplication().getMainFrame();
      aboutBox = new JTKLogViewerAboutBox(mainFrame);
      aboutBox.setLocationRelativeTo(mainFrame);
    }
    JTKLogViewerApp.getApplication().show(aboutBox);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    mainPanel = new javax.swing.JPanel();
    sendMessagePanel = new javax.swing.JPanel();
    sendLogMessageButton = new javax.swing.JButton();
    logMessageTextField = new javax.swing.JTextField();
    loggerLevelComboBox = new javax.swing.JComboBox();
    jSplitPane1 = new javax.swing.JSplitPane();
    logRecordScrollPane = new javax.swing.JScrollPane();
    logRecordTable = new javax.swing.JTable();
    levelsPieChart = new org.jfree.beans.JPieChart();
    menuBar = new javax.swing.JMenuBar();
    javax.swing.JMenu fileMenu = new javax.swing.JMenu();
    javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
    javax.swing.JMenu helpMenu = new javax.swing.JMenu();
    javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
    statusPanel = new javax.swing.JPanel();
    javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
    statusMessageLabel = new javax.swing.JLabel();
    statusAnimationLabel = new javax.swing.JLabel();
    progressBar = new javax.swing.JProgressBar();
    pieChartDataset = new com.jmstoolkit.logging.console.JTKPieChartDataset();
    logRecordTableModel = new com.jmstoolkit.logging.console.LogRecordTableModel();

    mainPanel.setName("mainPanel"); // NOI18N

    sendMessagePanel.setName("sendMessagePanel"); // NOI18N

    javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.jmstoolkit.logging.console.JTKLogViewerApp.class).getContext().getActionMap(JTKLogViewerView.class, this);
    sendLogMessageButton.setAction(actionMap.get("sendMessage")); // NOI18N
    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.jmstoolkit.logging.console.JTKLogViewerApp.class).getContext().getResourceMap(JTKLogViewerView.class);
    sendLogMessageButton.setText(resourceMap.getString("sendLogMessageButton.text")); // NOI18N
    sendLogMessageButton.setToolTipText(resourceMap.getString("sendLogMessageButton.toolTipText")); // NOI18N
    sendLogMessageButton.setName("sendLogMessageButton"); // NOI18N

    logMessageTextField.setText(resourceMap.getString("logMessageTextField.text")); // NOI18N
    logMessageTextField.setToolTipText(resourceMap.getString("logMessageTextField.toolTipText")); // NOI18N
    logMessageTextField.setName("logMessageTextField"); // NOI18N

    loggerLevelComboBox.setEditable(true);
    loggerLevelComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"severe", "warning", "info", "config", "fine", "finer", "finest", " ", " "}));
    loggerLevelComboBox.setToolTipText(resourceMap.getString("loggerLevelComboBox.toolTipText")); // NOI18N
    loggerLevelComboBox.setName("loggerLevelComboBox"); // NOI18N

    javax.swing.GroupLayout sendMessagePanelLayout = new javax.swing.GroupLayout(sendMessagePanel);
    sendMessagePanel.setLayout(sendMessagePanelLayout);
    sendMessagePanelLayout.setHorizontalGroup(
      sendMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(sendMessagePanelLayout.createSequentialGroup()
          .addComponent(loggerLevelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
          .addComponent(logMessageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
          .addComponent(sendLogMessageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    sendMessagePanelLayout.setVerticalGroup(
      sendMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(sendMessagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(loggerLevelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(logMessageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(sendLogMessageButton))
    );

    jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    jSplitPane1.setDoubleBuffered(true);
    jSplitPane1.setName("jSplitPane1"); // NOI18N

    logRecordScrollPane.setDoubleBuffered(true);
    logRecordScrollPane.setName("logRecordScrollPane"); // NOI18N

    logRecordTable.setAutoCreateRowSorter(true);
    logRecordTable.setModel(logRecordTableModel);
    logRecordTable.setCellSelectionEnabled(true);
    logRecordTable.setDoubleBuffered(true);
    logRecordTable.setName("logRecordTable"); // NOI18N
    logRecordScrollPane.setViewportView(logRecordTable);

    jSplitPane1.setRightComponent(logRecordScrollPane);

    levelsPieChart.setDataset(pieChartDataset.getPieDataset());
    levelsPieChart.setLegendPosition(org.jfree.beans.LegendPosition.NONE);
    levelsPieChart.setChartBackgroundPaint(new java.awt.Color(102, 102, 102, 255));
    levelsPieChart.setPlotBackgroundPaint(new java.awt.Color(204, 204, 204, 255));
    levelsPieChart.setName("levelsPieChart"); // NOI18N
    levelsPieChart.setSource(resourceMap.getString("levelsPieChart.source")); // NOI18N
    levelsPieChart.setSourceFont(resourceMap.getFont("levelsPieChart.sourceFont")); // NOI18N
    levelsPieChart.setSourcePaint(new java.awt.Color(255, 255, 255, 255));
    levelsPieChart.setSubtitle(resourceMap.getString("levelsPieChart.subtitle")); // NOI18N
    levelsPieChart.setSubtitleFont(resourceMap.getFont("levelsPieChart.subtitleFont")); // NOI18N
    levelsPieChart.setSubtitlePaint(new java.awt.Color(255, 204, 102, 255));
    levelsPieChart.setTitle(resourceMap.getString("levelsPieChart.title")); // NOI18N
    levelsPieChart.setTitlePaint(new java.awt.Color(255, 255, 255, 255));
    System.out.println("Listeners: " + pieChartDataset.getPropertySupport().getPropertyChangeListeners().length);

    javax.swing.GroupLayout levelsPieChartLayout = new javax.swing.GroupLayout(levelsPieChart);
    levelsPieChart.setLayout(levelsPieChartLayout);
    levelsPieChartLayout.setHorizontalGroup(
      levelsPieChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 480, Short.MAX_VALUE)
    );
    levelsPieChartLayout.setVerticalGroup(
      levelsPieChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 230, Short.MAX_VALUE)
    );

    jSplitPane1.setLeftComponent(levelsPieChart);

    javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
    mainPanel.setLayout(mainPanelLayout);
    mainPanelLayout.setHorizontalGroup(
      mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(mainPanelLayout.createSequentialGroup()
          .addGap(37, 37, 37)
          .addComponent(sendMessagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addContainerGap(36, Short.MAX_VALUE))
        .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
    );
    mainPanelLayout.setVerticalGroup(
      mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
          .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
          .addComponent(sendMessagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    menuBar.setName("menuBar"); // NOI18N

    fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
    fileMenu.setName("fileMenu"); // NOI18N

    exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
    exitMenuItem.setName("exitMenuItem"); // NOI18N
    fileMenu.add(exitMenuItem);

    menuBar.add(fileMenu);

    helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
    helpMenu.setName("helpMenu"); // NOI18N

    aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
    aboutMenuItem.setName("aboutMenuItem"); // NOI18N
    helpMenu.add(aboutMenuItem);

    menuBar.add(helpMenu);

    statusPanel.setName("statusPanel"); // NOI18N

    statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

    statusMessageLabel.setName("statusMessageLabel"); // NOI18N

    statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

    progressBar.setName("progressBar"); // NOI18N

    javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
    statusPanel.setLayout(statusPanelLayout);
    statusPanelLayout.setHorizontalGroup(
      statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
        .addGroup(statusPanelLayout.createSequentialGroup()
          .addContainerGap()
          .addComponent(statusMessageLabel)
          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 312, Short.MAX_VALUE)
          .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
          .addComponent(statusAnimationLabel)
          .addContainerGap())
    );
    statusPanelLayout.setVerticalGroup(
      statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(statusPanelLayout.createSequentialGroup()
          .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(statusMessageLabel)
            .addComponent(statusAnimationLabel)
            .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGap(3, 3, 3))
    );

    pieChartDataset = (JTKPieChartDataset) this.getApplicationContext().getBean("pieChartDataService");
    pieChartDataset.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        pieChartDatasetPropertyChange(evt);
      }
    });

    setComponent(mainPanel);
    setMenuBar(menuBar);
    setStatusBar(statusPanel);
  }// </editor-fold>//GEN-END:initComponents

  private void pieChartDatasetPropertyChange(final PropertyChangeEvent evt) {
    //GEN-FIRST:event_pieChartDatasetPropertyChange
    levelsPieChart.setDataset(pieChartDataset.getPieDataset());
    levelsPieChart.setSubtitle("Total messages: "
      + pieChartDataset.getTotal());
    logRecordTableModel.getData().add(pieChartDataset.getRecord());
    logRecordTable.revalidate();
  }//GEN-LAST:event_pieChartDatasetPropertyChange

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JSplitPane jSplitPane1;
  private org.jfree.beans.JPieChart levelsPieChart;
  private javax.swing.JTextField logMessageTextField;
  private javax.swing.JScrollPane logRecordScrollPane;
  private javax.swing.JTable logRecordTable;
  private com.jmstoolkit.logging.console.LogRecordTableModel logRecordTableModel;
  private javax.swing.JComboBox loggerLevelComboBox;
  private javax.swing.JPanel mainPanel;
  private javax.swing.JMenuBar menuBar;
  private com.jmstoolkit.logging.console.JTKPieChartDataset pieChartDataset;
  private javax.swing.JProgressBar progressBar;
  private javax.swing.JButton sendLogMessageButton;
  private javax.swing.JPanel sendMessagePanel;
  private javax.swing.JLabel statusAnimationLabel;
  private javax.swing.JLabel statusMessageLabel;
  private javax.swing.JPanel statusPanel;
  // End of variables declaration//GEN-END:variables
  private final Timer messageTimer;
  private final Timer busyIconTimer;
  private final Icon idleIcon;
  private final Icon[] busyIcons = new Icon[15];
  private int busyIconIndex = 0;
  private JDialog aboutBox;
  private final Properties appProperties = new Properties();

  /**
   * Initialize the Spring application context.
   */
  private void _init() {

    try {
      Settings.loadSystemSettings(Settings.JNDI_PROPERTIES);
      // load settings from default file: app.properties
      // which contains previously used connection
      // factories and destinations
      Settings.loadSettings(appProperties);
      //FIXME: add GUI for setting destination and connection factory.
      //connectionFactoryList =
      //  Settings.getSettings(appProperties, P_CONNECTION_FACTORIES);
      //destinationList = Settings.getSettings(appProperties, P_DESTINATIONS);
    } catch (JTKException se) {
      // this happens BEFORE initComponents, so can't put the error in the
      // status area or any other part of the GUI
      System.out.println(se.toStringWithStackTrace());
    }
    this.setApplicationContext(new ClassPathXmlApplicationContext(
      new String[]{"/infrastructure-context.xml",
        "/com/jmstoolkit/logging/console/mdb-context.xml",
        "/jmx-context.xml",
        "/logging-context.xml"}));
    this.getApplicationContext().start();
  }

  /**
   * Send a message.
   *
   * @return the Task handling this
   */
  @Action
  public final Task sendMessage() {
    return new SendMessageTask(getApplication());
  }

  /**
   * @return the applicationContext
   */
  public final ClassPathXmlApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * @param inApplicationContext the applicationContext to set
   */
  public final void setApplicationContext(
    final ClassPathXmlApplicationContext inApplicationContext) {
    this.applicationContext = inApplicationContext;
  }

  /**
   * Task for sending messages asynchronously.
   */
  private class SendMessageTask extends Task<Object, Void> {

    /**
     * Message text.
     */
    private String message;
    /**
     * Logger level.
     */
    private String level;

    /**
     * Constructor.
     *
     * @param app The application containing this task
     */
    SendMessageTask(final Application app) {
      // Runs on the EDT.  Copy GUI state that
      // doInBackground() depends on from parameters
      // to SendMessageTask fields, here.
      super(app);
      message = logMessageTextField.getText();
      level = (String) loggerLevelComboBox.getSelectedItem();
    }

    @Override
    protected Object doInBackground() {
      // Your Task's code here.  This method runs
      // on a background thread, so don't reference
      // the Swing GUI from here.
      LOGGER.log(Level.parse(getLevel().toUpperCase()), getMessage());
      return null;  // return your result
    }

    @Override
    protected void succeeded(final Object result) {
      // Runs on the EDT.  Update the GUI based on
      // the result computed by doInBackground().
    }

    /**
     * @return the message
     */
    @Override
    public String getMessage() {
      return message;
    }

    /**
     * @param inMessage the message to set
     */
    @Override
    public void setMessage(final String inMessage) {
      this.message = inMessage;
    }

    /**
     * @return the level
     */
    public String getLevel() {
      return level;
    }

    /**
     * @param inLevel the level to set
     */
    public void setLevel(final String inLevel) {
      this.level = inLevel;
    }
  }
}
