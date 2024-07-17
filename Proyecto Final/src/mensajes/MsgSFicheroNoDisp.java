package mensajes;

import java.io.Serializable;

public class MsgSFicheroNoDisp extends Mensaje implements Serializable {
	public MsgSFicheroNoDisp(String origen, String destino) {
		super(TipoMensaje.S_FICHERO_NO_DISP, origen, destino);
	}
}
