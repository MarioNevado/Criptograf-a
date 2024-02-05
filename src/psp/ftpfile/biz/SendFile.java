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

    private byte[] fichBytesCifrados;
    private byte[] content;   

    public SendFile(int code, byte[] content) {
        this.code = code;
        this.content = content;
    }

    public SendFile(int code, byte[] fichBytesCifrados, byte[] content) {
        this.code = code;
        this.fichBytesCifrados = fichBytesCifrados;
        this.content = content;
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
    
}
