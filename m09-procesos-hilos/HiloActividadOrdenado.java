public class HiloActividadOrdenado {
    
    public static void main(String[] args) {
        System.out.println("--- INICIO DE ACTIVIDAD DE HILOS ORDENADA ---");

        // 1. CREACIÓN DE HILOS EXTENDIENDO LA CLASE THREAD (Hilos 1, 2 y 3)
        MiThread thread1 = new MiThread("Hilo-1_Thread", 1000);
        MiThread thread2 = new MiThread("Hilo-2_Thread", 2000);
        MiThread thread3 = new MiThread("Hilo-3_Thread", 3000);

        // 2. CREACIÓN DE HILOS IMPLEMENTANDO RUNNABLE (Hilos 4 y 5)
        MiRunnable runnable4 = new MiRunnable("Hilo-4_Runnable", 4000);
        Thread thread4 = new Thread(runnable4);

        MiRunnable runnable5 = new MiRunnable("Hilo-5_Runnable", 5000);
        Thread thread5 = new Thread(runnable5);
        
        // 3. EJECUCIÓN SECUENCIAL (INICIO Y ESPERA INMEDIATA)
        try {
            // Hilo 1: Inicia y el hilo principal espera a que termine.
            thread1.start();
            System.out.println("\n*** Hilo principal esperando por Hilo-1... ***");
            thread1.join(); 
            
            // Hilo 2: Inicia solo después de que Hilo-1 ha terminado. 
            // El hilo principal espera a que termine Hilo-2.
            thread2.start();
            System.out.println("\n*** Hilo principal esperando por Hilo-2... ***");
            thread2.join();
            
            // Hilo 3: Inicia solo después de que Hilo-2 ha terminado.
            thread3.start();
            System.out.println("\n*** Hilo principal esperando por Hilo-3... ***");
            thread3.join();
            
            // Hilo 4: Inicia solo después de que Hilo-3 ha terminado.
            thread4.start();
            System.out.println("\n*** Hilo principal esperando por Hilo-4... ***");
            thread4.join();
            
            // Hilo 5: Inicia solo después de que Hilo-4 ha terminado.
            thread5.start();
            System.out.println("\n*** Hilo principal esperando por Hilo-5... ***");
            thread5.join();
            
        } catch (InterruptedException e) {
            System.err.println("El hilo principal fue interrumpido.");
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- TODOS LOS 5 HILOS HAN TERMINADO EN ORDEN ---");
    }
}
// Las clases MiThread y MiRunnable son idénticas a las que proporcionaste.
// Asegúrate de incluirlas en el mismo archivo o en archivos separados.

// =================================================================
// CLASE PERSONALIZADA 1: EXTENDIENDO THREAD
// =================================================================
class MiThread extends Thread {
    private final int sleepTime;

    public MiThread(String name, int sleepTime) {
        super(name);
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(getName() + " - Iteración " + i); 
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println(getName() + " fue interrumpido.");
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(getName() + " ha finalizado su bucle.");
    }
}


// =================================================================
// CLASE PERSONALIZADA 2: IMPLEMENTANDO RUNNABLE
// =================================================================
class MiRunnable implements Runnable {
    private final String name;
    private final int sleepTime;

    public MiRunnable(String name, int sleepTime) {
        this.name = name;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(name + " - Iteración " + i);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println(name + " fue interrumpido.");
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(name + " ha finalizado su bucle.");
    }
}