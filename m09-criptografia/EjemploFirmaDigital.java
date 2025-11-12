import java.security.*;

public class EjemploFirmaDigital {

    public static KeyPair generarClaves() throws NoSuchAlgorithmException {
        // Crear un objeto KeyPairGenerator para generar claves
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);

        // Generar un par de claves
        return keyGen.generateKeyPair();
    }

    public static byte[] generarFirma(byte[] datos, PrivateKey clavePrivada) throws Exception {
        // Crear un objeto Signature para generar la firma
        Signature firma = Signature.getInstance("SHA256withRSA");

        // Inicializar el objeto Signature con la clave privada
        firma.initSign(clavePrivada);

        // Proporcionar los datos a firmar
        firma.update(datos);

        // Generar la firma
        return firma.sign();
    }

    public static boolean verificarFirma(byte[] datos, byte[] firma, PublicKey clavePublica) throws Exception {
        // Crear un objeto Signature para verificar la firma
        Signature verificador = Signature.getInstance("SHA256withRSA");

        // Inicializar el objeto Signature con la clave pública
        verificador.initVerify(clavePublica);

        // Proporcionar los datos y la firma a verificar
        verificador.update(datos);

        // Verificar la firma
        return verificador.verify(firma);
    }

    public static void main(String[] args) {
        try {
            // Generar un par de claves
            KeyPair claves = generarClaves();

            // CASO 1: Firma válida
            System.out.println("=== CASO 1: Verificación con datos originales ===");
            byte[] datos = "Maruku Putero".getBytes();
            byte[] firma = generarFirma(datos, claves.getPrivate());
            boolean firmaValida = verificarFirma(datos, firma, claves.getPublic());
            System.out.println("Firma válida: " + firmaValida);
            
            // CASO 2: Firma inválida (datos modificados)
            System.out.println("\n=== CASO 2: Verificación con datos modificados ===");
            byte[] datosModificados = "Maruku Putero Modificado".getBytes();
            boolean firmaInvalida = verificarFirma(datosModificados, firma, claves.getPublic());
            System.out.println("Firma válida: " + firmaInvalida);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}