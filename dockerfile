FROM adoptopenjdk/openjdk15:alpine-jre
# Add Maintainer Info
LABEL maintainer="mxxxx"
# Add a volume pointing to /tmp
VOLUME /tmp
# Make port 8080 available to the world outside this container
EXPOSE 8080
# The application's jar file
ARG JAR_FILE=utildefiner/target/utilitydefiner-1.0-SNAPSHOT.jar
ADD ${JAR_FILE} utilitydefiner-1.0-SNAPSHOT.jar 
# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom" ,"-Dspring.profiles.active=prod", "-jar","/utilitydefiner-1.0-SNAPSHOT.jar"]
