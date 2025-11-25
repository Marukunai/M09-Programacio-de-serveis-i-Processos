package notes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotesServer {
    private static final int PORT = 8000;

    public static final List<String> NOTES = Collections.synchronizedList(new ArrayList<>());
    
    public static int nextNoteId = 1;

    public static void main(String[] args) {
        System.out.println("Servidor de Notes Compartides actiu al port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client de notes connectat des de: " + clientSocket.getInetAddress());
                
                Thread clientThread = new Thread(new NotesClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Error al servidor: " + e.getMessage());
        }
    }
}