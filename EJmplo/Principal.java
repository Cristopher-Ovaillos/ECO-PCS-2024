package EJmplo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

class PuestoSnorkel {
    private final int totalEquipos;
    private final Semaphore equiposDisponibles;
    private final BlockingQueue<Visitante> visitantesEnEspera;

    public PuestoSnorkel(int totalEquipos) {
        this.totalEquipos = totalEquipos;
        this.equiposDisponibles = new Semaphore(totalEquipos);
        this.visitantesEnEspera = new LinkedBlockingQueue<>();
    }

    public void solicitarEquipo(Visitante visitante) throws InterruptedException {
        synchronized (visitantesEnEspera) {
            while (equiposDisponibles.availablePermits() == 0) {
                System.out.println(visitante.getNombre() + " está esperando un equipo.");
                visitantesEnEspera.add(visitante);
                visitantesEnEspera.wait();
            }
            visitantesEnEspera.remove(visitante);  // Remove visitor from queue if it was waiting
            equiposDisponibles.acquire();
            System.out.println(visitante.getNombre() + " recibió un equipo.");
        }
    }

    public void devolverEquipo(Visitante visitante) {
        equiposDisponibles.release();
        System.out.println(visitante.getNombre() + " devolvió un equipo.");
        notificarVisitantesEnEspera();
    }

    public void notificarVisitantesEnEspera() {
        synchronized (visitantesEnEspera) {
            if (!visitantesEnEspera.isEmpty()) {
                Visitante siguienteVisitante = visitantesEnEspera.poll();
                System.out.println("Notificando a " + siguienteVisitante.getNombre() + " que hay un equipo disponible.");
                visitantesEnEspera.notify();
            }
        }
    }
}

class Visitante implements Runnable {
    private final String nombre;
    private final PuestoSnorkel puestoSnorkel;

    public Visitante(String nombre, PuestoSnorkel puestoSnorkel) {
        this.nombre = nombre;
        this.puestoSnorkel = puestoSnorkel;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public void run() {
        try {
            puestoSnorkel.solicitarEquipo(this);
            // Simular tiempo de uso del equipo
            Thread.sleep((int) (Math.random() * 5000));
            puestoSnorkel.devolverEquipo(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Asistente implements Runnable {
    private final PuestoSnorkel puestoSnorkel;

    public Asistente(PuestoSnorkel puestoSnorkel) {
        this.puestoSnorkel = puestoSnorkel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Simular tiempo para chequear y entregar equipo
                Thread.sleep((int) (Math.random() * 3000));
                puestoSnorkel.notificarVisitantesEnEspera();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class Principal {
    public static void main(String[] args) {
        PuestoSnorkel puestoSnorkel = new PuestoSnorkel(5);

        Thread asistente1 = new Thread(new Asistente(puestoSnorkel));
        Thread asistente2 = new Thread(new Asistente(puestoSnorkel));

        asistente1.start();
        asistente2.start();

        for (int i = 1; i <= 10; i++) {
            Thread visitante = new Thread(new Visitante("Visitante " + i, puestoSnorkel));
            visitante.start();
        }
    }
}