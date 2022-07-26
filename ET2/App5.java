package CDI.ET2;

import java.util.ArrayList;

import java.lang.Math;

public class App5 implements Runnable {

    static float tiempos[][];

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
        if (args.length != 1) {
            System.err.println("El número de argumentos debe ser igual a uno");
            return false;
        }
        try {
            if (Integer.parseInt(args[0]) <= 0) {
                System.err.println("El argumento debe ser positivo.");
                return false;
            }
        } catch (NumberFormatException exc) {
            System.err.println("El argumento debe ser numérico.");
            return false;
        }
        return true;
    }

    /**
     * El run
     */
    public void run() {
        tiempos[Integer.parseInt(Thread.currentThread().getName().split("-")[1])][0] = System.nanoTime();
        for (int i = 0; i < 1000000; ++i) {
            double d = Math.tan(Math.atan(Math.tan(
                    Math.atan(Math.tan(Math.atan(Math.tan(Math.atan(Math.tan(Math.atan(123456789.123456789))))))))));
            Math.cbrt(d);
        }
        tiempos[Integer.parseInt(Thread.currentThread().getName().split("-")[1])][1] = System.nanoTime();
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
                if (array[i] > mayor)
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

            /* Inicializo el tamaño porque si no lo hago escucho a Floro en mi cabeza */
            tiempos = new float[Integer.parseInt(args[0])][2];

            App5 pepe = new App5();
            int numero_de_hilos = Integer.valueOf(args[0]);
            ArrayList<Thread> listhilos = new ArrayList<Thread>(numero_de_hilos);

            if (numero_de_hilos > 0)
                for (int i = numero_de_hilos; i > 0; i--) {

                    Thread thread = new Thread(pepe);
                    tiempos[Integer.parseInt(thread.getName().split("-")[1])][0] = System.nanoTime();
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

            boolean seguir = true;
            int i = 0;
            float tiemposFinales[] = new float[Integer.parseInt(args[0])];
            while (seguir) {
                try {
                    tiemposFinales[i] = tiempos[i][1];
                    i++;
                } catch (IndexOutOfBoundsException exception) {
                    seguir = false;
                }
            }

            // mensajes Finalessssssssssssssssssssssssssssssssss
            System.out.println(args[0] + " " + (mayor(tiemposFinales) - tiempos[0][0]));
        }
    }
}
