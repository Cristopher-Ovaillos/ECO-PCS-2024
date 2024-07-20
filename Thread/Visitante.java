package Thread;


import Resources.ECOPCS;

public class Visitante implements Runnable {
    private final int id;
    private final String tipoAcceso; // "Particular" o "Tour"s
    private ECOPCS parque;

    public Visitante(int id, String tipoAcceso, ECOPCS parque) {
        this.id = id;
        this.tipoAcceso = tipoAcceso;
        this.parque = parque;
    }

    @Override
    public void run() {
            if (!tipoAcceso.equals("Particular")) {
                parque.accionCole(id);
             
            }
            
            parque.Parque(this);
    }

    public void temp() {
        try {
            Thread.sleep((long) (Math.random() * 5000)); // Simulación de actividad
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }

    public int getNombre(){
        return id;
    }
}
