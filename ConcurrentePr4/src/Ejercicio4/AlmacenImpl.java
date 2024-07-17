package Ejercicio4;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AlmacenImpl implements Almacen {
	private final int capacidadMaxima;
    private final LinkedList<Producto> almacenado;
    private final Lock lock;
    private final Condition noLleno;
    private final Condition noVacio;

    public AlmacenImpl(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.almacenado = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.noLleno = lock.newCondition();
        this.noVacio = lock.newCondition();
    }

    public void almacenar(Producto producto) {
    	lock.lock();
        try {
            while (almacenado.size() >= capacidadMaxima) {
                try {
                	noLleno.await(); // Espera si el almacén está lleno
                } catch (InterruptedException ignored) {
                }
            }
            almacenado.addLast(producto); // Almacena el producto
            System.out.println("Producto " + producto + " almacenado, tamaño actual: " + almacenado.size());
            noVacio.signal(); // Notifica a los consumidores
        } finally {
            lock.unlock();
        }
    }
    

    public Producto extraer()  {
    	lock.lock();
        try {
            while (almacenado.isEmpty()) {
                try {
                	noVacio.await(); // Espera si el almacén está vacío
                } catch (InterruptedException ignored) {
                }
            }
            Producto producto = almacenado.getFirst(); // Extrae el producto
            almacenado.removeFirst();
            System.out.println("Producto " + producto + " extraído, tamaño actual: " + almacenado.size());
            noLleno.signal(); // Notifica a los productores
            return producto;
        } finally {
            lock.unlock();
        }
    }
}