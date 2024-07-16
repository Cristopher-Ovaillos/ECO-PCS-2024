import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Resources.Colectivo;
import Resources.ECOPCS;
import Thread.Colectivero;
import Thread.Reloj;
import Thread.Visitante;
public class Controlador {

    public static void main(String[] args) {
        ECOPCS parque = new ECOPCS();
        Thread relojThread = new Thread(new Reloj(parque));
        relojThread.start();
        Random numero = new Random();
        int id = 0;

        // Crear y manejar los Colectiveros con ExecutorService
        ExecutorService executorColectiveros = Executors.newFixedThreadPool(3);
        executorColectiveros.submit(new Colectivero(1, parque));
        executorColectiveros.submit(new Colectivero(2, parque));
        executorColectiveros.submit(new Colectivero(3, parque));

        // Crear un ExecutorService para manejar los visitantes
        ExecutorService executorVisitantes = Executors.newCachedThreadPool();

        while (parque.estaActivo()) { // Solo crea cuando está activo
            int i = numero.nextInt(2);
            String tipoAcceso = (i % 2 == 0) ? "Particular" : "Tour";
            Visitante visitante = new Visitante(id++, tipoAcceso, parque);
            executorVisitantes.submit(visitante);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Apagar los ejecutores después de que el parque cierre
        executorColectiveros.shutdown();
        executorVisitantes.shutdown();
        
        try {
            // Esperar que todos los hilos de los colectiveros terminen
            while (!executorColectiveros.isTerminated()) {
                // Simplemente espera
            }
            // Esperar que todos los hilos de los visitantes terminen
            while (!executorVisitantes.isTerminated()) {
                // Simplemente espera
            }
        } catch (Exception e) {
            executorColectiveros.shutdownNow();
            executorVisitantes.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
