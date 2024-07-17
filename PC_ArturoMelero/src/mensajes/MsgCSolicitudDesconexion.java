package mensajes;

import java.io.Serializable;

public class MsgCSolicitudDesconexion extends Mensaje implements Serializable {
	public MsgCSolicitudDesconexion(String origen, String destino) {
		super(TipoMensaje.C_SOLICITUD_DESCONEXION, origen, destino);
	}
}
