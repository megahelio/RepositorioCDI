import java.util.ArrayList;

public class noSynVerbose {

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

    public static float menor(float array[]) {
        float menor;
        menor = array[0];
        boolean seguir = true;
        int i = 1;
        while (seguir) {
            try {
                if (array[i] <= menor)
                    menor = array[i];
                i++;
            } catch (IndexOutOfBoundsException exception) {
                seguir = false;
            }
        }
        return menor;
    }

    public static boolean comprobarArgs(String args[]) {
        if (args.length != 1) {
            System.err.println("El numero de argumentos debe ser igual a uno");
            return false;
        }
        try {
            if (Integer.parseInt(args[0]) <= 0) {
                System.err.println("Los argumentos deben ser positivos.");
                return false;
            }
        } catch (NumberFormatException exc) {
            System.err.println("Los argumentos deben de ser numericos");
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("________Empieza Main_____________");
        // comprobamos que se introduccieron argumentos de consola válidos
        if (!comprobarArgs(args)) {

            // Si los argumentos del consola no son correctos...
            System.out.print("[numeroHilos]");

        } else {
            float Tiniciales[] = new float[Integer.parseInt(args[0])];
            float Tfinales[] = new float[Integer.parseInt(args[0])];
            int numeroHilos = Integer.parseInt(args[0]);
            Counter contador = new Counter();
            MyTask tarea = new MyTask(contador, Tiniciales, Tfinales);

            // Inicializamos el tamaño del contenedor de corredores/hilos
            ArrayList<Thread> listhilos = new ArrayList<>(numeroHilos);

            for (int i = numeroHilos; i > 0; i--) {
                Thread thread = new Thread(tarea);
                listhilos.add(thread);
            }
            for (Thread a : listhilos)
                a.start();

            for (Thread hilo : listhilos) {
                try {
                    hilo.join();
                } catch (InterruptedException excepcion) {
                    System.out.println(hilo.getId() + " InterruptedException :)");
                }
            }

            float Ttotal = mayor(Tfinales) - menor(Tiniciales);
            System.out.println(numeroHilos + " " + Ttotal);

        }
        System.out.println("________Acaba Main_____________");
    }
}

class MyTask implements Runnable {

    Counter counter;
    float ini[];
    float fin[];

    public MyTask(Counter c, float ini[], float fin[]) {
        this.counter = c;
        this.ini = ini;
        this.fin = fin;

    }

    public void run() {
        ini[Integer.parseInt(Thread.currentThread().getName().split("-")[1])] = System.nanoTime();
        System.out.println(Thread.currentThread().getName() + " " + "empieza");
        try {
            Thread.sleep((long) Math.rint(Math.random() * 100));
        } catch (InterruptedException e) {
            System.out.println("Excepcion sleep");
        }
        System.out.println(Thread.currentThread().getName() + ": " + counter.get() + "++ => " + counter.increment());
        System.out.println(Thread.currentThread().getName() + " " + "acaba");
        fin[Integer.parseInt(Thread.currentThread().getName().split("-")[1])] = System.nanoTime();
    }
}

class Counter {

    /* AtomicInteger */int contador;

    public Counter() {
        contador = 0;
    }

    public int increment() {
        return ++contador;
    }

    public int get() {
        return contador;
    }

}