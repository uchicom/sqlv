// (C) 2022 uchicom
package com.uchicom.sqlv.db;

import com.uchicom.util.ResourceUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Context {
  private static final Context context = new Context();

  private final Properties dbProperties;

  private HikariDataSource ds;

  private Context() {
    // Hikari設定
    dbProperties =
        ResourceUtil.createProperties(
            new File(System.getProperty("com.uchicom.sqlv.db", "src/main/resources/db.properties")),
            "UTF-8");
  }

  private synchronized HikariDataSource getDs(boolean create) {
    if (create && (ds == null || ds.isClosed())) {
      HikariConfig config = new HikariConfig(dbProperties);
      ds = new HikariDataSource(config);
    }
    return ds;
  }

  /**
   * データベースをオープンして取得します。
   *
   * @return DBオブジェクト
   */
  public static Connection getConnection() throws SQLException {
    return context.getDs(true).getConnection();
  }

  public static void close() {
    HikariDataSource ds = context.getDs(false);
    if (ds != null) {
      ds.close();
      System.out.println("close!");
    }
  }
}
