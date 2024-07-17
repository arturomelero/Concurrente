package Ejercicio2;

import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket extends Lock {
	private volatile AtomicInteger _number; // Fetch-and-add
    private volatile int _next;

    LockTicket(int N) {
        super(N);
        _number = new AtomicInteger(1);
        _next = 1;
    }

    @Override
    public void takeLock(int procId) {
        int turn;
        turn = _number.getAndIncrement();
        while (turn != _next);
    }

    @Override
    public void releaseLock(int procId) {
        ++_next;
    }
}
