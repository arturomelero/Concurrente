package p2p;

import java.io.Serializable;

public class ArchivoEnviado implements Serializable {

    private String nombreArchivo;
    private byte[] contenido;

    public ArchivoEnviado(String nombre, byte[] contenido) {
        this.nombreArchivo = nombre;
    	this.contenido = contenido;
    }

    public byte[] getContenido() {return contenido;}
    public String getNombreArchivo() {return nombreArchivo;}
}
