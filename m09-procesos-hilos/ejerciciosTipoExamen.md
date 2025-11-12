# 🧾 Ejercicios Probables — Procesos y Hilos (Tipo Examen)

Este fichero reúne ejercicios probables para practicar temas de procesos (ProcessBuilder), hilos (Thread/Runnable), concurrencia, sincronización, logging y Quartz en Spring Boot.

---

## 1. 🖥️ Ejercicios Probables sobre Procesos

Estos ejercicios se centran en la interacción con el sistema operativo usando `ProcessBuilder` y la gestión del flujo de ejecución.

### Tipo A: Ejecución y Control de Flujo (Similar a Actividad 1)
- Tarea: Encadenamiento Secuencial  
  - Descripción: Crea el Proceso A. Cuando A termina, usa su `exitValue()` para decidir si iniciar el Proceso B.  
  - Habilidades: arranque de procesos, `waitFor()`, lectura de `exitValue()`.

- Tarea: Ejecución Paralela Controlada  
  - Descripción: Lanza tres procesos externos (ej. `notepad.exe`, `calc.exe`, `ping`). Deben ejecutarse concurrentemente, y el programa Java debe esperar a que todos terminen antes de continuar.  
  - Habilidades: arranque concurrente, threads de control o `CompletableFuture`, espera y recolección de estados.

### Tipo B: Redirección de Entrada/Salida (I/O)
- Tarea: Capturar Salida  
  - Descripción: Ejecuta un comando (`dir` / `ls`) y captura su salida en una `String` o archivo en lugar de dejar que se imprima en consola.  
  - Habilidades: `Process.getInputStream()`, lectura de streams, redirección con `ProcessBuilder.redirectOutput()`.

- Tarea: Enviar Entrada  
  - Descripción: Ejecuta un proceso que lee desde stdin y envíale datos desde Java (ej. script que espera respuestas).  
  - Habilidades: `Process.getOutputStream()`, escritura a stdin del proceso, manejo de flushing y cierre.

---

## 2. 🧵 Ejercicios Probables sobre Hilos

Se centran en creación de hilos, coordinación y prevención de errores por concurrencia.

### Tipo C: Creación, Coordinación y Ciclo de Vida
- Tarea: Coordinación con `join()`  
  - Descripción: Crea 4 hilos que ejecutan tareas largas (por ejemplo `sleep(5000)`). El hilo principal inicia los 4, pero solo puede ejecutar su tarea final después de que los dos primeros terminen.  
  - Habilidades: `Thread.start()`, `Thread.join()`, control de dependencias.

- Tarea: Interrupción  
  - Descripción: Crea un hilo con bucle infinito que hace `sleep()` corto. El hilo principal lo inicia y, tras 3 segundos, lo interrumpe. Maneja `InterruptedException` en el hilo.  
  - Habilidades: `interrupt()`, comprobación de `Thread.interrupted()` y manejo de excepciones.

### Tipo D: Sincronización y Condiciones de Carrera (Crítico)
- Tarea: Simulación de Cuenta Bancaria (Race Condition)  
  - Descripción: Clase `CuentaBancaria` con `depositar()` y variable `saldo`. Crea 10 hilos que llaman `depositar()` 100 veces cada uno **sin sincronización**. Mostrar que el saldo final es incorrecto.  
  - Habilidades: efectos de race condition, reproducir resultados no deterministas.

- Tarea: Implementación Sincronizada  
  - Descripción: Repetir el ejercicio anterior y corregir `depositar()` usando `synchronized` (o `ReentrantLock`) para garantizar atomicidad. Verificar saldo correcto.  
  - Habilidades: `synchronized` en métodos/bloques, locks, principios de exclusión mutua.

### Tipo E: Ejercicio de Comunicación de Hilos: Patrón Productor-Consumidor

Crea un programa que simule el patrón Productor-Consumidor utilizando los métodos `wait()` y `notify()`.

**Clases Requeridas:**

1.  **BufferCompartido:** Una clase que simule un almacén (usando una estructura de datos, como una `ArrayList`) con una **capacidad máxima fija** (ej. 5). Debe contener los métodos **`producir()`** y **`consumir()`**.
2.  **Productor:** Una clase `Thread` que continuamente produce números enteros y los deposita en el `BufferCompartido`.
3.  **Consumidor:** Una clase `Thread` que continuamente extrae números enteros del `BufferCompartido`.
4.  **Clase Principal:** Inicia la simulación, creando una instancia del `BufferCompartido` y lanzando un hilo `Productor` y un hilo `Consumidor`.

**Reglas de Sincronización:**

* **Productor:** Debe usar `wait()` cuando el buffer esté **lleno** y usar `notify()` o `notifyAll()` cuando deposite un elemento.
* **Consumidor:** Debe usar `wait()` cuando el buffer esté **vacío** y usar `notify()` o `notifyAll()` cuando extraiga un elemento.

---

## 3. ⚙️ Tipo E: Uso de Recursos de Spring (si aplica)

Si el examen incluye Spring Boot y los temas vistos (logging y Quartz).

- Tarea: Quartz Scheduler  
  - Descripción: Definir una clase `Job`. Configurar `JobDetail` y `Trigger` con una Cron Expression concreta (ej. "cada lunes a las 9:00 AM").  
  - Habilidades: `org.quartz.Job`, `JobDetail`, `Trigger`, `CronScheduleBuilder`, configuración en Spring.

- Tarea: Debugging con Logs  
  - Descripción: En una clase con concurrencia, usar `@Slf4j` para imprimir `DEBUG`, `INFO` y `ERROR` en puntos clave, demostrando uso de niveles de log.  
  - Habilidades: Lombok `@Slf4j`, configuración de `application.properties` para niveles y patrones, seguimiento de ejecución concurrente.

---

## 4. Consejos Rápidos para el Examen
- Saber usar `ProcessBuilder` y leer `InputStream`/`ErrorStream`.  
- Entender diferencia entre `start()` y `run()` en `Thread`.  
- Practicar `synchronized`, `wait()/notify()`, y `join()`.  
- Familiarizarse con `@Slf4j` y niveles de logs en Spring Boot.  
- Reproducir race conditions y corregirlas mediante locks/sincronización.  
- Para Quartz: saber configurar `JobDetail`, `Trigger` y cron expressions.
