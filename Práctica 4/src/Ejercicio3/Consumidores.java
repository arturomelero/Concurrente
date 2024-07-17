package Ejercicio3;


class Consumidores extends Thread {
    private final Almacen almacen;
    private final int maxIteraciones;

    public Consumidores(Almacen almacen, int maxIteraciones) {
        this.almacen = almacen;
        this.maxIteraciones = maxIteraciones;
    }

    public void run() {
        for (int i = 0; i < maxIteraciones; i++) {
        	Producto producto = almacen.extraer();
        }
    }
}