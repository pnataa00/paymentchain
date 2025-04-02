# Usa una imagen base mínima con JRE 21
FROM eclipse-temurin:21-jre-alpine

# Configurar usuario rootless
RUN addgroup -g 1028 devopsc && \
    adduser -D -G devopsc admin

# Crear a new mount point
VOLUME /tmp



# Copiar el JAR en la imagen (se pasará como argumento)
ARG JAR_FILE
COPY ${JAR_FILE} /tmp/app.jar

# Cambiar propietario del directorio a usuario rootless
RUN chown -R admin:devopsc /tmp



# Cambiar a usuario rootless
USER admin

# Comando por defecto para ejecutar la aplicación
#ENTRYPOINT ["java", "-jar", "/tmp/app.jar"]
ENTRYPOINT  ["sh", "-c", "java $JAVA_OPTS -jar /tmp/app.jar"]
