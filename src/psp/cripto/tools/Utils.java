package psp.cripto.tools;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


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

    public static byte[] getBytes(File fichero) {
        FileInputStream fis = null;
        byte[] buff = null;
        try {
            fis = new FileInputStream(fichero);
            long bytes = fichero.length();
            buff = new byte[(int) bytes];
            int i, j = 0;
            while ((i = fis.read()) != -1) {
                buff[j] = (byte) i;
                j++;
            }
        } catch (FileNotFoundException ex) {
            System.err.println("El fichero no existe");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return buff;
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
}
