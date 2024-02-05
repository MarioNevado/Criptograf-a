/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psp.cripto.gui;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import psp.ftpfile.biz.SendFile;
import psp.cripto.tools.Utils;

import javax.crypto.Cipher;

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
            do {
                System.out.print("Introducir ruta absoluta del fichero (exit para salir): ");
                path = sc.nextLine();
                fileName = path.split("/")[path.split("/").length - 1];
                //fileName = path.split("\\\\")[path.split("\\\\").length - 1];

                oos = new ObjectOutputStream(costumer.getOutputStream());
                oos.writeObject(path);
                if (!path.equalsIgnoreCase("exit")) {
                    ois = new ObjectInputStream(costumer.getInputStream());
                    file = (SendFile) ois.readObject();
                    if (file.getCode() == 0) {
                        route = "/home/dev/NetBeansProjects/FileTCP/" + fileName;
                        //route = "C:\\pruebaCripto\\" + fileName;
                        GenerarClave keyObj;
                        ois = new ObjectInputStream(new FileInputStream("miclave.key"));
                        keyObj = (GenerarClave) ois.readObject();
                        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
                        c.init(Cipher.DECRYPT_MODE, keyObj.getClave());
                        byte[] hash = Utils.getHash("SHA-512", file.getContent());
                        if (Arrays.equals(hash, file.getContent())) {
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
}
