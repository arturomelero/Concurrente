package Ejercicio2;

public class AlmacenUnitario implements Almacen {

    private Producto _producto;

    AlmacenUnitario() {
        _producto = null;
    }

    @Override
    public void almacenar(Producto producto) {
        _producto = producto;
    }

    @Override
    public Producto extraer() {
        return _producto;
    }
}