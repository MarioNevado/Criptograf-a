/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psp.ftpfile.biz;

import java.io.*;
import java.net.Socket;
import psp.cripto.tools.Utils;

/**
 *
 * @author dev
 */
public class ThreadServer extends Thread {

    Socket costumer;
    String path;

    public ThreadServer(Socket costumer) {
        this.costumer = costumer;
    }

    @Override
    public void run() {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        SendFile file;
        File aux;
        byte[] array;
        try {
            do {
                ois = new ObjectInputStream(costumer.getInputStream());
                path = (String) ois.readObject();
                if (!path.equals("exit")) {
                    aux = new File(path);
                    if (aux.exists()) {
                        if (aux.isFile()) {
                            if (aux.canRead()) {
                                try {
                                    file = new SendFile(0, Utils.fileToByteArray(path));
                                    byte[] hash = Utils.getHash("SHA-512", file.getContent());
                                } catch (FileNotFoundException fnf) {
                                    file = new SendFile(4, null);
                                }
                            } else {
                                file = new SendFile(3, null);
                            }
                        } else {
                            file = new SendFile(2, null);
                        }
                    } else {
                        file = new SendFile(1, null);
                    }
                    oos = new ObjectOutputStream(costumer.getOutputStream());
                    oos.writeObject(file);
                }
            } while (!path.equalsIgnoreCase("exit"));
            System.out.println("Cierre controlado del cliente");
        } catch (Exception e) {
            System.err.println("Cierre abrupto del cliente");
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
                    System.err.println("Error cerrando outputstream");
                }
            }
        }
    }

}
