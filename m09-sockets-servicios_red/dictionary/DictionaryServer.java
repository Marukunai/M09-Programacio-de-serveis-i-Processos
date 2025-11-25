package dictionary;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DictionaryServer {
    private static final int PORT = 6000;
    
    public static final Map<String, String> DICTIONARY = Collections.synchronizedMap(new HashMap<>());
    
    static {
        DICTIONARY.put("JAVA", "Llenguatge de programació orientat a objectes.");
        DICTIONARY.put("HILO", "Unitat d'execució dins d'un procés.");
        DICTIONARY.put("CONCURRENCIA", "Execució simultània de tasques.");
        DICTIONARY.put("THREAD", "HILO");
    }

    public static void main(String[] args) {
        System.out.println("Servidor de Diccionari actiu al port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat: " + clientSocket.getInetAddress());
                
                Thread clientThread = new Thread(new DictionaryClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Error al servidor: " + e.getMessage());
        }
    }
}