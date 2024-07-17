package mensajes;

import java.io.Serializable;

import recursos.LockTicket;

public class MsgSAceptConexion extends Mensaje implements Serializable {
	public MsgSAceptConexion(String origen, String destino) {
		super(TipoMensaje.S_ACEPT_CONEXION, origen, destino);
	}
}
