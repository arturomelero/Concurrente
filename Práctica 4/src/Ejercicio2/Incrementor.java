package Ejercicio2;

public class Incrementor extends Thread {

    public static final int N = 10;
    private final Auxiliar _a;

    Incrementor(Auxiliar a) {
        _a = a;
    }

    public void run() {
        for (int i = 0; i < N; ++i)
            _a.incrementarN();
    }

}