import java.util.Random;

import Resources.ECOPCS;
import Thread.Reloj;
import Thread.Visitante;

public class Controlador {

    public static void main(String[] args) {

        ECOPCS parque = new ECOPCS();
        Thread relojThread = new Thread(new Reloj(parque));
        relojThread.start();
        Random numero = new Random();
        int i;
 
        int id = 0;
      
        while (true) {

            i = numero.nextInt(2);
            String tipoAcceso = (i % 2 == 0) ? "Particular" : "Tour";
            Visitante visitante = new Visitante(id++, tipoAcceso, parque);
            Thread thread = new Thread(visitante);
            thread.start();
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
       
    }
}
