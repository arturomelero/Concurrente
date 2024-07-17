package Pruebas;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

public class ClienteConectado implements Runnable{
    private Socket clientSocket;
    private int id;

    public ClienteConectado(Socket clientSocket, int id) {
        this.clientSocket = clientSocket;
        this.id = id;
        System.out.println("Cliente "+id+ ": Cliente Conectado");
    }

    @Override
	public void run() {
    	try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                String fileName = in.readUTF();
                System.out.println("Cliente " + id + ": Solicitando archivo '" + fileName + "' al servidor...");
                String content = readFile(fileName);
                System.out.println("Respuesta enviada."); 
                out.writeUTF(content);
            }
        } catch (IOException e) {
            System.out.println("Cliente " + id + ": Cliente Desconectado");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
    
    private String readFile(String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists() || !file.isFile()) {
                return "\tEl archivo solicitado no existe";
            }
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al leer el archivo";
        }
    }
	
}