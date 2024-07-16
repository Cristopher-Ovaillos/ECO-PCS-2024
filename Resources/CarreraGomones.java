package Resources;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import Vista.Salida;

public class CarreraGomones {
    private static final int CAPACIDAD_TREN = 3;
    private static final int NUM_GOMONES_INDIVIDUALES = 5;
    private static final int NUM_GOMONES_DOBLES = 3;
    private static final int NUM_BOLSOS = 20;
    private static final int H_GOMONES_LISTOS = 4;

    private BlockingQueue<Integer> tren = new LinkedBlockingQueue<>(CAPACIDAD_TREN);
    private BlockingQueue<Integer> bicicletas = new LinkedBlockingQueue<>();
    private BlockingQueue<String> gomonesIndividuales = new LinkedBlockingQueue<>();
    private BlockingQueue<String> gomonesDobles = new LinkedBlockingQueue<>();
    private BlockingQueue<Integer> bolsos = new LinkedBlockingQueue<>();
    private static CyclicBarrier barrera;

    private Semaphore semaforoGomonesDobles = new Semaphore(NUM_GOMONES_DOBLES * 2);
    private Salida sout;

    public CarreraGomones(Salida sout) {
        for (int i = 1; i <= 100; i++) {
            bicicletas.add(i);
        }

        for (int i = 0; i < NUM_GOMONES_INDIVIDUALES; i++) {
            gomonesIndividuales.add("Gomon Individual " + (i + 1));
        }
        for (int i = 0; i < NUM_GOMONES_DOBLES; i++) {
            gomonesDobles.add("Gomon Doble " + (i + 1));
        }
        for (int i = 1; i <= NUM_BOLSOS; i++) {
            bolsos.add(i);
        }

        barrera = new CyclicBarrier(H_GOMONES_LISTOS, () -> {
            sout.soutCarrera("Todos los gomones están listos. Continuando...");
        });
        this.sout = sout;
    }

    public Integer tomarBicicleta() throws InterruptedException {
        return bicicletas.take();
    }

    public void devolverBicicleta(Integer bicicleta) {
        bicicletas.offer(bicicleta);
    }

    public boolean subirAlTren(int id) {
        return tren.offer(id);
    }

    public void bajarDelTren(int id) {
        tren.remove(id);
    }

    public String tomarGomonIndividual() throws InterruptedException {
        return gomonesIndividuales.take();
    }

    public String tomarGomonDoble() throws InterruptedException {
        String gomonDoble = null;
        // Intenta adquirir dos permisos del semáforo para un gomón doble
        semaforoGomonesDobles.acquire(1);

        gomonDoble = gomonesDobles.take(); // Toma el gomón doble
        return gomonDoble;
    }

    public void devolverGomon(String gomon) {
        if (gomon.startsWith("Gomon Individual")) {
            gomonesIndividuales.offer(gomon);
        } else if (gomon.startsWith("Gomon Doble")) {
            gomonesDobles.offer(gomon);
            semaforoGomonesDobles.release(2);
        }
    }

    public Integer tomarBolso() throws InterruptedException {
        return bolsos.take();
    }

    public void devolverBolso(Integer bolso) {
        bolsos.offer(bolso);
    }

    // Método para esperar a que h gomones estén listos
    public void esperarGomonesListos() {
        try {
            barrera.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    // Método que simula la actividad de los gomones
    public void realizarActividadGomones(int id) throws InterruptedException {
        sout.soutCarrera("Visitante " + id + " está preparándose para la carrera de gomones.");

        // El visitante elige entre bicicleta o tren para llegar al inicio del recorrido
        if (Math.random() < 0.5) {
            Integer bicicleta = tomarBicicleta();
            sout.soutCarrera("Visitante " + id + " ha tomado la bicicleta " + bicicleta + ".");
            Thread.sleep(1000); // Simula el tiempo de viaje en bicicleta
            devolverBicicleta(bicicleta);
            sout.soutCarrera("Visitante " + id + " ha devuelto la bicicleta " + bicicleta + ".");
        } else {
            if (subirAlTren(id)) {
                sout.soutCarrera("Visitante " + id + " ha subido al tren.");
                Thread.sleep(1000); // Simula el tiempo de viaje en tren
                bajarDelTren(id);
                sout.soutCarrera("Visitante " + id + " se ha bajado del tren.");
            } else {
                sout.soutCarrera("Visitante " + id + " no pudo subir al tren porque estaba lleno.");
                return;
            }
        }
        Integer bolso = tomarBolso();
        sout.soutCarrera("Visitante " + id + " ha tomado el bolso con llave número " + bolso + ".");
        String gomon;
        if (Math.random() < 0.5) {
            gomon = tomarGomonIndividual();
        } else {
            gomon = tomarGomonDoble();
        }

        sout.soutCarrera("Visitante " + id + " ha tomado el " + gomon + ".");

        sout.soutCarrera("Visitante " + id + " está esperando a que todos los gomones estén listos.");
        esperarGomonesListos();
        Thread.sleep(2000);
        devolverGomon(gomon);
        sout.soutCarrera("Visitante " + id + " ha devuelto el " + gomon + ".");
        devolverBolso(bolso);
        sout.soutCarrera("Visitante " + id + " ha devuelto el bolso con llave número " + bolso + ".");
        sout.soutCarrera("Visitante " + id + " ha completado la carrera de gomones.");
    }
}
