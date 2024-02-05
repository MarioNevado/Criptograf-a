package psp.ftpfile.biz;

import java.io.*;
import java.net.Socket;
import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import psp.cripto.gui.GenerarClave;
import psp.cripto.tools.Utils;

/**
 *
 * author dev
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
        GenerarClave keyObj;

        try {
            ObjectInputStream claveStream = new ObjectInputStream(new FileInputStream("miClave.key"));
            keyObj = (GenerarClave) claveStream.readObject();

            do {
                ois = new ObjectInputStream(costumer.getInputStream());
                path = (String) ois.readObject();
                if (!path.equals("exit")) {
                    aux = new File(path);
                    if (aux.exists()) {
                        if (aux.isFile()) {
                            if (aux.canRead()) {
                                try {

                                    Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
                                    c.init(Cipher.ENCRYPT_MODE, keyObj.getClave());
                                    array = Utils.getBytes(aux);
                                    byte[] fichBytesCifrados = c.doFinal(array);

                                    byte[] hashOriginal = Utils.getHash("SHA-512", array);


                                    file = new SendFile(0, fichBytesCifrados, hashOriginal);
                                    oos = new ObjectOutputStream(costumer.getOutputStream());
                                    oos.writeObject(file);

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

        } catch (Exception e) {

            System.err.println("Cierre abrupto del cliente");
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ex) {
                    System.err.println("Error cerrando ObjectOutputStream");
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ex) {
                    System.err.println("Error cerrando ObjectInputStream");
                }
            }
        }
    }
}
