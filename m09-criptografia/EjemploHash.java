import java.security.*;

public class EjemploHash {

    public static byte[] generarHash(byte[] datos) throws Exception {
        // Crear un objeto MessageDigest para generar el hash
        MessageDigest digest = MessageDigest.getInstance("SHA-512");

        // Proporcionar los datos para generar el hash
        digest.update(datos);

        // Generar el hash
        return digest.digest();
    }

    public static void main(String[] args) {
        try {
            // Generar un hash
            byte[] datos = "Maruku Putero".getBytes();
            byte[] hash = generarHash(datos);

            // Imprimir el hash
            for (byte b : hash) {
                System.out.printf("%02x", b);
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}