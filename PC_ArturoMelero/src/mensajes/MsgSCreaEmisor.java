package mensajes;

import java.io.Serializable;

public class MsgSCreaEmisor extends Mensaje implements Serializable {
	private String fichero_solicitado;
	private int puerto;
	
	public MsgSCreaEmisor(String origen, String destino, String fichero_solicitado, int puerto) {
		super(TipoMensaje.S_CREA_EMISOR, origen, destino);
		this.fichero_solicitado = fichero_solicitado;
		this.puerto = puerto;
	}
	
	public String getFicheroSolicitado() {return this.fichero_solicitado;}
	public int getPuerto() {return this.puerto;}
}
