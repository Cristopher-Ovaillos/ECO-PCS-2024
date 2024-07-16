package Resources;
import Vista.Salida;

public class EspacioCol {
    private final int MAX_CAPACIDAD = 25;
    private int numPasajeros = 0;
    private int pasajerosBajados = 0; // Contador de pasajeros que han bajado
    private boolean lleno = false;
    private Salida sout;
    private int ESPERA = 2000;// 20 MINUTOS
    private int RECORRIDO = 1500;// 15 MINUTOS
    private String nombre;

    public EspacioCol(Salida sout, String name) {
        this.sout = sout;
        this.nombre=name;
    }

    public synchronized void subirPasajero(int idPasajero) throws InterruptedException {
        if (!lleno) {
            numPasajeros++;
            sout.soutColectivo(
                    "Pasajero " + idPasajero + " ha subido al colectivo " + nombre + ". Total: " + numPasajeros);
            if (numPasajeros == MAX_CAPACIDAD) {
                lleno = true;
                sout.soutColectivo("Colectivo lleno " + idPasajero + ". Esperando para partir.");
            }
        }
    }

    public void manejarColectivo(int idColectivero) throws InterruptedException {
        sout.soutColectivo("Colectivero " + idColectivero + " está manejando el colectivo " + nombre);

        // Si el colectivo no se llena, manejar hasta el lugar determinado
        Thread.sleep(ESPERA);// solo espera 20 minutos

        synchronized (this) {
            if (!lleno) {
                sout.soutColectivo("Colectivo " + nombre + " no se lleno a tiempo " + numPasajeros + "/" + MAX_CAPACIDAD
                        + " Manejando hasta el lugar determinado.");
            } else {
                sout.soutColectivo("Colectivo " + nombre + " lleno. Manejando hasta el lugar determinado.");
            }
            Thread.sleep(RECORRIDO);
            sout.soutColectivo("Colectivo " +nombre  + " llego al estacionamiento");
            while (pasajerosBajados < numPasajeros) {
                wait(); // Espera a que todos los pasajeros bajen
            }
    
            sout.soutColectivo("Todos los pasajeros han bajado del colectivo.");
            pasajerosBajados = 0;
            numPasajeros = 0; // Reiniciar el numero de pasajeros
            lleno = false; // Reiniciar el estado de lleno
        }
    }

    public synchronized void bajarPasajero() {
        pasajerosBajados++;
        sout.soutColectivo("Pasajero ha bajado del colectivo "+nombre+" Total: " + pasajerosBajados);

        if (pasajerosBajados == numPasajeros) {

            notifyAll();
        }
    }
}
