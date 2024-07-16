package Vista;

import java.util.concurrent.Semaphore;

public class Salida {
    
    private final String esc = "\033[H\033[2J"; // Limpia la pantalla 
  

    private final String HORA = "\033[0;1H"; 
    private final String COLECTIVOS = "\033[3;1H"; 
    private final String SHOP = "\033[4;1H"; 
    private final String SNORKEL = "\033[5;1H"; 
    private final String RESTAURANTE = "\033[6;1H"; 
    private final String FARO = "\033[7;1H"; 
    private final String CARRERA = "\033[8;1H"; 

    private Semaphore semaforoPersonas = new Semaphore(1);
    private Semaphore semaforoHora = new Semaphore(1);
    private Semaphore semaforoShop = new Semaphore(1);
    private Semaphore semaforoSnorkel = new Semaphore(1);
    private Semaphore semaforoRestaurante = new Semaphore(1);
    private Semaphore semaforoFaro = new Semaphore(1);
    private Semaphore semaforoCarrera = new Semaphore(1);

    public Salida() {
        System.out.print(esc); // Limpia la pantalla en la creación del objeto
    }

 
    public void soutColectivo(String str)  {
        try {
            semaforoPersonas.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.print(COLECTIVOS + "\033[KCOLECTIVO    " + str); // Limpia la línea antes de imprimir
        semaforoPersonas.release();
    }

    public void soutHora(String str)  {
        try {
            semaforoHora.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.print(HORA + "\033[K" + str); // Limpia la línea antes de imprimir
        semaforoHora.release();
    }

    public void soutShop(String str)  {
        try {
            semaforoShop.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.print(SHOP + "\033[KSHOP    " + str); // Limpia la línea antes de imprimir
        semaforoShop.release();
    }

    public void soutSnorkel(String str){
        try {
            semaforoSnorkel.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.print(SNORKEL + "\033[KSNORKEL  " + str); // Limpia la línea antes de imprimir
        semaforoSnorkel.release();
    }

    public void soutRestaurante(String str)  {
        try {
            semaforoRestaurante.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.print(RESTAURANTE + "\033[KRESTAURANTE  " + str); // Limpia la línea antes de imprimir
        semaforoRestaurante.release();
    }

    public void soutFaro(String str) {
        try {
            semaforoFaro.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.print(FARO + "\033[KFARO    " + str); // Limpia la línea antes de imprimir
        semaforoFaro.release();
    }

    public void soutCarrera(String str) {
        try {
            semaforoCarrera.acquire();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.print(CARRERA + "\033[KGOMONES  " + str); // Limpia la línea antes de imprimir
        semaforoCarrera.release();
    }
}
