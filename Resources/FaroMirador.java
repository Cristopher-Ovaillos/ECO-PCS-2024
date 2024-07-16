package Resources;

import java.util.concurrent.Semaphore;

import Vista.Salida;

public class FaroMirador {
    private final int CAPACIDAD_ESCALERA = 5; // Capacidad máxima de la escalera caracol
    private final int CAPACIDAD_TOBOGAN = 1; // Capacidad máxima por tobogán (uno a la vez)
    private Salida sout;
    private int personasEnTobogan1 = 0; // Contador de personas en tobogán 1
    private int personasEnTobogan2 = 0; // Contador de personas en tobogán 2

    private Semaphore semaforoEscalera = new Semaphore(CAPACIDAD_ESCALERA, true);
    private Semaphore hayLugar = new Semaphore(2);
    private Semaphore semaforoTobogan1 = new Semaphore(CAPACIDAD_TOBOGAN);
    private Semaphore semaforoTobogan2 = new Semaphore(CAPACIDAD_TOBOGAN);
    private Semaphore semaforoEleccionTobogan = new Semaphore(1); // Semáforo para la elección del tobogán
    public FaroMirador(Salida sout){
        this.sout=sout;
    }

    public void actividadesFaro(int id) {
        try {
            subirEscalera(id);
            sout.soutFaro("Observa Visitante: " + id);
            elegirTobogan(id);

        } catch (InterruptedException e) {
            e.printStackTrace();
            
        }
    }

    public void subirEscalera(int personaId) throws InterruptedException {
        semaforoEscalera.acquire();
        sout.soutFaro("Persona " + personaId + " sube por la escalera.");
        Thread.sleep(1000); // Simulación de tiempo para subir por la escalera
        semaforoEscalera.release();
    }

    public void elegirTobogan(int personaId) throws InterruptedException {
        boolean espera = false;// para ver si la persona espera o no
        semaforoEleccionTobogan.acquire();
        while (personasEnTobogan1 == 1 && personasEnTobogan2 == 1) {
            sout.soutFaro("No hay espacio en ningun tobogan para la persona " + personaId);
            semaforoEleccionTobogan.release();
            hayLugar.acquire();
            espera = true;

        }
        if (!espera) {
            semaforoEleccionTobogan.release();
            hayLugar.acquire();
        }
        // el sem hayLugar sirve para consultar por cual hay que ir, me sirve para ver
        // si personaEnToboganX es igual a 0 o no
        if (semaforoTobogan1.availablePermits() == 1) {
            // hay lugar
            usarTobogan1(personaId);
        } else {
            // hay lugar en el 2
            usarTobogan2(personaId);
        }
    }

    public void usarTobogan1(int personaId) throws InterruptedException {
        if (personasEnTobogan1 < CAPACIDAD_TOBOGAN) {

            semaforoTobogan1.acquire();
            personasEnTobogan1 = 1;
            sout.soutFaro("Persona " + personaId + " se desliza por tobogan 1.");
            Thread.sleep(500); // Tiempo simulado
            personasEnTobogan1 = 0;
            sout.soutFaro("Persona " + personaId + " llega a la piscina desde tobogan 1.");
            hayLugar.release();// libero porque tengo espacio aca
            semaforoTobogan1.release();
        }
    }

    public void usarTobogan2(int personaId) throws InterruptedException {
        if (personasEnTobogan2 < CAPACIDAD_TOBOGAN) {
            semaforoTobogan2.acquire();

            personasEnTobogan2 = 1;

            sout.soutFaro("Persona " + personaId + " se desliza por tobogan 2.");
            Thread.sleep(500); // Tiempo simulado
            personasEnTobogan2 = 0;
            sout.soutFaro("Persona " + personaId + " llega a la piscina desde tobogan 2.");
            hayLugar.release();// libero porque tengo espacio aca
            semaforoTobogan2.release();
        }
    }
}
