import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;

public class EjemploDES {

    public static byte[] cifrarDES(String texto, SecretKey clave) throws Exception {
        // Crear un objeto Cipher para cifrar
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, clave);

        // Cifrar el texto
        byte[] textoCifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
        return textoCifrado;
    }

    public static String descifrarDES(byte[] textoCifrado, SecretKey clave) throws Exception {
        // Crear un objeto Cipher para descifrar
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, clave);

        // Descifrar el texto
        byte[] textoDescifrado = cipher.doFinal(textoCifrado);
        return new String(textoDescifrado, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        try {
            // Generar una clave DES de 8 bytes
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            keyGenerator.init(new SecureRandom());
            SecretKey clave = keyGenerator.generateKey();

            // Cifrar una cadena de texto
            String texto = "Este es un texto secreto";
            byte[] textoCifrado = cifrarDES(texto, clave);

            // Descifrar el texto
            String textoDescifrado = descifrarDES(textoCifrado, clave);

            System.out.println("Texto original: " + texto);
            System.out.println("Texto cifrado: " + new String(textoCifrado));
            System.out.println("Texto descifrado: " + textoDescifrado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}