package mensajes;

import java.io.Serializable;

public class MsgSAceptDesconexion extends Mensaje implements Serializable {
	public MsgSAceptDesconexion(String origen, String destino) {
		super(TipoMensaje.S_ACEPT_DESCONEXION, origen, destino);
	}
}
