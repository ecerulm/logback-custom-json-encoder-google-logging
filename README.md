# Run

```
mvn compile exec:java
[INFO] Scanning for projects...
[INFO]
[INFO] ------------< com.rubenlaguna:logback-custom-json-encoder >-------------
[INFO] Building logback-custom-json-encoder 1.0-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- resources:3.3.1:resources (default-resources) @ logback-custom-json-encoder ---
[INFO] Copying 1 resource from src/main/resources to target/classes
[INFO]
[INFO] --- compiler:3.13.0:compile (default-compile) @ logback-custom-json-encoder ---
[INFO] Nothing to compile - all classes are up to date.
[INFO]
[INFO] --- exec:3.5.1:java (default-cli) @ logback-custom-json-encoder ---
{"timestampSeconds":1758610671,"timestampNanos":258630000,"severity":"INFO","message":"Hello World owODKl, iteration: 0"}
{"timestampSeconds":1758610671,"timestampNanos":260865000,"severity":"INFO","message":"Hello World owqg0I, iteration: 1"}
{"timestampSeconds":1758610671,"timestampNanos":260907000,"severity":"INFO","message":"Hello World AbjKZj, iteration: 2"}
{"timestampSeconds":1758610671,"timestampNanos":260937000,"severity":"INFO","message":"Hello World FX0Vax, iteration: 3"}
{"timestampSeconds":1758610671,"timestampNanos":260964000,"severity":"INFO","message":"Hello World K7gaTK, iteration: 4"}
{"timestampSeconds":1758610671,"timestampNanos":260988000,"severity":"INFO","message":"Hello World UVhzXW, iteration: 5"}
{"timestampSeconds":1758610671,"timestampNanos":261010000,"severity":"INFO","message":"Hello World 368UBt, iteration: 6"}
{"timestampSeconds":1758610671,"timestampNanos":261032000,"severity":"INFO","message":"Hello World Rq878E, iteration: 7"}
{"timestampSeconds":1758610671,"timestampNanos":261053000,"severity":"INFO","message":"Hello World XPbyAK, iteration: 8"}
{"timestampSeconds":1758610671,"timestampNanos":261079000,"severity":"INFO","message":"Hello World nqjMDx, iteration: 9"}
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.270 s
[INFO] Finished at: 2025-09-23T08:57:51+02:00
[INFO] ------------------------------------------------------------------------
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
