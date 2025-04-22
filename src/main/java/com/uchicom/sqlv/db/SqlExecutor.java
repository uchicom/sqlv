// (C) 2022 uchicom
package com.uchicom.sqlv.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlExecutor {
  public Table query(String sql) {
    try (Connection con = Context.getConnection();
        Statement state = con.createStatement();
        ResultSet result = state.executeQuery(sql); ) {
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
      return new Table(header, resultList);
    } catch (Exception e) {
      e.printStackTrace();
      return new Table(e.getMessage());
    }
  }

  public Table execute(String sql) {
    try (Connection con = Context.getConnection();
        Statement state = con.createStatement()) {
      int count = state.executeUpdate(sql);
      con.commit();
      List<String[]> list = new ArrayList<>();
      list.add(new String[] {String.valueOf(count)});
      return new Table(new String[] {"result"}, list);
    } catch (Exception e) {
      e.printStackTrace();
      return new Table(e.getMessage());
    }
  }

  public Table command(String sql) {
    try (Connection con = Context.getConnection();
        Statement state = con.createStatement()) {
      boolean result = state.execute(sql);
      con.commit();
      List<String[]> list = new ArrayList<>();
      list.add(new String[] {String.valueOf(result)});
      return new Table(new String[] {"result"}, list);
    } catch (Exception e) {
      return new Table(e.getMessage());
    }
  }
}
