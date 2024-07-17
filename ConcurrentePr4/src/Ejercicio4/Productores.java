package Ejercicio4;

public class Productores extends Thread {

	private final Almacen almacen;
    private final int maxIteraciones;
    private final int id;

    public Productores(Almacen almacen, int maxIteraciones, int id) {
        this.almacen = almacen;
        this.maxIteraciones = maxIteraciones;
        this.id = id;
    }

    public void run() {
        for (int i = 0; i < maxIteraciones; i++) {
            Producto producto = new Producto(id + i);
            almacen.almacenar(producto);
        }
    }
}