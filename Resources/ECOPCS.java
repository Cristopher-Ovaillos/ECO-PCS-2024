package Resources;

import java.util.Random;

import Vista.Salida;

public class ECOPCS {
    private Snorkel snorkel;
    private SouvenirShop shop;
    private SeccionComida restaurantes;
    private FaroMirador faro;
    private CarreraGomones carrera;
    private Hora hora;
    private Salida SOUT;
    private EspacioCol colA, colB, colC;

    public ECOPCS() {
        this.SOUT = new Salida();
        this.snorkel = new Snorkel(SOUT);
        this.faro = new FaroMirador(SOUT);
        this.restaurantes = new SeccionComida(SOUT);
        this.shop = new SouvenirShop(SOUT);
        this.carrera = new CarreraGomones(SOUT);
        this.hora = new Hora(SOUT);
        this.colA = new EspacioCol(SOUT, "Verde");
        this.colB = new EspacioCol(SOUT, "Rojo");
        this.colC = new EspacioCol(SOUT, "VICTORIA");
    }
    //

    //
    public void Parque(int id) {
        try {
            Random random = new Random();
            int randomRest = random.nextInt(3);
            int randomFood = random.nextInt(2);
            boolean snk = true, lch = true;
            boolean continuar = true;

            while (continuar) {
                if (!hora.isActivo()) {
                    continuar = false;
                    continue;
                }

                int randomActividad = random.nextInt(6);
                switch (randomActividad) {
                    case 0:

                        shop.irShop(id);

                        break;
                    case 1:

                        snorkel.actividadSnorkel(id);

                        break;
                    case 2:

                        if (randomFood == 0) {
                            try {
                                restaurantes.takeLunch(randomRest, id, lch);
                                lch = false;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                restaurantes.takeSnack(randomRest, id, snk);
                                snk = false;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case 3:

                        faro.actividadesFaro(id);

                        break;
                    case 4:

                        carrera.realizarActividadGomones(id);

                        break;
                    default:
                        continuar = false;
                        break;
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean estaActivo() {
        return hora.isActivo();
    }

    public void accederHora() {
        // solo el Hilo reloj accede a este.
        this.hora.avanzarHora();
    }

    public void accederColA(int id) {
        try {
            colA.manejarColectivo(id);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void accederColB(int id) {
        try {
            colB.manejarColectivo(id);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void accederColC(int id) {
        try {
            colC.manejarColectivo(id);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}