package mensajes;

import java.io.Serializable;
import java.util.List;

public class MsgCSolicitudEsteFichero extends Mensaje implements Serializable {
	private String nombre_fichero;
	private List<String> usuarios;
	private int opcion;
	
	public MsgCSolicitudEsteFichero(String origen, String destino, String nombre_fichero, List<String> usuarios, int opcion) {
		super(TipoMensaje.C_SOLICITUD_ESTE_FICHERO, origen, destino);
		this.nombre_fichero = nombre_fichero;
		this.usuarios = usuarios;
		this.opcion = opcion;
	}
	
	public String getNombreFichero() {return this.nombre_fichero;}
	public List<String> getNombreUsuarios() {return this.usuarios;}
	public int getOpcion() {return this.opcion;}
}
