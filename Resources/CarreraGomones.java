package Resources;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Vista.Salida;

// Gomon Class
class Gomon {
    private final int id;
    private final String tipo;

    public Gomon(int id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }
}

// GomonIndividual Class
class GomonIndividual extends Gomon {
    public static final String TIPO = "INDIVIDUAL";

    public GomonIndividual(int id) {
        super(id, TIPO);
    }
}

// GomonDoble Class
class GomonDoble extends Gomon {
    public static final String TIPO = "DOBLE";

    public GomonDoble(int id) {
        super(id, TIPO);
    }
}

// Carrera Class
public class CarreraGomones {
    // VARIABLES
    private int BICICLETAS_DISPONIBLES = 6;
    private int ASIENTOS_DISPONIBLES = 15;
    private int TOTAL_TREN = 0;
    private int BOLSOS = 0;
    private int START = 4;
    private int gomonesListos;
    private int numGomonesDobles = 2; // Define el número de gomones dobles
    private int numGomonesIndividuales = 5; // Define el número de gomones individuales
    // LOCK
    private ReentrantLock medioTransporte = new ReentrantLock();
    private ReentrantLock bolso = new ReentrantLock();
    private ReentrantLock carrera = new ReentrantLock();
    private Condition esperarMedio;
    private Condition llegadaTren;
    private Condition personasBajaronTren;
    private Condition minPersonasAvanzar;
    // GOMONES
    private Condition buscarGomones, esperarListos;
    private final BlockingQueue<Gomon> gomonesDisponibles;
    // VARIABLES
    private Boolean trenDisponible;
    private Boolean inicioTren;
    private Boolean COMIENCEN;
    private String ganador;
    private Salida sout;

    public CarreraGomones(Salida SOUT) {
        // condicion para esperar que haya bicis o tren disponible
        esperarMedio = medioTransporte.newCondition();
        // para avisar a los visitantes que llego el tren
        llegadaTren = medioTransporte.newCondition();
        // puede avanzar y volver porque ya se bajaron todos
        personasBajaronTren = medioTransporte.newCondition();
        // minimo de personas que necesita el tren para avanzar
        minPersonasAvanzar = medioTransporte.newCondition();
        //condiciones para gomones
        buscarGomones = carrera.newCondition();
        esperarListos = carrera.newCondition();
        //extras
        trenDisponible = true; // el tren de primeras si estara disponible
        sout = SOUT;
        inicioTren = false;
        gomonesListos = 0;
        COMIENCEN = false;
        ganador = "";
        gomonesDisponibles = new LinkedBlockingQueue<>();

        for (int i = 0; i < numGomonesIndividuales; i++) {
            gomonesDisponibles.add(new GomonIndividual(i + 1));
        }

        for (int i = 0; i < numGomonesDobles; i++) {
            gomonesDisponibles.add(new GomonDoble(i + 1));
        }
    }

    // METODOS VISITANTES
    public void actividadCarrera(int id) {
        buscarMedio(id);
        int bolso = tomarBolso(id);
        descenderRio(id);
        sout.soutCarreraBolso("Vis " + id + " DEVOLVIO EL BOLSO " + bolso);
        devolverBolso(id);
    }
    //metodos gomones
    public Gomon elegirGomon() throws InterruptedException {
        return gomonesDisponibles.take();
    }

    public void devolverGomon(Gomon gomon) {
        gomonesDisponibles.add(gomon);
    }

