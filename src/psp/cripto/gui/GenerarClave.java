package psp.cripto.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author enrique
 */
public class GenerarClave implements Serializable {

    private SecretKey clave;

    public SecretKey getClave() {
        return clave;
    }

    public void setClave(SecretKey clave) {
        this.clave = clave;
    }

    public GenerarClave() {
    }

    public static void main(String[] args) throws Exception {
        System.out.println("1");
        ObjectOutputStream claveObj = null;
        File fichero;
        GenerarClave key;
        KeyGenerator keyGen;
        try {
            key = new GenerarClave();
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            key.setClave(keyGen.generateKey());
            fichero = new File("miClave.key");
            claveObj = new ObjectOutputStream(new FileOutputStream(fichero));
            claveObj.writeObject(key);
//            System.out.println("Clave generada de tipo:" + clave.getAlgorithm());
//            System.out.println("Clave format:" + clave.getFormat());
//            System.out.println("Clave Encoded:" + Arrays.toString(clave.getEncoded()));

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (claveObj != null) {
                    claveObj.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
