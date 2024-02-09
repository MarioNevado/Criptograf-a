package psp.cripto.gui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author enrique
 */
public class DescifrarFichero {

    public static void main(String[] args) {

        File ficheroDescifrar;
        File keyFichero = new File("miClave.key");
        GenerarClave keyObj;

        FileOutputStream ficheroOut;

        ObjectInputStream clave;
        byte[] fichBytes = null;
        byte[] fichBytesCifrados = null;
        if (args.length > 0) {
            try {
                ficheroDescifrar = new File(args[0]);
                clave = new ObjectInputStream(new FileInputStream(keyFichero));
                keyObj = (GenerarClave) clave.readObject();
                // Cifrando byte[] con Cipher.
                Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
                c.init(Cipher.DECRYPT_MODE, keyObj.getClave());
                fichBytes = ficheroBytes(ficheroDescifrar);
                fichBytesCifrados = c.doFinal(fichBytes);
                grabarFicheroDescifrado(ficheroDescifrar, fichBytesCifrados);
                System.out.println("Desencriptado el fichero...:" + ficheroDescifrar.getName());

            } catch (IOException ex) {
                System.out.println("Error I/O");
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (InvalidKeyException ex) {
                System.out.println("Clave no valida");
            } catch (IllegalBlockSizeException ex) {
                System.out.println(ex.getMessage());
            } catch (BadPaddingException ex) {
                System.out.println(ex.getMessage());
            } catch (NoSuchAlgorithmException ex) {
                System.out.println(ex.getMessage());
            } catch (NoSuchPaddingException ex) {
                System.out.println(ex.getMessage());
            } catch (java.lang.IllegalArgumentException ex) {
            }

        } else {
            System.out.println("No se ha especificado archivo a descifrar.");
        }
    }
    public static byte[] ficheroBytes(File ficheroCifrar) {
        byte[] fichBytes = null;
        try {
            FileInputStream ficheroIn;
            ficheroIn = new FileInputStream(ficheroCifrar);
            long bytes = ficheroCifrar.length();
            fichBytes = new byte[(int) bytes];
            int i, j = 0;
            while ((i = ficheroIn.read()) != -1) {
                fichBytes[j] = (byte) i;
                j++;
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Fichero no encontrado");
        } catch (IOException ex) {
            System.out.println("Error I/O");
        }
        return fichBytes;
    }

    public static void grabarFicheroDescifrado(File fichero, byte[] ficheroBytes) {

        File ficheroCifrado;
        BufferedOutputStream fichSalida = null;
        try {
            ficheroCifrado = new File("DesCifrado_" + fichero.getName());
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
}
