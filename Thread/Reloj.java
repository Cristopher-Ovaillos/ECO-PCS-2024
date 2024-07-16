package Thread;

import Resources.ECOPCS;
import Resources.Hora;

public class Reloj implements Runnable {
    private ECOPCS hora;

    public Reloj(ECOPCS hora) {
        this.hora = hora;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100); 
                hora.accederHora();
                ;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
