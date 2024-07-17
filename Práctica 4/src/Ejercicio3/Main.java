package Ejercicio3;

import java.util.Vector;

public class Main {

    public static final int NUM_PROD = 3;
    public static final int NUM_CONS = 2;
    public static final int N = 5;
    public static final int CAP_ALMACEN = 10;

    public static void main(String[] args) throws InterruptedException {

        AlmacenImpl almacen = new AlmacenImpl(CAP_ALMACEN);

        Vector<Productores> productores = new Vector<>(NUM_PROD);
        Vector<Consumidores> consumidores = new Vector<>(NUM_CONS);

        for (int i = 0; i < NUM_PROD; ++i)
            productores.add(new Productores(almacen, N, i));

        for (int i = 0; i < NUM_CONS; ++i)
            consumidores.add(new Consumidores(almacen, N));

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