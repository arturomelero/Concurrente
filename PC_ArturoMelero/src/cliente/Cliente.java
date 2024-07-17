package cliente;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import mensajes.*;
import servidor.Servidor;
 
public class Cliente {
	
	private String nombreUsuario;
	private String IP;
	private List<String> archivos = new ArrayList<String>();
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	private final static String servidorIP = "127.0.0.1";
	private Socket socket;
	
	private static Scanner scanner = new Scanner(System.in);
	private Semaphore semMenu;
	
	protected Cliente() {
		try {	
			System.out.println("Conectando con el servidor. Por favor, espere...");
			this.socket = new Socket(servidorIP, Servidor.PORT);
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new ObjectInputStream(socket.getInputStream());
			this.semMenu = new Semaphore(0, true);
			iniciar();
		} catch (IOException | InterruptedException e) {
			System.err.println("[ERROR]: Algo salió mal: " + e.getMessage());
		}
	}
	
	// Getters
	public String getClienteIP() {return this.IP;}
	public String getNombreUsuario() {return this.nombreUsuario;}
	protected ObjectInputStream getInputStream() {return in;}
	protected ObjectOutputStream getOutputStream() {return out;}
	
	protected void iniciar() throws InterruptedException, IOException {
		while(!iniciarSesion());
		Thread listener = new OyenteServidor(semMenu, scanner, this);
		listener.start(); 
		inicializarFicheros();
		
		out.writeObject(new MsgCSolicitudConexion(this.nombreUsuario, "Servidor", this.IP, new ArrayList<String>(this.archivos)));
		out.flush();
		
		buclePrograma();
		
		listener.join();
		scanner.close();
		socket.close();	
	}
	
	private boolean iniciarSesion()  {
		System.out.println("¡Bienvenido a la aplicación Cliente!");
		System.out.print("\t>> Nombre de Usuario: ");
		String nombreUsuario = scanner.nextLine();
		System.out.println("");
		if (existeUsuario(nombreUsuario)) {
			// Almacenamos su información
			this.nombreUsuario = nombreUsuario;
			InetAddress address;
			try {
				address = InetAddress.getLocalHost();
				this.IP = address.getHostAddress();
				System.out.println("Bienvenido/a " + this.nombreUsuario + ", tu dirección IP es " + this.IP +".");
			} catch (UnknownHostException e) {
				System.err.println("[ERROR]: UknownHostException: " + e.getMessage());
				return false;
			}
			return true;
        } else {
            System.out.println("[FALLO]: Usuario no registrado. Inténtelo de nuevo.\n");
            return false;
        }		
	}
	
	private boolean existeUsuario(String nombreUsuario) {
		Path filePath = Paths.get("database/users.txt");
		
		if (!Files.exists(filePath)) {
            System.err.println("[ERROR]: Imposible conectar con la base de datos.");
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals(nombreUsuario)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("[Error]: Error al realizar la lectura de usuarios: " + e.getMessage());
        }

        return false;
	}
	
	private void inicializarFicheros() throws IOException {
         File carpetaUsuario = new File("database/files/" + this.nombreUsuario);
         if (carpetaUsuario.exists() && carpetaUsuario.isDirectory()) {
        	 // Si existe la carpeta
        	 File[] files = carpetaUsuario.listFiles();
             if (files != null) {
            	 // Si hay archivos
                 for (File file : files) {
                     if (file.isFile()) {
                         this.archivos.add(file.getName());
                     }
                 }
             }
         }
	}
	
	public void buclePrograma() throws InterruptedException, IOException {
		int opcion;		
		do {
			semMenu.acquire();			
			opcion = menu();
			// Usuarios conectados y ficheros
			switch(opcion) {
			case 0: // Cerrar sesion
				System.out.println("... Enviando solicitud (Desconexión).\n");
				out.writeObject(new MsgCSolicitudDesconexion(this.nombreUsuario, "Servidor"));
				out.flush();
				break;
			case 1: // Lista de usuarios
				System.out.println("... Enviando solicitud (Lista de Usuarios y sus Archivos).\n");
				out.writeObject(new MsgCSolicitudListaUsuarios(this.nombreUsuario, "Servidor"));
				out.flush();
				break;
			case 2: // Descargar fichero
				System.out.println("... Preparando solicitud (Solicitud de Fichero). \nIntroduzca el nombre del fichero deseado.");
				System.out.print("\t>> Nombre del fichero: ");
				String fichero = scanner.next();
				System.out.println("");
				out.writeObject(new MsgCSolicitudFichero(this.nombreUsuario, "Servidor", fichero));
				out.flush();
				break;
			default:
				System.err.println("[ERROR]: Error fatal (Opción Inválida). Este mensaje no debería poder verse.");
				opcion = 0;
				break;
			}
			System.out.println("-----------------------------------------------------------------------------");
			System.out.println("-----------------------------------------------------------------------------\n");
			
		} while(opcion != 0);
				
	}
		
	private int menu() {
		System.out.println("\nFuncionalidad Disponible:");
	    System.out.println("\t1. Consultar lista de usuarios conectados con sus archivos.");
	    System.out.println("\t2. Descargar un archivo.");
	    System.out.println("\t0. Salir.\n");
	    System.out.println("Por favor, introduzca una opción válida:");
	    System.out.print("(" + this.nombreUsuario + ")\t>> Opción: ");
	    int opcion; 
	    do {
	    	try {
	    		opcion = Integer.parseInt(scanner.next()); 
	    	} catch(NumberFormatException e) {
	    		opcion = -1;
	    	}
	    	if(opcion != 0 && opcion != 1 && opcion != 2) {
	    		System.out.println("\n[Error]: Ingrese una opción válida.");
				System.out.print("(" + this.nombreUsuario + ")\t>> Opción: ");
				opcion = -1;
	    	}
	    } while(opcion == -1);
	    
		System.out.println("");
		
		return opcion;
	}
	
	public static void main(String[] args) {
		new Cliente();
	}
	
}