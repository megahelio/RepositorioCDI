package CDI.ET1;

import java.util.ArrayList;
//import java.lang.Math;

public class App3 implements Runnable {
    /*
     * Bueno pues la movida es que usando Runnable puedo extender las clases
     * que me dé la gana pero si extiendo thread pues coomo java es así solo puede
     * extender una cosa y se tensa el asunto si quiero que extienda de varias.
     */
    public void run() {
        System.out.println("Inicio: " + Thread.currentThread().getId() + " " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
            // for (int i = 0; i < 1000000; ++i) {
            // double d =
            // Math.tan(Math.atan(Math.tan(Math.atan(Math.tan(Math.atan(Math.tan(Math.atan(Math.tan(Math.atan(123456789.123456789))))))))));
            // Math.cbrt(d);
            // }

        } catch (InterruptedException exception) {
            System.out.println("Saltó excepcion mientras el hilo dormía");
        }
        System.out.println("Fin: " + Thread.currentThread().getId() + " " + Thread.currentThread().getName());
    }

    public static void main(String[] args) throws Exception {

        String delimitador = "############################################";
        System.out.println(delimitador);
        long startTime = System.nanoTime();
        long endTime;

        App3 pepe = new App3();
        int numero_de_hilos = Integer.valueOf(args[0]);
        ArrayList<Thread> listhilos = new ArrayList<Thread>(numero_de_hilos);
        if (numero_de_hilos > 0)
            for (int i = numero_de_hilos; i > 0; i--) {
                Thread thread = new Thread(pepe);
                listhilos.add(thread);
                thread.start();
                // thread.join();

                while (thread.isAlive()) {
                    try {
                        Thread.sleep(20);
                    } catch (Exception exception) {
                        System.out.println(thread.getId() + "excepcion");
                    }

                }
            }

        // System.out.println(listhilos);
        endTime = System.nanoTime();
        System.out.println("Final main: " + (endTime - startTime) + "ns");
        System.out.println(delimitador);
    }
}
