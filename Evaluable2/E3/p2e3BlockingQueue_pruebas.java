import java.util.LinkedList;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class p2e3BlockingQueue_pruebas {


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
        //System.out.println("___________________InicioMain__________________");
        if (!comprobarArgs(args)) {
            //System.out.println("java p2e1 nConsumidores nProductores");

        } else {
            Bateria bateria = new Bateria();
            BlockingQueue<Integer> buffer = new ArrayBlockingQueue<Integer>(10);

            ArrayList<Thread> listTodos = new ArrayList<>(Integer.parseInt(args[0]) + Integer.parseInt(args[1]));

            for (int i = Integer.parseInt(args[0]); i > 0; i--) {
                Reader reader = new Reader(buffer, bateria);
                listTodos.add(reader);
            }
            for (int i = 0; i < Integer.parseInt(args[1]); i++) {
                Writer writer = new Writer(buffer, bateria);
                listTodos.add(writer);
            }

            for (Thread a : listTodos)
                a.start();

            for (Thread hilo : listTodos) {
                try {
                    hilo.join();
                } catch (InterruptedException excepcion) {
                    System.out.println(hilo.getId() + " InterruptedException :)");
                }
            }
        }
        //System.out.println("___________________FinMain__________________");
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

    public Writer(BlockingQueue<Integer> buffer, Bateria bateria) {
        this.buffer = buffer;
        this.bateria = bateria;
    }

    public void run() {
        while (bateria.gastaBateria() > 0) {
            System.out.println(currentThread().getName() + " :::BATERIA::: " + bateria.get());
            int id = (int) (Math.random() * 10);
            try {
                if (buffer.add(id)) {
                    System.out.println("Enviamos el email " + id + " a la bandeja");
                }
            } catch (IllegalStateException e) {
                System.out.println(currentThread().getName() + " encontró la bandeja llena.");
            }

        }
    }
}

class Reader extends Thread {
    private Bateria bateria;
    private BlockingQueue<Integer> buffer;

    public Reader(BlockingQueue<Integer> buffer, Bateria bateria) {
        this.buffer = buffer;
        this.bateria = bateria;
    }

    public void run() {

        while (bateria.gastaBateria() > 0) {
            System.out.println(currentThread().getName() + " :::BATERIA::: " + bateria.get());
            try {
                System.out.println("Abrimos el email " + buffer.take() + " de la bandeja");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}