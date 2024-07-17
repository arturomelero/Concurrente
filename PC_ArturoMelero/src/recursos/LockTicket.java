package recursos;

import java.util.concurrent.atomic.AtomicInteger;


public class LockTicket {
	
	private volatile AtomicInteger _number; // Fetch-and-add
    private volatile int _next;

    public LockTicket() {
        _number = new AtomicInteger(1);
        _next = 1;
    }

    public void takeLock() {
        int turn;
        turn = _number.getAndIncrement();
        while (turn != _next);
    }

    public void releaseLock() {
        ++_next;
    }
}