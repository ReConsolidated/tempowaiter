FROM openjdk:17-oracle
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
COPY cacerts usr/java/openjdk-17/lib/security/cacerts
COPY cacerts usr/java/default/lib/security/cacerts
COPY cacerts cacerts
ENTRYPOINT ["java", "-jar", "application.jar" ]