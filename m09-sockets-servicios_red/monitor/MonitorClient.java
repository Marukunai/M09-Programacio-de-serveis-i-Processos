package monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MonitorClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 7000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Client de Monitorització connectat a " + SERVER_IP + ":" + SERVER_PORT);
            
            String serverMessage = in.readLine();
            System.out.println("Servidor: " + serverMessage);

            String userInput;
            while (true) {
                System.out.print(">> Entra comanda (REPORT ID VALOR, GET ID, o EXIT): ");
                userInput = scanner.nextLine();
                
                if (userInput.equalsIgnoreCase("EXIT")) {
                    out.println("EXIT");
                    break;
                }
                
                out.println(userInput);
                
                String response = in.readLine();
                System.out.println("<< Resposta: " + response);
            }
        } catch (IOException e) {
            System.err.println("No s'ha pogut connectar al Servidor de Monitorització: " + e.getMessage());
            System.err.println("Assegura't que MonitorServer està executant-se al port " + SERVER_PORT + ".");
        }
    }
}