package CDI.ET1;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.lang.Math;

public class App4 implements Runnable {
    /*
     * Bueno pues la movida es que usando Runnable puedo extender las clases
     * que me dé la gana pero si extiendo thread pues coomo java es así solo puede
     * extender una cosa y se tensa el asunto si quiero que extienda de varias.
     */

    static float startTime;
    static float crono;
    static DecimalFormat formato;

    /**
     * 
     */
    public void run() {
        crono=System.nanoTime();
        System.out.println("Trabaja: " + /* Thread.currentThread().getId() + */" " + Thread.currentThread().getName() + " "+formato.format((crono - startTime) / 1000000000) + "s");
        // try {
        // Thread.sleep(1000);

        // } catch (InterruptedException exception) {
        // System.out.println("Saltó excepcion mientras el hilo dormía");
        // }
        for (int i = 0; i < 1000000; ++i) {
            double d = Math.tan(Math.atan(Math.tan(
                    Math.atan(Math.tan(Math.atan(Math.tan(Math.atan(Math.tan(Math.atan(123456789.123456789))))))))));
            Math.cbrt(d);
        }
    }

    /**
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        formato = new DecimalFormat("####.#######################");
        // Delimitadores para consola
        String delimitador = "############################################";
        System.out.println(delimitador);

        /* Variables para control de tiempo */
        startTime = System.nanoTime();
        float endTime;

        App4 pepe = new App4();
        int numero_de_hilos = Integer.valueOf(args[0]);
        ArrayList<Thread> listhilos = new ArrayList<Thread>(numero_de_hilos);

        if (numero_de_hilos > 0)
            for (int i = numero_de_hilos; i > 0; i--) {

                Thread thread = new Thread(pepe);
                crono = System.nanoTime();
                System.out.println("Nace: " /* + thread.getId() */ + " " + thread.getName() + " "
                        + (formato.format((crono - startTime) / 1000000000) + "s"));
                listhilos.add(thread);
                thread.start();

                /*
                 * try {
                 * hilo.join();
                 * } catch (InterruptedException excepcion) {
                 * System.out.println(hilo.getId() + " InterruptedException :)");
                 * }
                 * 
                 * /*
                 * while (thread.isAlive()) {
                 * try {
                 * Thread.sleep(20);
                 * } catch (Exception exception) {
                 * System.out.println(thread.getId() + "excepcion");
                 * }
                 * 
                 * 
                 * }
                 */
            }

        for (Thread hilo : listhilos) {
            try {
                hilo.join();
                crono = System.nanoTime();
                System.out.println("Muere: " + /* hilo.getId() + */ " " + hilo.getName()
                        + " " + (formato.format((crono - startTime) / 1000000000) + "s"));
            } catch (InterruptedException excepcion) {
                System.out.println(hilo.getId() + " InterruptedException :)");
            }
        }

        // System.out.println(listhilos);
        endTime = System.nanoTime();

        System.out.println("Final main: " + formato.format((endTime - startTime) / 1000000000) + "s");
        System.out.println(delimitador);
    }
}
