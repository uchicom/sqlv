// (C) 2022 uchicom
package com.uchicom.sqlv.job;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Task {

  public String name;
  public String jdbcUrl;
  public String username;
  public String password;
  public String sql;

  boolean execute() throws SQLException {
    try (var con = DriverManager.getConnection(jdbcUrl, username, password)) {
      try (var state = con.createStatement()) {
        var result = state.execute(sql);
        con.commit();
        return result;
      } catch (SQLException e) {
        con.rollback();
        throw e;
      }
    }
  }
}
