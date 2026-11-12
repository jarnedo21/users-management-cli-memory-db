# Users CLI con DDD y arquitectura hexagonal

Ejemplo pedagógico simple para comprender las bases de Domain-Driven Design (DDD) y arquitectura hexagonal en Java, sin depender de frameworks avanzados ni contenedores de inyección de dependencias.

## Objetivo

El proyecto muestra cómo estructurar y organizar el código separando las reglas de negocio, los casos de uso, los puertos y los adaptadores.

La intención no es construir una aplicación lista para producción. El objetivo es facilitar el aprendizaje de los conceptos fundamentales antes de incorporar herramientas como Spring, bases de datos, proveedores de caché, ORM o mecanismos automáticos de inyección de dependencias.

## Casos de uso

La aplicación permite gestionar usuarios desde una interfaz de línea de comandos:

1. Registrar un usuario.
2. Buscar un usuario por ID.
3. Listar los usuarios registrados.

Además, desde la opción **4 - Gestionar clientes** del menú principal, se administran los clientes del CEA **Agencia de Castings** (empresas de moda o de publicidad y cine) con operaciones CRUDL:

| Operación | Opción del submenú | Caso de uso (puerto de entrada) | Puerto de salida | Adaptador en memoria |
|---|---|---|---|---|
| Create | 1 - Registrar cliente | `AgregarClienteUseCase` | `GuardarClientePort` | `GuardarClienteAdapter` |
| Read | 2 - Buscar por código | `ObtenerClienteUseCase` | `ObtenerClientesPort` | `ObtenerClientesAdapter` |
| List | 3 - Ver todos | `ObtenerClienteUseCase` | `ObtenerClientesPort` | `ObtenerClientesAdapter` |
| Update | 4 - Actualizar cliente | `ActualizarClienteUseCase` | `ActualizarClientePort` | `ActualizarClienteAdapter` |
| Delete | 5 - Eliminar cliente | `EliminarClienteUseCase` | `EliminarClientePort` | `EliminarClienteAdapter` |

Los datos se almacenan en memoria durante la ejecución de la aplicación.

## Arquitectura

El proyecto aplica arquitectura hexagonal para mantener las reglas de negocio independientes de la interfaz de usuario y del mecanismo de persistencia.

```text
src/main/java/com/jcaa/udec/collections
├── domain
│   ├── core
│   │   ├── exception
│   │   ├── model
│   │   └── valueobject
│   └── port
│       └── out
├── application
│   └── service
├── adapter
│   └── persistence
│       └── memory
└── entrypoint
    ├── cli
    └── controller
```

### Dominio

Contiene el modelo `Usuario`, las excepciones y los Value Objects responsables de proteger las invariantes:

- `UsuarioId`
- `NombreUsuario`
- `Password`
- `Email`

También contiene el modelo `Cliente` de la Agencia de Castings, con sus excepciones (`ClienteInvalidoException`, `ClienteNoExisteException`, `ClienteYaExisteException`) y Value Objects:

- `CodigoCliente`: entre 3 y 20 caracteres, solo letras, números y guion. Es único y no distingue mayúsculas.
- `NombreCliente`: mínimo 3 caracteres.
- `DireccionCliente`: mínimo 5 caracteres.
- `TelefonoCliente`: entre 7 y 15 dígitos, puede iniciar con `+`.
- `PersonaContacto`: mínimo 3 caracteres.
- `TipoActividad`: `MODA` o `PUBLICIDAD_CINE`.

Los puertos de salida (`domain/port/out`) son interfaces que el dominio define y que la infraestructura implementa.

### Aplicación

Contiene los servicios que coordinan los casos de uso y se comunican con el exterior mediante puertos. Los puertos de entrada (`application/service/ports/in`) exponen los casos de uso; los comandos y consultas (`dto`) transportan los datos, y los mappers los convierten en entidades de dominio.

### Adaptadores

Implementan persistencia en memoria para guardar y consultar usuarios y clientes (`UsuariosMemoria`, `ClientesMemoria`). No son mocks: son implementaciones concretas no durables.

### Entrypoints

Incluyen la interfaz CLI (`GuiCli` y el submenú `ClienteCli`) y los controladores que transforman las entradas del usuario en comandos de aplicación.

### Flujo de ejecución (ejemplo: registrar un cliente)

```text
ClienteCli  →  ClienteControlador  →  AgregarClienteUseCase  →  GuardarClientePort  →  GuardarClienteAdapter
(captura y     (petición →            (AgregarClienteService:     (interfaz del          (lista en memoria
 valida)        CrearClienteComando)   mapea a Cliente)            dominio)               ClientesMemoria)
```

La lógica de negocio (`domain` y `application`) no conoce la CLI ni la lista en memoria: solo depende de interfaces. Cambiar la persistencia a una base de datos solo requiere un nuevo adaptador que implemente los mismos puertos y ensamblarlo en `Main`.

## Decisiones pedagógicas

- Las dependencias se ensamblan manualmente en `Main`.
- No se utiliza Spring ni un contenedor de inyección de dependencias.
- No se utiliza una base de datos.
- No se utiliza ORM.
- No se utiliza caché.
- La persistencia en memoria permite observar con claridad el flujo entre capas.

## Requisitos

- Java 17 o superior.
- Maven 3.9 o superior.

## Ejecutar pruebas

```bash
mvn clean install
```

## Revisar cobertura

El build genera el reporte JaCoCo en:

```text
target/site/jacoco/index.html
```

Para revisar la cobertura diferencial respecto a `main`:

```bash
diff-cover target/site/jacoco/jacoco.xml --compare-branch=main
```
Debes instalar **diff-cover** que es una utilidad desarrollando sobre Python.

## Ejecutar la aplicación

```bash
mvn org.codehaus.mojo:exec-maven-plugin:3.5.0:java -Dexec.mainClass=com.jcaa.udec.Main
```
