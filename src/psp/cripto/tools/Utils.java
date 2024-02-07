package psp.cripto.tools;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import psp.cripto.gui.GenerarClavePubPriv;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;

/**
 *
 * @author dev
 */
public class Utils {

    public static byte[] fileToByteArray(String origin) throws Exception {
        File file = new File(origin);
        byte[] byteArray = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(byteArray);

        } catch (Exception e) {
            throw e;
        }
        return byteArray;
    }

    public static void byteArrayToFile(String goal, byte[] byteArray) throws Exception {
        File fichero = new File(goal);
        try (FileOutputStream fos = new FileOutputStream(fichero)) {
            fos.write(byteArray);
        } catch (Exception e) {
            throw e;
        }
    }

    public static byte[] getHash(String algorythm, byte[] content) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorythm);
            md.update(content);
            return md.digest();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    public static String hex(byte[] resumen) {
        String hex = "";
        for (int i = 0; i < resumen.length; i++) {
            String h = Integer.toHexString(resumen[i] & 0xFF) + ":";
            if (h.length() == 1) {
                hex += "0";
            }
            hex += h;
        }
        return hex;
    }
    
    public static void cifrarClaveSimetrica(){
        
    }
    public static void descifrarClaveSimetrica(){
        
    }
    public static void cifrarClavePublica(){
        
    }
    public static void descifrarClavePublica(){
        
    }
    public static void cifrarClavePrivada(String[] args){

    }

    public static void descifrarClavePrivada(){


        
    }

    
}
