FROM maven:3.8.6-jdk-11 as MAVEN_BUILD

ADD ./pom.xml pom.xml
ADD ./src src/

RUN mvn clean -DskipTests=true package

FROM openjdk:11

COPY --from=MAVEN_BUILD /target/e-shop-spring-boot-data-rest-postgresql-reactjs-docker-2.5.2.jar /e-shop-postgres.jar

ENTRYPOINT ["java","-jar","e-shop-postgres.jar"]

