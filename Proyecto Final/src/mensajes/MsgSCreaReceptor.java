package mensajes;

import java.io.Serializable;

public class MsgSCreaReceptor extends Mensaje implements Serializable {
	private String direccionIP;
	private int puerto;
	
	public MsgSCreaReceptor(String origen, String destino, String direccionIP, int puerto) {
		super(TipoMensaje.S_CREA_RECEPTOR, origen, destino);
		this.direccionIP = direccionIP;
		this.puerto = puerto;
	}
	
	public String getIP() {return this.direccionIP;}
	public int getPuerto() {return this.puerto;}
}
