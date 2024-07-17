package Ejercicio2;

public class LockRompeEmpate extends Lock {
	private volatile int[] _in;
	private volatile int[] _last;

	LockRompeEmpate(int N) {
		super(N);
		_in = new int[N + 1];
		_last = new int[N + 1];
	}
	
	public void takeLock(int procId) {
		for (int i = 1; i <= _N; ++i) {
			_in[procId] = i;
	        _in = _in;
	        _last[i] = procId;
	        _last = _last;
	        for (int j = 1; j <= _N; ++j)
	        	if (j != procId)
	        		while (_in[j] >= _in[procId] && _last[i] == procId);
		 	}
	 	}

    public void releaseLock(int procId) {
    	_in[procId] = 0;
        _in = _in;
    }
}
