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
│   ├── domain/       #   Modelos de dominio
│   ├── port/input/   #   Input ports (interfaces)
│   └── usecase/      #   Casos de uso (orquestados por FlowEngine)
│       ├── context/  #     Flow contexts
│       └── steps/    #     Steps (validacion, persistencia, auditoria, etc.)
└── infra/            # Adaptadores de salida y config
    ├── client/       #   Clientes externos
    └── config/       #   LoggerConfig, JacksonConfig
```


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
