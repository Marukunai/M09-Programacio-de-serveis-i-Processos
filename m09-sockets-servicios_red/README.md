# 📚 Unidad Formativa 3 (UF3): Programación de Servicios y Procesos de Red

Este repositorio contiene el material de estudio y los proyectos prácticos desarrollados para la **Unidad Formativa 3 (UF3)** del Módulo M09.

El contenido se enfoca en dos grandes áreas: la comunicación a bajo nivel (**Sockets TCP/UDP**) y el desarrollo de servicios a alto nivel (**APIs REST con Spring Boot**).

---

## 🧭 Índice de Contenidos

Utiliza los siguientes enlaces para navegar a la documentación detallada de cada concepto o proyecto.

| Documento | Contenido Principal | Acceso Directo |
|:---|:---|:---|
| **`sockets.md`** | Bases de la Comunicación en Red: Documentación sobre la arquitectura y la implementación de **Sockets TCP** (orientados a conexión) y **UDP** (sin conexión) en Java. | [Ver Sockets TCP/UDP](./sockets.md) |
| **`servicios-api.md`** | Servicios Web y Spring Boot: Documentación y conceptos clave sobre el desarrollo de **servicios de red avanzados**, el uso de Maven y la introducción al framework **Spring Boot** para crear APIs REST. | [Ver Servicios API](./servicios-api.md) |
| **`TaskApplication.md`** | Documentación de la API REST: Guía completa del proyecto práctico, incluyendo la arquitectura del controlador, el repositorio en memoria y las instrucciones detalladas para la prueba de los **6 endpoints CRUD** (Create, Read, Update, Delete) con Postman. | [Ver API ToDo](./TaskApplication.md) |

---

## 🛠️ Estructura del Proyecto

La estructura del repositorio está organizada de la siguiente manera:

```text
m09-sockets-servicios_red/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       └── TodoApplication.java (Código fuente de la API)
├── pom.xml (Archivo de configuración de Maven)
├── queries.json (Archivo de importación de los endpoints del proyecto de tasks)
├── README.md           <-- ESTE ARCHIVO
├── sockets.md          <-- Documentación UF3: Sockets
├── servicios-api.md    <-- Documentación UF3: Servicios y API REST
└── TaskApplication.md  <-- Documentación de la API de Tareas
```