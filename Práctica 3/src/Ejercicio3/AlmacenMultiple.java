package Ejercicio3;

import java.util.LinkedList;

public class AlmacenMultiple implements Almacen {

    private int _capacidad;
    private LinkedList<Producto> _colaProductos;

    AlmacenMultiple(int capacidad) {
        _capacidad = capacidad;
        _colaProductos = new LinkedList<>();
    }

    @Override
    public void almacenar(Producto producto) {
        _colaProductos.addLast(producto);
    }

    @Override
    public Producto extraer() {
        Producto producto = _colaProductos.getFirst();
        _colaProductos.removeFirst();
        return producto;
    }
}