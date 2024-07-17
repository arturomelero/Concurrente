package Ejercicio3;

import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Main {

    public static final int NUM_PROD = 5;
    public static final int NUM_CONS = 5;
    public static final int N = 5;
    public static final int CAP_ALMACEN = 3;

    public static void main(String[] args) throws InterruptedException {

        AlmacenMultiple almacen = new AlmacenMultiple(CAP_ALMACEN);

        Semaphore lleno = new Semaphore(0);
        Semaphore vacio = new Semaphore(CAP_ALMACEN);

        Semaphore mutexProd = new Semaphore(1);
        Semaphore mutexCons = new Semaphore(1);

        Vector<Productores> productores = new Vector<>(NUM_PROD);
        Vector<Consumidores> consumidores = new Vector<>(NUM_CONS);

        for (int i = 0; i < NUM_PROD; ++i)
            productores.add(new Productores(i, N, lleno, vacio, mutexProd,almacen));

        for (int i = 0; i < NUM_CONS; ++i)
            consumidores.add(new Consumidores(i, N, lleno, vacio, mutexCons, almacen));

        for (int i = 0; i < NUM_PROD; ++i)
            productores.elementAt(i).start();

        for (int i = 0; i < NUM_CONS; ++i)
            consumidores.elementAt(i).start();

        for (int i = 0; i < NUM_PROD; ++i)
            productores.elementAt(i).join();

        for (int i = 0; i < NUM_CONS; ++i)
            consumidores.elementAt(i).join();

    }

}