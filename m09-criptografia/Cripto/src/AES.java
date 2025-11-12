import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String KEY_FILE = "clau.aes";
    private static final int IV_SIZE = 16;    

    private static SecretKey getSecretKey() throws IOException, NoSuchAlgorithmException {
        Path keyPath = Path.of(KEY_FILE);
        if (Files.exists(keyPath)) {
            byte[] keyBytes = Files.readAllBytes(keyPath);
            System.out.println("Clau carregada des de: " + KEY_FILE);
            return new SecretKeySpec(keyBytes, ALGORITHM);
        } else {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();
            Files.write(keyPath, secretKey.getEncoded());
            System.out.println("Nova clau generada i guardada a " + KEY_FILE);
            return secretKey;
        }
    }

    public static String encrypt (String text, SecretKey key) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes("UTF-8"));
        byte[] finalData = new byte[IV_SIZE + encryptedBytes.length];
        System.arraycopy(iv, 0, finalData, 0, IV_SIZE);
        System.arraycopy(encryptedBytes, 0, finalData, IV_SIZE, encryptedBytes.length);
        return Base64.getEncoder().encodeToString(finalData);
    }

    public static String decrypt (String text, SecretKey key) throws Exception {
        byte[] encryptedData = Base64.getDecoder().decode(text);
        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(encryptedData, 0, iv, 0, IV_SIZE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        int encryptedLength = encryptedData.length - IV_SIZE;
        byte[] encryptedBytes = new byte[encryptedLength];
        System.arraycopy(encryptedData, IV_SIZE, encryptedBytes, 0, encryptedLength);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] original = cipher.doFinal(encryptedBytes);
        return new String(original, "UTF-8");
    }

    public static void main (String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            SecretKey secretKey = getSecretKey();
            System.out.println("1. Xifrar text");
            System.out.println("2. Desxifrar text");
            System.out.println("Tria una opció: ");
            String option = scanner.nextLine();
            if ("1".equals(option)) {
                System.out.println("Introdueix el text a xifrar: ");
                String text = scanner.nextLine();
                String encrypted = encrypt(text, secretKey);
                System.out.println("\nText xifrat en Base 64: " + encrypted);
            } else if ("2".equals(option)) {
                System.out.println("Introdueix el text xifrat en Base 64: ");
                String text = scanner.nextLine();
                try {
                    String decrypted = decrypt(text, secretKey);
                    System.out.println("\nText desxifrat: " + decrypted);
                } catch (BadPaddingException e) {
                    System.err.println("\nError de desxifrat");
                }
            } else {
                System.out.println("Opció no vàlida.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}