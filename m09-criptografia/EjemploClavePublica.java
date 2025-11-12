import java.security.*;
import javax.crypto.*;

public class EjemploClavePublica {

    public static byte[] cifrar(String texto, PublicKey clavePublica) throws Exception {
        // Generar una clave simétrica AES
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey claveSimetrica = keyGen.generateKey();

        // Crear un objeto Cipher para cifrar con RSA
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.WRAP_MODE, clavePublica);

        // Envolver la clave simétrica con la clave pública
        byte[] claveEnvoltorio = cipher.wrap(claveSimetrica);

        // Crear un objeto Cipher para cifrar con AES
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, claveSimetrica);

        // Cifrar el texto
        byte[] textoCifrado = cipher.doFinal(texto.getBytes());

        // Concatenar el envoltorio de clave con el texto cifrado
        byte[] resultado = new byte[claveEnvoltorio.length + textoCifrado.length];
        System.arraycopy(claveEnvoltorio, 0, resultado, 0, claveEnvoltorio.length);
        System.arraycopy(textoCifrado, 0, resultado, claveEnvoltorio.length, textoCifrado.length);

        return resultado;
    }

    public static String descifrar(byte[] datosCifrados, PrivateKey clavePrivada) throws Exception {
        // Obtener el envoltorio de clave
        byte[] claveEnvoltorio = new byte[256];
        System.arraycopy(datosCifrados, 0, claveEnvoltorio, 0, 256);

        // Crear un objeto Cipher para descifrar el envoltorio con RSA
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.UNWRAP_MODE, clavePrivada);

        // Desenvolver la clave simétrica
        SecretKey claveSimetrica = (SecretKey) cipher.unwrap(claveEnvoltorio, "AES", Cipher.SECRET_KEY);

        // Obtener el texto cifrado
        int longitudTextoCifrado = datosCifrados.length - 256;
        byte[] textoCifrado = new byte[longitudTextoCifrado];
        System.arraycopy(datosCifrados, 256, textoCifrado, 0, longitudTextoCifrado);

        // Crear un objeto Cipher para descifrar el texto con AES
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, claveSimetrica);

        // Descifrar el texto
        byte[] textoDescifrado = cipher.doFinal(textoCifrado);
        return new String(textoDescifrado);
    }

    public static void main(String[] args) {
        try {
            // Generar un par de claves
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);

            KeyPair claves = keyGen.generateKeyPair();

            // Cifrar el texto
            String texto = "Maru Putero";
            byte[] datosCifrados = cifrar(texto, claves.getPublic());

            // Descifrar el texto
            String textoDescifrado = descifrar(datosCifrados, claves.getPrivate());

            System.out.println("Texto original: " + texto);
            System.out.println("Texto cifrado: " + datosCifrados);
            System.out.println("Texto descifrado: " + textoDescifrado);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}