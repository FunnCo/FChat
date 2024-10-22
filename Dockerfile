# Используйте официальный образ OpenJDK
FROM openjdk:17-jdk-alpine

# Установите рабочую директорию
WORKDIR /app

# Скопируйте jar файл вашего Spring Boot приложения в контейнер
COPY build/libs/FChat-0.0.1-SNAPSHOT.jar app.jar

# Запустите приложение
ENTRYPOINT ["java", "-jar", "app.jar"]