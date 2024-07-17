package Pruebas;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class Cliente {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private Scanner scanner;

    public Cliente(){
        try{
            socket = new Socket("127.0.0.1", Servidor.PORT);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            scanner = new Scanner(System.in);
            requestFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    private void requestFile() throws IOException {
    	while (true) {
    		System.out.println("Introduce el nombre del archivo:");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase(Servidor.STOP_STRING)) {
                close();
                break;
            } else {
                new Thread(() -> {
                    try {
                        out.writeUTF(input);
                        String response = in.readUTF();
                        System.out.println("Contenido del fichero ------------ \n\n" + response + "\n\nContenido del fichero ------------ \n\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    private void close() {
        try {
            socket.close();
            out.close();
            in.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }
}
