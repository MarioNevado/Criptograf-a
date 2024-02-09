/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psp.cripto.gui;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import psp.ftpfile.biz.SendFile;
import psp.cripto.tools.Utils;
import javax.crypto.SecretKey;

/**
 *
 * @author dev
 */
public class Exec {
    
    final static String HOST = "localhost", KEYFILE = "miClave.key", DOWNLOAD_ROUTE = "/home/dev/NetBeansProjects/Criptografía/Descifrado_", ALGORYTHM = "SHA-512";
    final  static int PORT = 6666;
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String path, fileName, route, hash;
        SecretKey key;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        SendFile file;
        byte[] descifrado;
        try (Socket costumer = new Socket(HOST, PORT)) {
            key = Utils.getKey(KEYFILE);
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
                        route = DOWNLOAD_ROUTE + fileName;
                        descifrado = Utils.descifrarClaveSimetrica(file.getContent(), key);
                        hash = Utils.getHash(ALGORYTHM, new File(path));
                        if (hash.equals(file.getResumen())) {
                            Utils.byteArrayToFile(route, descifrado);
                            System.out.println("Descargado correctamente");
                        } else {
                            System.out.println("No se ha descargado");
                        }
                    } else {
                        System.err.println(file);
                    }
                }
            } while (!path.equals("exit"));
            System.out.println("Cierre controlado del cliente");

        } catch (Exception e) {
            System.err.println("Cierre abrupto del cliente");
            e.printStackTrace();
        } finally {
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

}
