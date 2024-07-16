package Resources;

public class Visitante implements Runnable {
    private int id;
    private CarreraGomones carrera;

    public Visitante(int id, CarreraGomones carrera) {
        this.id = id;
        this.carrera = carrera;
    }

    @Override
    public void run() {
        try {
            carrera.realizarActividadGomones(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
