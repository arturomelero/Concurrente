package mensajes;

import java.io.Serializable;
import java.util.List;

import recursos.Pair;

public class MsgSListaUsuario extends Mensaje implements Serializable {
	private List<Pair<String, List<String>>> usuarios_ficheros;
	
	public MsgSListaUsuario(String origen, String destino, List<Pair<String, List<String>>> usuarios_ficheros) {
		super(TipoMensaje.S_LISTA_USUARIOS, origen, destino);
		this.usuarios_ficheros = usuarios_ficheros;
	}
	
	public List<Pair<String, List<String>>> getListaUsuariosYArchivos(){return this.usuarios_ficheros;}
}
