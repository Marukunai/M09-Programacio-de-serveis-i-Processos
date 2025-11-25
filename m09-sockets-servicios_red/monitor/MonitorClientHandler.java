package monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MonitorClientHandler implements Runnable {
    private final Socket socket;

    public MonitorClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("READY. Comandes: REPORT <sensorID> <valor>, GET <sensorID>, EXIT");

            String input;
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("EXIT")) {
                    break;
                }
                String resposta = processCommand(input.trim().toUpperCase());
                out.println(resposta);
            }

        } catch (IOException e) {
            System.out.println("Error en la comunicació amb el sensor: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("Sensor desconnectat.");
            } catch (IOException e) {
                // Ignora
            }
        }
    }

    private String processCommand(String command) {
        String[] parts = command.split(" ");
        String action = parts[0];
        
        switch (action) {
            case "REPORT":
                if (parts.length != 3) return "ERROR 301: Format: REPORT <ID> <VALOR>";
                return reportData(parts[1], parts[2]);
            
            case "GET":
                if (parts.length != 2) return "ERROR 301: Format: GET <ID>";
                return getData(parts[1]);
            
            default:
                return "ERROR 300: Comanda no reconeguda.";
        }
    }
        
    private String reportData(String sensorId, String valueStr) {
        try {
            double value = Double.parseDouble(valueStr);
            
            MonitorServer.SENSOR_DATA.put(sensorId, value);
            
            String response = "OK 100: Dada registrada.";
            
            if (MonitorServer.THRESHOLDS.containsKey(sensorId)) {
                double threshold = MonitorServer.THRESHOLDS.get(sensorId);
                
                if (value > threshold) {
                    response += " ALERTA 400: Umbral de " + threshold + " superat!";
                    System.err.println("!!! ALERTA a " + sensorId + ": " + value + " > " + threshold);
                }
            }
            
            return response;
            
        } catch (NumberFormatException e) {
            return "ERROR 302: Valor de dades no vàlid.";
        }
    }

    private String getData(String sensorId) {
        Double value = MonitorServer.SENSOR_DATA.get(sensorId);
        
        if (value != null) {
            return "OK 100: Últim valor de " + sensorId + " es: " + value;
        } else {
            return "ERROR 201: Sensor " + sensorId + " sense dades reportades.";
        }
    }
}