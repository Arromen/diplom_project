# Этап 1: Сборка приложения из исходных кодов
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Копируем конфигурацию Maven и исходный код
COPY pom.xml .
COPY src ./src

# Компилируем проект и собираем jar-архив, пропуская unit-тесты для ускорения
RUN mvn clean package -DskipTests

# Этап 2: Запуск готового приложения
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Копируем собранный jar-файл из предыдущего этапа
COPY --from=build /app/target/diplom_project_2-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт, на котором работает Spring Boot
EXPOSE 8080

# Команда для запуска приложения внутри контейнера
ENTRYPOINT ["java", "-jar", "app.jar"]