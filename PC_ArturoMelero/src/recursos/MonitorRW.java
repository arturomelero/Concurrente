package recursos;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorRW {
	
	private int numLectores;
	private int numEscritores;
	private final Lock lock;
	
	private final Condition colaLectores;
    private final Condition colaEscritores;

	public MonitorRW () {
		numLectores = 0;
		numEscritores = 0;
		lock = new ReentrantLock(true);
		colaLectores = lock.newCondition();
		colaEscritores = lock.newCondition();
	}
	
	/** Método privado para solicitar la lectura. */
	public void solicitarLectura() throws InterruptedException {    	
    	lock.lock();    	
    	while(numEscritores > 0) {
    		colaLectores.wait();
    	}    	
    	numLectores++;
    	lock.unlock();
	}
    
    public void liberarLectura() {
    	lock.lock();
    	numLectores--;    	
    	if(numLectores == 0) {
    		colaEscritores.signal();
    	}
    	lock.unlock();
    }
    
    public void solicitarEscritura() throws InterruptedException {    	
    	lock.lock();    	
    	while(numLectores > 0 || numEscritores > 0) {
    		colaEscritores.wait();
    	}    	
    	numEscritores++;    	
    	lock.unlock();
    }
    
    public void liberarEscritura() {    	
    	lock.lock();    	
    	numEscritores--;    	
		colaLectores.signal();
		colaEscritores.signal();		
		lock.unlock();
    }
	
}