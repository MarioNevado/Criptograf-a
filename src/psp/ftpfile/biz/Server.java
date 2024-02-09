/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psp.ftpfile.biz;

import java.io.File;
import java.net.*;
import javax.crypto.SecretKey;
import psp.cripto.tools.Utils;

/**
 *
 * @author dev
 */
public class Server {
    
    static final String ROUTE = "miClave.key";
    public static void main(String[] args) {
        Socket costumer;
        ThreadServer th;
        try (ServerSocket ss = new ServerSocket(6666)) {
            SecretKey key = Utils.getKey(ROUTE);
            while (true){
                costumer = ss.accept();
                th = new ThreadServer(costumer, key);
                th.start();
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
