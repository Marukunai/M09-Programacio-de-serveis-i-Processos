import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcesoCapturaSalida {

    public static void main(String[] args) {
        
        // 1. Definir el comando y la ProcessBuilder
        // Vamos a ejecutar 'ping' a Google con 2 peticiones (-n 2 en Windows, -c 2 en Linux/macOS)
        // Nota: Asegúrate de usar el parámetro correcto para tu SO. Usamos un comando genérico.
        List<String> command = new ArrayList<>();
        
        // Ejemplo de comando (funciona en la mayoría de los sistemas):
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            command.add("ping");
            command.add("-n");
            command.add("2");
            command.add("google.com");
        } else {
            command.add("ping");
            command.add("-c");
            command.add("2");
            command.add("google.com");
        }
        
        ProcessBuilder builder = new ProcessBuilder(command);
        Process pingProcess = null;
        
        System.out.println("--- INICIO: Ejecutando comando externo y capturando su salida ---");

        try {
            // 2. Iniciar el proceso
            pingProcess = builder.start();
            System.out.println("Proceso iniciado: " + command);
            
            // 3. Capturar el Flujo de Entrada (Input Stream) del proceso
            // El 'Input Stream' del objeto Process representa la SALIDA del proceso externo.
            try (BufferedReader reader = new BufferedReader(
                 new InputStreamReader(pingProcess.getInputStream()))) {
                
                String line;
                System.out.println("\n--- SALIDA CAPTURADA DEL PING ---");
                
                // 4. Leer línea por línea la salida del proceso externo
                while ((line = reader.readLine()) != null) {
                    // Imprimimos la línea en la consola de nuestro programa Java.
                    System.out.println(">> " + line);
                }
            }

            // 5. Esperar a que el proceso termine y obtener el valor de salida
            int exitCode = pingProcess.waitFor();
            System.out.println("------------------------------------------");
            System.out.println("Proceso terminado. Código de salida: " + exitCode);
            
        } catch (IOException e) {
            System.err.println("Error de I/O al ejecutar el proceso: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("El hilo fue interrumpido mientras esperaba.");
            Thread.currentThread().interrupt();
        }
    }
}