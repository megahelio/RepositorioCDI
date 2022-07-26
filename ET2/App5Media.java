package CDI.ET2;

import java.util.ArrayList;
import java.lang.Math;

public class App5Media implements Runnable {

    static float tiempos[][];

    public void run() {
        tiempos[Integer.parseInt(Thread.currentThread().getName().split("-")[1])][0] = System.nanoTime();
        for (int i = 0; i < 1000000; ++i) {
            double d = Math.tan(Math.atan(Math.tan(
                    Math.atan(Math.tan(Math.atan(Math.tan(Math.atan(Math.tan(Math.atan(123456789.123456789))))))))));
            Math.cbrt(d);
        }
        tiempos[Integer.parseInt(Thread.currentThread().getName().split("-")[1])][1] = System.nanoTime();
    }

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
     * 
     */
    public static void main(String[] args) throws InterruptedException {

        /* Inicializo el tamaño porque si no lo hago escucho a Floro en mi cabeza */
        tiempos = new float[Integer.parseInt(args[0])][2];

        App5 pepe = new App5();
        int numero_de_hilos = Integer.valueOf(args[0]);
        int numeroDeMediasTotales = Integer.valueOf(args[1]);

        ArrayList<Thread> listhilos = new ArrayList<Thread>(numero_de_hilos);
        float tiemposFinales[] = new float[Integer.parseInt(args[0])];

        for (int numeroDeMediasActual = 0; numeroDeMediasActual < numeroDeMediasTotales; numeroDeMediasActual++) {

            numero_de_hilos = Integer.valueOf(args[0]);

            if (numero_de_hilos > 0)
                for (int i = numero_de_hilos; i > 0; i--) {

                    Thread thread = new Thread(pepe);
                    tiempos[Integer.parseInt(thread.getName().split("-")[1]) % numeroDeMediasTotales][0] = System
                            .nanoTime();
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
            tiemposFinales = new float[Integer.parseInt(args[0])];
            while (seguir) {
                try {
                    tiemposFinales[i] = tiempos[i][1];
                    i++;
                } catch (IndexOutOfBoundsException exception) {
                    seguir = false;
                }
            }
        }

        // mensajes Finalessssssss s sss s sssssssssssssssssssss
        System.out.println(args[0] + " " + (mayor(tiemposFinales) - tiempos[0][0]));
    }
}
