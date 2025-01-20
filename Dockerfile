#FROM maven:3.9.9-eclipse-temurin-23-alpine as build

# Définir le répertoire de travail (dans le container)
#WORKDIR /app

# Copier le code source dans le conteneur
#COPY . .

# Construire le projet avec Maven
#RUN mvn clean package -DskipTests

# Étape 2 : Utiliser une image OpenJDK 21 pour exécuter l'application
#FROM openjdk:21-jdk-slim

# Copier le fichier JAR généré depuis la phase de build dans /app/gestion-client.jar 
#COPY --from=build /app/target/*.jar /app/gestion-client.jar

# Exposer le port 8080 (Spring Boot par défaut)
#EXPOSE 8080

# Lancer l'application Spring Boot
#ENTRYPOINT ["java", "-jar", "/app/gestion-client.jar"]

