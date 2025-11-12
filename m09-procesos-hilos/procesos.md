# 📚 Apuntes para Examen: Programación Multiproceso en Java (Procesos)

## 1. 🚀 Programación Multiproceso: Conceptos Fundamentales

La **Programación Multiproceso** es un paradigma en el que múltiples **procesos** se ejecutan de manera simultánea, generalmente en diferentes núcleos o procesadores. Su objetivo es maximizar la capacidad de procesamiento y mejorar el rendimiento de la aplicación.

### Programación Concurrente, Paralela y Distribuida

| Concepto | Definición | Contexto |
| :--- | :--- | :--- |
| **Concurrencia** | Ejecución **simultánea** de procesos (o su intercalación/entrelazado) en un período de tiempo. No implica ejecución en el mismo instante. | Sistemas operativos actuales, acceso a datos compartido. Requiere técnicas de **bloqueo** y **comunicación** para evitar problemas. |
| **Multiprogramación** | Un tipo de concurrencia que se da en un ordenador con **un único procesador**. | Uniprocesador. |
| **Paralelismo** | Ejecución de procesos de forma **realmente simultánea** (al mismo instante) porque el ordenador tiene **más de un procesador**. | Multiprocesador (CPU con múltiples núcleos). |
| **Programación Distribuida** | Un tipo especial de paralelismo. Se da en un **sistema distribuido** (conjunto de ordenadores independientes, geográficamente dispersos o no, unidos por una red). | Visto por el usuario como **una sola computadora**. |

---

## 2. 🖥️ Gestión de Procesos con `java.lang.Process`

Java proporciona la clase **`java.lang.Process`** para crear y controlar procesos externos (del sistema operativo).

### Clase `Process` y `ProcessBuilder`

Para iniciar un proceso externo en Java, se utiliza la clase auxiliar **`java.lang.ProcessBuilder`**.

**Ejemplo Básico de Ejecución:**

