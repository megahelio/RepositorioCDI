import java.util.LinkedList;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class p2e2Locks {

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
        System.out.println("___________________InicioMain__________________");
        if (!comprobarArgs(args)) {
            System.out.println("java p2e1 nConsumidores nProductores");

        } else {
            Bateria bateria = new Bateria();
            Buffer buffer = new Buffer(10);

            ArrayList<Reader> listReaders = new ArrayList<>(Integer.parseInt(args[0]));// no se usa
            ArrayList<Writer> listWriter = new ArrayList<>(Integer.parseInt(args[1]));// no se usa

            ArrayList<Thread> listTodos = new ArrayList<>(Integer.parseInt(args[0]) + Integer.parseInt(args[1]));

            for (int i = Integer.parseInt(args[0]); i > 0; i--) {
                Reader reader = new Reader(buffer, bateria);
                listReaders.add(reader);
                listTodos.add(reader);
            }
            for (int i = 0; i < Integer.parseInt(args[1]); i++) {
                Writer writer = new Writer(buffer, bateria);
                listWriter.add(writer);
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

    public Writer(Buffer buffer, Bateria bateria) {
        this.buffer = buffer;
        this.bateria = bateria;
    }

    public void run() {
        while (bateria.gastaBateria() > 0) {
            System.out.println(currentThread().getName() + " :::BATERIA::: " + bateria.get());
            int id = (int) (Math.random() * 10);
            buffer.recibir(id);

        }
    }
}

class Reader extends Thread {
    private Bateria bateria;
    private Buffer buffer;

    public Reader(Buffer buffer, Bateria bateria) {
        this.buffer = buffer;
        this.bateria = bateria;
    }

    public void run() {

        while (bateria.gastaBateria() > 0) {
            System.out.println(currentThread().getName() + " :::BATERIA::: " + bateria.get());
            buffer.abrir();
        }
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

    // AKA put en la API de java
    public void recibir(int id) {
        lock.lock();
        try {
            while (bandeja.size() == tamanho) {
                System.out.println("Bandeja Llena");
                // En lenguaje coloquial "espera a que no esté lleno"
                noLleno.await();
            }
            // Añadimos un email
            bandeja.add(id);
            System.out.println("Enviamos el email " + id + " a la bandeja");
            // En lenguaje coloquial "avisa de que no estás vacio"
            noVacio.signal();

        } catch(InterruptedException ex){
            ex.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    // AKA take en la API de java
    public void abrir() {
        lock.lock();
        try {
            while (bandeja.size() == 0) {
                System.out.println("Bandeja Vacía");
                // En lenguaje coloquial "espera a que no esté vacio"
                noVacio.await();
            }

            System.out.println("Abrimos el email " + bandeja.removeLast() + " de la bandeja");
            // En lenguaje coloquial "avisa de que no estás lleno"
            noLleno.signal();
        }catch(InterruptedException ex){
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
