import java.io.IOException;

public class ProcesoSecuencialCondicional {

    public static void main(String[] args) {
        
        // Comandos de ejemplo:
        // Cargar un comando que sabemos que tiene éxito (exit code 0)
        // 'cmd /c exit 0' o 'true' en Linux/macOS
        String[] commandSuccess = getCommand("success"); 
        
        // Cargar un comando que sabemos que falla (exit code 1)
        // 'cmd /c exit 1' o 'false' en Linux/macOS
        String[] commandFailure = getCommand("failure"); 
        
        Process pSuccess = null;
        Process pFailure = null;

        System.out.println("--- INICIO: Ejecución de Procesos Secuenciales y Condicionales ---");

        try {
            // =======================================================
            // PARTE 1: Ejecución y Evaluación del Proceso de Éxito
            // =======================================================
            
            // 1. Iniciar el primer proceso (éxito esperado)
            pSuccess = new ProcessBuilder(commandSuccess).inheritIO().start();
            System.out.println("Proceso 1 iniciado: Comando de Éxito (exit 0).");

            // 2. Esperar la terminación del Proceso 1
            pSuccess.waitFor();
            int exitCodeSuccess = pSuccess.exitValue();
            System.out.println("Proceso 1 terminado con código: " + exitCodeSuccess);
            
            // 3. Decisión Condicional
            if (exitCodeSuccess == 0) {
                System.out.println("Condición OK (código 0). Ejecutando Proceso 2.");
                
                // =======================================================
                // PARTE 2: Ejecución Condicional del Proceso de Falla
                // =======================================================
                
                // 4. Iniciar el segundo proceso (falla esperada)
                pFailure = new ProcessBuilder(commandFailure).inheritIO().start();
                System.out.println("Proceso 2 iniciado: Comando de Falla (exit 1).");
                
                // 5. Esperar la terminación del Proceso 2
                pFailure.waitFor();
                int exitCodeFailure = pFailure.exitValue();
                System.out.println("Proceso 2 terminado con código: " + exitCodeFailure);
                
                // 6. Evaluación final
                if (exitCodeFailure != 0) {
                    System.out.println("Éxito en la secuencia condicional: El segundo comando falló como se esperaba (código != 0).");
                }
                
            } else {
                System.out.println("ERROR: El Proceso 1 falló inesperadamente. Terminando secuencia.");
            }

        } catch (IOException e) {
            System.err.println("Error de I/O al ejecutar el proceso: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("El hilo principal fue interrumpido: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        System.out.println("--- FIN DE LA ACTIVIDAD DE PROCESOS ---");
    }

    // Método auxiliar para construir comandos compatibles con Windows y Unix/Linux
    private static String[] getCommand(String type) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        if (isWindows) {
            // En Windows, usamos cmd /c exit X
            return type.equals("success") ? new String[]{"cmd", "/c", "exit", "0"} : new String[]{"cmd", "/c", "exit", "1"};
        } else {
            // En Unix/Linux, usamos /bin/sh -c 'exit X' o comandos como true/false
            return type.equals("success") ? new String[]{"/bin/sh", "-c", "exit 0"} : new String[]{"/bin/sh", "-c", "exit 1"};
        }
    }
}