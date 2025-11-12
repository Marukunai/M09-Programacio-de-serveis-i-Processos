import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class SHA256 {

    private static final String HASH_FILE = "hash_sha256.txt";
    private static final String ALGORITHM = "SHA-256";

    public static String calculateSHA256Hash(String text) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        byte[] hashBytes = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void saveHashToFile(String hash) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HASH_FILE))) {
            writer.write(hash);
        }
        System.out.println("Hash SHA-256 guardat correctament a: " + HASH_FILE);
    }

    public static String loadHashFromFile() throws IOException {
        File file = new File(HASH_FILE);
        if (!file.exists()) {
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine();
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Introdueix el text original: ");
            String originalText = scanner.nextLine();
            String generatedHash = calculateSHA256Hash(originalText);
            System.out.println("Hash generat (" + ALGORITHM + "): " + generatedHash);
            saveHashToFile(generatedHash);
            System.out.println("Torna a introduir el text per a la verificació.");
            System.out.print("Introdueix el text de verificació: ");
            String verificationText = scanner.nextLine();
            String storedHash = loadHashFromFile();
            if (storedHash == null) {
                System.err.println("ERROR: No s'ha pogut carregar el hash del fitxer.");
                return;
            }
            String verificationHash = calculateSHA256Hash(verificationText);
            System.out.println("Hash de verificació: " + verificationHash);
            if (storedHash.equals(verificationHash)) {
                System.out.println("\nRESULTAT: Els texts coincideixen (els hashes són idèntics).");
            } else {
                System.out.println("\nRESULTAT: Els texts NO coincideixen.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            scanner.close();
        }
    }
}