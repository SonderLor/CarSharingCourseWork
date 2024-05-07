# Используем официальный образ OpenJDK 17
FROM openjdk:17-jdk-slim AS build

# Устанавливаем переменную окружения для директории приложения
ENV APP_HOME=/app

# Создаем директорию для приложения
WORKDIR $APP_HOME

# Копируем JAR файл приложения в директорию приложения
COPY build/libs/*.jar app.jar

# Запускаем приложение при старте контейнера
ENTRYPOINT ["java","-jar","app.jar"]
