package Ejercicio1;

public class Decrementor extends Thread {
	
	private int N;
	
	public Decrementor(int n) {
		this.N = n;
	}
	
	public void run() {
		for(int i = 0; i<N; i++) {
			//entry protocol
			Main.in2 = true;
			Main.last = 2;
			while(Main.in1 && Main.last == 2);
			//CS
			Main.n--;
			//Out protocol
			Main.in2 = false;
		}
	}
}
