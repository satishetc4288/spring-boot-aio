FROM openjdk:21-jdk
COPY target/SprngBootAllInOne-1.0-SNAPSHOT.jar .
CMD ["java", "-jar", "/SprngBootAllInOne-1.0-SNAPSHOT.jar"]