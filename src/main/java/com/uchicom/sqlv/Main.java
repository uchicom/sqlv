// (C) 2022 uchicom
package com.uchicom.sqlv;

import com.uchicom.sqlv.db.Context;
import com.uchicom.sqlv.job.Job;
import com.uchicom.sqlv.ui.window.SqlViewer;
import com.uchicom.util.Parameter;
import java.io.File;
import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args) {
    var parameter = new Parameter(args);
    if (parameter.is("file") && parameter.is("job")) {
      new Main().execute(parameter.getFile("file"), parameter.get("job"));
    } else {
      new Main().execute();
    }
  }

  void execute(File configFile, String jobName) {
    try {
      var job = Job.create(configFile, jobName);
      job.execute();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  void execute() {
    SwingUtilities.invokeLater(
        () -> {
          SqlViewer viewer = new SqlViewer();
          viewer.pack();
          viewer.setVisible(true);
        });
    Runtime.getRuntime().addShutdownHook(new Thread(Context::close));
  }
}
