import javax.crypto.Cipher;
import java.io.IOException;
import java.security.*;
import java.util.Base64;
import java.util.Scanner;

public class RSA {

    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException{
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        SecureRandom random = SecureRandom.getInstanceStrong();
        keyGen.initialize(KEY_SIZE, random);
        return keyGen.generateKeyPair();
    }

    public static String encrypt (String text, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = cipher.doFinal(text.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(cipherText);
    }
    public static String decrypt (String text, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] toDecrypt = Base64.getDecoder().decode(text);
        byte[] decryptedBytes = cipher.doFinal(toDecrypt);
        return new String(decryptedBytes, "UTF-8");
    }


    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Generant keys...");
            KeyPair keyPair= generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            System.out.print("\nIntrodueix el missatge a xifrar: ");
            String message = scanner.nextLine();

            // 1. Xifrar el missatge (amb la clau pública)
            String encrypted64 = encrypt(message, publicKey);
            System.out.println("Text xifrat: " + encrypted64);

            // 2. CORRECCIÓ: Desxifrar el text xifrat (amb la clau privada)
            String decrypted64 = decrypt(encrypted64, privateKey);
            System.out.println("Text desxifrat: " + decrypted64);

            if (message.equals(decrypted64)) System.out.print("Èxit: Missatge desxifrat correctament.");
            else System.out.print("Error de xifrat / desxifrat");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}