# sqlv
Sql Database viewer

## mvn
### start viewer.
```
mvn exec:java "-Dexec.mainClass=com.uchicom.sqlv.Main"
```

### start job.
```
mvn exec:java "-Dexec.mainClass=com.uchicom.sqlv.Main" "-Dexec.args=-file src/test/resources/config.yml -job job1"
```

## jar
```
java -cp ./target/sqlv-0.0.1-jar-with-dependencies.jar com.uchicom.sqlv.Main
```

### SQLスクリプト実行
```
java -cp /home/gitpod/.m2/repository/com/h2database/h2/2.2.224/h2-2.2.224.jar org.h2.tools.RunScript -url "jdbc:h2:./database/sqlv;CIPHER=AES;" -user sqlv_man -password "sqlv_man sqlv_man" -script ./database/sql/create.sql  -showResults
```
