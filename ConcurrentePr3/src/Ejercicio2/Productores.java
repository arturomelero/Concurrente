package Ejercicio2;

import java.util.concurrent.Semaphore;

public class Productores extends Thread {

    private int _id;
    private int _n;
    private Semaphore _lleno, _vacio;
    private Almacen _almacen;

    Productores(int id, int n, Semaphore lleno, Semaphore vacio, Almacen almacen) {
        _id = id;
        _n = n;
        _lleno = lleno;
        _vacio = vacio;
        _almacen = almacen;
    }

    public void run() {
        for (int i = 0; i < _n; ++i) {
            try {
                _vacio.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            _almacen.almacenar(new Producto(_id * _n + i));
            System.out.println("Objeto con ID " + (_id * _n + i)
                    + " almacenado por el productor con ID " + _id);
            _lleno.release();
        }
    }
}