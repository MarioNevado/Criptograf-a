package psp.ftpfile.biz;

import java.io.*;
import java.net.Socket;
import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    GenerarClave keygen;

    public ThreadServer(Socket costumer, GenerarClave keygen) {
        this.costumer = costumer;
        this.keygen = keygen;
    }

    @Override
    public void run() {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        SendFile file;
        File aux;
        GenerarClave keyObj;
        try {
            do {
                ois = new ObjectInputStream(costumer.getInputStream());
                path = (String) ois.readObject();
                if (!path.equals("exit")) {
                    ObjectInputStream claveStream = new ObjectInputStream(new FileInputStream("miClave.key"));
                    keyObj = (GenerarClave) claveStream.readObject();
                    aux = new File(path);
                    if (aux.exists()) {
                        if (aux.isFile()) {
                            if (aux.canRead()) {
                                file = cifrar(keyObj, aux);
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
                    Utils.grabarFicheroCifrado(file, file.getContent());
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

    public SendFile cifrar(GenerarClave keyObj, File aux) { //TODO hacer enum con fallos
        byte[] array;
        Cipher c;
        try {
            c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, keyObj.getClave());
            array = Utils.fileToByteArray(aux.getAbsolutePath());
            byte[] fichBytesCifrados;
            fichBytesCifrados = c.doFinal(array);
            byte[] hashOriginal = Utils.getHash("SHA-512", array);
            System.out.println("Cifrando fichero");
            return new SendFile(0, fichBytesCifrados, hashOriginal);
        } catch (NoSuchAlgorithmException ex) {
            return new SendFile(4, null);
        } catch (NoSuchPaddingException ex) {
            return new SendFile(5, null);
        } catch (InvalidKeyException ex) {
            return new SendFile(6, null);
        } catch (IllegalBlockSizeException ex) {
            return new SendFile(7, null);
        } catch (BadPaddingException ex) {
            return new SendFile(8, null);
        } catch (Exception ex) {
            return new SendFile(9, null);
        }
    }
}
