package servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DatosConexion {
    private Usuario usuario;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    
    public DatosConexion(Usuario usuario, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
    	this.usuario = usuario;
    	this.inputStream = inputStream;
    	this.outputStream = outputStream;
    }
    
    // Getters
    public Usuario getUsuario() {return this.usuario;}
    public ObjectInputStream getInputStream() {return this.inputStream;}
    public ObjectOutputStream getOutputStream() {return this.outputStream;}
    
 };
