public class HiloActividad {
    
    public static void main(String[] args) {
        System.out.println("--- INICIO DE ACTIVIDAD DE HILOS ---");

        // 1. CREACIÓN DE HILOS EXTENDIENDO LA CLASE THREAD (Hilos 1, 2 y 3)
        // Se crean instancias de la clase personalizada que extiende Thread.
        
        // Hilo 1: Extiende Thread, duerme 1000ms (1s)
        MiThread thread1 = new MiThread("Hilo-1_Thread", 1000);
        
        // Hilo 2: Extiende Thread, duerme 2000ms (2s)
        MiThread thread2 = new MiThread("Hilo-2_Thread", 2000);
        
        // Hilo 3: Extiende Thread, duerme 3000ms (3s)
        MiThread thread3 = new MiThread("Hilo-3_Thread", 3000);

        // 2. CREACIÓN DE HILOS IMPLEMENTANDO RUNNABLE (Hilos 4 y 5)
        // Se crean instancias de la clase que implementa Runnable, y luego se 
        // envuelve cada una en un objeto Thread.
        
        // Hilo 4: Implementa Runnable, duerme 4000ms (4s)
        MiRunnable runnable4 = new MiRunnable("Hilo-4_Runnable", 4000);
        Thread thread4 = new Thread(runnable4);

        // Hilo 5: Implementa Runnable, duerme 5000ms (5s)
        MiRunnable runnable5 = new MiRunnable("Hilo-5_Runnable", 5000);
        Thread thread5 = new Thread(runnable5);

        // 3. INICIO DE TODOS LOS HILOS
        // start() llama al método run() de cada hilo en paralelo.
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        try {
            // 4. ESPERAR A QUE TODOS LOS HILOS TERMINEN
            // join() bloquea el hilo principal (main) hasta que el hilo en cuestión 
            // (threadX) finalice su ejecución. Esto asegura que el mensaje final 
            // se imprima después de que todos los bucles hayan concluido.
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            System.err.println("El hilo principal fue interrumpido.");
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- TODOS LOS 5 HILOS HAN TERMINADO ---");
    }
}

// =================================================================
// CLASE PERSONALIZADA 1: EXTENDIENDO THREAD
// =================================================================
class MiThread extends Thread {
    private final int sleepTime; // Tiempo de espera en milisegundos

    // Constructor que llama al constructor de la clase padre (Thread) 
    // para asignar el nombre del hilo.
    public MiThread(String name, int sleepTime) {
        super(name);
        this.sleepTime = sleepTime;
    }

    // El método run() contiene la lógica que se ejecutará en el nuevo hilo.
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            // getName() devuelve el nombre asignado en el constructor.
            System.out.println(getName() + " - Iteración " + i); 
            try {
                // sleep() pausa la ejecución del hilo actual por el tiempo especificado.
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                // Se lanza si el hilo es interrumpido mientras está dormido.
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

    // El método run() contiene la lógica del hilo.
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            // currentThread().getName() obtiene el nombre del hilo actual que ejecuta este Runnable.
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