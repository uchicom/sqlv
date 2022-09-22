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
