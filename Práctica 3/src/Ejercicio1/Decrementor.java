package Ejercicio1;

import java.util.concurrent.Semaphore;

public class Decrementor extends Thread {
	
	private Auxiliar _a;
    private Semaphore _mutex;
    private int _n;

    Decrementor (Auxiliar a, Semaphore mutex, int n) {
        _a = a;
        _mutex = mutex;
        _n = n;
    }

	public void run() {
        for (int i = 0; i < _n; ++i) {
            try {
                _mutex.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            _a.decrementarN();
            _mutex.release();
        }
    }
}
