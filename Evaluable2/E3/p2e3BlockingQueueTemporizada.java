import java.util.LinkedList;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class p2e3BlockingQueueTemporizada {

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

        if (args.length != 2) {
            System.err.println("El número de argumentos debe ser igual a dos");
            return false;
        }

        try {
            if (Integer.parseInt(args[0]) <= 0) {
                System.err.println("El numero de consumidores debe ser positivo");
                return false;
            }
        } catch (NumberFormatException exc) {
            System.err.println("El numero de consumidores debe ser un argumento numerico");
            return false;
        }
        try {
            if (Integer.parseInt(args[1]) <= 0) {
                System.err.println("El numero de productores debe ser positivo.");
                return false;
            }
        } catch (NumberFormatException exc) {
            System.err.println("El numero de productores debe ser un argumento numerico");
            return false;
        }
        return true;
    }

    /**
     * args[0]-> n Consumidores
     * args[1]-> n Productores
     */
    public static void main(String[] args) {
        if (!comprobarArgs(args)) {
            System.out.println("java p2e1 nConsumidores nProductores");

        } else {
            int numeroHilos = Integer.parseInt(args[0]) + Integer.parseInt(args[1]);
            float tiempos_iniciales[] = new float[numeroHilos];
            float tiempos_finales[] = new float[numeroHilos];

            Bateria bateria = new Bateria();
            BlockingQueue<Integer> buffer = new ArrayBlockingQueue<Integer>(10);

            ArrayList<Thread> listTodos = new ArrayList<>(Integer.parseInt(args[0]) + Integer.parseInt(args[1]));

            for (int i = Integer.parseInt(args[0]); i > 0; i--) {
                Reader reader = new Reader(buffer, bateria, numeroHilos, tiempos_iniciales, tiempos_finales);
                listTodos.add(reader);
            }
            for (int i = 0; i < Integer.parseInt(args[1]); i++) {
                Writer writer = new Writer(buffer, bateria, numeroHilos, tiempos_iniciales, tiempos_finales);
                listTodos.add(writer);
            }

            for (Thread a : listTodos)
                a.start();

            for (Thread hilo : listTodos) {
                try {
                    hilo.join();
                } catch (InterruptedException excepcion) {
                }
            }
            float tiempo_total = mayor(tiempos_finales) - menor(tiempos_iniciales);
            System.out.println(numeroHilos + " " + tiempo_total);
        }
    }

}

class Bateria {
    int bateria;

    public Bateria() {
        bateria = 100;
    }

    public synchronized int gastaBateria() {
        if (bateria > 0)
            bateria = bateria - 1;
        return bateria;
    }

    public int get() {
        return bateria;
    }
}

class Writer extends Thread {
    Bateria bateria;
    private BlockingQueue<Integer> buffer;

    private int numeroHilos;
    private float[] tiempos_iniciales;
    private float[] tiempos_finales;

    public Writer(BlockingQueue<Integer> buffer, Bateria bateria, int numeroHilos, float[] tiempos_iniciales,
            float[] tiempos_finales) {
        this.buffer = buffer;
        this.bateria = bateria;

        this.numeroHilos = numeroHilos;
        this.tiempos_iniciales = tiempos_iniciales;
        this.tiempos_finales = tiempos_finales;
    }

    public void run() {
        tiempos_iniciales[Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % numeroHilos] = System
                .nanoTime();
        while (bateria.gastaBateria() > 0) {
            int id = (int) (Math.random() * 10);
            try {
                buffer.add(id);
            } catch (IllegalStateException e) {
            }

        }
        tiempos_finales[Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % numeroHilos] = System
                .nanoTime();
    }
}

class Reader extends Thread {
    private Bateria bateria;
    private BlockingQueue<Integer> buffer;

    private int numeroHilos;
    private float[] tiempos_iniciales;
    private float[] tiempos_finales;

    public Reader(BlockingQueue<Integer> buffer, Bateria bateria, int numeroHilos, float[] tiempos_iniciales,
            float[] tiempos_finales) {
        this.buffer = buffer;
        this.bateria = bateria;

        this.numeroHilos = numeroHilos;
        this.tiempos_iniciales = tiempos_iniciales;
        this.tiempos_finales = tiempos_finales;
    }

    public void run() {
        tiempos_iniciales[Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % numeroHilos] = System
                .nanoTime();
        while (bateria.gastaBateria() > 0) {
            try {
                buffer.take();
            } catch (InterruptedException ex) {
            }
        }
        tiempos_finales[Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % numeroHilos] = System
                .nanoTime();
    }
}
