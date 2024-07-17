package Ejercicio1;

import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Main {

    public static final int M = 10;
    public static final int N = 1000;

    public static void main(String[] args) throws InterruptedException {

        Auxiliar a = new Auxiliar();
        Semaphore mutex = new Semaphore(1);

        Vector<Incrementor> procesosInc = new Vector<>(M);
        Vector<Decrementor> procesosDec = new Vector<>(M);

        for (int i = 0; i < M; ++i) {
            procesosInc.add(new Incrementor(a, mutex, N));
            procesosDec.add(new Decrementor(a, mutex, N));
        }

        for (int i = 0; i < M; ++i) {
            procesosInc.elementAt(i).start();
            procesosDec.elementAt(i).start();
        }

        for (int i = 0; i < M; ++i) {
            procesosInc.elementAt(i).join();
            procesosDec.elementAt(i).join();
        }

        System.out.println(a.getN());

    }

}