/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psp.ftpfile.biz;

import java.net.*;
import psp.cripto.gui.GenerarClave;

/**
 *
 * @author dev
 */
public class Server {

    public static void main(String[] args) {
        Socket costumer;
        ThreadServer th;
        
        try (ServerSocket ss = new ServerSocket(6666)) {
            GenerarClave keygen = new GenerarClave();
            while (true){
                costumer = ss.accept();
                th = new ThreadServer(costumer, keygen);
                th.start();
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
