public class HiloCoordinacionInterrupcion {

    public static void main(String[] args) {
        
        System.out.println("--- INICIO: Coordinación de Hilos ---");
        
        // 1. Creación de Hilos
        // Hilo A: Tarea larga de 2 segundos (necesitamos esperarlo con join)
        MiTareaLarga hiloA = new MiTareaLarga("Hilo-A_Largo", 2000); 
        
        // Hilo B: Tarea larga de 3 segundos (necesitamos esperarlo con join)
        MiTareaLarga hiloB = new MiTareaLarga("Hilo-B_Largo", 3000); 
        
        // Hilo C: Tarea interrumpible con un sleep largo (vamos a detenerlo)
        MiTareaInterrumpible hiloC = new MiTareaInterrumpible("Hilo-C_Interrumpir");

        // 2. Iniciar todos los hilos
        hiloA.start();
        hiloB.start();
        hiloC.start();

        try {
            // =======================================================
            // PARTE 1: Coordinación con join()
            // =======================================================
            System.out.println("Hilo Principal: Esperando a que Hilo-A y Hilo-B terminen...");

            // Bloquea el hilo principal hasta que Hilo-A finalice.
            hiloA.join(); 
            System.out.println("Hilo Principal: Hilo-A ha finalizado. Continuando...");

            // Bloquea el hilo principal hasta que Hilo-B finalice.
            hiloB.join(); 
            System.out.println("Hilo Principal: Hilo-B ha finalizado. Tarea coordinada completada.");
            
            // =======================================================
            // PARTE 2: Interrupción del Hilo
            // =======================================================
            
            // Dormir un poco para asegurarnos de que Hilo-C ya está en el sleep()
            Thread.sleep(100); 
            
            System.out.println("Hilo Principal: Interrumpiendo Hilo-C...");

            // Llama al método interrupt(). Esto establece la bandera de interrupción 
            // y, si el hilo está en estado WAITING o TIMED_WAITING (por sleep/join), 
            // fuerza la excepción InterruptedException.
            hiloC.interrupt(); 
            
            // Esperamos un momento para ver el resultado de la interrupción.
            hiloC.join();
            
        } catch (InterruptedException e) {
            System.err.println("El hilo principal fue interrumpido inesperadamente.");
            Thread.currentThread().interrupt();
        }
        
        System.out.println("--- FIN DE LA ACTIVIDAD DE HILOS ---");
    }
}

// -----------------------------------------------------------------
// CLASE 1: Hilo para tareas largas
// -----------------------------------------------------------------
class MiTareaLarga extends Thread {
    private final int duration;

    public MiTareaLarga(String name, int duration) {
        super(name);
        this.duration = duration;
    }

    @Override
    public void run() {
        System.out.println(getName() + " iniciado. Duración: " + duration + "ms.");
        try {
            // Simula una tarea larga con Thread.sleep
            Thread.sleep(duration); 
        } catch (InterruptedException e) {
            System.out.println(getName() + " fue interrumpido, pero esto no se espera.");
            Thread.currentThread().interrupt();
        }
        System.out.println(getName() + " ha terminado su ejecución.");
    }
}

// -----------------------------------------------------------------
// CLASE 2: Hilo diseñado para ser interrumpido
// -----------------------------------------------------------------
class MiTareaInterrumpible extends Thread {
    public MiTareaInterrumpible(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println(getName() + " iniciado. Entrará en un sleep largo.");
        try {
            // Entramos en un estado WAITING/TIMED_WAITING
            Thread.sleep(10000); // 10 segundos
            System.out.println(getName() + " terminó su sleep sin interrupción."); // Esto no debería verse
        } catch (InterruptedException e) {
            // ¡Manejo de la interrupción! 
            // Cuando interrupt() es llamado mientras dormimos, se lanza esta excepción.
            System.out.println("-----------------------------------------------------------------");
            System.out.println(getName() + ": ¡Fui interrumpido! Capturé la InterruptedException.");
            System.out.println("-----------------------------------------------------------------");
            
            // Al capturar la excepción, la bandera de interrupción se limpia. 
            // Si tuviéramos más lógica, deberíamos llamar a Thread.currentThread().interrupt(); 
            // para restablecer la bandera y notificar a otros códigos superiores.
            // En este caso, simplemente terminamos el hilo.
        }
        System.out.println(getName() + " ha finalizado su ejecución después de la interrupción.");
    }
}