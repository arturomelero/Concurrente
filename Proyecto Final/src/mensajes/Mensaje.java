package mensajes;

import java.io.Serializable;
import java.util.Date;


@SuppressWarnings("serial")
public abstract class Mensaje implements Serializable {

	private TipoMensaje tipo;
	private String emisor;
	private String receptor;

	public Mensaje(TipoMensaje tipo, String emisor, String receptor) {
		this.tipo = tipo;
		this.emisor = emisor;
		this.receptor = receptor;
	}
	
	public TipoMensaje getTipo() {return this.tipo;}
	public String getEmisor() {return this.emisor;}
	public String getReceptor() {return this.receptor;}
	
}