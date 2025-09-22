// (C) 2025 uchicom
package com.uchicom.sqlv.ssh;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SshUserInfo implements UserInfo, UIKeyboardInteractive {
  private final String password;
  private String passphrase;

  public SshUserInfo(String password, String passphrase) {
    this.password = password;
    this.passphrase = passphrase;
  }

  @Override
  public String getPassword() {
    return passwd;
  }

  @Override
  public boolean promptYesNo(String str) {
    Object[] options = {"yes", "no"};
    int foo =
        JOptionPane.showOptionDialog(
            null,
            str,
            "Warning",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[0]);
    return foo == 0;
  }

  String passwd;
  JTextField passwordField = (JTextField) new JPasswordField(20);

  @Override
  public String getPassphrase() {
    return passphrase;
  }

  @Override
  public boolean promptPassphrase(String message) {
    return true;
  }

  @Override
  public boolean promptPassword(String message) {
    Object[] ob = {passwordField};
    int result = JOptionPane.showConfirmDialog(null, ob, message, JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
      passwd = passwordField.getText();
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void showMessage(String message) {
    JOptionPane.showMessageDialog(null, message);
  }

  final GridBagConstraints gbc =
      new GridBagConstraints(
          0,
          0,
          1,
          1,
          1,
          1,
          GridBagConstraints.NORTHWEST,
          GridBagConstraints.NONE,
          new Insets(0, 0, 0, 0),
          0,
          0);
  private Container panel;

  @Override
  public String[] promptKeyboardInteractive(
      String destination, String name, String instruction, String[] prompt, boolean[] echo) {
    /*
     * //System.out.println("promptKeyboardInteractive");
     * System.out.println("destination: "+destination);
     * System.out.println("name: "+name);
     * System.out.println("instruction: "+instruction);
     * System.out.println("prompt.length: "+prompt.length);
     * System.out.println("prompt: "+prompt[0]);
     */
    panel = new JPanel();
    panel.setLayout(new GridBagLayout());

    gbc.weightx = 1.0;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.gridx = 0;
    panel.add(new JLabel(instruction), gbc);
    gbc.gridy++;

    gbc.gridwidth = GridBagConstraints.RELATIVE;

    JTextField[] texts = new JTextField[prompt.length];
    for (int i = 0; i < prompt.length; i++) {
      if ("Password: ".equals(prompt[i])) {
        texts[i] = new JTextField(20);
        texts[i].setText(password);
        continue;
      }
      gbc.fill = GridBagConstraints.NONE;
      gbc.gridx = 0;
      gbc.weightx = 1;
      panel.add(new JLabel(prompt[i]), gbc);

      gbc.gridx = 1;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.weighty = 1;
      if (echo[i]) {
        texts[i] = new JTextField(20);
      } else {
        texts[i] = new JPasswordField(20);
      }
      panel.add(texts[i], gbc);
      gbc.gridy++;
    }

    if (texts[0].getText().length() > 0
        || JOptionPane.showConfirmDialog(
                null,
                panel,
                destination + ": " + name,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE)
            == JOptionPane.OK_OPTION) {
      String[] response = new String[prompt.length];
      for (int i = 0; i < prompt.length; i++) {
        response[i] = texts[i].getText();
      }
      return response;
    } else {
      return null; // cancel
    }
  }
}
