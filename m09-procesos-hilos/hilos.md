# 📚 Apuntes para Examen: Programación Multihilos en Java (Hilos)

## 1. 🧵 Conceptos Fundamentales del Multihilo

La **Programación Multihilos** es un enfoque donde un programa ejecuta múltiples tareas de manera **simultánea** o **concurrente** dentro de un **único proceso** utilizando varios **hilos de ejecución**.

### ¿Qué es un Hilo?

Un **hilo** (Thread) es la **unidad más pequeña de procesamiento** que puede ser gestionada por el sistema operativo.

* **Ligeros:** Son más ligeros y eficientes de crear que los procesos.
* **Memoria Compartida:** Todos los hilos dentro del mismo proceso **comparten el mismo espacio de memoria** y recursos del proceso, lo que permite una comunicación más eficiente.

### 📈 Ventajas del Multihilo

* **Mejora del Rendimiento:** Aprovecha mejor los recursos, permitiendo que las tareas se realicen en paralelo.
* **Mayor Capacidad de Respuesta:** La interfaz de usuario puede seguir siendo funcional mientras las tareas pesadas se ejecutan en segundo plano.
* **Eficiencia en Recursos:** Reduce el costo general al compartir el espacio de memoria del proceso.
* **Uso Óptimo de Multiprocesador:** Permite distribuir el trabajo entre múltiples núcleos de CPU (paralelismo real).

### 🆚 Multiproceso vs. Multihilo

| Característica | Multiproceso | Multihilo |
| :--- | :--- | :--- |
| **Definición** | Múltiples **procesos** ejecutándose. | Múltiples **hilos** ejecutándose dentro de un **único proceso**. |
| **Espacio de Memoria** | **Propio y separado** para cada proceso. | **Compartido** entre todos los hilos del proceso. |
| **Costo (Overhead)** | **Más costoso** en creación y recursos. | **Más ligero y eficiente** en creación y comunicación. |
| **Aislamiento** | Alto. Un fallo en un proceso no afecta a los demás. | Bajo. Un fallo en un hilo puede afectar a todo el proceso. |

---

## 2. 📝 Creación y Estructura de Hilos en Java

Existen dos formas principales de crear hilos de ejecución en Java:

### Opción 1: Extender la Clase `Thread`

```java
class MiHilo extends Thread {
    @Override
    public void run() {
        // Código que ejecutará el hilo
        System.out.println("El hilo está corriendo.");
    }
}

// Uso:
MiHilo hilo = new MiHilo();
hilo.start(); // Inicia un nuevo hilo de ejecución
```

### Opción 2: Implementar la Interfaz `Runnable` (Opción Preferida)

```java
class MiRunnable implements Runnable {
    @Override
    public void run() {
        // Código que ejecutará el hilo
        System.out.println("El hilo está corriendo.");
    }
}

// Uso:
MiRunnable runnable = new MiRunnable();
Thread hilo = new Thread(runnable); // Encapsular Runnable en un objeto Thread
hilo.start(); // Inicia un nuevo hilo de ejecución
```

### 🎯 Comparación: Thread vs. Runnable

| Característica | Runnable (Preferido) | Thread (Alternativa) |
| :--- | :--- | :--- |
| **Herencia** | Permite heredar de otras clases (Java solo tiene herencia única). | No permite heredar de otra clase (ya extiende Thread). |
| **Modularidad** | Separación de responsabilidades (lógica del trabajo separada de la gestión del hilo). | La lógica del hilo y la gestión están acopladas. |
| **Reutilización** | La misma instancia de Runnable puede usarse para múltiples objetos Thread. | Cada hilo requiere una nueva instancia de la subclase Thread. |

---

## 3. 🚨 Concurrencia y Sincronización

Cuando múltiples hilos acceden y modifican los mismos datos compartidos, pueden surgir problemas de inconsistencia de datos.

### Condiciones de Carrera (Race Conditions)

Ocurren cuando el resultado de una operación depende del **orden de ejecución** de múltiples hilos que acceden a los mismos recursos.

### Sincronización

Es el mecanismo para garantizar que **solo un hilo a la vez** pueda acceder a una **sección crítica** de código (recurso compartido).

### Uso de la Palabra Clave `synchronized`

La palabra clave `synchronized` en Java se utiliza para controlar el acceso concurrente a un bloque de código o a un método.

```java
public class MiClase {
    private int contador = 0;

    // El método sincronizado garantiza que solo un hilo acceda a la vez
    public synchronized void incrementar() {
        contador++; // Sección crítica
    }

    // Se puede sincronizar un bloque de código específico:
    public void disminuir() {
        // Bloque sincronizado usando el objeto 'this' como monitor
        synchronized (this) {
            contador--;
        }
    }
}
```

