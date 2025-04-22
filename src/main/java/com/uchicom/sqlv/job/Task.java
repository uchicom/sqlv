// (C) 2022 uchicom
package com.uchicom.sqlv.job;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Task {

  public String name;
  public String driver;
  public String jdbcUrl;
  public String username;
  public String password;
  public String sql;

  boolean execute() throws SQLException, ClassNotFoundException {
    if (driver != null) {
      Class.forName(driver);
    }
    try (var con = DriverManager.getConnection(jdbcUrl, username, password)) {
      con.setAutoCommit(false);
      try (var state = con.createStatement()) {
        var result = state.execute(sql);
        con.commit();
        return result;
      } catch (SQLException e) {
        try {
          con.rollback();
        } catch (Exception e1) {
          throw e;
        }
        throw e;
      }
    }
  }
}
