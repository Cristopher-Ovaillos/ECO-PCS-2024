package Resources;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Vista.Salida;

public class SouvenirShop {
    private final int NUM_CHECKOUTS = 2; // Numero de cajas disponibles
    private final Lock shopLock = new ReentrantLock();
    private final Condition[] checkoutConditions = new Condition[NUM_CHECKOUTS];
    private boolean[] checkoutAvailable = { true, true }; // Inicialmente ambas cajas están disponibles
    private Salida sout;

    public SouvenirShop(Salida sout) {
        for (int i = 0; i < NUM_CHECKOUTS; i++) {
            checkoutConditions[i] = shopLock.newCondition();
        }
        this.sout = sout;
    }

    public void irShop(int id) {

        Random random = new Random();
        int rdm = random.nextInt(2);
        try {
            purchaseItem(rdm, id);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

    }

    public void purchaseItem(int checkoutId, int personId) throws InterruptedException {
        shopLock.lock();

        while (!checkoutAvailable[checkoutId]) {
            checkoutConditions[checkoutId].await(); // Esperar si la caja no esta disponible
        }
        checkoutAvailable[checkoutId] = false; // Marcar la caja como no disponible
        sout.soutShop("Persona " + personId + " está pagando  en Caja " + checkoutId);

        Thread.sleep(1000); // Simular el tiempo de pago
        sout.soutShop("Persona " + personId + " ha pagado por en Caja " + checkoutId);
        checkoutAvailable[checkoutId] = true; // Marcar la caja como disponible nuevamente
        checkoutConditions[checkoutId].signal(); // Despertar a un hilo esperando en esta condicion

        shopLock.unlock();// sale porque se canso de esperar
    }

    public void cerrado() {
        // notifica a todos
        shopLock.lock();
        checkoutAvailable[0]=true;
        checkoutAvailable[1]=true;
        shopLock.unlock();
        checkoutConditions[0].signalAll();
        checkoutConditions[1].signalAll();
    }
}
