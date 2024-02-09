package psp.cripto.tools;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import javax.crypto.SecretKey;
import psp.cripto.gui.GenerarClave;
import psp.ftpfile.biz.SendFile;

/**
 *
 * @author dev
 */
public class Utils {
    
    static final String CYPHEREDFILE_NAME = "Cifrado_file.txt", TRANSFORMATION ="AES/ECB/PKCS5Padding";

    public static byte[] fileToByteArray(String origin) throws Exception {
        File file = new File(origin);
        byte[] byteArray = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(byteArray);
        } catch (Exception e) {
            throw e;
        }
        return byteArray;
    }

    public static void byteArrayToFile(String goal, byte[] byteArray) throws Exception {
        File fichero = new File(goal);
        try (FileOutputStream fos = new FileOutputStream(fichero)) {
            fos.write(byteArray);
            fos.flush();
        } catch (Exception e) {
            throw e;
        }
    }

    public static byte[] getBytes(File file) {
        byte[] buffer;
        long bytes;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            bytes = file.length();
            buffer = new byte[(int) bytes];
            int pointer, counter = 0;
            while ((pointer = fis.read()) != -1) {
                buffer[counter] = (byte) pointer;
                counter++;
            }
            return buffer;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException ex) {
                System.err.println("ERROR al cerrar el inputstream");
            }
        }
        return null;
    }
    
    public static String getHash(String algorythm, File file) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorythm);
            md.update(getBytes(file));
            return hex(md.digest());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String hex(byte[] resumen) {
        String hex = "", h;
        for (int i = 0; i < resumen.length; i++) {
            h = Integer.toHexString(resumen[i] & 0xFF) + ":";
            if (h.length() == 1) {
                hex += "0";
            }
            hex += h;
        }
        return hex;
    }

    public static void grabarFicheroCifrado(SendFile fichero, byte[] ficheroBytes) {
        File ficheroCifrado;
        BufferedOutputStream fichSalida = null;
        try {
            ficheroCifrado = new File(CYPHEREDFILE_NAME);
            fichSalida = new BufferedOutputStream(new FileOutputStream(ficheroCifrado));
            fichSalida.write(ficheroBytes);
            fichSalida.flush();

        } catch (FileNotFoundException ex) {
            System.out.println("Fichero no encontrado");
        } catch (IOException ex) {
            System.out.println("Error I/O");

        } finally {
            try {
                fichSalida.close();
            } catch (IOException ex) {
                System.out.println("Error I/O");
            }
        }
    }
    
    public static void grabarFicheroDescifrado(File fichero, byte[] ficheroBytes) {
        File ficheroCifrado;
        BufferedOutputStream bos = null;
        try {
            ficheroCifrado = new File("DesCifrado_" + fichero.getName());
            bos = new BufferedOutputStream(new FileOutputStream(ficheroCifrado));
            bos.write(ficheroBytes);
            bos.flush();
        } catch (FileNotFoundException ex) {
            System.out.println("Fichero no encontrado");
        } catch (IOException ex) {
            System.out.println("Error I/O");
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                System.out.println("Error I/O");
            }
        }
    }

    public static SecretKey getKey(String route) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(route))) {
            GenerarClave keygen = (GenerarClave) ois.readObject();
            return keygen.getClave();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] cifrarClaveSimetrica(byte[] data, SecretKey key) {
        Cipher c;
        try {
            c = Cipher.getInstance(TRANSFORMATION);
            c.init(Cipher.ENCRYPT_MODE, key);
            return c.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] descifrarClaveSimetrica(byte[] data, SecretKey key) {
        Cipher c;
        try {
            c = Cipher.getInstance(TRANSFORMATION);
            c.init(Cipher.DECRYPT_MODE, key);
            return c.doFinal(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        return null;
    }

    public static void cifrarClavePublica() {

    }

    public static void descifrarClavePublica() {

    }

    public static void cifrarClavePrivada(String[] args) {
    }

    public static void descifrarClavePrivada() {
    }

}
