package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import recursos.LockTicket;
import recursos.MonitorRW;
import recursos.Pair;
 
public class Servidor {
	
	public static final int MAX_CLIENTS = 100000;
	
	// Empleamos cerrojo para acceso seguro a los métodos de la clase AsignarPuerto
	private LockTicket lock;
    // Empleamos Semaphore para controlar el número de clientes que pueden conectarse al servidor.
    private Semaphore semaforoClientes;
    // Empleamos monitor para controlar el acceso a las variables compartidas
    private MonitorRW monitorUsuarios;
    
    private ServerSocket server;
    private Socket socketCliente;  
    public static final int PORT = 3030;
    
    // Variables compartidas, accedidas a través del listener OyenteCliente
    private Map <String, DatosConexion> usuariosConexion;
    
    protected Servidor() {
    	lock = new LockTicket();
        semaforoClientes = new Semaphore(MAX_CLIENTS);
        monitorUsuarios = new MonitorRW();
        usuariosConexion = new HashMap<String, DatosConexion>();     
        iniciar();
    }
    
    protected void iniciar() {  	
		try {
            server = new ServerSocket(PORT);
            System.out.println("Servidor iniciado. Esperando conexiones...");
            while (true) {
                socketCliente = server.accept();
                manejarConexion(socketCliente);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null && !server.isClosed()) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }		
    }
    
    // GETTERS DE LOS OBJETOS DE FLUJO DE ENTRADA Y DE SALIDA:
    
	protected ObjectOutputStream getUserOutputStream(String userName) throws InterruptedException {
		monitorUsuarios.solicitarLectura();
		ObjectOutputStream flujo = usuariosConexion.get(userName).getOutputStream();
		monitorUsuarios.liberarLectura();
		return flujo;
	}
	
	protected ObjectInputStream getUserInputStream(String userName) throws InterruptedException {
		monitorUsuarios.solicitarLectura();
		ObjectInputStream flujo = usuariosConexion.get(userName).getInputStream();
		monitorUsuarios.liberarLectura();
		return flujo;
	}

	// CONTROL SOBRE EL Nº DE CLIENTES CONECTADOS AL SERVIDOR
	
	public void manejarConexion(Socket socket) {
        try {
            semaforoClientes.acquire(); // Adquirir un permiso del semáforo
            (new OyenteCliente(socket, this, lock)).start();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
	
	public void clienteDesconectado() {
		semaforoClientes.release(); // Liberar un permiso del semáforo
    }
	
	// FUNCIONES PARA LAS LISTAS DE USUARIO:
	
    protected void conectarUsuario(Usuario usuario, ObjectOutputStream out, ObjectInputStream in) throws InterruptedException {
    	monitorUsuarios.solicitarEscritura();
    	try{
    		usuariosConexion.put(usuario.getNombreUsuario(), new DatosConexion(usuario, in, out));
    	}
    	finally{
    		monitorUsuarios.liberarEscritura();
    	}
    }
    
    protected void desconectarUsuario(String nombreUsuario) throws InterruptedException {
    	monitorUsuarios.solicitarEscritura();
    	try{
    		usuariosConexion.remove(nombreUsuario);
        	// Decrementamos en el semáforo el número de clientes conectados
        	clienteDesconectado();
    	}
    	finally{
    		monitorUsuarios.liberarEscritura();
    	}
	}
	
	protected List<Pair<String, List<String>>> getUsuariosYArchivos(String nombreUsuario) throws InterruptedException {
	    monitorUsuarios.solicitarLectura();
	    List<Pair<String, List<String>>> usuariosYArchivos = new ArrayList<>();
	    try {
	    	usuariosYArchivos = usuariosConexion.values().stream()
	            .filter(data -> !data.getUsuario().getNombreUsuario().equals(nombreUsuario))
	            .map(data -> {
	                String usuario = data.getUsuario().getNombreUsuario();
	                List<String> archivos = new ArrayList<>(data.getUsuario().getFicheros());
	                return new Pair<>(usuario, archivos);
	            })
	            .collect(Collectors.toList());
	    } finally {
	        monitorUsuarios.liberarLectura();
	    }
	    return usuariosYArchivos;
	}
	
	protected List<String> getNombresUsuarioConArchivo(String nombre_fichero, String nombreUsuario) throws InterruptedException {
	    monitorUsuarios.solicitarLectura();
	    List<String> usuariosConArchivoExcluyendoUsuario = new ArrayList<>();
	    try {
	        for (DatosConexion data : usuariosConexion.values()) {
	            // Verifica si el usuario no coincide con el nombre proporcionado
	            if (!data.getUsuario().getNombreUsuario().equals(nombreUsuario)) {
	                for (String fichero : data.getUsuario().getFicheros()) {
	                    if (nombre_fichero.equals(fichero)) {
	                        usuariosConArchivoExcluyendoUsuario.add(data.getUsuario().getNombreUsuario());
	                        break;
	                    }
	                }
	            }
	        }
	    } finally {
	        monitorUsuarios.liberarLectura();
	    }
	    return usuariosConArchivoExcluyendoUsuario;
	}

	
	public static void main(String[] args) {
		new Servidor();
	}
}