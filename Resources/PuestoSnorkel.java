package Resources;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import Thread.Visitante;
import Vista.Salida;

public class PuestoSnorkel {
  
    private final Semaphore equiposDisponibles;
    private final BlockingQueue<Visitante> visitantesEnEspera;
    private Salida sout;
    private final int totalEquipos=5;
    public PuestoSnorkel(Salida sout) {
        
        this.equiposDisponibles = new Semaphore(totalEquipos);
        this.visitantesEnEspera = new LinkedBlockingQueue<>();
        this.sout=sout;
    }

    public void solicitarEquipo(Visitante visitante)  {
        synchronized (visitantesEnEspera) {
            while (equiposDisponibles.availablePermits() == 0) {
                sout.soutSnorkel(visitante.getNombre() + " esta esperando un equipo.");
                visitantesEnEspera.add(visitante);
                try {
                    visitantesEnEspera.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            visitantesEnEspera.remove(visitante);  
            try {
                equiposDisponibles.acquire();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            sout.soutSnorkel(visitante.getNombre() + " recibio un equipo.");
        }
    }

    public void devolverEquipo(Visitante visitante) {
        equiposDisponibles.release();
        sout.soutSnorkel(visitante.getNombre() + " devolvio un equipo.");
        notificarVisitantesEnEspera();
    }

    public void notificarVisitantesEnEspera() {
        synchronized (visitantesEnEspera) {
            if (!visitantesEnEspera.isEmpty()) {
                Visitante siguienteVisitante = visitantesEnEspera.poll();
                sout.soutSnorkel("Notificando a " + siguienteVisitante.getNombre() + " que hay un equipo disponible.");
                visitantesEnEspera.notify();
            }
        }
    }

}
