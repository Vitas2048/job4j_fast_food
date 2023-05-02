FROM maven:3.6.3-openjdk-17 as maven

WORKDIR /dish_app

COPY . /dish_app

RUN mvn install

FROM openjdk:17.0.2-jdk

WORKDIR /dish_app

COPY --from=maven /dish_app/dish/target/dish-1.0-SNAPSHOT.jar dish_app.jar

CMD ["java", "-jar", "dish_app.jar"]