package Ejercicio1;

public class Auxiliar {

    private int _n;

    Auxiliar () {
        _n = 0;
    }

    public synchronized void incrementarN() {
    	++_n;
    	System.out.println("Valor incrementado: " + _n);
    	notifyAll(); // Notifica a todos los hilos que están esperando
    }
    
    public synchronized void decrementarN() {
    	while(_n == 0) {
    		try { 
    			wait(); // Espera hasta que n no sea 0
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    	--_n;
    	System.out.println("Valor decrementado: " + _n);
    }
    
    public int getN() {return _n;}

}