package Thread;

import Resources.ECOPCS;

public class Colectivero implements Runnable {
    private final int id;
    private ECOPCS parque;
    Colectivero(int id,ECOPCS parque) {
        this.id = id;
        this.parque=parque;
    }

    @Override
    public void run() {
      
        while (parque.estaActivo()) {//este hilo quedara trabajo, cuando este cerrado el parque 
            parque.accederColA(id);
        }
    }
}
