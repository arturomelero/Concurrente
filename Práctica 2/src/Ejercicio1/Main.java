package Ejercicio1;

public class Main {
	
	public static int n = 0;
	
	volatile public static boolean in1 = false;
	volatile public static boolean in2 = false;
	volatile static int last = 1;
	
	public static void main(String [] args) throws InterruptedException {
		
		assert args.length == 1: "Debes especificar el valor de N";
		
		int N = Integer.parseInt(args[0]);
		
		Decrementor dcs = new Decrementor(N);
		Incrementor ics = new Incrementor(N);
		
		ics.start();
		dcs.start();
		
		ics.join();
		dcs.join();
		
		System.out.println("Todos los procesos han terminado.\nEl valor de n es: " + n);
		
	}
}
