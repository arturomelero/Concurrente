package p2p;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import cliente.Cliente;


public class Emisor extends Thread {

	private Cliente cliente;
	private String nombreArchivo;
	private int puerto;
	
	public Emisor(Cliente cliente, String archivo, int puerto) {
		this.cliente = cliente;
		this.nombreArchivo = archivo;
		this.puerto = puerto;
	}
	
	public void run() {
		try {
			ServerSocket sc = new ServerSocket(puerto);
			Socket socket = sc.accept();
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());			
			
			ArchivoEnviado archivo = crearArchivo();
			
			out.writeObject(archivo);
			out.flush();
			
			sc.close();
			socket.close();
			
		} catch (IOException e) {
			System.err.println("[ERROR]: Error al enviar el archivo durante la conexión P2P: " + e.getMessage());
		}
	}
	
	private ArchivoEnviado crearArchivo() throws IOException {        
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("database/files/" + cliente.getNombreUsuario() + "/" + nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea);
                contenido.append("\n");
            }
        }

        return new ArchivoEnviado(nombreArchivo, contenido.toString().getBytes());
    }
	
}