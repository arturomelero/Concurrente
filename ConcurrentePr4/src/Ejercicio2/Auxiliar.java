package Ejercicio2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Auxiliar {

    private int _n;
    private final Lock lock = new ReentrantLock();
    private final Condition noZero = lock.newCondition();

    Auxiliar () {
        _n = 0;
    }

    public void incrementarN() {
    	lock.lock();
    	try {
    		++_n;
    		System.out.println("Valor incrementado: " + _n);
    		noZero.signalAll(); // Notifica a todos los hilos esperando que n no sea cero
    	} finally {
    		lock.unlock();
    	}    	
    }
    
    public void decrementarN() {
    	lock.lock();
    	try {
    		while(_n == 0) {
    			try {
    				noZero.await();
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		--_n;
        	System.out.println("Valor decrementado: " + _n);
    	} finally {
    		lock.unlock();
    	}
    }
    
    public int getN() {return _n;}

}