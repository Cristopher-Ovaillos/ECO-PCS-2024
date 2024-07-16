package Resources;

import java.util.concurrent.Semaphore;

import Vista.Salida;

public class SeccionComida {
    private static final int NUM_RESTAURANTS = 3;
    private static final int RESTAURANT_CAPACITY = 10;
    private static Semaphore[] restaurantSemaphores = new Semaphore[NUM_RESTAURANTS];
    private Salida sout;

    public SeccionComida(Salida sout) {
        for (int i = 0; i < NUM_RESTAURANTS; i++) {
            restaurantSemaphores[i] = new Semaphore(RESTAURANT_CAPACITY, true);
        }
        this.sout=sout;
    }

    public void takeLunch(int restaurantId, int personId,boolean comprado) throws InterruptedException {
       sout.soutRestaurante("Persona " + personId + " come almuerzo en Restaurante " + restaurantId+" "+(comprado ? "comprado" : "gratis"));
        restaurantSemaphores[restaurantId].acquire();
        sout.soutRestaurante("Persona " + personId + " está almorzando en Restaurante " + restaurantId);
        
        Thread.sleep(1000); // Simula el tiempo de almuerzo
        restaurantSemaphores[restaurantId].release();
    }

    public void takeSnack(int restaurantId, int personId,boolean comprado) throws InterruptedException {
        sout.soutRestaurante("Persona " + personId + " quiere merienda en Restaurante " + restaurantId+" "+(comprado ? "comprado" : "gratis"));
        restaurantSemaphores[restaurantId].acquire();
        sout.soutRestaurante("Persona " + personId + " está tomando merienda en Restaurante " + restaurantId);
        
        Thread.sleep(500); // Simula el tiempo de merienda
        restaurantSemaphores[restaurantId].release();
    }
}
