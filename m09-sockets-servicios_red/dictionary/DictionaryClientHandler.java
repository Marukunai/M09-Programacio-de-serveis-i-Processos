package dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DictionaryClientHandler implements Runnable {
    private final Socket socket;

    public DictionaryClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("BENVINGUT. Comandes disponibles: DEFINE <paraula>, ADD <paraula> <def>, INFO, EXIT");

            String input;
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("EXIT")) {
                    break;
                }
                String resposta = processCommand(input.trim().toUpperCase());
                out.println(resposta);
            }

        } catch (IOException e) {
            System.out.println("Error en la comunicació amb el client: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("Client desconnectat.");
            } catch (IOException e) {
                // Ignora
            }
        }
    }

    private String processCommand(String command) {
        String[] parts = command.split(" ", 3);
        String action = parts[0];
        
        switch (action) {
            case "DEFINE":
                if (parts.length < 2) return "ERROR: Falta la paraula.";
                return defineWord(parts[1]);
            
            case "ADD":
                if (parts.length < 3) return "ERROR: Falta la paraula o la definició.";
                return addWord(parts[1], parts[2]); 
            
            case "INFO":
                return getInfo();

            default:
                return "ERROR: Comanda no reconeguda.";
        }
    }
    
    private String defineWord(String word) {
        String definition = DictionaryServer.DICTIONARY.get(word);
        if (definition != null) {
            if (definition.matches("[A-Z\\s]+")) {
                return "TRADUCCIO: " + word + " es tradueix com: " + definition;
            } else {
                return "DEFINICIO: " + word + " vol dir: " + definition;
            }
        } else {
            return "ERROR: Paraula '" + word + "' no trobada.";
        }
    }

    private String addWord(String word, String definition) {
        synchronized (DictionaryServer.DICTIONARY) { 
            if (DictionaryServer.DICTIONARY.containsKey(word)) {
                return "WARNING: La paraula '" + word + "' ja existeix. No s'ha modificat.";
            }
            DictionaryServer.DICTIONARY.put(word, definition);
            return "OK: Paraula '" + word + "' afegida correctament.";
        }
    }
    
    private String getInfo() {
        int size = DictionaryServer.DICTIONARY.size();
        return "INFO: El diccionari conté " + size + " paraules/entrades.";
    }
}