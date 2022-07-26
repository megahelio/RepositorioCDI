package CDI.ET3;

import java.util.ArrayList;


public class App7 implements Runnable {

    //
    // private static int pi;
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

    // private static final ThreadLocal<Double> pi = ThreadLocal.withInitial(() ->
    // 0.0);
    private static final ThreadLocal<Double> pi = new ThreadLocal<Double>() {
        @Override
        protected Double initialValue() {
            return 0.0;
        }
    };
    private static final ThreadLocal<Boolean> negative = new ThreadLocal<Boolean>() {

        @Override
        protected Boolean initialValue() {
            return false;
        }

    };

    /**
     * El run
     */
    public void run() {
        System.out.println("started" + " " + Thread.currentThread().getId() + " " + pi.get());

        for (int i = 1; i < num; i += 2) {
            try {
                Thread.sleep((long)Math.rint(Math.random() * 10));
            } catch (InterruptedException e) {
                System.out.println("interrumpido" + " " + Thread.currentThread().getId() + " " + pi.get());
                break;
            }
            if (negative.get())
                pi.set(pi.get() - 1.0 / i);
            else
                pi.set(pi.get() + 1.0 / i);

            negative.set(!negative.get());

        }
        pi.set(pi.get() * 4.0);
        System.out.println("finished" + " " + Thread.currentThread().getId() + " " + pi.get());
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
            App7 pepe = new App7();
            int numero_de_hilos = Integer.valueOf(args[0]);
            ArrayList<Thread> listhilos = new ArrayList<Thread>(numero_de_hilos);

            if (numero_de_hilos > 0)
                for (int i = numero_de_hilos; i > 0; i--) {

                    Thread thread = new Thread(pepe);
                    listhilos.add(thread);
                    thread.start();

                }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Thread hilo : listhilos) {
                try {
                    hilo.interrupt();
                } catch (SecurityException exception) {
                    System.err.println("SecurityException");

                }
            }
            for (Thread hilo : listhilos) {
                try {
                    hilo.join();

                } catch (InterruptedException excepcion) {
                    System.out.println(hilo.getId() + " InterruptedException :)");
                }
            }
            System.out.println(pi.get());
        } else {
            System.out.println("[numeroHilos][iteracionesBucle]");
        }

    }
}
