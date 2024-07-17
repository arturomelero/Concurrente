package servidor;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable{

	private String nombre;	
	private String ip;	
	private List<String> ficheros;	

	public Usuario(String nombre, String ip, List<String> ficheros) {
		this.nombre = nombre;
		this.ip = ip;
		this.ficheros = ficheros;
	}
	
	public Usuario (Usuario usuario) {
		this.nombre = usuario.nombre;
		this.ip = usuario.ip;
		this.ficheros = usuario.ficheros;
	}
		
	public String getNombreUsuario() { return this.nombre;}	
	public String getIP() { return this.ip;}	
	public List<String> getFicheros() {return new ArrayList<String> (this.ficheros);}
	

	public boolean equals(Object o) {
		if(this.getClass() != o.getClass()) {
			return false;
		}
		if(this.nombre != ((Usuario) o).getNombreUsuario()) {
			return false;
		}
		return true;
	}
	
	public String toString() {
		String s = "Usuario: " + nombre + "\n\tIP: " + ip +"\n\tArchivos ("+ getFicheros().size() + "): ";
		for(String i : getFicheros()) {
			s += " " + i;
		}
		s += "\n\tEstado: ";
		return s;
	}
	
}

