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
import javax.crypto.SecretKey;
import psp.cripto.gui.GenerarClave;
import psp.cripto.tools.Utils;

/**
 *
 * author dev
 */
public class ThreadServer extends Thread {

    Socket costumer;
    String path;
    SecretKey key;
    final String KEY_FILE = "miClave.key", ALGORYTHM = "SHA-512";

    public ThreadServer(Socket costumer, SecretKey key) {
        this.costumer = costumer;
        this.key = key;
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
                    ObjectInputStream claveStream = new ObjectInputStream(new FileInputStream(KEY_FILE));
                    keyObj = (GenerarClave) claveStream.readObject();
                    aux = new File(path);
                    if (aux.exists()) {
                        if (aux.isFile()) {
                            if (aux.canRead()) {
                                file = cifrar(keyObj, aux);
                            } else {
                                file = new SendFile(3, null, "No es legible"); //TODO ESCRIBIR ERROR
                            }
                        } else {
                            file = new SendFile(2, null, "No es un fichero");
                        }
                    } else {
                        file = new SendFile(1, null, "El fichero no existe");
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

    public SendFile cifrar(GenerarClave keyObj, File aux) { 
        byte[] content;
        try {
             content = Utils.fileToByteArray(path);
            return new SendFile(0, Utils.cifrarClaveSimetrica(content, keyObj.getClave()),Utils.getHash(ALGORYTHM, aux));
        } catch (NoSuchAlgorithmException ex) {
            return new SendFile(4, null, "No existe el algoritmo");
        } catch (NoSuchPaddingException ex) {
            return new SendFile(5, null, "Padding Inexistente");
        } catch (InvalidKeyException ex) {
            return new SendFile(6, null, "Clave Inválida");
        } catch (IllegalBlockSizeException ex) {
            return new SendFile(7, null, "Bloque Ilegal");
        } catch (BadPaddingException ex) {
            return new SendFile(8, null, "Padding Erroneo");
        } catch (Exception ex) {
            return new SendFile(9, null, ex.getMessage());
        }
    }
}
