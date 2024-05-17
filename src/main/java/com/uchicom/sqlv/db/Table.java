// (C) 2022 uchicom
package com.uchicom.sqlv.db;

import java.util.List;
import javax.swing.JTable;

public class Table {
  public String[] headers;
  public List<String[]> recordList;
  public String errorMessage;

  public Table(String[] headers, List<String[]> recordList) {
    this.headers = headers;
    this.recordList = recordList;
  }

  public Table(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public JTable createJTable() {
    return new JTable(recordList.toArray(new String[0][0]), headers);
  }

  public int indexAt(String search) {
    int length = headers.length;
    for (int index = 0; index < length; index++) {
      String header = headers[index];
      if (header.equals(search)) {
        return index;
      }
    }
    return -1;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (String header : headers) {
      builder.append(header);
      builder.append(",");
    }
    builder.replace(builder.length() - 1, builder.length(), "\n");
    for (String[] cells : recordList) {
      for (String cell : cells) {
        builder.append(cell);
        builder.append(",");
      }
      builder.replace(builder.length() - 1, builder.length(), "\n");
    }
    return builder.toString();
  }
}
