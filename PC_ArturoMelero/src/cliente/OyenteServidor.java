package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import mensajes.*;
import p2p.Emisor;
import p2p.Receptor;
import recursos.AsignarPuerto;
import recursos.LockTicket;
import recursos.Pair;

public class OyenteServidor extends Thread {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Cliente cliente;
	// Semaforo para la impresión del menú
	Semaphore semMenu;
	// Scaner del cliente para la impresión del texto en selección fichero
	Scanner scanner;
	
	protected OyenteServidor(Semaphore semMenu, Scanner scanner, Cliente cliente) throws IOException {
		this.cliente = cliente;
		this.semMenu = semMenu;
		this.scanner = scanner;
		this.out = cliente.getOutputStream();
		this.in = cliente.getInputStream();
	}
	
	public void run() {
	    try {
	        boolean continuar = true;
	        while (continuar) {
	            Mensaje m = (Mensaje) in.readObject();
	            switch (m.getTipo()) {
	                case S_ACEPT_CONEXION:
	                	handleAceptConexion(m);
	                    break;
	                case S_LISTA_USUARIOS:
	                    handleListaUsuarios(m);
	                    break;
	                case S_CUAL_FICHERO:
	                	handleMultiplesFicheros(m);
	                	break;
	                case S_FICHERO_NO_DISP:
	                    handleErrorFichero();
	                    break;
	                case S_CREA_EMISOR:
	                    handleCreaEmisor(m);
	                    break;
	                case S_CREA_RECEPTOR:
	                    handleCreaReceptor(m);
	                    break;
	                case S_ACEPT_DESCONEXION:
	                    handleCerrarConexionConfirmacion();
	                    continuar = false;
	                    break;
	                default:
	                    break;
	            }
	        }
	    } catch (ClassNotFoundException | IOException e) {
	        System.err.println("[ERROR]: Problema desconocido durante la recepción de mensajes del Servidor: " + e.getMessage());
	    }
	}

	private void handleAceptConexion(Mensaje m) {
	    MsgSAceptConexion msg = (MsgSAceptConexion) m;
		System.out.println("Conexión establecida.");
	    semMenu.release();
	}

	private void handleListaUsuarios(Mensaje m) {
	    MsgSListaUsuario msg = (MsgSListaUsuario) m;

	    List<Pair<String, List<String>>> usuarios_archivos = msg.getListaUsuariosYArchivos();
	    
	    System.out.println("=================== Usuarios y sus archivos ===================\n");
	    for (int i = 0; i < usuarios_archivos.size(); i++) {
	        System.out.println("\t" + usuarios_archivos.get(i).getKey() + ":");
	        List<String> archivosUsuario = usuarios_archivos.get(i).getValue();
	        if (archivosUsuario.isEmpty()) {
	            System.out.println("\t\t[AVISO]: Sin archivos.");
	        } else {
	            for (int j = 0; j < archivosUsuario.size(); j++) {
	                System.out.println("\t\t- " + archivosUsuario.get(j));
	            }
	        }
	    }
	    System.out.println("\n=================== Fin Usuarios y archivos ===================");
        semMenu.release();
	}
	
	private void handleErrorFichero() {
	    System.err.println("[ERROR]: El fichero con dicho nombre no existe en la base de datos.");
	    semMenu.release();
	}
	
	private void handleMultiplesFicheros(Mensaje m) throws IOException {
		MsgSCualFichero msg = (MsgSCualFichero) m;
		System.out.println("Existen múltiples ficheros con ese nombre para distintos usuarios:");
        for (int i = 0; i < msg.getNombreUsuarios().size(); i++) {
            System.out.println("\t" + (i + 1) + ". " + msg.getNombreUsuarios().get(i));
        }
        System.out.println("\t0. Salir");
        int opcion;
        do {
            System.out.print("(" + cliente.getNombreUsuario() + ")\t>> Opción:");
            try {
	    		opcion = Integer.parseInt(scanner.next()); 
	    	} catch(NumberFormatException e) {
	    		opcion = -1;
	    	}
            if (opcion > 0 && opcion <= msg.getNombreUsuarios().size()) {
            	System.out.println("\nHa seleccionado el archivo de: " + msg.getNombreUsuarios().get(opcion - 1) + "\n");
                
				out.writeObject(new MsgCSolicitudEsteFichero(cliente.getNombreUsuario(), "Servidor", msg.getNombreFichero(), msg.getNombreUsuarios(), opcion-1));
				out.flush();
				break;
                
            } else if (opcion == 0) {
                System.out.println("Saliendo...");
                semMenu.release();
                break;
            } else {
                System.out.println("[FALLO]: Opción inválida. Por favor, seleccione una opción adecuada.");
            }
        } while (true);
	}

	private void handleCreaEmisor(Mensaje m) throws IOException {
		MsgSCreaEmisor msg = (MsgSCreaEmisor) m;
		out.writeObject(new MsgCEmisorEsperando(cliente.getNombreUsuario(), m.getEmisor(), cliente.getClienteIP(), msg.getPuerto()));
		out.flush();
	    (new Emisor(cliente, msg.getFicheroSolicitado(), msg.getPuerto())).start();
	}

	private void handleCreaReceptor(Mensaje m) {
		MsgSCreaReceptor msg = (MsgSCreaReceptor) m;
		(new Receptor(msg.getIP(), msg.getPuerto(), semMenu)).start();
	}

	private void handleCerrarConexionConfirmacion() {
	    System.out.println("[DESCONEXIÓN]: Desconexión exitosa.");
	}

}
