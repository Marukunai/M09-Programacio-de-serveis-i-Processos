# 📝 Proyecto: API REST de Tareas Pendientes (ToDo List)

Este documento describe la arquitectura y la funcionalidad de la **API REST de Tareas Pendientes**, desarrollada utilizando **Java** y el framework **Spring Boot**.

El objetivo de esta API es gestionar una lista simple de tareas mediante operaciones **CRUD** (Crear, Leer, Actualizar, Borrar) utilizando los métodos HTTP estándar.

---

## 🏗️ 1. Arquitectura y Tecnologías

| Tecnología | Descripción |
|:---|:---|
| **Java** | Lenguaje principal de desarrollo. |
| **Spring Boot** | Framework que simplifica la configuración y el desarrollo de APIs REST. |
| **Maven** | Herramienta de gestión de dependencias y automatización de la construcción del proyecto. |
| **REST** | Estilo arquitectónico para la comunicación a través de HTTP. |

### Componente Clave: TodoApplication.java

El corazón de la aplicación, donde se define:

* **Modelo de Datos (`Tarea`):** Un `record` simple con `id`, `descripcion` y `completada`.
* **Repositorio (`TareaRepository`):** Una **lista estática en memoria** que simula la base de datos (los datos se pierden al reiniciar el servidor).
* **Controlador (`@RestController`):** Contiene los **6 endpoints (métodos)** que mapean las peticiones HTTP a las funciones de gestión de tareas.

---

## ⚙️ 2. Guía de Ejecución

Para iniciar esta aplicación, se requiere tener **Java (JDK 17 o superior)** y **Maven** configurados en el entorno.

### 2.1. Iniciar el Servidor

1.  **Navega al Directorio:** Abre una terminal (PowerShell o CMD) y navega hasta la carpeta raíz del proyecto (`m09-sockets-servicios_red`), donde se encuentra el archivo `pom.xml`.
2.  **Ejecuta Spring Boot:** Utiliza el plugin de Maven para ejecutar la aplicación.

```bash
# En entornos con Path de Maven configurado:
mvn spring-boot:run

# Si Maven no está en el Path (usando la ruta de IntelliJ):
& "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.1\plugins\maven\lib\maven3\bin\mvn.cmd" spring-boot:run
```

## 2.2. Verificación

El servidor se inicia por defecto en el **puerto 8080**. La terminal mostrará el siguiente mensaje al iniciar con éxito:

> ...
> Tomcat started on port 8080 (http) with context path '/'
> Started TodoApplication in X.XX seconds (...)

Una vez que veas este mensaje, el servidor está listo.

---

## 🚀 3. Endpoints de la API

Todos los endpoints utilizan la ruta base: `http://localhost:8080/api/tareas`

A continuación, se detalla cómo interactuar con cada uno usando la colección de Postman importada.

### A. Crear Tarea (POST)

| Método | URL | Body (JSON) | Descripción |
|:---|:---|:---|:---|
| **POST** | `/api/tareas` | `{"descripcion": "...", "completada": false}` | Añade una tarea. Devuelve **201 Created** con el objeto y el nuevo `id`. |

### B. Leer Tareas (GET)

| Método | URL | Descripción |
|:---|:---|:---|
| **GET** | `/api/tareas` | Obtiene **todas** las tareas. |
| **GET** | `/api/tareas/{id}` | Obtiene una tarea específica. (Ej: `/api/tareas/1`) |

### C. Actualizar Tareas (PUT y PATCH)

| Método | URL | Body (JSON) | Uso |
|:---|:---|:---|:---|
| **PUT** | `/api/tareas/{id}` | `{"id": 1, "descripcion": "nuevo valor", ...}` | **Actualización Completa:** Reemplaza el objeto completo. |
| **PATCH** | `/api/tareas/{id}/completada?estado=true` | (Vacío) | **Actualización Parcial:** Cambia solo el estado usando un *Query Parameter*. |

### D. Eliminar Tarea (DELETE)

| Método | URL | Descripción |
|:---|:---|:---|
| **DELETE** | `/api/tareas/{id}` | Elimina la tarea por su ID. Devuelve **204 No Content**. |

---

## 🧪 4. Guía de Pruebas con Postman

Sigue estos pasos en el orden indicado para probar todos los métodos CRUD:

| Paso | Acción | Endpoint a Ejecutar | Verificación de Resultado |
|:---|:---|:---|:---|
| **1.** | Ejecutar **POST** - Crear Nueva Tarea | `POST /api/tareas` | Obtén el `id` asignado (normalmente **1**). |
| **2.** | Ejecutar **GET** - Obtener Tarea por ID | `GET /api/tareas/1` | Recibe el objeto recién creado. |
| **3.** | Ejecutar **PUT** - Actualización COMPLETA | `PUT /api/tareas/1` | El cuerpo de respuesta debe reflejar la **nueva descripción** y el estado. |
| **4.** | Ejecutar **PATCH** - Actualización Parcial | `PATCH /api/tareas/1/completada?estado=true` | El campo `completada` debe cambiar a **`true`** en la respuesta. |
| **5.** | Ejecutar **DELETE** - Eliminar Tarea | `DELETE /api/tareas/1` | El código de estado debe ser **204 No Content**. |
| **6.** | Ejecutar **GET** - Obtener TODAS las Tareas | `GET /api/tareas` | El array de respuesta debe estar **vacío (`[]`)** ya que la tarea fue eliminada. |