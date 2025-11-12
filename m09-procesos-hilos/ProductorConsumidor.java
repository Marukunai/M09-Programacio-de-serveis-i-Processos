import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ProductorConsumidor {

    public static void main(String[] args) {
        
        // El recurso compartido central que gestiona la comunicación
        BufferCompartido buffer = new BufferCompartido(5); 

        System.out.println("--- INICIO: Simulación Productor-Consumidor ---");
        System.out.println("Capacidad máxima del Buffer: 5 elementos.");

        // 1. Crear Hilos
        // Se crea un Productor que depositará datos en el buffer.
        Productor productor = new Productor(buffer);
        
        // Se crea un Consumidor que extraerá datos del buffer.
        Consumidor consumidor = new Consumidor(buffer);

        // 2. Iniciar la Simulación
        productor.start();
        consumidor.start();

        // Nota: Para una simulación real, podrías añadir un tercer hilo 
        // para detener el productor y consumidor después de un tiempo (ej. 10 segundos).
        try {
            Thread.sleep(10000); // Ejecutar la simulación por 10 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Detener los bucles internos de los hilos después del tiempo de espera
        productor.parar();
        consumidor.parar();
        
        System.out.println("\n--- SIMULACIÓN FINALIZADA ---");
    }
}

// =================================================================
// CLASE 1: RECURSO COMPARTIDO (BUFFER)
// Contiene la lógica de wait/notify para sincronizar el acceso y el flujo.
// =================================================================
class BufferCompartido {
    private final Queue<Integer> lista; // Almacén de datos (usamos Queue/LinkedList para FIFO)
    private final int capacidad;        // Capacidad máxima del buffer
    private final Random random;

    public BufferCompartido(int capacidad) {
        this.lista = new LinkedList<>();
        this.capacidad = capacidad;
        this.random = new Random();
    }

    // Método para depositar datos en el buffer
    public void producir(int valor) throws InterruptedException {
        // La operación debe ser SYNCHRONIZED para usar wait/notify 
        // y asegurar el acceso exclusivo al recurso compartido.
        synchronized (this) {
            // 1. CONDICIÓN DE ESPERA: Si el buffer está lleno, el Productor debe esperar.
            while (lista.size() == capacidad) {
                String name = Thread.currentThread().getName();
                // [LLENO] es el indicador para la condición de buffer LLENO
                System.out.println("[LLENO] " + name + ": Buffer LLENO. Esperando..."); 
                // wait() libera el candado (this) y pone al hilo en estado WAITING (suspensión pasiva).
                wait(); 
            }
            
            // 2. PRODUCCIÓN: Si hay espacio, se añade el elemento.
            lista.add(valor);
            String name = Thread.currentThread().getName();
            // [P] es el indicador para la acción de PRODUCIR
            System.out.println("[P] " + name + ": Produce -> " + valor + ". (Tamaño: " + lista.size() + ")");

            // 3. NOTIFICACIÓN: notifyAll() despierta al Consumidor.
            notifyAll(); 
        }
    }

    // Método para extraer datos del buffer
    public int consumir() throws InterruptedException {
        synchronized (this) {
            // 1. CONDICIÓN DE ESPERA: Si el buffer está vacío, el Consumidor debe esperar.
            while (lista.isEmpty()) {
                String name = Thread.currentThread().getName();
                // [VACIO] es el indicador para la condición de buffer VACÍO
                System.out.println("[VACIO] " + name + ": Buffer VACÍO. Esperando...");
                // wait() libera el candado (this) y pone al hilo en estado WAITING.
                wait();
            }

            // 2. CONSUMO: Si hay elementos, se extrae el primero (FIFO).
            int valor = lista.poll(); 
            String name = Thread.currentThread().getName();
            // [C] es el indicador para la acción de CONSUMIR
            System.out.println("[C] " + name + ": Consume <- " + valor + ". (Tamaño: " + lista.size() + ")");
            
            // 3. NOTIFICACIÓN: notifyAll() despierta al Productor.
            notifyAll();
            return valor;
        }
    }
}

// =================================================================
// CLASE 2: PRODUCTOR (Hilo)
// =================================================================
class Productor extends Thread {
    private final BufferCompartido buffer;
    private int contador = 0;
    private volatile boolean running = true; 

    public Productor(BufferCompartido buffer) {
        super("PRODUCER");
        this.buffer = buffer;
    }
    
    public void parar() {
        this.running = false;
        this.interrupt(); 
    }

    @Override
    public void run() {
        while (running) {
            try {
                contador++; 
                buffer.producir(contador);
                Thread.sleep(200 + new Random().nextInt(300)); 
            } catch (InterruptedException e) {
                // Indicador de interrupción en el Productor
                System.out.println("[LLENO] PRODUCER: Interrumpido.");
                running = false;
                Thread.currentThread().interrupt();
            }
        }
    }
}

// =================================================================
// CLASE 3: CONSUMIDOR (Hilo)
// =================================================================
class Consumidor extends Thread {
    private final BufferCompartido buffer;
    private volatile boolean running = true;

    public Consumidor(BufferCompartido buffer) {
        super("CONSUMER");
        this.buffer = buffer;
    }
    
    public void parar() {
        this.running = false;
        this.interrupt(); 
    }

    @Override
    public void run() {
        while (running) {
            try {
                buffer.consumir(); 
                Thread.sleep(500 + new Random().nextInt(200)); 
            } catch (InterruptedException e) {
                // Indicador de interrupción en el Consumidor
                System.out.println("[VACIO] CONSUMER: Interrumpido.");
                running = false;
                Thread.currentThread().interrupt();
            }
        }
    }
}