// (C) 2022 uchicom
package com.uchicom.sqlv.db;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.uchicom.sqlv.ssh.SshUserInfo;
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

  private Properties sshProperties;

  private HikariDataSource ds;

  private Session session;

  private Context() {
    try {
      // Hikari設定
      dbProperties =
          ResourceUtil.createProperties(
              new File(
                  System.getProperty("com.uchicom.sqlv.db", "src/main/resources/db.properties")),
              "UTF-8");
      // ssh設定
      var sshConfigFile =
          new File(System.getProperty("com.uchicom.sqlv.ssh", "src/main/resources/ssh.properties"));
      if (sshConfigFile.exists()) {
        sshProperties = ResourceUtil.createProperties(sshConfigFile, "UTF-8");
        if (Boolean.parseBoolean(sshProperties.getProperty("ssh", "false"))) {
          connectSsh();
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
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
    context.closeSssh();
  }

  void connectSsh() throws JSchException {
    var jsch = new JSch();
    jsch.setKnownHosts(sshProperties.getProperty("ssh.known_hosts"));
    if (sshProperties.getProperty("ssh.key_file") != null) {
      if (sshProperties.getProperty("ssh.passphrase") != null) {
        jsch.addIdentity(
            sshProperties.getProperty("ssh.key_file"), sshProperties.getProperty("ssh.passphrase"));
      } else {
        jsch.addIdentity(sshProperties.getProperty("ssh.key_file"));
      }
    }
    var username = sshProperties.getProperty("ssh.user");
    var hostname = sshProperties.getProperty("ssh.host");
    var port =
        sshProperties.getProperty("ssh.port") != null
            ? Integer.parseInt(sshProperties.getProperty("ssh.port"))
            : 22;

    session = jsch.getSession(username, hostname, port);
    var ui = new SshUserInfo("", sshProperties.getProperty("ssh.passphrase"));
    session.setUserInfo(ui);
    session.connect();
    if (sshProperties.getProperty("ssh.forwarding.remote.host") != null) {
      session.setPortForwardingL(
          Integer.parseInt(sshProperties.getProperty("ssh.forwarding.local.port")),
          sshProperties.getProperty("ssh.forwarding.remote.host"),
          Integer.parseInt(sshProperties.getProperty("ssh.forwarding.remote.port")));
    }
  }

  void closeSssh() {
    if (session != null && session.isConnected()) {
      session.disconnect();
      System.out.println("ssh close!");
    }
  }
}
