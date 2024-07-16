package Resources;

import Vista.Salida;

public class Hora {
    private int hora;
    private int minuto;
    private int dia;
    private boolean activo;
    private int CIERRE = 11;
    private int APERTURA = 9;
    private int HORA_INICIO = 8;
    private Salida sout;

    public Hora(Salida sout) {
        this.hora = HORA_INICIO; // Comienza a las 09:00
        this.minuto = 30;
        this.dia = 1; // Comienza en el día 1
        this.activo = false; // El parque comienza activo
        this.sout = sout;
    }

    public synchronized void avanzarHora() {
        minuto++;
        if (minuto == 60) {
            minuto = 0;
            hora++;
            if (hora == CIERRE) {
                cerrarParque();
            }
            if (hora == APERTURA) {
                abrirParque();
            }

            if (hora == 24) {
                hora = 0;
                dia++;
            }
        }

        if (activo) {
            notifyAll();//si esta activo notifico todavia
        }
        sout.soutHora("ESTADO " + (activo ? "ABIERTO" : "CERRADO") + "    Día " + dia + " - Hora: "
                + String.format("%02d:%02d\n", hora, minuto));
    }

    public synchronized boolean atencionActiva() {
        return activo;
    }

    public void cerrarParque() {
        activo = false;

    }

    public void abrirParque() {
        activo = true;
       // notifyAll(); // Asegura que todos los visitantes sean notificados cuando el parque abre
    }

    public synchronized boolean isActivo() {
        while (!activo) {
            try {
                wait(); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return activo; 
    }
    

}
