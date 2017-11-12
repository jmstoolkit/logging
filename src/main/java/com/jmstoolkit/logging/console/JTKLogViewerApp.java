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

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class JTKLogViewerApp extends SingleFrameApplication {

  /**
   * At startup create and show the main frame of the application.
   */
  @Override
  protected void startup() {
    show(new JTKLogViewerView(this));
  }

  /**
   * This method is to initialize the specified window by injecting
   * resources.Windows shown in our application come fully initialized from the
   * GUI builder, so this additional configuration is not needed.
   *
   * @param root Root java.awt.Window
   */
  @Override
  protected void configureWindow(java.awt.Window root) {
  }

  /**
   * A convenient static getter for the application instance.
   *
   * @return the instance of JTKLogViewerApp
   */
  public static JTKLogViewerApp getApplication() {
    return Application.getInstance(JTKLogViewerApp.class);
  }

  /**
   * Main method launching the application.
   *
   * @param args Comand line arguments
   */
  public static void main(String[] args) {
    launch(JTKLogViewerApp.class, args);
  }
}
