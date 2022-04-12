package com.uchicom.sqlv.window;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class SqlViewer extends JFrame {
  JTextArea sqlArea;
  JTextArea outputArea;
  public SqlViewer() {
    super("SqlViewer");
    initComponent();
  }
  void initComponent() {
    sqlArea = new JTextArea();
    outputArea = new JTextArea();
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setTopComponent(new JScrollPane(sqlArea));
    splitPane.setBottomComponent(new JScrollPane(outputArea));
    setContentPane(splitPane);
    setPreferredSize(new Dimension(300,200));
  }
}
