package p2p;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Receptor extends Thread {

	private String emisorIP;
	private int puerto;
	Semaphore semMenu;
	
	public Receptor(String emisorIP, int puerto, Semaphore semMenu) {
		this.emisorIP = emisorIP;
		this.puerto = puerto;
		this.semMenu = semMenu;
	}
	
	public void run() {
		
		try {
			Socket socket = new Socket(emisorIP, puerto);			
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			ArchivoEnviado archivo = (ArchivoEnviado) in.readObject();
						
			descargarArchivo(archivo);
			
			System.out.println("[EXITO]: Archivo " + archivo.getNombreArchivo() + " descargado satisfactoriamente.");
			
			socket.close();
			
			semMenu.release();
			
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("[ERROR]: Error en la recepción del archivo durante la conexión P2P: " + e.getMessage());
		} 
	}
	
	public void descargarArchivo(ArchivoEnviado archivoEnviado) throws IOException {
        String nombreArchivo = archivoEnviado.getNombreArchivo();
        byte[] contenido = archivoEnviado.getContenido();

        File carpetaDescargas = new File("downloads");
        File archivoDescarga = new File(carpetaDescargas, nombreArchivo);
        int i = 1;

        while (archivoDescarga.exists()) {
            archivoDescarga = new File(carpetaDescargas, nombreArchivo + " (" + i + ")");
            i++;
        }
        
        try (FileOutputStream fos = new FileOutputStream(archivoDescarga)) {
            fos.write(contenido);
        }
        
    }
	
}