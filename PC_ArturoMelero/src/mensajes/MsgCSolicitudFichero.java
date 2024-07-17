package mensajes;

import java.io.Serializable;

public class MsgCSolicitudFichero extends Mensaje implements Serializable {

	private String nombre_fichero;
	
	public MsgCSolicitudFichero(String origen, String destino, String nombre_fichero) {
		super(TipoMensaje.C_SOLICITUD_FICHERO, origen, destino);
		this.nombre_fichero = nombre_fichero;
	}
	
	public String getNombreFichero() {return this.nombre_fichero;}
}
