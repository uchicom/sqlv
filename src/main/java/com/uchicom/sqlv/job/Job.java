// (C) 2022 uchicom
package com.uchicom.sqlv.job;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import org.yaml.snakeyaml.Yaml;

public class Job {
  public List<Task> tasks;

  public static Job create(File file, String name) throws IOException {

    var config =
        new Yaml()
            .loadAs(
                new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8),
                Config.class);
    return config.jobs.get(name);
  }

  public void execute() throws SQLException, ClassNotFoundException {
    for (var task : tasks) {
      System.out.println(task.name + ":" + task.execute());
    }
  }
}
