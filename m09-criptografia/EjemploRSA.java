import java.security.KeyPair;
import java.security.KeyPairGenerator;
import javax.crypto.Cipher;

public class EjemploRSA {

    public static byte[] cifrarRSA(byte[] texto, KeyPair claves) throws Exception {
        // Crear un objeto Cipher para cifrar
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, claves.getPublic());

        // Cifrar el texto
        byte[] textoCifrado = cipher.doFinal(texto);
        return textoCifrado;
    }

    public static byte[] descifrarRSA(byte[] textoCifrado, KeyPair claves) throws Exception {
        // Crear un objeto Cipher para descifrar
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, claves.getPrivate());

        // Descifrar el texto
        byte[] textoDescifrado = cipher.doFinal(textoCifrado);
        return textoDescifrado;
    }

    public static void main(String[] args) {
        try {
            // Generar un par de claves RSA
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair claves = keyGen.generateKeyPair();

            // Cifrar una cadena de texto
            String texto = "Este es un texto secreto";
            byte[] textoCifrado = cifrarRSA(texto.getBytes(), claves);

            // Descifrar el texto
            byte[] textoDescifrado = descifrarRSA(textoCifrado, claves);

            System.out.println("Texto original: " + texto);
            System.out.println("Texto cifrado: " + new String(textoCifrado));
            System.out.println("Texto descifrado: " + new String(textoDescifrado));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}