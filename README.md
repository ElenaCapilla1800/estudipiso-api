# EstudiPiso API

REST API backend para **EstudiPiso**, una app móvil que ayuda a estudiantes a encontrar habitaciones cerca de universidades españolas.

## Tecnologías

- Java 23
- Spring Boot 4.1.0
- Spring Security + JWT (jjwt 0.12.6)
- Spring Data JPA + Hibernate
- MySQL 8
- Maven
- Docker
- Cloudinary (almacenamiento de fotos)
- Groq API — llama-3.3-70b-versatile (chatbot IA)

## Funcionalidades

- Registro y login con JWT
- Dos roles: `STUDENT` (busca habitaciones) y `OWNER` (publica habitaciones)
- CRUD de habitaciones con filtros (precio, wifi, facturas, mascotas)
- Subida de fotos a Cloudinary
- Opción de compartir ubicación exacta con estudiantes (`showOnMap`)
- Mensajería entre estudiantes y propietarios
- Chatbot IA integrado para ayudar a los estudiantes
- Gestión de perfiles de usuario (nombre y teléfono)
- Gestión de universidades por ciudad y comunidad autónoma

## Requisitos

- Java 23
- Docker (para la base de datos)
- Cuenta en [Cloudinary](https://cloudinary.com) (gratuita)
- API key de [Groq](https://console.groq.com) (gratuita)

## Instalación y ejecución

### 1. Levantar MySQL con Docker

```bash
docker run --name mysql-estudipiso \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=estudipiso \
  -p 3307:3306 \
  -d mysql:8
```

### 2. Configurar application.properties

Crear `src/main/resources/application.properties` (está en `.gitignore`, no subir al repo):

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/estudipiso
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update

jwt.secret=TU_CLAVE_SECRETA_JWT

cloudinary.cloud_name=TU_CLOUD_NAME
cloudinary.api_key=TU_API_KEY
cloudinary.api_secret=TU_API_SECRET

groq.api.key=TU_GROQ_API_KEY
```

### 3. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

La API arranca en `http://localhost:8083`

## Endpoints principales

### Autenticación
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/register` | Registrar usuario (STUDENT u OWNER) |
| POST | `/api/auth/login` | Login → devuelve token JWT |

### Habitaciones
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/rooms?ownerId={id}` | Publicar habitación |
| GET | `/api/rooms` | Buscar con filtros opcionales |
| GET | `/api/rooms/{id}` | Ver detalle |
| GET | `/api/rooms/owner/{ownerId}` | Habitaciones de un propietario |
| PUT | `/api/rooms/{id}?ownerId={id}` | Actualizar habitación |
| DELETE | `/api/rooms/{id}?ownerId={id}` | Eliminar habitación |
| POST | `/api/rooms/{id}/photos` | Subir foto (multipart/form-data) |

**Filtros disponibles para GET /api/rooms:** `maxPrice`, `wifiIncluded`, `billsIncluded`, `petsAllowed`

### Mensajes
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/messages?senderId={id}&receiverId={id}&roomId={id}` | Enviar mensaje |
| GET | `/api/messages/received/{userId}` | Mensajes recibidos |
| GET | `/api/messages/conversation?senderId={id}&receiverId={id}&roomId={id}` | Ver conversación |
| GET | `/api/messages/unread/{userId}` | Contar no leídos |
| PATCH | `/api/messages/{id}/read` | Marcar como leído |

### Usuarios
| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/users/{id}` | Ver perfil de usuario |
| PUT | `/api/users/{id}` | Actualizar nombre y teléfono |

### Chat IA
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/chat` | Enviar pregunta al chatbot |

### Universidades
| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/universities` | Añadir universidad |
| GET | `/api/universities` | Listar todas |
| GET | `/api/universities/city/{city}` | Buscar por ciudad |
| GET | `/api/universities/region/{region}` | Buscar por comunidad |

## Seguridad

Todas las rutas excepto `/api/auth/register` y `/api/auth/login` requieren el header:

## Autor

Elena Capilla Salgado — Java/AI Developer Junior