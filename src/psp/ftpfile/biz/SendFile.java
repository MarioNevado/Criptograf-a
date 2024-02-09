/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package psp.ftpfile.biz;

import java.io.Serializable;

/**
 *
 * @author dev
 */
public class SendFile implements Serializable{
    private int code;
    private String resumen;
    private byte[] content;   

    public SendFile(int code, byte[] content, String resumen) {
        this.code = code;
        this.content = content;
        this.resumen = resumen;
    }


    public int getCode() {
        return code;
    }

    public byte[] getContent() {
        return content;
    }
    
    @Override
    public String toString() {
        return "Fichero -> " + "código: " + code + "\t contenido: " + new String(content);
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }
    
}
