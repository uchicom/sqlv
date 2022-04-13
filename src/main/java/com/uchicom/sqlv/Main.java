// (C) 2022 uchicom
package com.uchicom.sqlv;

import com.uchicom.sqlv.db.Context;
import com.uchicom.sqlv.ui.window.SqlViewer;
import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          SqlViewer viewer = new SqlViewer();
          viewer.pack();
          viewer.setVisible(true);
        });
    Runtime.getRuntime().addShutdownHook(new Thread(Context::close));
  }
}
