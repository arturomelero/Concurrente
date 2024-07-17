package mensajes;

import java.io.Serializable;

public class MsgCSolicitudListaUsuarios extends Mensaje implements Serializable {
	public MsgCSolicitudListaUsuarios(String origen, String destino) {
		super(TipoMensaje.C_SOLICITUD_LISTA_USUARIOS, origen, destino);
	}
}