---

## 4. ⚙️ Gestión y Ciclo de Vida de los Hilos

### Estados de un Hilo (Ciclo de Vida)

Un hilo pasa por varios estados desde su creación hasta su finalización:

* **Nuevo (New):** Se crea la instancia, pero no se ha llamado a `start()`.
* **Ejecutable (Runnable):** Se ha llamado a `start()`. El hilo está listo para ejecutarse o está siendo ejecutado por el planificador del S.O.
* **Bloqueado (Blocked):** Está esperando para adquirir un monitor lock (generalmente por entrar a un bloque `synchronized`).
* **En Espera (Waiting):** Espera indefinidamente a que otro hilo realice una acción (ej. llamando a `wait()` o `join()` sin tiempo).
* **En Espera con Tiempo (Timed Waiting):** Espera por un tiempo específico (ej. llamando a `sleep(millis)` o `join(millis)`).
* **Terminado (Terminated):** El método `run()` ha finalizado o ha ocurrido una excepción no controlada.

### Métodos Clave de la Clase Thread

| Método | Descripción | Estado Involucrado |
| :--- | :--- | :--- |
| `start()` | Inicia la ejecución del hilo. Llama a `run()` en un nuevo hilo. (¡No llamar a `run()` directamente!) | Pasa de New a Runnable. |
| `run()` | Contiene el código que se ejecutará. Se sobrescribe en Thread o se implementa en Runnable. | — |
| `sleep(millis)` | Suspende la ejecución del hilo actual por el tiempo especificado. | Pasa a Timed Waiting. |
| `join()` | El hilo actual espera a que el hilo sobre el que se llama termine su ejecución. | Pasa a Waiting o Timed Waiting. |
| `interrupt()` | Envía una señal de interrupción. Si el hilo está bloqueado (sleep, wait, join), lanza `InterruptedException`. | — |
| `setName(String)` | Asigna un nombre personalizado al hilo. | — |
| `getName()` | Devuelve el nombre del hilo. | — |
| `isAlive()` | Devuelve `true` si el hilo ha comenzado y aún no ha terminado. | New → false; Runnable, Blocked, Waiting → true; Terminated → false. |
| `Thread.currentThread()` | Método estático que devuelve una referencia al hilo que se está ejecutando en ese momento. | — |

### Asignación de Nombres

* **Por Defecto:** `Thread-X` (donde X es un número).
* **Personalizado (Constructor):** `new Thread("Mi-Nombre")`
* **Personalizado (setName):** `hilo.setName("Hilo-Personalizado");`

### 💡 Uso de `start()` vs. `run()`

* **`hilo.start()`:** Crea un nuevo hilo de ejecución y ejecuta el código de `run()` en paralelo. **(Forma Correcta)**
* **`hilo.run()`:** Ejecuta el código de `run()` en el hilo actual (generalmente el hilo principal o main), sin crear un nuevo hilo concurrente. **(Forma Incorrecta para Multihilo)**

## 5. 🪵 Logging con SLF4J y Lombok

### 5.1. Introducción a SLF4J y Lombok

**SLF4J (Simple Logging Facade for Java)** es una **API de registro (Facade)**.

* **Función:** Actúa como un **puente** o abstracción. Permite que el código de tu aplicación use una única API de logging sin depender de una implementación específica (como Logback, Log4j, o `java.util.logging`).
* **Ventaja:** Puedes cambiar el framework de logging subyacente sin modificar una sola línea de código en tu aplicación.

**@Slf4j (Lombok):**

* **Función:** Es una anotación de la librería **Lombok** que, durante la compilación, genera automáticamente un campo estático y final llamado `log` de tipo `org.slf4j.Logger` en tu clase.
* **Sintaxis:** Permite usar `log.info(...)`, `log.error(...)`, etc., de forma concisa.

### 5.2. Configuración y Uso

**Dependencias Clave (Maven):**

```xml
<dependencies>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>2.0.9</version>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.4.11</version>
    </dependency>
</dependencies>
```

El **logging** es fundamental para monitorear la ejecución de aplicaciones multihilo, permitiendo rastrear el comportamiento de múltiples hilos y detectar problemas de concurrencia.

### 5.3. Uso de `@Slf4j`

Lombok proporciona la anotación `@Slf4j` que genera automáticamente un campo `log` en la clase, simplificando el acceso a SLF4J (Simple Logging Facade for Java).

```java
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogExample {
    public static void main(String[] args) {
        // El campo 'log' es generado automáticamente por Lombok
        log.info("Este es un mensaje informativo");
        log.warn("Este es un mensaje de advertencia");
        log.error("Este es un mensaje de error");
    }
}
```

