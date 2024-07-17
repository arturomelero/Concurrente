package Ejercicio1;

public class Incrementor extends Thread {
	
	private int N;
	
	public Incrementor(int n) {
		this.N = n;
	}
	
	public void run() {
		for(int i = 0; i < N; i++) {
			//Entry protocol
			Main.in1 = true;
			Main.last = 1;
			while(Main.in2 && Main.last == 1);
			//CS
			Main.n++;
			//Out protocol
			Main.in1 = false;
		}
	}
}