```java
import java.io.IOException;

public class ProcessExample {
    public static void main(String[] args) {
        try {
            // 1. Crear un ProcessBuilder con el comando a ejecutar
            ProcessBuilder builder = new ProcessBuilder("notepad.exe");
            
            // 2. Iniciar el proceso y obtener la instancia de Process
            Process process = builder.start();
            
            // Opcional: Esperar a que el proceso termine
            process.waitFor(); 
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

### Interfaz y Métodos de la Clase Process

La clase `Process` es una clase abstracta. Aunque normalmente no se hereda de ella, sus métodos definen el comportamiento básico de un proceso.

| Método | Descripción | Uso Común |
| :--- | :--- | :--- |
| `waitFor()` | Bloquea el hilo actual hasta que el proceso termine. Devuelve el valor de salida del proceso. | Esperar la finalización del proceso externo. |
| `exitValue()` | Devuelve el valor de salida del proceso. Solo se puede llamar si el proceso ya ha terminado. | Obtener el resultado final del proceso. |
| `destroy()` | Termina el proceso (envía una señal de terminación). | Cerrar un proceso que está en ejecución. |
| `getOutputStream()` | Devuelve el flujo de salida (para enviar datos al proceso). | Escribir datos al input del proceso. |
| `getInputStream()` | Devuelve el flujo de entrada (para leer datos desde la salida normal del proceso). | Leer el output del proceso (p. ej., mensajes de consola). |
| `getErrorStream()` | Devuelve el flujo de error (para leer mensajes de error del proceso). | Leer los mensajes de error del proceso. |

### Redirección de Flujos (Alternativa a Sobreescribir)

En lugar de heredar y sobreescribir los métodos de flujo (`getInputStream`, etc.), es mucho más común y recomendable utilizar los métodos de redirección de `ProcessBuilder`:

```java
// Redirige la salida estándar y de error a archivos
ProcessBuilder pb = new ProcessBuilder("java", "MainClass");
pb.redirectOutput(new File("output.txt"));
pb.redirectError(new File("error.txt"));
Process process = pb.start();
process.waitFor();
```

### Gestión de Procesos Secuenciales

Los procesos se ejecutan de forma secuencial cuando se utiliza el método `waitFor()`, ya que este bloquea el hilo principal hasta que el proceso finaliza.

```java
public class Main {
    public static void procesos() throws IOException, InterruptedException {
        // Proceso 1
        ProcessBuilder builder1 = new ProcessBuilder("notepad.exe");
        Process process1 = builder1.start();
        process1.waitFor(); // Espera obligatoria
        System.out.println("proceso 1 finalizado");

        // Proceso 2 (no inicia hasta que el Proceso 1 termine)
        ProcessBuilder builder2 = new ProcessBuilder("notepad.exe");
        Process process2 = builder2.start();
        process2.waitFor(); // Espera obligatoria
        System.out.println("proceso 2 finalizado");
    }
}
```

---

## 3. ☁️ Microservicios y Procesos

### Arquitectura de Microservicios

Los **Microservicios** son un estilo arquitectónico donde una aplicación se construye como una colección de pequeños servicios independientes, cada uno enfocado en una función de negocio específica.

| Característica Clave | Descripción |
| :--- | :--- |
| **Independencia** | Desarrollo, prueba, despliegue y escalado de cada servicio sin afectar a los demás. |
| **Desacoplamiento** | Bajas dependencias entre servicios. Flexibilidad tecnológica. |
| **Comunicación** | Se comunican mediante APIs (típicamente HTTP/REST o mensajería). |

### Relación con Procesos

- En la práctica, cada microservicio se implementa y se ejecuta como un **proceso independiente** en el sistema operativo.
- El **Proceso** (instancia de un programa en ejecución con su propio espacio de memoria) es el mecanismo fundamental del S.O. para ejecutar el **Microservicio** (la unidad lógica/arquitectónica de la aplicación).
- El despliegue independiente implica que cada proceso (microservicio) se puede gestionar (iniciar, detener, escalar) por separado.

### Comunicación entre Microservicios (Llamadas HTTP)

La comunicación entre los procesos (microservicios) se realiza a través de APIs. En el entorno Java/Spring, las librerías principales para realizar llamadas HTTP son:

| Característica | RestTemplate | WebClient |
| :--- | :--- | :--- |
| **Soporte Spring** | Deprecado (en desuso desde Spring 5) | ✅ Recomendado (Integrado con Spring WebFlux) |
| **Paradigma** | Bloqueante (Síncrono) | No bloqueante (Reactivo/Asíncrono) |
| **Uso en Microservicios** | Menos ideal por ser bloqueante. | 🔥 Ideal (Mejor manejo de la concurrencia). |
| **Soporte Reactivo** | ❌ No (Mono/Flux) | ✅ Sí (Mono/Flux) |

### Ejemplo de WebClient (Recomendado)

WebClient es ideal para arquitecturas de microservicios por su naturaleza no bloqueante:

```java
// Requiere la dependencia: spring-boot-starter-webflux
@RestController
public class LoginController {
    private final WebClient webClient;

    public LoginController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
    }

    // Retorna un Mono (flujo asíncrono)
    @PostMapping("/login")
    public Mono<ResponseEntity<Usuario>> login(@RequestBody Usuario usuario) {
        // Enviar el JWT (si aplica) en la cabecera Authorization
        return webClient.post()
                .uri("/authenticate")
                .bodyValue(usuario)
                .retrieve() // Inicia la recuperación de la respuesta
                .bodyToMono(Usuario.class) // Convierte el cuerpo a un Mono<Usuario>
                .map(usuarioConToken -> { // Mapeo de éxito
                    System.out.println("Usuario autenticado: " + usuarioConToken);
                    return ResponseEntity.ok(usuarioConToken);
                })
                .onErrorResume(e -> { // Manejo de error
                    System.err.println("Error en autenticación: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
                });
    }
}
```

### Envío de JWT con WebClient

Tanto con `RestTemplate` como con `WebClient`, el token JWT se debe enviar en la cabecera `Authorization` (ej. `Authorization: Bearer <token>`).

**Estructura de la Petición (Conceptual):**

```java
// Ejemplo con WebClient para incluir el token en la cabecera
webClient.get()
    .uri("/protected-resource")
    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token) // Incluir el JWT aquí
    // ... otros métodos
```