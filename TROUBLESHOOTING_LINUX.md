# Solución de Problemas en Linux

## Error: "unable to resolve host address 'archive.apache.org'"

Este error ocurre cuando Docker no puede resolver nombres DNS durante la construcción de imágenes en sistemas Linux.

### Solución 1: Configurar DNS en Docker Daemon (Recomendado)

1. **Editar o crear el archivo de configuración de Docker:**
   ```bash
   sudo nano /etc/docker/daemon.json
   ```

2. **Agregar los servidores DNS:**
   ```json
   {
     "dns": ["8.8.8.8", "8.8.4.4", "1.1.1.1"]
   }
   ```

3. **Reiniciar el servicio Docker:**
   ```bash
   sudo systemctl restart docker
   ```

4. **Verificar la configuración:**
   ```bash
   docker info | grep DNS
   ```

5. **Ejecutar nuevamente el script:**
   ```bash
   ./docker-start.sh
   ```

### Solución 2: Usar --network=host durante el build

Si la Solución 1 no funciona, puedes modificar temporalmente el docker-compose.yml:

```bash
docker-compose build --no-cache --network=host
docker-compose up -d
```

### Solución 3: Configurar DNS en el archivo de red del sistema

**Para Ubuntu/Debian:**
```bash
sudo nano /etc/systemd/resolved.conf
```

Agregar o descomentar:
```
[Resolve]
DNS=8.8.8.8 8.8.4.4 1.1.1.1
```

Reiniciar el servicio:
```bash
sudo systemctl restart systemd-resolved
```

**Para sistemas con NetworkManager:**
```bash
sudo nano /etc/NetworkManager/NetworkManager.conf
```

Agregar en la sección [main]:
```
[main]
dns=default
```

Luego editar:
```bash
sudo nano /etc/resolv.conf
```

Agregar:
```
nameserver 8.8.8.8
nameserver 8.8.4.4
nameserver 1.1.1.1
```

Reiniciar NetworkManager:
```bash
sudo systemctl restart NetworkManager
```

### Solución 4: Verificar firewall

A veces el firewall bloquea las consultas DNS:

```bash
# UFW
sudo ufw allow 53/tcp
sudo ufw allow 53/udp

# iptables
sudo iptables -A OUTPUT -p tcp --dport 53 -j ACCEPT
sudo iptables -A OUTPUT -p udp --dport 53 -j ACCEPT
```

### Solución 5: Limpiar caché y reconstruir

```bash
# Detener todos los contenedores
docker-compose down

# Limpiar caché de Docker
docker system prune -a --volumes

# Reconstruir sin caché
docker-compose build --no-cache

# Iniciar
docker-compose up -d
```

### Verificación de conectividad

Antes de ejecutar el script, verifica la conectividad:

```bash
# Verificar DNS desde el host
nslookup archive.apache.org
ping -c 4 archive.apache.org

# Verificar DNS desde un contenedor Docker
docker run --rm alpine ping -c 4 archive.apache.org
docker run --rm alpine nslookup archive.apache.org
```

### Alternativa: Usar Maven preinstalado

Si ninguna solución funciona, puedes usar una imagen que ya tenga Maven instalado:

Edita el Dockerfile y cambia la primera línea:
```dockerfile
FROM maven:3.9.9-eclipse-temurin-21 AS build
```

Y elimina la sección de instalación de Maven.

## Otros errores comunes en Linux

### Error: "permission denied" al ejecutar docker-compose

```bash
# Opción 1: Usar sudo
sudo docker-compose up -d

# Opción 2: Agregar tu usuario al grupo docker (recomendado)
sudo usermod -aG docker $USER
newgrp docker

# Verificar
docker ps
```

### Error: "port is already allocated"

```bash
# Ver qué proceso usa el puerto
sudo lsof -i :8082
sudo lsof -i :3306

# Matar el proceso
sudo kill -9 <PID>

# O cambiar el puerto en docker-compose.yml
```

### Error: "no space left on device"

```bash
# Limpiar imágenes y contenedores no usados
docker system prune -a --volumes

# Ver uso de espacio
docker system df
```

## Contacto y Soporte

Si el problema persiste después de probar estas soluciones, por favor:
1. Ejecuta: `docker info > docker-info.txt`
2. Ejecuta: `cat /etc/os-release > os-info.txt`
3. Comparte ambos archivos con el equipo de soporte
