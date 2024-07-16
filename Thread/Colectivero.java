package Thread;

import Resources.ECOPCS;

public class Colectivero implements Runnable {
    private final int id;
    private ECOPCS parque;

    public Colectivero(int id, ECOPCS parque) {
        this.id = id;
        this.parque = parque;
    }

 

    @Override
    public void run() {
        int i = parque.asignarColectivo();
        while (parque.estaActivo()) {// este hilo quedara trabado, cuando este cerrado el parque

            switch (i) {
                case 0:
                parque.accederColA(id);
                    break;
                case 1:
                parque.accederColB(id);
                    break;
                default:
                parque.accederColC(id);
                    break;
            }
        }

    }
}
