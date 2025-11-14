import java.util.concurrent.CountDownLatch;

public class HiloActividadMixto {
    
    // Latch para Hilo 2 (espera a que Hilo 1 llegue a la iteración 3)
    private static final CountDownLatch LATCH_1_PARA_2 = new CountDownLatch(3);
    
    // Latch para Hilo 4 (espera a que Hilo 3 llegue a la iteración 5)
    private static final CountDownLatch LATCH_3_PARA_4 = new CountDownLatch(5);
    
    // Latch para Hilo 5 (espera a que Hilo 4 complete la iteración 10)
    private static final CountDownLatch LATCH_4_PARA_5 = new CountDownLatch(10); 

    public static void main(String[] args) {
        System.out.println("--- INICIO DE ACTIVIDAD DE HILOS PARALELO MIXTO (Prioridad Visual para Hilo-1) ---");
        
        // 1. CREACIÓN DE HILOS
        MiHiloMixto thread1 = new MiHiloMixto("Hilo-1", 1000, null, LATCH_1_PARA_2); // Emite señal 1
        MiHiloMixto thread2 = new MiHiloMixto("Hilo-2", 2000, LATCH_1_PARA_2, null); // Espera señal 1

        MiHiloMixto thread3 = new MiHiloMixto("Hilo-3", 3000, null, LATCH_3_PARA_4); // Emite señal 2
        MiHiloMixto thread4 = new MiHiloMixto("Hilo-4", 4000, LATCH_3_PARA_4, LATCH_4_PARA_5); // Espera señal 2, Emite señal 3
        
        MiHiloMixto thread5 = new MiHiloMixto("Hilo-5", 5000, LATCH_4_PARA_5, null); // Espera señal 3
        
        // 2. INICIO DE HILOS CON VENTAJAS:
        
        // 2a. Inicia el hilo más rápido (Hilo-1)
        thread1.start(); 
        
        try {
            // 2b. El hilo principal espera 50ms, dando tiempo a Hilo-1 para que imprima 
            // al menos una o dos iteraciones antes de que los demás empiecen.
            Thread.sleep(50); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 2c. Inicia el resto de los hilos
        thread2.start(); 
        thread3.start(); 
        thread4.start(); 
        thread5.start(); 

        try {
            // 3. ESPERAR A QUE TODOS LOS HILOS TERMINEN (join)
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            System.err.println("El hilo principal fue interrumpido.");
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- TODOS LOS 5 HILOS HAN TERMINADO (Mezcla de Paralelismo y Dependencia) ---");
    }
}


// =================================================================
// CLASE PERSONALIZADA PARA SINCRONIZACIÓN CONDICIONAL (Sin cambios)
// =================================================================
class MiHiloMixto extends Thread {
    private final int sleepTime;
    private final CountDownLatch prevLatch;
    private final CountDownLatch nextLatch;

    public MiHiloMixto(String name, int sleepTime, CountDownLatch prevLatch, CountDownLatch nextLatch) {
        super(name);
        this.sleepTime = sleepTime;
        this.prevLatch = prevLatch;
        this.nextLatch = nextLatch;
    }

    @Override
    public void run() {
        if (prevLatch != null) {
            System.out.println(getName() + " - [Bloqueado] Esperando la señal del hilo anterior. Contador: " + prevLatch.getCount());
            try {
                // Bloquea el hilo hasta que el Latch llegue a cero.
                prevLatch.await(); 
                System.out.println(getName() + " - [Desbloqueado] ¡Señal recibida! Comenzando ejecución.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        
        // Lógica de ejecución del hilo
        for (int i = 1; i <= 10; i++) {
            System.out.println(getName() + " - Iteración " + i); 
            
            // Si el Latch siguiente existe y el contador es > 0, lo decrementamos.
            if (nextLatch != null && nextLatch.getCount() > 0) {
                nextLatch.countDown();
                // Opcional: Mostrar cuándo el hilo siguiente está a punto de liberarse
                if (nextLatch.getCount() == 0) {
                     System.out.println(" [Señal] " + getName() + " LIBERÓ el siguiente hilo.");
                }
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println(getName() + " ha finalizado su bucle.");
    }
}