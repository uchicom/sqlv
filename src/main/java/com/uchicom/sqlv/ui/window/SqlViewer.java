// (C) 2022 uchicom
package com.uchicom.sqlv.ui.window;

import com.uchicom.sqlv.db.Context;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class SqlViewer extends JFrame {
  JTextArea sqlArea;

  JTabbedPane tabbedPane;
  JCheckBox displayCheckBox;
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
    buttonPanel.add(executeButton);
    buttonPanel.add(displayCheckBox);
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
    try (Connection con = Context.getConnection();
        Statement state = con.createStatement();
        ResultSet result = state.executeQuery(sqlArea.getText()); ) {
      ResultSetMetaData metaData = result.getMetaData();
      int columnCount = metaData.getColumnCount();
      String[] header = new String[columnCount];
      for (int i = 0; i < columnCount; i++) {
        header[i] = metaData.getColumnLabel(i + 1);
      }
      List<String[]> resultList = new ArrayList<>();
      while (result.next()) {
        String[] row = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
          row[i] = result.getString(i + 1);
        }
        resultList.add(row);
      }

      addResult(new JTable(resultList.toArray(new String[0][0]), header));
    } catch (Exception e) {
      e.printStackTrace();
      addResult(e.getMessage());
    }
  }

  void addResult(String result) {
    JTextArea outputArea = new JTextArea();
    outputArea.setEditable(false);
    outputArea.setText(result);
    tabbedPane.addTab(now(), new JScrollPane(outputArea));
  }

  void addResult(JTable result) {
    result.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    result.setCellSelectionEnabled(true);
    tabbedPane.addTab(now(), new JScrollPane(result));
  }

  String now() {
    return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
  }
}
