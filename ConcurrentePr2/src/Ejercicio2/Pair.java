package Ejercicio2;

public class Pair {
    private final int _a;
    private final int _b;
    
    public Pair(int a, int b) {
        _a = a;
        _b = b;
    }

	public int getA() {
        return _a;
    }

    public int getB() {
        return _b;
    }

    public boolean gtgt(Pair p) {
        return _a > p.getA() || _a == p.getA() && _b > p.getB();
    }
}
