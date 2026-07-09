# Usamos la imagen oficial de Java 23
FROM eclipse-temurin:23-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el jar generado por Maven
COPY target/*.jar app.jar

# Puerto que expone la aplicación
EXPOSE 8083

# Comando para arrancar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]