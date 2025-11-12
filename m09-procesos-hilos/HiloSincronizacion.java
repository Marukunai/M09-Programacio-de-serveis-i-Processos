public class HiloSincronizacion {

    public static void main(String[] args) throws InterruptedException {
        
        // El recurso compartido que será accedido por todos los hilos
        CuentaBancaria cuenta = new CuentaBancaria();
        
        final int NUM_HILOS = 10;
        final int NUM_DEPOSITOS = 1000;
        final int CANTIDAD_DEPOSITO = 1;
        
        Thread[] hilos = new Thread[NUM_HILOS];

        System.out.println("--- INICIO: Simulando depósitos concurrentes ---");
        System.out.println("Objetivo: " + (NUM_HILOS * NUM_DEPOSITOS * CANTIDAD_DEPOSITO) + " unidades.");
        
        // =======================================================
        // PARTE 1: Ejecución SIN Sincronización (Race Condition)
        // =======================================================
        
        // Reiniciamos la cuenta para la prueba sin sincronización
        cuenta.reset(0);
        System.out.println("\n--- PRUEBA SIN SYNCHRONIZED ---");
        
        // Creamos y ejecutamos los hilos que usan el método NO sincronizado
        for (int i = 0; i < NUM_HILOS; i++) {
            hilos[i] = new DepositoHilo(cuenta, NUM_DEPOSITOS, CANTIDAD_DEPOSITO, false);
            hilos[i].start();
        }

        // Esperar a que todos los hilos terminen
        for (Thread t : hilos) {
            t.join();
        }
        
        // Casi siempre, el resultado será MENOR que el objetivo debido a la Race Condition.
        System.out.println("RESULTADO SIN SYNC: " + cuenta.getSaldo()); 
        
        // =======================================================
        // PARTE 2: Ejecución CON Sincronización (Atomicidad)
        // =======================================================
        
        // Reiniciamos la cuenta para la prueba con sincronización
        cuenta.reset(0);
        System.out.println("\n--- PRUEBA CON SYNCHRONIZED ---");
        
        // Creamos y ejecutamos los hilos que usan el método sincronizado
        for (int i = 0; i < NUM_HILOS; i++) {
            hilos[i] = new DepositoHilo(cuenta, NUM_DEPOSITOS, CANTIDAD_DEPOSITO, true);
            hilos[i].start();
        }
        
        // Esperar a que todos los hilos terminen
        for (Thread t : hilos) {
            t.join();
        }
        
        // El resultado SIEMPRE debe ser igual al objetivo.
        System.out.println("RESULTADO CON SYNC: " + cuenta.getSaldo()); 
        System.out.println("--- FIN DE LA SIMULACIÓN ---");
    }
}

// -----------------------------------------------------------------
// CLASE 1: RECURSO COMPARTIDO (Cuenta Bancaria)
// -----------------------------------------------------------------
class CuentaBancaria {
    private int saldo = 0;

    public int getSaldo() {
        return saldo;
    }

    public void reset(int initial) {
        this.saldo = initial;
    }
    
    // Método SIN sincronización: Múltiples hilos pueden acceder y modificar 
    // 'saldo' a la vez. Esto crea una Condición de Carrera.
    public void depositarNoSincronizado(int cantidad) {
        // Operación no atómica: 1. Leer saldo. 2. Calcular nuevo saldo. 3. Escribir.
        // Si dos hilos leen el mismo saldo antes de que el primero escriba, se pierde un depósito.
        saldo = saldo + cantidad;
    }

    // Método CON sincronización: Solo un hilo puede ejecutar este método a la vez.
    // Esto asegura que la operación (Leer -> Modificar -> Escribir) sea atómica.
    public synchronized void depositarSincronizado(int cantidad) {
        saldo = saldo + cantidad;
    }
}

// -----------------------------------------------------------------
// CLASE 2: TAREA CONCURRENTE (Hilo de Depósito)
// -----------------------------------------------------------------
class DepositoHilo extends Thread {
    private final CuentaBancaria cuenta;
    private final int numDepositos;
    private final int cantidad;
    private final boolean usarSincronizacion;

    public DepositoHilo(CuentaBancaria cuenta, int numDepositos, int cantidad, boolean sync) {
        this.cuenta = cuenta;
        this.numDepositos = numDepositos;
        this.cantidad = cantidad;
        this.usarSincronizacion = sync;
    }

    @Override
    public void run() {
        // El hilo realiza el número de depósitos especificado
        for (int i = 0; i < numDepositos; i++) {
            if (usarSincronizacion) {
                // Llama al método seguro y atómico.
                cuenta.depositarSincronizado(cantidad);
            } else {
                // Llama al método inseguro, causando la Race Condition.
                cuenta.depositarNoSincronizado(cantidad);
            }
        }
    }
}