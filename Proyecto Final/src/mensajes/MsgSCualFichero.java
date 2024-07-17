package mensajes;

import java.io.Serializable;
import java.util.List;

public class MsgSCualFichero extends Mensaje implements Serializable {
	private String nombre_fichero;
	private List<String> usuarios;
	
	public MsgSCualFichero(String origen, String destino, String nombre_fichero, List<String> usuarios) {
		super(TipoMensaje.S_CUAL_FICHERO, origen, destino);
		this.nombre_fichero = nombre_fichero;
		this.usuarios = usuarios;
	}
	
	public String getNombreFichero() {return this.nombre_fichero;}
	public List<String> getNombreUsuarios() {return this.usuarios;}
}
