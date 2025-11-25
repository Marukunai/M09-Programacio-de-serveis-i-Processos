package monitor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MonitorServer {
    private static final int PORT = 7000;

    public static final Map<String, Double> SENSOR_DATA = Collections.synchronizedMap(new HashMap<>()); 

    public static final Map<String, Double> THRESHOLDS = Map.of(
        "TEMP_01", 30.0,
        "STOCK_05", 100.0,
        "PRESU_02", 5.5
    );

    public static void main(String[] args) {
        System.out.println("Servidor de Monitorització actiu al port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Sensor connectat des de: " + clientSocket.getInetAddress());
                
                // Crea i inicia un nou hilo per a cada sensor/client
                Thread clientThread = new Thread(new MonitorClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Error al servidor: " + e.getMessage());
        }
    }
}