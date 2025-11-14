import java.util.concurrent.CountDownLatch;

public class HiloActividadSincronizada {
    
    // Se necesitan 4 Latch para controlar la secuencia de 5 hilos: H1->H2, H2->H3, H3->H4, H4->H5.
    // Cada Latch se inicializa con 1, lo que significa que solo necesita una señal (el countDown) para liberarse.
    private static final CountDownLatch LATCH_1_2 = new CountDownLatch(1);
    private static final CountDownLatch LATCH_2_3 = new CountDownLatch(1);
    private static final CountDownLatch LATCH_3_4 = new CountDownLatch(1);
    private static final CountDownLatch LATCH_4_5 = new CountDownLatch(1);

    public static void main(String[] args) {
        System.out.println("--- INICIO DE ACTIVIDAD DE 5 HILOS EN ORDEN SECUENCIAL FORZADO ---");
        
        // 1. CREACIÓN DE LOS HILOS (Usando MiHiloSincronizado para todos)
        
        // Hilo 1: No espera a nadie, pero da la señal al Hilo 2
        MiHiloSincronizado thread1 = new MiHiloSincronizado("Hilo-1", 1000, null, LATCH_1_2);
        
        // Hilo 2: Espera al Hilo 1 (LATCH_1_2) y da la señal al Hilo 3 (LATCH_2_3)
        MiHiloSincronizado thread2 = new MiHiloSincronizado("Hilo-2", 2000, LATCH_1_2, LATCH_2_3); 
        
        // Hilo 3: Espera al Hilo 2 (LATCH_2_3) y da la señal al Hilo 4 (LATCH_3_4)
        MiHiloSincronizado thread3 = new MiHiloSincronizado("Hilo-3", 3000, LATCH_2_3, LATCH_3_4);
        
        // Hilo 4: Espera al Hilo 3 (LATCH_3_4) y da la señal al Hilo 5 (LATCH_4_5)
        MiHiloSincronizado thread4 = new MiHiloSincronizado("Hilo-4", 4000, LATCH_3_4, LATCH_4_5);
        
        // Hilo 5: Espera al Hilo 4 (LATCH_4_5) y no da señal a nadie (nextLatch = null)
        MiHiloSincronizado thread5 = new MiHiloSincronizado("Hilo-5", 5000, LATCH_4_5, null);
        

        // 2. INICIO DE TODOS LOS HILOS
        // Todos se inician en paralelo, pero solo Hilo-1 podrá empezar a trabajar.
        thread1.start(); 
        thread2.start(); 
        thread3.start(); 
        thread4.start(); 
        thread5.start(); 

        try {
            // 3. ESPERAR A QUE TODOS LOS HILOS TERMINEN (join)
            // Esto asegura que el programa principal espera al hilo más lento (Hilo-5).
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            System.err.println("El hilo principal fue interrumpido.");
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- TODOS LOS 5 HILOS HAN TERMINADO EN EL ORDEN FORZADO ---");
    }
}


// =================================================================
// CLASE PERSONALIZADA PARA SINCRONIZACIÓN DE N HILOS
// =================================================================
class MiHiloSincronizado extends Thread {
    private final int sleepTime;
    // Latch que este hilo debe esperar (espera al hilo anterior)
    private final CountDownLatch prevLatch; 
    // Latch que este hilo debe liberar (da paso al siguiente hilo)
    private final CountDownLatch nextLatch; 

    public MiHiloSincronizado(String name, int sleepTime, CountDownLatch prevLatch, CountDownLatch nextLatch) {
        super(name);
        this.sleepTime = sleepTime;
        this.prevLatch = prevLatch;
        this.nextLatch = nextLatch;
    }

    @Override
    public void run() {
        if (prevLatch != null) {
            System.out.println(getName() + " - Esperando a la señal del hilo anterior...");
            try {
                // Bloquea el hilo hasta que el hilo anterior libere el latch.
                prevLatch.await(); 
                System.out.println(getName() + " - ¡Señal recibida! Comenzando ejecución.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        
        // Lógica de ejecución del hilo
        for (int i = 1; i <= 10; i++) {
            System.out.println(getName() + " - Iteración " + i); 
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println(getName() + " fue interrumpido.");
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println(getName() + " ha finalizado su bucle.");
        
        // Una vez que el hilo termina, libera el latch para el siguiente hilo.
        if (nextLatch != null) {
            nextLatch.countDown();
            System.out.println(" [Señal] " + getName() + " liberó el siguiente hilo.");
        }
    }
}