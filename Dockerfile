FROM openjdk:17-oracle
COPY target/tempowaiter-*.jar application.jar
ENTRYPOINT ["java","-XX:MaxRAM=128m", "-Xmx64m", "-Xms32m", "-jar", "application.jar"]