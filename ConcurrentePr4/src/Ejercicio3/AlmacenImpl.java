package Ejercicio3;

import java.util.LinkedList;

public class AlmacenImpl implements Almacen {
	private final int capacidadMaxima;
    private final LinkedList<Producto> almacenado;

    public AlmacenImpl(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.almacenado = new LinkedList<>();
    }

    public synchronized void almacenar(Producto producto) {
        while (almacenado.size() >= capacidadMaxima) {
            try {
            	wait(); // Espera si el almacén está lleno
            } catch (InterruptedException ignored) {
            }
        }
        almacenado.addLast(producto); // Almacena el producto
        System.out.println("Producto " + producto + " almacenado, tamaño actual: " + almacenado.size());
        notifyAll(); // Notifica a los consumidores
    }
    

    public synchronized Producto extraer()  {
        while (almacenado.isEmpty()) {
            try {
            	wait(); // Espera si el almacén está vacío
            } catch (InterruptedException ignored) {
            }
        }
        Producto producto = almacenado.getFirst(); // Extrae el producto
        almacenado.removeFirst();
        System.out.println("Producto " + producto + " extraído, tamaño actual: " + almacenado.size());
        notifyAll(); // Notifica a los productores
        return producto;
    }
}