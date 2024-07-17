package mensajes;

import java.io.Serializable;
import java.util.List;

public class MsgCSolicitudConexion extends Mensaje implements Serializable {
	private List<String> ficheros;
	private String userIP;
	
	public MsgCSolicitudConexion(String origen, String destino, String userIP, List<String> ficheros) {
		super(TipoMensaje.C_SOLICITUD_CONEXION, origen, destino);
		this.userIP = userIP;
		this.ficheros = ficheros;
	}
	
	public String getUserIP() {return this.userIP;}
	public List<String> getFicheros(){return this.ficheros;}
}