# Dockerfile para la aplicación Spring Boot
# Este archivo es opcional y permite dockerizar también la aplicación

# Etapa 1: Build con Maven y Java 21
FROM mcr.microsoft.com/devcontainers/java:1-21-bullseye AS build
WORKDIR /app

# Instalar Maven (última versión)
ARG MAVEN_VERSION=3.9.9

# Configurar DNS y instalar Maven con reintentos
RUN echo "nameserver 8.8.8.8" > /etc/resolv.conf && \
    echo "nameserver 8.8.4.4" >> /etc/resolv.conf && \
    apt-get update && \
    apt-get install -y wget curl && \
    for i in 1 2 3; do \
        wget --timeout=30 --tries=3 https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz -P /tmp && break || sleep 5; \
    done && \
    tar xf /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm /tmp/apache-maven-${MAVEN_VERSION}-bin.tar.gz

ENV M2_HOME=/opt/maven
ENV MAVEN_HOME=/opt/maven
ENV PATH=${M2_HOME}/bin:${PATH}

# Copiar archivos de configuración Maven
COPY pom.xml .

# Descargar dependencias (se cachea esta capa)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Runtime con Java 21
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Crear usuario no-root para ejecutar la aplicación
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Puerto de la aplicación
EXPOSE 8082

# Variables de entorno (pueden ser sobrescritas)
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

# Punto de entrada
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

