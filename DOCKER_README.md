# 🐳 Guía de Docker - Sistema de Recetas

## 📋 Índice

- [Descripción](#descripción)
- [Requisitos Previos](#requisitos-previos)
- [Estructura de Docker](#estructura-de-docker)
- [Inicio Rápido](#inicio-rápido)
- [Comandos Disponibles](#comandos-disponibles)
- [Configuración](#configuración)
- [Solución de Problemas](#solución-de-problemas)

---

## 📖 Descripción

Este proyecto incluye una configuración completa de Docker Compose que levanta una instancia de MySQL para la base de datos de la aplicación. Esto permite tener un entorno de desarrollo reproducible y fácil de configurar.

### Servicios Incluidos

- **MySQL 8.0**: Base de datos principal
  - Puerto: `3306`
  - Base de datos: `recetas_db`
  - Usuario: `recetas_user` / `recetas_pass`
  - Usuario root: `root` / `root123`

---

## 🔧 Requisitos Previos

### 1. Docker Desktop

Debes tener Docker Desktop instalado en tu sistema:

- **Mac**: [Descargar Docker Desktop para Mac](https://www.docker.com/products/docker-desktop)
- **Windows**: [Descargar Docker Desktop para Windows](https://www.docker.com/products/docker-desktop)
- **Linux**: Instalar Docker Engine y Docker Compose

### 2. Verificar Instalación

```bash
# Verificar Docker
docker --version
# Salida esperada: Docker version 24.x.x o superior

# Verificar Docker Compose
docker-compose --version
# o
docker compose version
```

---

## 🏗️ Estructura de Docker

```
sumativa_1_semana_3/
├── docker-compose.yml          # Configuración de servicios Docker
├── env.example                 # Ejemplo de variables de entorno
├── docker-start.sh            # Script para iniciar servicios
├── docker-stop.sh             # Script para detener servicios
├── docker-reset.sh            # Script para resetear todo
└── database/
    ├── schema.sql             # Script de creación de tablas
    └── data.sql               # Script de datos iniciales
```

### Archivos Principales

#### `docker-compose.yml`
Define los servicios Docker:
- Servicio MySQL con configuración completa
- Volúmenes para persistencia de datos
- Red interna para comunicación entre servicios
- Scripts de inicialización automática

#### Scripts de Gestión
- `docker-start.sh`: Inicia todos los servicios
- `docker-stop.sh`: Detiene los servicios (mantiene los datos)
- `docker-reset.sh`: Resetea todo (elimina datos)

---

## 🚀 Inicio Rápido

### Para macOS / Windows

```bash
# 1. Ir al directorio del proyecto
cd "ruta/del/proyecto"

# 2. Iniciar servicios Docker
./docker-start.sh

# 3. Esperar a que MySQL esté listo (10-15 segundos)

# 4. Iniciar la aplicación Spring Boot
mvn spring-boot:run -Dspring-boot.run.profiles=docker
```

### Para Linux (Recomendado)

```bash
# 1. Ir al directorio del proyecto
cd "ruta/del/proyecto"

# 2. Dar permisos de ejecución (solo primera vez)
chmod +x docker-start-linux.sh

# 3. Iniciar servicios Docker
./docker-start-linux.sh

# La aplicación se construirá e iniciará automáticamente
# Accede a: http://localhost:8082
```

**Nota para Linux:** Si encuentras errores de DNS, consulta el archivo `TROUBLESHOOTING_LINUX.md`

### Opción 2: Comandos Manuales

```bash
# 1. Iniciar servicios
docker-compose up -d

# 2. Ver logs (opcional)
docker-compose logs -f mysql

# 3. Verificar que esté corriendo
docker ps

# 4. Iniciar aplicación
mvn spring-boot:run -Dspring-boot.run.profiles=docker
```

---

## 📝 Comandos Disponibles

### Scripts Automatizados

```bash
# Iniciar todos los servicios
./docker-start.sh

# Detener servicios (mantiene los datos)
./docker-stop.sh

# Resetear completamente (BORRA DATOS)
./docker-reset.sh
```

### Comandos Docker Compose

```bash
# Iniciar servicios en background
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs solo de MySQL
docker-compose logs -f mysql

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes (BORRA DATOS)
docker-compose down -v

# Reiniciar un servicio específico
docker-compose restart mysql

# Ver estado de servicios
docker-compose ps
```

### Comandos Docker Directos

```bash
# Listar contenedores activos
docker ps

# Entrar a MySQL desde línea de comandos
docker exec -it recetas_mysql mysql -uroot -proot123

# Ejecutar consulta SQL directamente
docker exec recetas_mysql mysql -uroot -proot123 -e "USE recetas_db; SELECT * FROM usuarios;"

# Ver logs del contenedor
docker logs recetas_mysql

# Inspeccionar contenedor
docker inspect recetas_mysql

# Ver uso de recursos
docker stats recetas_mysql
```

---

## ⚙️ Configuración

### Variables de Entorno

Las variables de entorno se pueden configurar en el archivo `docker-compose.yml`:

```yaml
environment:
  MYSQL_ROOT_PASSWORD: root123
  MYSQL_DATABASE: recetas_db
  MYSQL_USER: recetas_user
  MYSQL_PASSWORD: recetas_pass
```

### Persistencia de Datos

Los datos se almacenan en un volumen Docker llamado `mysql_data`:

```bash
# Ver volúmenes
docker volume ls

# Inspeccionar volumen
docker volume inspect sumativa_1_semana_3_mysql_data

# Eliminar volumen (BORRA DATOS)
docker volume rm sumativa_1_semana_3_mysql_data
```

### Scripts de Inicialización

Los archivos SQL en `database/` se ejecutan automáticamente al crear el contenedor:

1. `01-schema.sql` - Crea las tablas
2. `02-data.sql` - Inserta datos iniciales

**Nota:** Los scripts solo se ejecutan la primera vez o después de eliminar el volumen.

---

## 🔧 Configuración de Spring Boot

### Perfiles de Spring

El proyecto tiene dos perfiles configurados:

#### Perfil por Defecto (`application.properties`)
```properties
# Conecta a MySQL en puerto 3307 (contenedor existente)
spring.datasource.url=jdbc:mysql://localhost:3307/recetas_db
```

#### Perfil Docker (`application-docker.properties`)
```properties
# Conecta a MySQL en puerto 3306 (Docker Compose)
spring.datasource.url=jdbc:mysql://localhost:3306/recetas_db
spring.datasource.username=recetas_user
spring.datasource.password=recetas_pass
```

### Usar el Perfil Docker

```bash
# Opción 1: Línea de comandos
mvn spring-boot:run -Dspring-boot.run.profiles=docker

# Opción 2: Variable de entorno
export SPRING_PROFILES_ACTIVE=docker
mvn spring-boot:run
```

---

## 🐛 Solución de Problemas

### Error: Puerto 3306 ya está en uso

```bash
# Ver qué está usando el puerto
lsof -i :3306

# Detener otro contenedor MySQL
docker stop <container_name>

# O cambiar el puerto en docker-compose.yml
ports:
  - "3307:3306"  # Usa puerto 3307 en host
```

### Error: No se puede conectar a MySQL

```bash
# 1. Verificar que el contenedor esté corriendo
docker ps | grep recetas_mysql

# 2. Ver logs del contenedor
docker logs recetas_mysql

# 3. Verificar salud del contenedor
docker inspect recetas_mysql | grep Health -A 10

# 4. Probar conexión manual
docker exec -it recetas_mysql mysql -uroot -proot123 -e "SELECT 1"
```

### Los Scripts de Inicialización No Se Ejecutan

Los scripts SQL solo se ejecutan en la primera inicialización. Para forzar la reinicialización:

```bash
# Opción 1: Usar script de reset
./docker-reset.sh

# Opción 2: Manual
docker-compose down -v
docker volume rm sumativa_1_semana_3_mysql_data
docker-compose up -d
```

### Error: "Access Denied" al Conectar

Verifica las credenciales en `application-docker.properties`:

```properties
spring.datasource.username=recetas_user
spring.datasource.password=recetas_pass
```

O usa las credenciales root:
```properties
spring.datasource.username=root
spring.datasource.password=root123
```

### Ver Todas las Bases de Datos

```bash
docker exec recetas_mysql mysql -uroot -proot123 -e "SHOW DATABASES;"
```

### Ver Todas las Tablas

```bash
docker exec recetas_mysql mysql -uroot -proot123 -e "USE recetas_db; SHOW TABLES;"
```

### Backup de la Base de Datos

```bash
# Crear backup
docker exec recetas_mysql mysqldump -uroot -proot123 recetas_db > backup.sql

# Restaurar backup
cat backup.sql | docker exec -i recetas_mysql mysql -uroot -proot123 recetas_db
```

---

## 📊 Monitoreo

### Ver Estado de Servicios

```bash
# Estado general
docker-compose ps

# Uso de recursos
docker stats recetas_mysql

# Logs en tiempo real
docker-compose logs -f
```

### Acceso Directo a MySQL

```bash
# Línea de comandos MySQL
docker exec -it recetas_mysql mysql -uroot -proot123

# Luego dentro de MySQL:
USE recetas_db;
SHOW TABLES;
SELECT * FROM usuarios;
```

---

## 🔄 Flujo de Trabajo Completo

### Desarrollo Diario

```bash
# 1. Iniciar Docker
./docker-start.sh

# 2. Desarrollar código
# (Editar archivos, hacer cambios)

# 3. Ejecutar aplicación
mvn spring-boot:run -Dspring-boot.run.profiles=docker

# 4. Probar en el navegador
# http://localhost:8082

# 5. Al terminar, detener servicios
./docker-stop.sh
```

### Resetear Ambiente Completo

```bash
# 1. Detener aplicación Spring Boot (Ctrl+C)

# 2. Resetear Docker (borra datos)
./docker-reset.sh

# 3. Iniciar aplicación
mvn spring-boot:run -Dspring-boot.run.profiles=docker
```

---

## 📦 Integración Continua

Para usar en CI/CD:

```bash
# En .github/workflows/ci.yml o similar
- name: Start MySQL
  run: docker-compose up -d mysql

- name: Wait for MySQL
  run: |
    until docker exec recetas_mysql mysql -uroot -proot123 -e "SELECT 1"; do
      sleep 3
    done

- name: Run Tests
  run: mvn test -Dspring.profiles.active=docker
```

---

## 🎯 Mejores Prácticas

1. **No commitear credenciales reales** en `docker-compose.yml`
2. **Usar volúmenes** para persistencia de datos en producción
3. **Hacer backups regulares** de la base de datos
4. **Monitorear recursos** con `docker stats`
5. **Revisar logs** regularmente con `docker-compose logs`
6. **Usar healthchecks** para verificar estado de servicios

---

## 📚 Referencias

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [MySQL Docker Image](https://hub.docker.com/_/mysql)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)

---

## ✅ Checklist de Verificación

- [ ] Docker Desktop instalado y corriendo
- [ ] Scripts tienen permisos de ejecución (`chmod +x *.sh`)
- [ ] Puerto 3306 disponible
- [ ] Aplicación configurada con perfil `docker`
- [ ] Base de datos inicializada con datos de prueba
- [ ] Credenciales de usuario verificadas

---

**¿Necesitas ayuda?** Revisa la sección de [Solución de Problemas](#solución-de-problemas) o contacta al equipo de desarrollo.

