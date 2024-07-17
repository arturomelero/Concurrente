package Ejercicio2;

public abstract class Lock {
	protected int _N;
    Lock(int N) {
        _N = N;
    }
    public abstract void takeLock(int id);
    public abstract void releaseLock(int id);
}
