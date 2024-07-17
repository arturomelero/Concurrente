package Pruebas;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private ServerSocket server;
    public static final int PORT = 3030;
    public static final String STOP_STRING = "exit";
    private int index = 0;

    public Servidor(){
        try{
            server = new ServerSocket(PORT);
            System.out.println("Servidor iniciado.");
            while(true) iniConnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniConnections() throws IOException {
        Socket clientSocket = server.accept();

        if(clientSocket.isConnected()) {
        	++index;
        	Thread hiloCliente = new Thread(new ClienteConectado(clientSocket, index));
            hiloCliente.start();
        }
    }

    public static void main(String[] args) {
        new Servidor();
    }
}