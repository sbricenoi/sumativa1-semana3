# 🍳 Recetas Seguras - Aplicación Web con Spring Security

Aplicación web segura desarrollada con **Spring Boot**, **Spring Security** y **Thymeleaf**, cumpliendo con los estándares **OWASP Top 10** para la actividad sumativa de la semana 3.

---

## 📋 Descripción del Proyecto

Sistema web de gestión de recetas de cocina que implementa:

- ✅ Autenticación y autorización con Spring Security
- ✅ Protección de URLs (públicas y privadas)
- ✅ Encriptación de contraseñas con BCrypt
- ✅ Protección contra CSRF
- ✅ Headers de seguridad configurados
- ✅ Cumplimiento de OWASP Top 10

### Funcionalidades Implementadas

#### Páginas Públicas (Sin autenticación)
- **Página de Inicio**: Muestra recetas populares y recientes
- **Búsqueda de Recetas**: Búsqueda por nombre, tipo de cocina, país y dificultad
- **Login**: Formulario de inicio de sesión

#### Páginas Privadas (Requieren autenticación)
- **Detalle de Receta**: Vista completa con ingredientes, instrucciones, tiempo, etc.

---

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security 6**
- **Spring Data JPA**
- **Thymeleaf**
- **MySQL** (o H2 para pruebas)
- **Maven**
- **Bootstrap CSS** (custom)

---

## 📦 Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener instalado:

1. **Java JDK 17 o superior**
   ```bash
   java -version
   ```

2. **Maven 3.6 o superior**
   ```bash
   mvn -version
   ```

3. **Docker Desktop** (Recomendado - forma más fácil)
   ```bash
   docker --version
   ```

4. **Git** (para clonar el repositorio)
   ```bash
   git --version
   ```

---

## 🚀 Instalación y Configuración

### Paso 1: Clonar o Descargar el Proyecto

```bash
# Si está en Git
git clone [URL_DEL_REPOSITORIO]
cd sumativa_1_semana_3

# O simplemente extraer el archivo ZIP en una carpeta
```

### Paso 2: Configurar Base de Datos

#### 🐳 Opción A: Usar Docker Compose (⭐ RECOMENDADO)

Esta es la forma más fácil y rápida. Todo está preconfigurado.

```bash
# 1. Iniciar MySQL con Docker
./docker-start.sh

# 2. Esperar 10-15 segundos a que MySQL esté listo

# 3. Iniciar la aplicación
mvn spring-boot:run -Dspring-boot.run.profiles=docker
```

**¡Listo!** La aplicación estará en `http://localhost:8082`

📖 **Ver documentación completa:** [DOCKER_README.md](./DOCKER_README.md)

#### Opción B: Usar MySQL Local

1. **Iniciar MySQL:**
   ```bash
   # Linux/Mac
   sudo systemctl start mysql
   
   # Windows
   net start MySQL80
   ```

2. **Crear la base de datos:**
   ```bash
   mysql -u root -p
   ```
   
   Luego ejecutar:
   ```sql
   source database/schema.sql
   source database/data.sql
   exit;
   ```

3. **Configurar credenciales:**
   
   Editar `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/recetas_db
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_CONTRASEÑA
   ```

#### Opción C: Usar H2 (Base de datos en memoria)

1. En `application.properties`, comentar MySQL y descomentar H2:
   ```properties
   # MySQL
   #spring.datasource.url=jdbc:mysql://localhost:3306/recetas_db
   
   # H2
   spring.datasource.url=jdbc:h2:mem:recetas_db
   spring.datasource.driverClassName=org.h2.Driver
   spring.h2.console.enabled=true
   ```

2. **Nota:** Con H2, los datos se perderán al reiniciar la aplicación.

### Paso 3: Compilar el Proyecto

```bash
mvn clean install
```

O si no tienes Maven instalado globalmente:
```bash
./mvnw clean install  # Linux/Mac
mvnw.cmd clean install  # Windows
```

### Paso 4: Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

O si usas el wrapper de Maven:
```bash
./mvnw spring-boot:run  # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```

### Paso 5: Acceder a la Aplicación

Abrir el navegador y visitar:
```
http://localhost:8082
```

**⚠️ IMPORTANTE:** La aplicación usa el puerto **8082** (NO 8080).
- ✅ Correcto: `http://localhost:8082`
- ❌ Incorrecto: `http://localhost:8080` (Apache de otro proyecto)

---

## 👤 Usuarios de Prueba

La aplicación viene con 4 usuarios precargados:

| Usuario | Contraseña | Rol | Descripción |
|---------|------------|-----|-------------|
| `admin` | `admin123` | ADMIN, USER | Administrador del sistema |
| `usuario1` | `usuario123` | USER | Usuario estándar |
| `usuario2` | `usuario123` | USER | Usuario estándar |
| `chef` | `usuario123` | USER | Usuario chef |

**✅ ACTUALIZADAS:** Las contraseñas han sido verificadas y están funcionando correctamente con BCrypt.

**📝 Nota:** Para simplificar las pruebas, usuario1, usuario2 y chef comparten la misma contraseña (usuario123).

**⚠️ IMPORTANTE:** En producción, cambiar todas las contraseñas por unas más seguras y únicas.

---

## 🗺️ Estructura del Proyecto

```
sumativa_1_semana_3/
├── src/
│   ├── main/
│   │   ├── java/com/duoc/recetas/
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java          # Configuración de Spring Security
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java          # Controlador principal
│   │   │   │   └── RecetaController.java        # Controlador de recetas
│   │   │   ├── model/
│   │   │   │   ├── Usuario.java                 # Entidad Usuario
│   │   │   │   ├── Rol.java                     # Entidad Rol
│   │   │   │   └── Receta.java                  # Entidad Receta
│   │   │   ├── repository/
│   │   │   │   ├── UsuarioRepository.java       # Repositorio de usuarios
│   │   │   │   ├── RolRepository.java           # Repositorio de roles
│   │   │   │   └── RecetaRepository.java        # Repositorio de recetas
│   │   │   ├── service/
│   │   │   │   ├── UserDetailsServiceImpl.java  # Servicio de autenticación
│   │   │   │   └── RecetaService.java           # Servicio de recetas
│   │   │   └── RecetasApplication.java          # Clase principal
│   │   └── resources/
│   │       ├── static/
│   │       │   └── css/
│   │       │       └── style.css                # Estilos CSS
│   │       ├── templates/
│   │       │   ├── index.html                   # Página de inicio
│   │       │   ├── login.html                   # Página de login
│   │       │   ├── buscar.html                  # Página de búsqueda
│   │       │   ├── detalle.html                 # Página de detalle (privada)
│   │       │   └── error.html                   # Página de error
│   │       └── application.properties           # Configuración de la app
├── database/
│   ├── schema.sql                                # Script de creación de tablas
│   └── data.sql                                  # Script de datos de prueba
├── docs/
│   └── GUIA_COMPLETA_ACTIVIDAD.md               # Guía completa de la actividad
├── pom.xml                                       # Dependencias Maven
└── README.md                                     # Este archivo
```

---

## 🔒 Características de Seguridad Implementadas

### OWASP Top 10 Compliance

| OWASP | Vulnerabilidad | Implementación |
|-------|----------------|----------------|
| **A01** | Broken Access Control | ✅ URLs protegidas con Spring Security |
| **A02** | Cryptographic Failures | ✅ Contraseñas encriptadas con BCrypt (fuerza 12) |
| **A03** | Injection | ✅ JPA/Hibernate con consultas parametrizadas |
| **A04** | Insecure Design | ✅ Arquitectura segura con capas |
| **A05** | Security Misconfiguration | ✅ Headers de seguridad configurados |
| **A06** | Vulnerable Components | ✅ Dependencias actualizadas (Spring Boot 3.2) |
| **A07** | Authentication Failures | ✅ Spring Security con autenticación robusta |
| **A08** | Data Integrity Failures | ✅ Protección CSRF habilitada |
| **A09** | Logging Failures | ✅ Logging configurado en application.properties |
| **A10** | SSRF | ✅ Validación de URLs |

### Configuraciones de Seguridad

```java
// SecurityConfig.java
- CSRF Protection: ✅ Habilitado con CookieCsrfTokenRepository
- Password Encoding: ✅ BCrypt con fuerza 12
- Session Management: ✅ Máximo 1 sesión por usuario
- Headers Security: ✅ X-Frame-Options, XSS-Protection
- URL Protection: ✅ Rutas públicas/privadas definidas
```

---

## 🧪 Probar la Aplicación

### 1. Probar Páginas Públicas

- Visita `http://localhost:8082` → Debe mostrar la página de inicio
- Visita `http://localhost:8082/buscar` → Debe mostrar búsqueda
- Intenta acceder a `http://localhost:8082/detalle/1` → Debe redirigir al login

### 2. Probar Autenticación

- Ir a `http://localhost:8082/login`
- Ingresar: `admin` / `admin123`
- Debe redirigir a la página de inicio con sesión iniciada

### 3. Probar Páginas Privadas

- Con sesión iniciada, visita `http://localhost:8082/detalle/1`
- Debe mostrar el detalle de la receta

