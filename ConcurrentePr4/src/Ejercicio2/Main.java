package Ejercicio2;

import java.util.Vector;

public class Main {

    public static final int M = 5;

    public static void main(String[] args) throws InterruptedException {

        Auxiliar a = new Auxiliar();

        Vector<Incrementor> procesosInc = new Vector<>(M);
        Vector<Decrementor> procesosDec = new Vector<>(M);

        for (int i = 1; i <= M; ++i) {
            procesosInc.add(new Incrementor(a));
            procesosDec.add(new Decrementor(a));
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