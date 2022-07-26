import java.util.LinkedList;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class p2e2LocksTemporizador {

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

    /**
     * 
     * Causa interbloqueos
     * args[0]-> n Consumidores
     * args[1]-> n Productores
     */
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

        System.println("___________________InicioMain__________________");
        if (!comprobarArgs(args)) {
            //System.out.println("java p2e1 nConsumidores nProductores");

        } else {
            int numeroHilos = Integer.parseInt(args[0]) + Integer.parseInt(args[1]);
            float tiempos_iniciales[] = new float[numeroHilos];
            float tiempos_finales[] = new float[numeroHilos];

            Bateria bateria = new Bateria();
            Buffer buffer = new Buffer(10);

            ArrayList<Reader> listReaders = new ArrayList<>(Integer.parseInt(args[0]));// no se usa
            ArrayList<Writer> listWriter = new ArrayList<>(Integer.parseInt(args[1]));// no se usa

            ArrayList<Thread> listTodos = new ArrayList<>(Integer.parseInt(args[0]) + Integer.parseInt(args[1]));

            for (int i = Integer.parseInt(args[0]); i > 0; i--) {
                Reader reader = new Reader(buffer, bateria, numeroHilos, tiempos_iniciales, tiempos_finales);
                listReaders.add(reader);
                listTodos.add(reader);
            }
            for (int i = 0; i < Integer.parseInt(args[1]); i++) {
                Writer writer = new Writer(buffer, bateria, numeroHilos, tiempos_iniciales, tiempos_finales);
                listWriter.add(writer);
                listTodos.add(writer);
            }

            for (Thread a : listTodos)
                a.start();
               
            System.out.println("___________Tiempos iniciales___________");

            for (int i = 0; i < tiempos_iniciales.length; i++);
                System.out.println("Thread-" + i + " --> " + tiempos_iniciales[i]);
                System.out.println(i + " " + tiempos_iniciales[i]);

            System.out.println("___________FIN Tiempos iniciales___________");

            for (Thread hilo : listTodos) {
                try {
                    hilo.join();
                } catch (InterruptedException excepcion) {
                }
            }

            System.out.println("___________ Tiempos finales___________");

            for (int i = 0; i < tiempos_finales.length; i++);
                System.out.println("Thread-" + i + " --> " + tiempos_finales[i]);
                System.out.println(i + " " + tiempos_finales[i]);

                System.out.println("___________FIN Tiempos finales___________");

            float tiempo_total = mayor(tiempos_finales) - menor(tiempos_iniciales);
            System.out.println(numeroHilos + " " + tiempo_total);
        }
        System.out.println("___________________FinMain__________________");
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
    private Buffer buffer;

    private int numeroHilos;
    private float[] tiempos_iniciales;
    private float[] tiempos_finales;

    public Writer(Buffer buffer, Bateria bateria, int numeroHilos, float[] tiempos_iniciales, float[] tiempos_finales) {
        this.buffer = buffer;
        this.bateria = bateria;
        this.numeroHilos = numeroHilos;
        this.tiempos_iniciales = tiempos_iniciales;
        this.tiempos_finales = tiempos_finales;
    }

    public void run() {
        tiempos_iniciales[Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % numeroHilos] = System
                .nanoTime();
        while (bateria.get() > 0) {
            System.out.println(currentThread().getName() + " :::BATERIA::: " + bateria.get());
            System.out.println(" " + bateria.get());
            int c = (int) (Math.random() * 10);
            buffer.recibir(c);

        }
        tiempos_finales[Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % numeroHilos] = System
                .nanoTime();
    }
}

class Reader extends Thread {
    private Bateria bateria;
    private Buffer buffer;

    private int numeroHilos;
    private float[] tiempos_iniciales;
    private float[] tiempos_finales;

    public Reader(Buffer buffer, Bateria bateria, int numeroHilos, float[] tiempos_iniciales, float[] tiempos_finales) {
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
            System.out.println(currentThread().getName() + " :::BATERIA::: " + bateria.get());
            System.out.println(" " + bateria.get());
            buffer.abrir();
        }
        tiempos_finales[Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % numeroHilos] = System
                .nanoTime();
    }
}

class Buffer {
    // como solo instanciamos un buffer el static no es estrictamente necesario
    private static LinkedList<Integer> bandeja;
    private int tamanho;

    final private Lock lock = new ReentrantLock();
    final private Condition noLleno = lock.newCondition();
    final private Condition noVacio = lock.newCondition();

    public Buffer(int tamanho) {
        bandeja = new LinkedList<>();
        this.tamanho = tamanho;

    }

    // AKA put en la API
    public void recibir(int id) {
        lock.lock();
        try {
            while (bandeja.size() == tamanho) {
                System.out.println("BANDEJA LLENA");
                // En lenguaje coloquial "espera a que no esté lleno"
                noLleno.await();
            }
            // Añadimos un email
            bandeja.add(id);
            // En lenguaje coloquial "avisa de que no estás vacio"
            noVacio.signal();

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // AKA take en la API
    public void abrir() {
        lock.lock();
        try {
            while (bandeja.size() == 0) {
                System.out.println("BANDEJA VACÍA");
                // En lenguaje coloquial "espera a que no esté vacio"
                noVacio.await();
            }

bandeja.removeLast();
            // En lenguaje coloquial "avisa de que no estás lleno"
            noLleno.signal();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
