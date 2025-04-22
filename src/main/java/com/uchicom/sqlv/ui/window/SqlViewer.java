// (C) 2022 uchicom
package com.uchicom.sqlv.ui.window;

import com.uchicom.sqlv.db.SqlExecutor;
import com.uchicom.sqlv.db.Table;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class SqlViewer extends JFrame {
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  JTextArea sqlArea;

  JTabbedPane tabbedPane;
  JCheckBox displayCheckBox;
  JCheckBox commandCheckBox;
  JButton executeButton;

  JScrollPane topPane;
  JSplitPane splitPane;

  public SqlViewer() {
    super("SqlViewer");
    initComponent();
  }

  void initComponent() {
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    JPanel buttonPanel = new JPanel();
    executeButton =
        new JButton(
            new AbstractAction("Execute") {
              @Override
              public void actionPerformed(ActionEvent e) {
                execute();
              }
            });
    displayCheckBox =
        new JCheckBox(
            new AbstractAction("SQL View") {
              @Override
              public void actionPerformed(ActionEvent e) {
                if (displayCheckBox.isSelected()) {
                  splitPane.setTopComponent(topPane);
                } else {
                  splitPane.setTopComponent(null);
                }
              }
            });
    displayCheckBox.setSelected(true);
    commandCheckBox = new JCheckBox("Command");
    buttonPanel.add(executeButton);
    buttonPanel.add(displayCheckBox);
    buttonPanel.add(commandCheckBox);
    sqlArea = new JTextArea();
    splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    topPane = new JScrollPane(sqlArea);
    splitPane.setTopComponent(topPane);
    tabbedPane = new JTabbedPane();
    splitPane.setBottomComponent(tabbedPane);
    splitPane.setDividerLocation(100);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(buttonPanel, BorderLayout.NORTH);
    getContentPane().add(splitPane, BorderLayout.CENTER);
    setPreferredSize(new Dimension(400, 300));
  }

  void execute() {

    String sql = sqlArea.getText().trim();
    if (sql.isBlank()) {
      JOptionPane.showMessageDialog(this, "Please enter the SQL.");
      return;
    }
    execute(sql);
  }

  void execute(String sql) {
    SqlExecutor executor = new SqlExecutor();
    Table table = null;
    if (sql.toLowerCase(Locale.US).startsWith("select")) {
      table = executor.query(sql);
    } else if (commandCheckBox.isSelected()) {
      table = executor.command(sql);
    } else {
      table = executor.execute(sql);
    }
    if (table.errorMessage != null) {
      addResult(sql, table.errorMessage);
      return;
    }
    addResult(sql, table.createJTable());
  }

  void addResult(String sql, String result) {
    JTextArea outputArea = new JTextArea();
    outputArea.setEditable(false);
    outputArea.setText(result);
    addComponent(sql, outputArea);
  }

  void addResult(String sql, JTable result) {
    result.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    result.setCellSelectionEnabled(true);
    addComponent(sql, result);
  }

  void addComponent(String sql, JComponent component) {

    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    JTextArea sqlArea = new JTextArea();
    sqlArea.setEditable(false);
    sqlArea.setText(sql);
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(new JScrollPane(sqlArea), BorderLayout.CENTER);
    JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
    buttonPanel.add(
        new JButton(
            new AbstractAction("Rerun") {
              @Override
              public void actionPerformed(ActionEvent e) {
                execute(sqlArea.getText());
              }
            }));
    buttonPanel.add(
        new JButton(
            new AbstractAction("Copy") {
              @Override
              public void actionPerformed(ActionEvent e) {
                copy(sqlArea.getText());
              }
            }));
    panel.add(buttonPanel, BorderLayout.EAST);
    splitPane.setTopComponent(panel);
    splitPane.setBottomComponent(new JScrollPane(component));
    splitPane.setDividerLocation(100);
    tabbedPane.addTab(now(), splitPane);
    tabbedPane.setSelectedComponent(splitPane);
  }

  String now() {
    return LocalDateTime.now(ZoneId.systemDefault()).format(formatter);
  }

  void copy(String sql) {
    sqlArea.setText(sql);
  }
}
