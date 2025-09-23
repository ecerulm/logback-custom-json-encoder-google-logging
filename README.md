# Run

```
mvn install
mvn exec:java
```

or

```
mvn package
java -jar target/logback-custom-json-encoder-1.0-SNAPSHOT.jar
```


or 

```
mvn install
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp ./target/classes:$(cat cp.txt) com.rubenlaguna.App
```