### 4. Probar Logout

- Click en "Cerrar Sesión"
- Debe cerrar sesión y redirigir al login

---

## 📊 Análisis con ZAP Proxy

### Instalación de ZAP

1. Descargar desde: https://www.zaproxy.org/download/
2. Instalar siguiendo el asistente
3. Ejecutar ZAP

### Configuración

1. Abrir ZAP
2. Ir a Tools > Options > Local Proxies
3. Configurar puerto (por defecto 8080, cambiar si es necesario)

### Realizar Escaneo

1. **Escaneo Automático:**
   - En ZAP, ir a "Quick Start"
   - URL: `http://localhost:8082`
   - Click en "Attack"

2. **Escaneo Manual:**
   - Navegar por la aplicación con ZAP como proxy
   - Click derecho en la URL > Attack > Active Scan

3. **Revisar Resultados:**
   - Panel "Alerts" muestra vulnerabilidades encontradas
   - Clasificadas por criticidad: Alta, Media, Baja

### Generar Reporte

- Tools > Generate HTML Report
- Guardar para incluir en el informe

---

## 📝 Generar Informe

El informe debe incluir:

1. **Instalación de ZAP** (capturas de pantalla)
2. **Ejecución del análisis** (capturas)
3. **Vulnerabilidades encontradas** (tabla con criticidad)
4. **Análisis de OWASP 10** (cuáles se encontraron)
5. **Correcciones implementadas** (código antes/después)
6. **Verificación final** (nuevo escaneo sin vulnerabilidades)

---

## 🎥 Grabar Video Demo

### Contenido del Video (8-10 minutos)

1. **Introducción** (1 min)
   - Presentación del equipo
   - Descripción del proyecto

2. **Demo de la Aplicación** (4-5 min)
   - Mostrar página de inicio
   - Mostrar búsqueda de recetas
   - Intentar acceder a detalle sin login
   - Hacer login
   - Mostrar detalle de receta
   - Hacer logout

3. **Explicación de Seguridad OWASP 10** (4-5 min)
   - Mostrar análisis con ZAP
   - Explicar vulnerabilidades encontradas
   - Mostrar correcciones en código
   - Mostrar verificación final

---

## 🐳 Docker - Infraestructura Completa

Este proyecto incluye una configuración completa de Docker Compose que facilita el despliegue.

### 📦 Inicio Rápido con Docker

```bash
# 1. Iniciar base de datos MySQL
./docker-start.sh

# 2. Iniciar aplicación con perfil Docker
mvn spring-boot:run -Dspring-boot.run.profiles=docker

# Acceder a: http://localhost:8082
```

### 🔧 Comandos Disponibles

```bash
# Iniciar servicios
./docker-start.sh

# Detener servicios (mantiene datos)
./docker-stop.sh

# Resetear todo (elimina datos)
./docker-reset.sh
```

### 📖 Documentación Completa

Para información detallada sobre Docker, configuración, troubleshooting y más, consulta:

**📄 [DOCKER_README.md](./DOCKER_README.md)**

### 🚀 Dockerizar la Aplicación (Opcional)

Si quieres ejecutar también la aplicación en Docker:

```bash
# Compilar JAR
mvn clean package -DskipTests

# Construir imagen Docker
docker build -t recetas-seguras .

# La imagen ya está lista en docker-compose.yml
# Descomenta la sección 'app' en docker-compose.yml

# Ejecutar todo el stack
docker-compose up -d
```

---

## 🚨 Solución de Problemas Comunes

### Error: "No se puede conectar a MySQL"

```bash
# Verificar que MySQL esté corriendo
sudo systemctl status mysql  # Linux
# o
net start MySQL80  # Windows

# Verificar credenciales en application.properties
```

### Error: "Puerto 8082 en uso"

```bash
# Ver qué está usando el puerto
lsof -i :8082

# Cambiar puerto en application.properties
server.port=8083
```

### Error: "Lombok no funciona"

```bash
# Asegurarse de tener el plugin de Lombok en tu IDE
# IntelliJ: Settings > Plugins > Lombok
# Eclipse: Instalar lombok.jar manualmente
```

### Error: "Las contraseñas no funcionan"

```bash
# Las contraseñas están encriptadas con BCrypt
# Usar las credenciales exactas del README
# Si creaste nuevos usuarios, encriptar con:
# https://bcrypt-generator.com/
```

---

## 📚 Recursos Adicionales

- [Documentación Spring Boot](https://spring.io/projects/spring-boot)
- [Documentación Spring Security](https://spring.io/projects/spring-security)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [ZAP Proxy Documentation](https://www.zaproxy.org/docs/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
