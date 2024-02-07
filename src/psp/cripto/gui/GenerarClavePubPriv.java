/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psp.cripto.gui;

import java.io.*;
import java.security.*;

/**
 *
 * @author dev
 */
public class GenerarClavePubPriv implements Serializable {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    //se cifra con la clave publica y se descifra con la privada
    public static void main(String[] args) {
        ObjectOutputStream claveObj = null;
        File ficheroPub = null;
        File ficheroPriv = null;
        GenerarClavePubPriv keyPrivada = null;
        GenerarClavePubPriv keyPublica = null;
        KeyPairGenerator keyGen;
        KeyPair par;

        try {
            keyPrivada = new GenerarClavePubPriv();
            keyPublica = new GenerarClavePubPriv();
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            par = keyGen.generateKeyPair();
            //Calve pública
            keyPrivada.setPrivateKey(par.getPrivate());
            keyPublica.setPublicKey(par.getPublic());
            ficheroPub = new File("miClavePublica.key");
            claveObj = new ObjectOutputStream(new FileOutputStream(ficheroPub));
            claveObj.writeObject(keyPublica);
            claveObj.close();

            //Clave privada
            ficheroPriv = new File("miClavePrivada.key");
            claveObj = new ObjectOutputStream(new FileOutputStream(ficheroPriv));
            claveObj.writeObject(keyPrivada);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (claveObj != null) {
                    claveObj.close();
                }

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }
}
