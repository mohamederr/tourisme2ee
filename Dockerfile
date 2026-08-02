# 1. Utiliser une image de base contenant Java (assure-toi que la version correspond à celle de ton projet, ex: 17 ou 21)
FROM eclipse-temurin:17-jdk-alpine

# 2. Définir le dossier de travail à l'intérieur du conteneur
WORKDIR /app

# 3. Copier le fichier .jar généré par Maven depuis ton dossier target vers le conteneur
COPY target/*.jar app.jar

# 4. Exposer le port sur lequel ton application Spring Boot écoute (8080 par défaut)
EXPOSE 8080

# 5. La commande pour démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]