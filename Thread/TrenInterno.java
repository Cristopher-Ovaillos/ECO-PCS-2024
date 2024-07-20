package Thread;

import Resources.CarreraGomones;
import Resources.ECOPCS;
public class TrenInterno implements Runnable {
    private ECOPCS parque;
    private CarreraGomones carrera;

    public TrenInterno(ECOPCS parque) {
        this.parque= parque;
        carrera= parque.carrera;
    }

    @Override
    public void run() {
        while (parque.estaActivo()) {
            carrera.accionesTren();
        }
    }
}