# Kotlin Quarkus Blueprint

[![CI](https://github.com/juanMaAV92/kotlin-quarkus-blueprint/actions/workflows/ci.yml/badge.svg)](https://github.com/juanMaAV92/kotlin-quarkus-blueprint/actions/workflows/ci.yml)
![Quarkus](https://img.shields.io/badge/Quarkus-3.30-4695EB?logo=quarkus&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2-7F52FF?logo=kotlin&logoColor=white)
![JVM](https://img.shields.io/badge/JVM-21-orange)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Template para microservicios y APIs con **Quarkus** y **Kotlin**, bajo **Arquitectura Hexagonal** e integrado con `com.juanmaav:kotlin-utils`.

## Stack

- **Runtime**: Quarkus 3.30.4
- **Lenguaje**: Kotlin 2.2.21 / Java 21
- **Observabilidad**: OpenTelemetry (W3C Trace Context)
- **Core Library**: `kotlin-utils` (Flow Engine, Structured Logging, Retry, Validation)
- **Health Check**: SmallRye Health (`/health`)

## Arquitectura Hexagonal

```
com.juanmaav.blueprint
├── api/              # Adaptadores de entrada
│   ├── controllers/  #   REST endpoints
│   ├── dto/          #   Request/Response DTOs
│   ├── exception/    #   Exception mappers (HttpException, PlatformException)
│   └── filters/      #   Response filters (X-Trace-Id)
├── core/             # Dominio y logica de negocio
│   ├── domain/       #   Modelos de dominio (User, Page)
│   ├── port/input/   #   Input ports (CreateUser, GetUser, ListUsers)
│   ├── port/output/  #   Output ports (UserRepository)
│   └── usecase/      #   Casos de uso
│       ├── context/  #     Flow contexts
│       └── steps/    #     Steps (validacion, persistencia, auditoria, etc.)
└── infra/            # Adaptadores de salida y config
    ├── persistence/  #   InMemoryUserRepository (adaptador del puerto)
    ├── client/       #   Clientes externos
    └── config/       #   LoggerConfig, JacksonConfig
```

## Ejemplo: Users (create / get / list)

El dominio `users` muestra la arquitectura end to end:

| Método | Ruta | Caso de uso | Notas |
|---|---|---|---|
| `POST` | `/users` | `CreateUserUseCase` | Orquestado con **FlowEngine** (saga): validar → persistir (retry) → en paralelo auditar/indexar → email async, con compensación |
| `GET` | `/users/{id}` | `GetUserUseCase` | Lectura simple; 404 si no existe |
| `GET` | `/users?page=1&limit=10` | `ListUsersUseCase` | Lectura paginada |

La persistencia se hace a través del puerto de salida `UserRepository`. El adaptador
incluido es **in-memory** (`infra/persistence`), así el blueprint corre sin infraestructura
y los tests no necesitan base de datos. Cambiarlo por un adaptador real (Hibernate Reactive
Panache, JDBC, …) no toca el `core`: solo se implementa el mismo puerto.

> Las lecturas (`get`/`list`) son consultas simples y **no** usan saga — la orquestación
> solo se justifica en el flujo de creación multi-paso.


## Perfiles

| Perfil | Uso | OTel | Log Level |
|---|---|---|---|
| `dev` | `./gradlew quarkusDev` | Desactivado | DEBUG |
| `prod` | Docker / deploy | Activo + OTLP exporter | INFO |

## Desarrollo

### Requisitos
- Java 21
- Gradle (wrapper incluido)

### Ejecucion
```bash
./gradlew quarkusDev
```

### Build
```bash
./gradlew build
```

### Docker
```bash
docker build \
  --build-arg GITHUB_USERNAME=tu-usuario \
  --build-arg GITHUB_TOKEN=tu-token \
  -t blueprint .

docker run -p 8080:8080 blueprint
```

## Health Check

```
GET /health          # Liveness + Readiness
GET /health/live     # Liveness
GET /health/ready    # Readiness
```

## Pruebas con Bruno

Para probar los endpoints de forma local, se incluye una colección de **Bruno** en el directorio `api-collection/`.

1.  **Descargar Bruno**: [https://www.usebruno.com/downloads](https://www.usebruno.com/downloads)
2.  **Importar Colección**:
    *   Abre Bruno.
    *   Selecciona "Open Collection" y elige la carpeta `api-collection` de este proyecto.
3.  **Entornos**: La colección incluye entornos (environments) para facilitar las pruebas en `dev` y `prod`.
