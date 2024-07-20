package Resources;

import java.util.Random;
import java.util.concurrent.Semaphore;

import Thread.Visitante;
import Vista.Salida;

public class ECOPCS {
    public PuestoSnorkel snorkel;
    private SouvenirShop shop;
    private SeccionComida restaurantes;
    private FaroMirador faro;
    public CarreraGomones carrera;
    private Hora hora;
    private Salida SOUT;
    private EspacioCol colA, colB, colC;
    private int ASIGNAR_COL = 0, TOTAL_COLECTIVOS = 2;
    private Semaphore aplicar, entrar;

    public ECOPCS() {
        this.SOUT = new Salida();
        this.snorkel = new PuestoSnorkel(SOUT);
        this.faro = new FaroMirador(SOUT);
        this.restaurantes = new SeccionComida(SOUT);
        this.shop = new SouvenirShop(SOUT);
        this.carrera = new CarreraGomones(SOUT);
        this.hora = new Hora(SOUT);
        this.colA = new EspacioCol(SOUT, "CONCURRENTE");
        this.colB = new EspacioCol(SOUT, "SILVIA");
        this.colC = new EspacioCol(SOUT, "VICTORIA");
        aplicar = new Semaphore(1);
        entrar = new Semaphore(1);
    }

    public void entrarParque(int id) {
        try {
            entrar.acquire();
         
            SOUT.soutPersonas(id+ " ENTRO");

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        entrar.release();
    }

    public void salirParque(int id) {
        try {
            Thread.sleep(1000);
            entrar.acquire();

          
            SOUT.soutPersonas(id+" SALIO");

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        entrar.release();
    }

    public void Parque(Visitante vis) {
        Random random = new Random();
        int randomRest = random.nextInt(3);
        int randomFood = random.nextInt(2);
        boolean snk = true, lch = true;
        boolean continuar = true;
        hora.isActivo();// espero a que se pueda entrar, si esta cerrado espero
        entrarParque(vis.getNombre());

        //
        while (continuar) {
            if (hora.atencionActiva()) {

                int randomActividad = random.nextInt(6);
                //int randomActividad = 4;
                switch (randomActividad) {
                    case 0:

                         shop.irShop(vis.getNombre());

                        break;
                    case 1:
                         snorkel.solicitarEquipo(vis);
                         snorkel.devolverEquipo(vis);
                        break;
                    case 2:

                        if (randomFood == 0) {
                            try {
                                restaurantes.takeLunch(randomRest, vis.getNombre(), lch);
                                lch = false;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                restaurantes.takeSnack(randomRest, vis.getNombre(), snk);
                                snk = false;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case 3:

                         faro.actividadesFaro(vis.getNombre());

                        break;
                    case 4:

                        
                         carrera.actividadCarrera(vis.getNombre());
                         

                        break;
                    default:
                        continuar = false;
                        break;
                }
            } else {
                continuar = false;// cerro
            }

        }
        //

        salirParque(vis.getNombre());
    }

    public boolean estaActivo() {
        return hora.isActivo();//lo que estan aca se quedan detenidos hasta esperar una respuesta
    }

    public void accederHora() {
        // solo el Hilo reloj accede a este.
        this.hora.avanzarHora();
    }

    public int asignarColectivo() {
        int num = 0;
        try {
            aplicar.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (ASIGNAR_COL != TOTAL_COLECTIVOS) {

            switch (ASIGNAR_COL) {
                case 0:
                    num = 0;
                    break;
                case 1:
                    num = 1;
                    break;
                case 2:
                    num = 2;
                    break;

                default:
                    ASIGNAR_COL = 0;
                    break;
            }
            ASIGNAR_COL++;
        }

        aplicar.release();
        return num;
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

    public void accionCole(int id) {
        Random x = new Random();
        int i = x.nextInt(3);
        try {

            switch (i) {
                case 0:
                    colA.subirPasajero(id);
                    colA.bajarPasajero();
                    break;
                case 1:
                    colB.subirPasajero(id);
                    colB.bajarPasajero();

                    break;

                default:
                    colC.subirPasajero(id);
                    colC.bajarPasajero();
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}