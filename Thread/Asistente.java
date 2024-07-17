package Thread;

import Resources.ECOPCS;
import Resources.PuestoSnorkel;

public class Asistente implements Runnable {
    private final PuestoSnorkel puestoSnorkel;
    private ECOPCS parque;

    public Asistente(ECOPCS parque) {
        this.puestoSnorkel = parque.snorkel;
        this.parque = parque;
    }

    @Override
    public void run() {
        while (parque.estaActivo()) {
            try {
                Thread.sleep(200);
                puestoSnorkel.notificarVisitantesEnEspera();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}