FROM maven:3.5-jdk-8-slim

COPY . /usr/quizduell/

WORKDIR /usr/quizduell/commons/
RUN mvn install

WORKDIR /usr/quizduell/backend/
RUN mvn install

EXPOSE 8080
CMD java -jar /usr/quizduell/backend/target/backend-1.0-SNAPSHOT.jar -host "http://0.0.0.0"