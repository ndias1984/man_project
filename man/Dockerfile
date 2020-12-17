FROM java:8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} man.jar
ENTRYPOINT ["java","-jar","/man.jar"]
EXPOSE 8080
EXPOSE 3306
CMD java -jar /target/man.jar