    public void buscarGom() {
        try {
            buscarGomones.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void esperarCarrera() {
        try {
            esperarListos.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void descenderRio(int id) {
        try {
            carrera.lock();
            while (gomonesListos >= START) {
                buscarGom();
            }
            Gomon elegido = elegirGomon();
            sout.soutCarreraStart("Visitante " + id + " toma el gomon " + elegido.getId() + " (" + elegido.getTipo() + ")");
            gomonesListos++;

            if (gomonesListos == START) {
                COMIENCEN = true;
                sout.soutCarreraStart("ARRANCO LA CARRERA");
                esperarListos.signalAll();
            } else {
                while (!COMIENCEN) {
                    esperarCarrera();
                }
            }
            carrera.unlock();

            temp(); // simulo carrera

            carrera.lock();
            
            if (gomonesListos == 4) {
                ganador = "Ganador: Visitante " + id;
                Thread.sleep(100);
                sout.soutCarreraStart(ganador);
            } else {
                sout.soutCarreraStart("           Visitante " + id + " llegó al final");
            }
            gomonesListos--;

            if (gomonesListos == 0) {
                //reinicio
                COMIENCEN = false;
                ganador = "";
            }
            devolverGomon(elegido);
            carrera.unlock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //metodos de bolso
    public int tomarBolso(int id) {
        int retornar;
        bolso.lock();
        retornar = BOLSOS;
        BOLSOS++;
        sout.soutCarreraBolso("Visitante " + id + " tomó el bolso " + id);
        bolso.unlock();
        return retornar;
    }

    public void devolverBolso(int id) {
        bolso.lock();
        BOLSOS--;
        bolso.unlock();
    }
    //metodos para visistanes
    public void buscarMedio(int id) {
        medioTransporte.lock();
        String medio = subirseAMedio();
        while (medio.equals("")) {
            sout.soutCarrera("Visitante " + id + " espera transporte");
            try {
                esperarMedio.await();
                medio = subirseAMedio();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sout.soutCarrera("Visitante " + id + " subió " + medio);
        medioTransporte.unlock();
        temp();
        medioTransporte.lock();
        if (medio.equals("TREN")) {

            
            if (!inicioTren) {
                try {
                    llegadaTren.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            sout.soutCarrera("Visitante " + id + " baja del tren.");
            TOTAL_TREN--;
            if (TOTAL_TREN == 0) {
                personasBajaronTren.signal(); 
            }
        } else {
            sout.soutCarrera("Visitante " + id + " devolvió bicicleta.");
            BICICLETAS_DISPONIBLES++;
            esperarMedio.signal();
        }
        medioTransporte.unlock();
    }

    private String subirseAMedio() {
        String retornar = "";
        if (BICICLETAS_DISPONIBLES != 0) {
            retornar = "BICICLETA " + BICICLETAS_DISPONIBLES;
            BICICLETAS_DISPONIBLES--;
        } else {
            //ENTRA AL IF SI HA LIGAR
            if (trenDisponible && ASIENTOS_DISPONIBLES > TOTAL_TREN) {
                TOTAL_TREN++;
                retornar = "TREN";
                if (TOTAL_TREN == 1) { 
                    //aviso que hay una para avanzar
                    minPersonasAvanzar.signal();
                }
            }
        }
        return retornar;
    }

    // METODOS TREN
    public void accionesTren() {
        try {
            medioTransporte.lock();
            while (TOTAL_TREN == 0) { 
                minPersonasAvanzar.await();
            }
            medioTransporte.unlock();
            Thread.sleep(610);//6 MINUTOS ESPERA
            esperarSubida();
            // Simulate train travel
            Thread.sleep(1000);
            //espero que se bajen todos, y la ultima persona en bajar avisa que puede avanzar
            esperarBajada();
            //ya bajaron todos, entonces el tren esta disponible para su uso
            avisoDisponibleTren();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void esperarSubida() {
        medioTransporte.lock();
        //el tren ya avanzo
        inicioTren = true;
        //por lo tanto no es disponible para el uso el tren
        trenDisponible = false;
        sout.soutCarreraTren("TREN: Saliendo con " + TOTAL_TREN+"/"+ASIENTOS_DISPONIBLES);
        medioTransporte.unlock();
    }

    public void esperarBajada() {
        medioTransporte.lock();
        //aviso que llego el tren
        llegadaTren.signalAll();
        //espero
        try {
            personasBajaronTren.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        inicioTren = false;
        medioTransporte.unlock();
    }

    public void avisoDisponibleTren() {
        medioTransporte.lock();
        trenDisponible = true;
        sout.soutCarreraTren("TREN: Disponible");
        esperarMedio.signalAll();
        medioTransporte.unlock();
    
    }
    // SIMULADOR TIEMPO
    public void temp() {
        Random rnd = new Random();
        int random = (int) (rnd.nextDouble() * 3 + 1);
        try {
            Thread.sleep(random * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
