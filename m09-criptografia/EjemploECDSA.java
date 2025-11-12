import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class EjemploECDSA {

    public static byte[] firmar(byte[] mensaje, KeyPair clave) throws Exception {
        Signature firma = Signature.getInstance("SHA256withECDSA");
        firma.initSign(clave.getPrivate());
        firma.update(mensaje);
        return firma.sign();
    }

    public static boolean verificar(byte[] mensaje, byte[] firmaDigital, KeyPair clave) throws Exception {
        Signature firma = Signature.getInstance("SHA256withECDSA");
        firma.initVerify(clave.getPublic());
        firma.update(mensaje);
        return firma.verify(firmaDigital);
    }

    public static void main(String[] args) {
        try {
            // Generar un par de claves ECDSA (sin BouncyCastle)
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1"); // Curva estándar
            keyGen.initialize(ecSpec, new SecureRandom());
            KeyPair clave = keyGen.generateKeyPair();

            // CASO 1: Firma válida
            System.out.println("=== CASO 1: Verificación con mensaje original ===");
            byte[] mensaje = "Este es un mensaje secreto".getBytes();
            byte[] firmaDigital = firmar(mensaje, clave);
            boolean esValida = verificar(mensaje, firmaDigital, clave);
            System.out.println("La firma es válida: " + esValida);

            // CASO 2: Firma inválida (mensaje modificado)
            System.out.println("\n=== CASO 2: Verificación con mensaje modificado ===");
            byte[] mensajeModificado = "Este es un mensaje modificado".getBytes();
            boolean esInvalida = verificar(mensajeModificado, firmaDigital, clave);
            System.out.println("La firma es válida: " + esInvalida);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}