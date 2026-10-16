# Estágio de Build
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio de Execução
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copia o .jar gerado no estágio anterior
COPY --from=build /app/target/*-jar-with-dependencies.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]