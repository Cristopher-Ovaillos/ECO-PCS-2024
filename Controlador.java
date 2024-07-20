import java.util.Random;
import Resources.ECOPCS;
import Thread.Asistente;
import Thread.Colectivero;
import Thread.Reloj;
import Thread.Visitante;
import Thread.TrenInterno;

public class Controlador {

    public static void main(String[] args) {
        ECOPCS parque = new ECOPCS();
        // Reloj
        Thread relojThread = new Thread(new Reloj(parque));
        relojThread.start();
        // Asistentes de snorkel
        Thread asistente1 = new Thread(new Asistente(parque));
        Thread asistente2 = new Thread(new Asistente(parque));
        asistente1.start();
        asistente2.start();
        
        Random numero = new Random();
        int id = 0;
        Thread tren = new Thread(new TrenInterno(parque));
        tren.start();
        
        // Crear y manejar los Colectiveros
        Thread colectivero1 = new Thread(new Colectivero(1, parque));
        Thread colectivero2 = new Thread(new Colectivero(2, parque));
        Thread colectivero3 = new Thread(new Colectivero(3, parque));
        colectivero1.start();
        colectivero2.start();
        colectivero3.start();

        // Crear y manejar los visitantes
        while (parque.estaActivo()) { // Solo crea cuando está activo
            int i = numero.nextInt(2);
            Thread visitante1 = new Thread(new Visitante(id++, (i % 2 == 0) ? "Particular" : "Tour", parque));
            visitante1.start();
            i = numero.nextInt(2);
            Thread visitante2 = new Thread(new Visitante(id++, (i % 2 == 0) ? "Particular" : "Tour", parque));
            visitante2.start();
            i = numero.nextInt(2);
            Thread visitante3 = new Thread(new Visitante(id++, (i % 2 == 0) ? "Particular" : "Tour", parque));
            visitante3.start();

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

    }
}
