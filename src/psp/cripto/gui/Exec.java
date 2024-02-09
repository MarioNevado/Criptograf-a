/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psp.cripto.gui;

import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import psp.ftpfile.biz.SendFile;
import psp.cripto.tools.Utils;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import static psp.cripto.gui.DescifrarFichero.ficheroBytes;
import static psp.cripto.gui.DescifrarFichero.grabarFicheroDescifrado;

/**
 *
 * @author dev
 */
public class Exec {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String path, fileName, route;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        SendFile file;
        try (Socket costumer = new Socket("localhost", 6666)) {
            GenerarClave keygen = (GenerarClave)(new ObjectInputStream(new FileInputStream(new File("miClave.key")))).readObject();
            do {
                System.out.print("Introducir ruta absoluta del fichero (exit para salir): ");
                path = sc.nextLine();
                fileName = path.split("/")[path.split("/").length - 1];
                oos = new ObjectOutputStream(costumer.getOutputStream());
                oos.writeObject(path);
                if (!path.equalsIgnoreCase("exit")) {
                    ois = new ObjectInputStream(costumer.getInputStream());
                    file = (SendFile) ois.readObject();
                    if (file.getCode() == 0) {
                        route = "/home/dev/NetBeansProjects/Criptografía/" + fileName;
                        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
                        c.init(Cipher.DECRYPT_MODE, keygen.getClave());
                        byte[] hash = Utils.getHash("SHA-512", file.getContent());
                        //System.out.println(hash == file.getContent());
                        
                        byte[] descifrado = descifrar("/home/dev/NetBeansProjects/Criptografía/Cifrado_file.txt");
                        grabarFicheroDescifrado(descifrado);
                        if (Arrays.equals(hash, descifrado)) {
                            Utils.byteArrayToFile(route, file.getContent());
                            System.out.println("Descargado correctamente");
                        }else System.out.println("No se ha descargado");
                    } else {
                        System.err.println("ERROR " + file.getCode());
                    }
                }
            } while (!path.equals("exit"));
            System.out.println("Cierre controlado del cliente");

        } catch (Exception e) {
            System.err.println("Cierre abrupto del cliente");
            e.printStackTrace();
        }finally{
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ex) {
                    System.err.println("Error cerrando outputstream");
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ex) {
                    System.err.println("Error cerrando inputstream");
                }
            }
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
    public static byte[] descifrar(String route){
        File ficheroDescifrar, keyFichero = new File("miClave.key");
        ObjectInputStream clave;
        GenerarClave keyObj;
        byte[] fichBytes = null;
        byte[] fichBytesCifrados = null;
        
        try {
                ficheroDescifrar = new File(route);
                clave = new ObjectInputStream(new FileInputStream(keyFichero));
                keyObj = (GenerarClave) clave.readObject();
                // Cifrando byte[] con Cipher.
                Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
                c.init(Cipher.DECRYPT_MODE, keyObj.getClave());
                fichBytes = ficheroBytes(ficheroDescifrar);
                System.out.println(c.doFinal(fichBytes) == null);
                return c.doFinal(fichBytes);
//                grabarFicheroDescifrado(ficheroDescifrar, fichBytesCifrados);
//                System.out.println("Desencriptado el fichero...:" + ficheroDescifrar.getName());
            } catch (IOException ex) {
                System.out.println("Error I/O");
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            } catch (InvalidKeyException ex) {
                System.out.println("Clave no valida");
            } catch (IllegalBlockSizeException ex) {
                System.out.println(ex.getMessage());
            } catch (BadPaddingException ex) {
                System.out.println(ex.getLocalizedMessage());
                System.out.println(ex.getMessage());
            } catch (NoSuchAlgorithmException ex) {
                System.out.println(ex.getMessage());
            } catch (NoSuchPaddingException ex) {
                System.out.println(ex.getMessage());
            } catch (java.lang.IllegalArgumentException ex) {

            }
        return null;
    }
    

    public static void grabarFicheroDescifrado(byte[] ficheroBytes) {
        File ficheroCifrado;
        BufferedOutputStream fichSalida = null;
        try {
            ficheroCifrado = new File("DesCifrado_file.txt");
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
