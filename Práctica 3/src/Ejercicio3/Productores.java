package Ejercicio3;

import java.util.concurrent.Semaphore;

public class Productores extends Thread {

    private int _id;
    private int _n;
    private Semaphore _lleno, _vacio, _mutex;
    private Almacen _almacen;

    Productores(int id, int n, Semaphore lleno, Semaphore vacio, Semaphore mutex, Almacen almacen) {
        _id = id;
        _n = n;
        _lleno = lleno;
        _vacio = vacio;
        _mutex = mutex;
        _almacen = almacen;
    }

    public void run() {
        for (int i = 0; i < _n; ++i) {
            try {
                _vacio.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                _mutex.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            _almacen.almacenar(new Producto(_id * _n + i));
            System.out.println("Objeto con ID " + (_id * _n + i)
                    + " almacenado por el productor con ID " + _id);
            _mutex.release();
            _lleno.release();
        }
    }
}