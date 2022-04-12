package com.uchicom.sqlv;

import javax.swing.SwingUtilities;

import com.uchicom.sqlv.window.SqlViewer;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(
      () -> {
        SqlViewer viewer = new SqlViewer();
        viewer.pack();
        viewer.setVisible(true);
      });
  }
}