### 5.4. Niveles de Log

El nivel de log define la severidad de un mensaje y controla qué mensajes son visibles en la salida:

| Nivel | Descripción | Propósito |
| :--- | :--- | :--- |
| **TRACE** | Información detallada de depuración. | Seguir el flujo de ejecución granular. |
| **DEBUG** | Información útil para la depuración. | Monitorear variables y pasos importantes. |
| **INFO** | Información general sobre la ejecución. | Mensajes de inicio, fin de tarea, etc. (Producción). |
| **WARN** | Advertencias sobre posibles problemas. | Situaciones inesperadas pero recuperables. |
| **ERROR** | Errores que impiden la ejecución de una parte de la aplicación. | Fallos críticos, excepciones no controladas. |

### 5.5. Configuración en Spring Boot

En aplicaciones Spring Boot, los niveles de log se configuran típicamente en `application.properties`:

```properties
# Nivel de log global (por defecto, solo se muestran WARN, ERROR)
logging.level.root=INFO 

# Nivel de log específico (ejemplo: mostrar DEBUG para nuestro paquete)
logging.level.com.miempresa.logging=DEBUG 

# Configuración del patrón de salida de la consola
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

---

## 6. ⏰ Quartz Scheduler

**Quartz Scheduler** es una librería de Java para programar y ejecutar tareas recurrentes (jobs) en momentos o intervalos específicos, similar a un `cron` de Linux.

### 6.1. Conceptos Clave

| Concepto | Descripción |
| :--- | :--- |
| **Scheduler** | El núcleo de Quartz. Gestiona la ejecución de los Jobs. |
| **Job** | La tarea que se quiere ejecutar. Debe implementar la interfaz `org.quartz.Job`. |
| **JobDetail** | Contiene los metadatos del Job: nombre, grupo y la clase (Job) a ejecutar. |
| **Trigger** | Define cuándo y con qué frecuencia se ejecuta un Job (horario). |
| **Cron Expression** | Cadena de texto que define horarios de ejecución flexibles (`"0/30 * * * * ?"` → Cada 30 segundos). |

### 6.2. Creación y Programación de un Job

#### 1. Definir el Job

Implementar la interfaz `Job` y su método `execute`:

```java
// Job.java
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("¡SimpleJob ejecutado! Hora actual: {}", System.currentTimeMillis());
    }
}
```

#### 2. Configurar JobDetail y Trigger

En una clase de configuración `QuartzConfig.java`:

```java
// QuartzConfig.java
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    // Define el Job (su metadata)
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(SimpleJob.class)
                .withIdentity("simpleJob", "group1")
                .storeDurably() // Permite que el job exista sin un trigger activo
                .build();
    }
    
    // Define el Trigger (su horario)
    @Bean
    public Trigger trigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("simpleTrigger", "group1")
                // Programación simple: cada 10 segundos para siempre
                .withSchedule(SimpleScheduleBuilder.simpleSchedule() 
                        .withIntervalInSeconds(10) 
                        .repeatForever())
                .build();
    }
}
```

#### 3. Programación con Cron Expressions

Se usa `CronScheduleBuilder` para horarios complejos:

```java
// Ejemplo de Cron Trigger: 0/30 * * * * ? (Cada 30 segundos)
@Bean
public Trigger cronTrigger(JobDetail jobDetail) {
    return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withSchedule(CronScheduleBuilder.cronSchedule("0/30 * * * * ?")) 
            .build();
}
```

### 6.3. Almacenamiento y Control del Scheduler

#### Almacenamiento del Job

* **memory** (Por defecto): Los jobs y triggers se pierden al reiniciar la aplicación.
* **jdbc** (Base de Datos): Los jobs y triggers se almacenan en la DB, persistiendo a través de reinicios. Requiere configurar `spring.quartz.job-store-type=jdbc` y las dependencias de JPA/DB, además de inicializar las tablas de Quartz.

#### Control Dinámico (Usando QuartzService)

El objeto `Scheduler` de Quartz puede ser inyectado para controlar los jobs en tiempo de ejecución:

```java
// QuartzService.java
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuartzService {
    @Autowired
    private Scheduler scheduler;

    public void pauseJob(String jobName, String jobGroup) throws SchedulerException {
        // Pausa un job específico
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup)); 
    }

    public void resumeJob(String jobName, String jobGroup) throws SchedulerException {
        // Reanuda un job previamente pausado
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
    }
    
    public void deleteJob(String jobName, String jobGroup) throws SchedulerException {
        // Elimina el job y sus triggers asociados
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
    }
}
```