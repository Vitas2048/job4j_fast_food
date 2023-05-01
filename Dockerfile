FROM maven:3.6.3-openjdk-17

WORKDIR dish_app

COPY . .

RUN mvn package -Dmaven.test.skip=true

CMD ["mvn", "liquibase:update", "-Pdocker"]

CMD ["java", "-jar", "dish/target/dish-1.0-SNAPSHOT.jar"]