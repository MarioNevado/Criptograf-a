/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psp.ftpfile.biz;

import java.net.*;

/**
 *
 * @author dev
 */
public class Server {

    public static void main(String[] args) {
        Socket costumer;
        ThreadServer th;
        try (ServerSocket ss = new ServerSocket(6666)) {
            while (true){
                costumer = ss.accept();
                th = new ThreadServer(costumer);
                th.start();
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
