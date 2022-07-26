package CDI.ET3;

import java.util.ArrayList;

public class App6 implements Runnable {

    //
    // private static int mySum;
    static private int num;

    /**
     * Excepcion para comprobar los argumentos introducidos
     */
    public class InputException extends Exception {
        public InputException(String errorMasaje) {
            super(errorMasaje);
        }
    }

    /**
     * Funcion para comprobar los argumentos introducidos.
     * 
     * @param args
     * @throws InputException
     */
    public static boolean comprobarArgs(String args[]) {
        if (args.length != 2) {
            System.err.println("El número de argumentos debe ser igual a dos");
            return false;
        }
        try {
            if (Integer.parseInt(args[0]) <= 0 && Integer.parseInt(args[1]) <= 0) {
                System.err.println("Los argumentos deben ser positivos.");
                return false;
            }
        } catch (NumberFormatException exc) {
            System.err.println("Los argumentos deben ser numéricos.");
            return false;
        }
        return true;
    }

    private static final ThreadLocal<Integer> mySum = ThreadLocal.withInitial(() -> 0);
    // private static final ThreadLocal<Integer> mySum = new ThreadLocal<Integer>()
    // {
    // @Override
    // protected Integer initialValue() {
    // return 0;
    // }
    // };

    /**
     * El run
     */
    public void run() {
        System.out.println("started" + " " + Thread.currentThread().getId() + " " + mySum.get());

        for (int i = 0; i != num; i++) {

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mySum.set(mySum.get() + 1);
        }
        System.out.println("finished" + " " + Thread.currentThread().getId() + " " + mySum.get());
    }

    /**
     * Devuelve el valora más grande de un array.
     * 
     * @param array
     * @return
     */
    public static float mayor(float array[]) {
        float mayor;
        mayor = array[0];
        boolean seguir = true;
        int i = 1;
        while (seguir) {
            try {
                if (array[i] >= mayor)
                    mayor = array[i];
                i++;
            } catch (IndexOutOfBoundsException exception) {
                seguir = false;
            }
        }
        return mayor;
    }

    

    /**
     * Devuelve los tiempos de ejecución de una secuencia de instrucciones definida.
     * 
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        if (comprobarArgs(args)) {

            num = Integer.parseInt(args[1]);
            App6 pepe = new App6();
            int numero_de_hilos = Integer.valueOf(args[0]);
            ArrayList<Thread> listhilos = new ArrayList<Thread>(numero_de_hilos);

            if (numero_de_hilos > 0)
                for (int i = numero_de_hilos; i > 0; i--) {

                    Thread thread = new Thread(pepe);
                    listhilos.add(thread);
                    thread.start();

                }

            for (Thread hilo : listhilos) {
                try {
                    hilo.join();

                } catch (InterruptedException excepcion) {
                    System.out.println(hilo.getId() + " InterruptedException :)");
                }
            }
            System.out.println(mySum.get());
        } else {
            System.out.println("[numeroHilos][iteracionesBucle]");
        }

    }
}
