package dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class DictionaryClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 6000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connectat al servidor de Diccionari.");
            
            // Llegeix el missatge de benvinguda del servidor
            String serverMessage = in.readLine();
            System.out.println("Servidor: " + serverMessage);

            String userInput;
            while (true) {
                System.out.print(">> Entra comanda (o EXIT): ");
                userInput = scanner.nextLine();
                
                if (userInput.equalsIgnoreCase("EXIT")) {
                    out.println("EXIT");
                    break;
                }
                
                out.println(userInput);
                
                // Llegeix la resposta del servidor
                String response = in.readLine();
                System.out.println("<< Resposta: " + response);
            }
        } catch (IOException e) {
            System.err.println("No s'ha pogut connectar al servidor: " + e.getMessage());
        }
    }
}