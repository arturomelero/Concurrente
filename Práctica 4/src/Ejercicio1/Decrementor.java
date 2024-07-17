package Ejercicio1;

public class Decrementor extends Thread {

    public static final int N = 10;
    private final Auxiliar _a;

    Decrementor(Auxiliar a) {
        _a = a;
    }

    public void run() {
        for (int i = 0; i < N; ++i)
            _a.decrementarN();
    }

}