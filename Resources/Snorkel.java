package Resources;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Vista.Salida;

public class Snorkel {
    private static final int NUM_EQUIPOS = 5; // Numero de equipos de snorkel disponibles
    private static final int NUM_ASISTENTES = 2; // Numero de asistentes
    private final Lock lock = new ReentrantLock();
    private final Condition asistenteDisponible = lock.newCondition();
    private final Condition equipoDisponible = lock.newCondition();
    private int equiposDisponibles = NUM_EQUIPOS;
    private int asistentesDisponibles = NUM_ASISTENTES;
    private Salida sout;

    public Snorkel(Salida sout) {
        this.sout = sout;
    }

    public void actividadSnorkel(int id) {
        try {
            realizarSnorkel(id);
        } catch (InterruptedException e) {
        }

    }

    public void realizarSnorkel(int id) throws InterruptedException {
        lock.lock();

        // Esperar a que haya un asistente disponible
        while (asistentesDisponibles <= 0) {
            sout.soutSnorkel("  La persona " + id + " espera ser atendido");
            asistenteDisponible.await();
        }
        asistentesDisponibles--;

        sout.soutSnorkel(id + " está siendo atendido por un asistente.");

        // Esperar a que haya un equipo de snorkel disponible
        while (equiposDisponibles <= 0) {
            equipoDisponible.await();
        }
        equiposDisponibles--;
        sout.soutSnorkel(id + " ha adquirido el equipo de snorkel.");

        // Simular el tiempo de disfrute de la actividad
        Thread.sleep(1500);

        // Devolver el equipo de snorkel
        equiposDisponibles++;
        //sout.soutSnorkel(id + " ha devuelto el equipo de snorkel.");
        equipoDisponible.signal(); // Despertar a otro hilo esperando por un equipo

        // Liberar al asistente
        asistentesDisponibles++;
        asistenteDisponible.signal(); // Despertar a otro hilo esperando por un asistente
        lock.unlock();

    }
}
