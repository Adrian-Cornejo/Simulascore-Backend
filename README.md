# SimulaScore Backend

Este es el backend del proyecto **SimulaScore**, desarrollado con **Play Framework** y **Scala**. Proporciona una API para gestionar las operaciones de la plataforma.

## 🚀 Tecnologías utilizadas

- **Play Framework** (backend en Scala)
- **PostgreSQL** (base de datos)
- **Slick** (ORM para acceso a datos)
- **JWT** (Autenticación segura)
- **Docker** (para contenedores)
- **Swagger** (Documentación de API)

## 📋 Requisitos previos

Antes de instalar el proyecto, asegúrate de tener:

- **Java 11+** instalado
- **sbt** (Scala Build Tool) instalado
- **PostgreSQL** configurado
- **Docker** (opcional, para entorno de desarrollo)

## 🔧 Instalación y ejecución

### 1️⃣ Clonar el repositorio

```sh
git clone https://github.com/usuario/simulascore-backend.git
cd simulascore-backend
```

### 2️⃣ Configurar variables de entorno

Crea un archivo `.env` en la raíz del proyecto y agrega:

```ini
DB_URL=jdbc:postgresql://localhost:5432/simulascore
DB_USER=tu_usuario
DB_PASSWORD=tu_contraseña
JWT_SECRET=clave_secreta
```

### 3️⃣ Instalar dependencias y compilar el proyecto

```sh
sbt compile
```

### 4️⃣ Ejecutar la aplicación

```sh
sbt run
```

El servidor se ejecutará en **http://localhost:9000**

## 📖 Uso de la API

Puedes probar los endpoints usando **Swagger** en:

```
http://localhost:9000/docs
```

Ejemplo de llamada a la API con `curl`:

```sh
curl -X GET http://localhost:9000/api/usuarios -H "Authorization: Bearer <TOKEN>"
```

## 🐳 Docker (opcional)

Si deseas ejecutar el backend en un contenedor Docker:

```sh
docker-compose up -d
```

## 📌 Contribución

Si deseas contribuir:

1. **Haz un fork** del repositorio.
2. **Crea una nueva rama** (`git checkout -b feature/nueva-funcionalidad`).
3. **Realiza cambios y haz commits** (`git commit -m "Nueva funcionalidad"`).
4. **Envía un Pull Request**.

## 📄 Licencia

Este proyecto está bajo la licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.
