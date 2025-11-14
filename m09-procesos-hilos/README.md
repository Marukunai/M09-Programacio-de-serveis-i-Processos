# 📚 Módulo de Concurrencia y Procesos (UF2)

Este repositorio contiene todo el material de estudio, ejercicios resueltos en Java y recursos de autoevaluación para la **Unidad Formativa 2: Gestión de Procesos y Concurrencia**.

---

## 📝 Documentación y Temario

| Documento | Enfoque | Descripción |
| :--- | :--- | :--- |
| `procesos.md` | **Temario UF2 - Parte 1** | Conceptos, clases y métodos clave para la **gestión de procesos externos** (`ProcessBuilder`, `Process`, E/S de Streams). |
| `hilos.md` | **Temario UF2 - Parte 2** | Conceptos, clases y métodos clave para la **Concurrencia y Hilos en Java** (`Thread`, `Runnable`, `synchronized`, `wait/notify`, `ReentrantLock`). |

---

## 💡 Ejercicios

| Documento | Tipo | Objetivo |
| :--- | :--- | :--- |
| `enunciados.md` | **Ejercicios Simples** | Tareas básicas y directas para practicar la sintaxis y los mecanismos fundamentales. |
| `ejerciciosTipoExamen.md` | **Ejercicios Complejos** | Problemas que simulan escenarios reales, ideales para la preparación de exámenes. |

---

## 💻 Código Fuente

Todos los ejercicios, ejemplos de temario y soluciones están implementados en Java (`*.java`) y se encuentran en la raíz de este repositorio.

### Hilos

| Archivo | Enfoque | Descripción |
| :--- | :--- | :--- |
| **ProductorConsumidor.java** | Sincronización Avanzada | Implementa el patrón Productor-Consumidor utilizando `wait()` y `notifyAll()` en un recurso compartido (`BufferCompartido`) para gestionar el flujo de datos y evitar condiciones de lleno/vacío. |
| **HiloActividad.java** | Concurrencia Básica | Muestra la ejecución en paralelo natural de hilos (Thread vs Runnable) sin forzar un orden. |
| **HiloActividadOrdenado.java** | Secuencialidad Estricta | Fuerza una ejecución secuencial (un hilo termina antes de que el siguiente comience) usando `start()` seguido inmediatamente de `join()`. |
| **HiloActividadMixto.java** | Sincronización Condicional | Usa `CountDownLatch` para implementar dependencias de inicio específicas, logrando paralelismo con control estricto de la secuencia de inicio (e.g., Hilo B espera la iteración N de Hilo A). |
| **HiloActividadSincronizada.java** | Sincronización Secuencial | Implementa un orden estricto de inicio a fin usando una cadena de `CountDownLatch` para que el Hilo N espere al Hilo N-1. |
| **HiloCoordinacionInterrupcion.java** | Coordinación | Muestra el manejo de interrupciones (`interrupt()`) y cómo los hilos responden mientras están bloqueados en `sleep()` o `wait()`. |
| **HiloSincronizacion.java** | Recursos Compartidos | Ejemplo que utiliza mecanismos como `synchronized` o `wait/notify` para gestionar el acceso seguro a recursos compartidos y evitar condiciones de carrera. |

### Procesos

| Archivo | Enfoque | Descripción |
| :--- | :--- | :--- |
| **ProcesoActividad.java** | Creación y Control | Ejecuta procesos externos en paralelo (como `notepad.exe` y `echo`), utilizando `ProcessBuilder` y controlando su terminación con `waitFor()`. |
| **ProcesoCapturaSalida.java** | Redirección de E/S | Muestra cómo capturar la salida (`InputStream`) de un proceso externo (`ping`) y leerla línea por línea desde el programa Java. |
| **ProcesoSecuencialCondicional.java** | Encadenamiento y `exitValue` | Demuestra la ejecución secuencial condicional, donde el inicio del Proceso B depende del código de salida (`exitValue`) del Proceso A. |