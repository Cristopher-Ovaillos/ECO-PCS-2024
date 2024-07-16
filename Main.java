public class Main {
    public static void main(String[] args) throws InterruptedException {
        String esc = "\033[H\033[2J"; // Limpia la pantalla

        // Crear los hilos
        Thread hilo1 = new Thread(new Tarea(1, esc));
        Thread hilo2 = new Thread(new Tarea(2, esc));

        // Iniciar los hilos
        hilo1.start();
        hilo2.start();

        // Esperar a que los hilos terminen (opcional)
        hilo1.join();
        hilo2.join();
    }
}

class Tarea implements Runnable {
    private int linea;
    private String esc;

    public Tarea(int linea, String esc) {
        this.linea = linea;
        this.esc = esc;
    }

    @Override
    public void run() {
        int dia = 1; // Ejemplo de valor de día
        int hora = 10; // Ejemplo de valor de hora
        int minuto = 30; // Ejemplo de valor de minuto

        // Mover el cursor a la posición deseada
        String posicion = "\033[" + linea + ";1H"; // Mueve el cursor a la línea especificada

        // Imprimir el mensaje en la posición especificada
        System.out.print(esc); // Limpia la pantalla una vez
        while (true) {
            System.out.print(posicion + "Día " + dia + " - Hora: " + String.format("%02d:%02d\n", hora, minuto));
            try {
                Thread.sleep(1000); // Espera 1 segundo antes de actualizar
                hora++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
