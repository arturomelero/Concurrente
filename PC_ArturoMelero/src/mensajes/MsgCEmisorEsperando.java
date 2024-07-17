package mensajes;

import java.io.Serializable;

public class MsgCEmisorEsperando extends Mensaje implements Serializable {
	private String direccionIP;
	private int puerto;

	public MsgCEmisorEsperando(String origen, String destino, String direccionIP, int puerto) {
		super(TipoMensaje.C_EMISOR_ESPERANDO, origen, destino);
		this.direccionIP = direccionIP;
		this.puerto = puerto;
	}
	
	public String getIP() {return this.direccionIP;}
	public int getPuerto() {return this.puerto;}
}
