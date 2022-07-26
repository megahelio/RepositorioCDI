package CDI.ET1;
import java.util.ArrayList;

public class App2 implements Runnable {
    /*
     * Bueno pues la movida es que usando Runnable puedo extender las clases
     * que me dé la gana pero si extiendo thread pues coomo java es así solo puede
     * extender una cosa y se tensa el asunto si quiero que extienda de varias.
     */
    public void run() {
        System.out.println("Inicio: " + Thread.currentThread().getId() + " " + Thread.currentThread().getName());
        try {
            Thread.sleep(100);
            ;
        } catch (InterruptedException exception) {
            System.out.println("caca");
        }
        System.out.println("Fin: " + Thread.currentThread().getId() + " " + Thread.currentThread().getName());
    }

    public static void main(String[] args) throws Exception {
        App2 pepe = new App2();
        int numero_de_hilos = Integer.valueOf(args[0]);
        ArrayList<Thread> listhilos = new ArrayList<Thread>(numero_de_hilos);
        if (numero_de_hilos > 0)
            for (int i = numero_de_hilos; i > 0; i--) {
                Thread thread = new Thread(pepe, args[0]);
                listhilos.add(thread);
                thread.start();
            }

        //System.out.println(listhilos);
        System.out.println("Final main");
    }
}
