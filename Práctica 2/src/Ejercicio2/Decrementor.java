package Ejercicio2;

public class Decrementor extends Thread {

	public static final int N = 1000;
	
	private Auxiliar a;
	private int id;
	private Lock lock;
	
	Decrementor(Auxiliar a, int id, Lock lock) {
        this.a = a;
        this.id = id;
        this.lock = lock;
    }
	
	public void run() {
        for (int i = 0; i < N; ++i) {
            lock.takeLock(id);
            a.decrementarN();
            lock.releaseLock(id);
        }
    }
}
