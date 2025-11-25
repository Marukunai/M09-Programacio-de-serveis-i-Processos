package notes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NotesClientHandler implements Runnable {
    private final Socket socket;

    public NotesClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("READY. Comandes: ADD <text>, LIST, DONE <id>, EXIT");

            String input;
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("EXIT")) {
                    break;
                }
                String resposta = processCommand(input.trim());
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
        String[] parts = command.split(" ", 2);
        String action = parts[0].toUpperCase();
        
        switch (action) {
            case "ADD":
                if (parts.length < 2 || parts[1].trim().isEmpty()) return "ERROR: Falta el text de la nota.";
                return addNote(parts[1].trim());
            
            case "LIST":
                return listNotes();
            
            case "DONE":
                if (parts.length < 2) return "ERROR: Falta l'ID de la nota a completar.";
                return markDone(parts[1].trim());

            default:
                return "ERROR: Comanda no reconeguda.";
        }
    }
        
    private String addNote(String text) {
        synchronized (NotesServer.NOTES) { 
            int currentId = NotesServer.nextNoteId++;
            String note = currentId + ": [PENDENT] " + text;
            NotesServer.NOTES.add(note);
            return "OK: Nota #" + currentId + " afegida.";
        }
    }

    private String listNotes() {
        StringBuilder sb = new StringBuilder("OK: Llista de Notes:\n");
        synchronized (NotesServer.NOTES) {
            if (NotesServer.NOTES.isEmpty()) {
                sb.append("No hi ha notes.");
            } else {
                for (String note : NotesServer.NOTES) {
                    sb.append("- ").append(note).append("\n");
                }
            }
        }
        return sb.toString().trim();
    }

    private String markDone(String idStr) {
        synchronized (NotesServer.NOTES) {
            try {
                int id = Integer.parseInt(idStr);
                
                for (int i = 0; i < NotesServer.NOTES.size(); i++) {
                    String note = NotesServer.NOTES.get(i);
                    if (note.startsWith(id + ": [PENDENT]")) {
                        String doneNote = note.replace("[PENDENT]", "[FET]");
                        NotesServer.NOTES.set(i, doneNote);
                        return "OK: Nota #" + id + " marcada com a completada.";
                    }
                }
                return "WARNING: Nota #" + id + " no trobada o ja completada.";
            } catch (NumberFormatException e) {
                return "ERROR: ID de nota no vàlid.";
            }
        }
    }
}