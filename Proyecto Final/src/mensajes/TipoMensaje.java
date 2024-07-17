package mensajes;

// C indica que es un mensaje (peticion) del cliente al servidor
// S indica que es un mensaje enviado por el servidor

public enum TipoMensaje {
	
	// Mensajes enviados por Cliente
	C_SOLICITUD_CONEXION,
	C_SOLICITUD_LISTA_USUARIOS,
	C_SOLICITUD_FICHERO,
	C_SOLICITUD_ESTE_FICHERO,
	C_EMISOR_ESPERANDO,
	C_SOLICITUD_DESCONEXION,
	
	// Mensajes enviados por Servidor
	S_ACEPT_CONEXION,
	S_LISTA_USUARIOS,
	S_FICHERO_NO_DISP,
	S_CUAL_FICHERO,
	S_CREA_EMISOR,
	S_CREA_RECEPTOR,
	S_ACEPT_DESCONEXION;
	
	public String toString() {
		switch (this) {
        case C_SOLICITUD_CONEXION:
            return "Solicitud de Conexión.";
        case C_SOLICITUD_LISTA_USUARIOS:
            return "Solicitud de Lista de Usuarios.";
        case C_SOLICITUD_FICHERO:
            return "Solicitud de Fichero.";
        case C_SOLICITUD_ESTE_FICHERO:
            return "Solicitud de Fichero Concreto.";
        case C_EMISOR_ESPERANDO:
            return "Emisor Esperando.";
        case C_SOLICITUD_DESCONEXION:
            return "Solicitud de Desconexión.";
        case S_ACEPT_CONEXION:
            return "Conexión Aceptada.";
        case S_LISTA_USUARIOS:
            return "Lista de Usuarios.";
        case S_FICHERO_NO_DISP:
            return "Fichero no Disponible.";
        case S_CUAL_FICHERO:
            return "Múltiples Ficheros con el mismo nombre. ¿Usuario?.";
        case S_CREA_EMISOR:
            return "Inicia P2P como Emisor.";
        case S_CREA_RECEPTOR:
            return "Inicia P2P como Receptor.";
        case S_ACEPT_DESCONEXION:
            return "Desconexion aceptada.";
        default:
            return "[ERROR FATAL]: Este mensaje no debería verse...";
		}
    }
}


