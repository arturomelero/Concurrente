package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import mensajes.*;
import recursos.AsignarPuerto;
import recursos.LockTicket;


public class OyenteCliente extends Thread {

	private Socket socket = null;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Servidor server;
	private LockTicket lock;
	
	protected OyenteCliente(Socket s, Servidor server, LockTicket lock) throws IOException {
		this.socket = s;
		this.server = server;		
		this.lock = lock;
	}
	
	public void run() {
	    try {
	        this.out = new ObjectOutputStream(this.socket.getOutputStream());
	        this.in = new ObjectInputStream(this.socket.getInputStream());

	        boolean continuar = true; // Variable de salida del bucle (cuando se solicite desconexión)

	        while (continuar) {
	            Mensaje m = (Mensaje) in.readObject();
	            System.out.println("(" + m.getEmisor() + "): " +  m.getTipo().toString());
	            switch (m.getTipo()) {
	                case C_SOLICITUD_CONEXION:
	                    handleConexion((MsgCSolicitudConexion) m);
	                    break;
	                case C_SOLICITUD_LISTA_USUARIOS:
	                    handleListaUsuarios((MsgCSolicitudListaUsuarios)m);
	                    break;
	                case C_SOLICITUD_FICHERO:
	                    handleSolicitarFichero((MsgCSolicitudFichero)m);
	                    break;
	                case C_SOLICITUD_ESTE_FICHERO:
	                	handleMultiplesFicherosRespuesta((MsgCSolicitudEsteFichero)m);
	                	break;
	                case C_EMISOR_ESPERANDO:
	                    handlePreparadoEmisor((MsgCEmisorEsperando)m);
	                    break;
	                case C_SOLICITUD_DESCONEXION:
	                    handleDesconexion((MsgCSolicitudDesconexion) m);
	                    continuar = false;
	                    break;
	                default:
	                    break;
	            }
	        }
	    } catch (IOException | ClassNotFoundException | InterruptedException e) {
	    	server.clienteDesconectado();
	        System.err.println("[ERROR]: Problema desconocido durante la recepción de mensajes de un Cliente: " + e.getMessage());
	    }
	}

	private void handleConexion(MsgCSolicitudConexion msg) throws IOException, InterruptedException {
	    Usuario u = new Usuario(msg.getEmisor(), msg.getUserIP(), msg.getFicheros());
	    server.conectarUsuario(u, this.out, this.in);

	    out.writeObject(new MsgSAceptConexion("Servidor", msg.getEmisor()));
	    out.flush();

	    System.out.println("[CONEXION]: ¡Bienvenido/a " + u.getNombreUsuario() + "!");
	}

	private void handleListaUsuarios(MsgCSolicitudListaUsuarios m) throws IOException, InterruptedException {
		
		out.writeObject(new MsgSListaUsuario("Servidor", m.getEmisor(), server.getUsuariosYArchivos(m.getEmisor())));
	    out.flush();
	}

	private void handleSolicitarFichero(MsgCSolicitudFichero msg) throws IOException, InterruptedException {	    
	    // Buscamos todas las coincidencias y las añadimos
		List<String> clientes = server.getNombresUsuarioConArchivo(msg.getNombreFichero(), msg.getEmisor());

	    switch(clientes.size()) {
	    	case 0:
	    		// Avisamos de que no encontramos el archivo
		        out.writeObject(new MsgSFicheroNoDisp("Servidor", msg.getEmisor()));
		        out.flush();

		        System.out.println("[FALLO]: Usuario " + msg.getEmisor() + " ha solicitado un fichero no disponible (" + msg.getNombreFichero() + ").");
		        break;
	    	case 1:
	    		// Solo hay un fichero. Obtengo flujo de salida emisor y le pido que inicie P2P como Emisor
		        ObjectOutputStream nuevoOut = server.getUserOutputStream(clientes.get(0));
		        try {
		            // Envio mensaje a Emisor
		        	lock.takeLock();
		            int nuevoPuerto = AsignarPuerto.asignarPuertoDisponible();
		            lock.releaseLock();
		            nuevoOut.writeObject(new MsgSCreaEmisor(msg.getEmisor(), clientes.get(0), msg.getNombreFichero(), nuevoPuerto));
		            nuevoOut.flush();
		        } catch (IOException e) {
		            e.printStackTrace();
		        } 
		        break;
	    	default:
	    		// Se han encontrado múltiples ficheros con el mismo nombre. Hay que elegir cuál de todos ellos (marcar Usuario).
		        out.writeObject(new MsgSCualFichero("Servidor", msg.getEmisor(), msg.getNombreFichero(), clientes));
		        out.flush();
		        break;
	    }
	}
	
	private void handleMultiplesFicherosRespuesta(MsgCSolicitudEsteFichero msg) throws IOException, InterruptedException {
		// Obtengo flujo de salida para el usuario que contiene el fichero
        ObjectOutputStream nuevoOut = server.getUserOutputStream(msg.getNombreUsuarios().get(msg.getOpcion()));
        // Envio mensaje de iniciar P2P como Emisor
        lock.takeLock();
        int nuevoPuerto = AsignarPuerto.asignarPuertoDisponible();
        lock.releaseLock();
        nuevoOut.writeObject(new MsgSCreaEmisor(msg.getEmisor(), msg.getNombreUsuarios().get(msg.getOpcion()), msg.getNombreFichero(), nuevoPuerto));
        nuevoOut.flush();
	}

	private void handlePreparadoEmisor(MsgCEmisorEsperando msg) throws IOException, InterruptedException {
	    // Obtengo flujo de salida del receptor
		ObjectOutputStream nuevoOut = server.getUserOutputStream(msg.getReceptor());

	    // Envío mensaje a receptor para iniciar P2P con emisor
	    nuevoOut.writeObject(new MsgSCreaReceptor(msg.getEmisor(), msg.getReceptor(), msg.getIP(), msg.getPuerto()));
	    nuevoOut.flush();
	}	
	
	private void handleDesconexion(MsgCSolicitudDesconexion msg) throws IOException, InterruptedException {	    
	    // Desconectamos al emisor de la petición de la sesión
	    server.desconectarUsuario(msg.getEmisor());

	    // Confirmamos cierre de sesión
	    out.writeObject(new MsgSAceptDesconexion("Servidor", msg.getEmisor()));
	    out.flush();

	    System.out.println("[DESCONEXIÓN]: ¡Hasta la vista Usuario " + msg.getEmisor() + "!");
	}
}
