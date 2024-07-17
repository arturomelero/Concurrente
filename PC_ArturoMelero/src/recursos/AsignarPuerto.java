package recursos;

import java.net.ServerSocket;

public class AsignarPuerto {
	
	private static int puertoActual = 1024; // Puerto inicial
	
    private static final int PUERTO_MINIMO = 1024;
    private static final int PUERTO_MAXIMO = 65535;

    public static int asignarPuertoDisponible() {
        int puertoAsignado = puertoActual;
        while (!puertoDisponible(puertoAsignado)) {
            puertoAsignado++;
            if (puertoAsignado > PUERTO_MAXIMO) {
                puertoAsignado = PUERTO_MINIMO; // Reinicia al mínimo si alcanza el máximo
            }
            if (puertoAsignado == puertoActual) {
                // Si se ha recorrido todo el rango sin encontrar un puerto disponible
                throw new IllegalStateException("No hay puertos disponibles en este momento.");
            }
        }
        puertoActual = puertoAsignado + 1; // Incrementa el puerto actual
        return puertoAsignado;
    }

    private static boolean puertoDisponible(int puerto) {
        try (ServerSocket ignored = new ServerSocket(puerto)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
