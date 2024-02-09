/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psp.ftpfile.biz;

import java.io.File;
import java.net.*;
import javax.crypto.SecretKey;
import psp.cripto.gui.GenerarClave;
import psp.cripto.tools.Utils;

/**
 *
 * @author dev
 */
public class Server {

    public static void main(String[] args) {
        Socket costumer;
        ThreadServer th;
        String route = "miClave.key";
        try (ServerSocket ss = new ServerSocket(6666)) {
            SecretKey key = Utils.getKey(route);
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
