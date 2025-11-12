import java.io.IOException;

public class ProcesoActividad {

    public static void main(String[] args) {
        
        // 1. Declaración de las variables Process para los procesos a ejecutar
        Process p1_notepad = null;
        Process p2_echo = null;
        Process p3_notepad_final = null;

        System.out.println("--- INICIO DE ACTIVIDAD DE PROCESOS ---");
        
        try {
            // =======================================================
            // PARTE 1: Procesos p1 y p2 (Ejecución PARALELA)
            // =======================================================

            // 1.1. Crear ProcessBuilder para el primer proceso (notepad.exe)
            // ProcessBuilder se usa para configurar la creación del proceso externo.
            ProcessBuilder builder1 = new ProcessBuilder("notepad.exe");
            
            // 1.2. Iniciar el proceso 1 (notepad.exe)
            // start() ejecuta el comando y devuelve el objeto Process. 
            // La ejecución continúa inmediatamente (PARALELO).
            p1_notepad = builder1.start(); 
            System.out.println("Proceso 1 (notepad) iniciado.");

            // 1.3. Crear ProcessBuilder para el segundo proceso (cmd echo)
            // El comando "cmd /c" ejecuta el comando y luego termina el cmd.
            ProcessBuilder builder2 = new ProcessBuilder("cmd", "/c", "echo Hello world desde proceso");

            // 1.4. Redirigir la salida del proceso hijo (echo) 
            // a la consola del proceso padre (Java) para que se vea el resultado.
            builder2.inheritIO();

            // 1.5. Iniciar el proceso 2 (echo)
            // start() ejecuta el comando y el programa principal continúa inmediatamente (PARALELO).
            p2_echo = builder2.start();
            System.out.println("Proceso 2 (echo) iniciado. Ver consola para salida.");
            
            // ------------------------------------------------------
            // 1.6. Esperar la terminación de p1 y p2 (CONTROL CONCURRENTE)
            // waitFor() BLOQUEA el hilo principal hasta que el proceso termina. 
            // Al llamarlo en p1 y p2, aseguramos que AMBOS finalicen antes de seguir.
            p1_notepad.waitFor();
            p2_echo.waitFor();
            // ------------------------------------------------------
            
            System.out.println("\nLos dos primeros procesos han terminado.");
            
            // =======================================================
            // PARTE 2: Proceso p3 (Ejecución SECUENCIAL)
            // =======================================================
            
            // 2.1. Crear ProcessBuilder para el tercer proceso (notepad.exe)
            ProcessBuilder builder3 = new ProcessBuilder("notepad.exe");
            
            // 2.2. Iniciar el proceso 3
            p3_notepad_final = builder3.start();
            System.out.println("Proceso 3 (notepad final) iniciado.");

            // 2.3. Esperar la terminación de p3
            // Este waitFor() BLOQUEA la ejecución hasta que el tercer notepad se cierre, 
            // forzando la secuencia.
            p3_notepad_final.waitFor();
            
            System.out.println("Todos los procesos han terminado.");

        } catch (IOException e) {
            // Maneja errores como "comando no encontrado"
            System.err.println("Error al ejecutar el proceso: " + e.getMessage());
        } catch (InterruptedException e) {
            // Maneja el caso en que el hilo principal sea interrumpido mientras espera (waitFor)
            System.err.println("El hilo principal fue interrumpido: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}