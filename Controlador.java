import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Resources.ECOPCS;
import Thread.Asistente;
import Thread.Colectivero;
import Thread.Reloj;
import Thread.Visitante;
public class Controlador {

    public static void main(String[] args) {
        ECOPCS parque = new ECOPCS();
        //reloj
        Thread relojThread = new Thread(new Reloj(parque));
        relojThread.start();
        //asistentes de snorkel
       Thread asistente1= new Thread(new Asistente(parque));
       Thread asistente2= new Thread(new Asistente(parque));
       asistente1.start();
       asistente2.start();
        Random numero = new Random();
        int id = 0;

        // Crear y manejar los Colectiveros con ExecutorService
        ExecutorService executorColectiveros = Executors.newFixedThreadPool(3);
        executorColectiveros.submit(new Colectivero(1, parque));
        executorColectiveros.submit(new Colectivero(2, parque));
        executorColectiveros.submit(new Colectivero(3, parque));

        // Crear un ExecutorService para manejar los visitantes
        ExecutorService executorVis = Executors.newFixedThreadPool(3);
        while (parque.estaActivo()) { // Solo crea cuando está activo
            int i = numero.nextInt(2);
            executorVis.submit(new Visitante(id++, (i % 2 == 0) ? "Particular" : "Tour", parque));
            i = numero.nextInt(2);
            executorVis.submit(new Visitante(id++, (i % 2 == 0) ? "Particular" : "Tour", parque));
            i = numero.nextInt(2);
            executorVis.submit(new Visitante(id++, (i % 2 == 0) ? "Particular" : "Tour", parque));

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }


        executorColectiveros.shutdown();
        executorVis.shutdown();
  
    }
}
