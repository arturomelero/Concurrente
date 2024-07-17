package Ejercicio2;

public class LockBakery extends Lock {
	private volatile int[] _turn;

    LockBakery(int N) {
        super(N);
        _turn = new int[N + 1];
    }

    private int max(int[] v, int begin, int end) {
        int ret = 0;
        for (int i = begin; i <= end; ++i)
            if (v[i] > ret)
                ret = v[i];
        return ret;
    }

    public void takeLock(int procId) {
        _turn[procId] = 1;
        _turn[procId] = max(_turn, 1, _N) + 1;
//        Pair p = new Pair()
        for (int i = 1; i <= _N; ++i)
            if (i != procId)
                while (_turn[i] != 0 && new Pair(_turn[procId], procId).gtgt(new Pair(_turn[i], i)));
    }

    public void releaseLock(int procId) {
        _turn[procId] = 0;
        _turn = _turn;
    }
}
