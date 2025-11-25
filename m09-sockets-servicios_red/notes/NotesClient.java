package notes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class NotesClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Client de Notes connectat a " + SERVER_IP + ":" + SERVER_PORT);
            
            String serverMessage = in.readLine();
            System.out.println("Servidor: " + serverMessage);

            String userInput;
            while (true) {
                System.out.print(">> Entra comanda (ADD <text>, LIST, DONE <id>, EXIT): ");
                userInput = scanner.nextLine();
                
                if (userInput.equalsIgnoreCase("EXIT")) {
                    out.println("EXIT");
                    break;
                }
                
                out.println(userInput);
                
                System.out.println("<< Resposta:");
                
                String responseLine;
                
                if ((responseLine = in.readLine()) != null) {
                    System.out.println(responseLine);
                    
                    if (userInput.trim().toUpperCase().startsWith("LIST") && responseLine.startsWith("OK: Llista de Notes:")) {
                        try {
                             while (in.ready()) {
                                responseLine = in.readLine();
                                if (responseLine != null) {
                                    System.out.println(responseLine);
                                }
                            }
                        } catch (IOException e) {
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("No s'ha pogut connectar al Servidor de Notes: " + e.getMessage());
            System.err.println("Assegura't que NotesServer està executant-se al port " + SERVER_PORT + ".");
        }
    }
}