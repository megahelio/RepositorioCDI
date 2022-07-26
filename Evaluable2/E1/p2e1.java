import java.util.LinkedList;
import java.util.ArrayList;

public class p2e1 {

    /**
     * 
     * Causa interbloqueos, falta temporizar y probar 160-161
     * args[0]-> n Consumidores
     * args[1]-> n Productores
     */
    public static boolean comprobarArgs(String args[]) {

        /* 
        * Función para comprobar los argumentos
        * Para introducir el numero de consumidores y de productores que tendrá el problema
        * Se comprueba que el numero de argumentos introducidos es el correcto
        * También  se mira que sean enteros positivos
        */
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
        /** 
        * Se comprueba que los argumentos introducidos son los correctos
        */
        System.out.println("___________________InicioMain__________________");
        if (!comprobarArgs(args)) {
            System.out.println("java p2e1" + args[0] + "nProductores" + args[1]);

        } else {
            /* En caso de ser correctos, se crea el buffer que actuará como bandeja de entrada de correos
            * La batería también
            */
            Bateria bateria = new Bateria();
            Buffer buffer = new Buffer(10);

            ArrayList<Reader> listReaders = new ArrayList<>(Integer.parseInt(args[0]));// no se usa
            ArrayList<Writer> listWriter = new ArrayList<>(Integer.parseInt(args[1]));// no se usa

            /* Esta lista se utiliza para almacenar a todos los hilos
            * Hilos productores
            * Hilos consumidores
            */
            ArrayList<Thread> listTodos = new ArrayList<>(Integer.parseInt(args[0]) + Integer.parseInt(args[1]));

            /*
            *Se van creando los procesos consumidores, en este caso, los Readers, leen correos.
            */
            for (int i = Integer.parseInt(args[0]); i > 0; i--) {
                Reader reader = new Reader(buffer, bateria);
                listReaders.add(reader);
                listTodos.add(reader);
            }

            /* 
            * Se van creando los procesos productores, en este caso, los Writers, escriben correos.
            */
            for (int i = 0; i < Integer.parseInt(args[1]); i++) {
                Writer writer = new Writer(buffer, bateria);
                listWriter.add(writer);
                listTodos.add(writer);
            }

            /* 
            * Se inicializa cada proceso/hilo
            */

            for (Thread a : listTodos)
                a.start();

            /* 
            * Para asegurarnos de que se ejecutan en el orden particular que buscamos.
            */
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
            int c = (int) (Math.random() * 10);
            buffer.recibir(c);

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

    private LinkedList<Integer> bandeja;
    private int tamanho;

    private boolean estaLlena;
    private boolean estaVacia;

    public Buffer(int tamanho) {
        bandeja = new LinkedList<>();
        estaLlena = false;
        estaVacia = true;
        this.tamanho = tamanho;

    }

    public synchronized void abrir() {

        while (estaVacia) {
            System.out.println("Bandeja Vacía");
            // Que pasa si pongo esto aqui:
            //notifyAll();
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        if (bandeja.size() == 0) {
            estaVacia = true;
        }

        System.out.println("Abrimos el email " + bandeja.removeLast() + " de la bandeja");
        estaLlena = false;
        notifyAll();
    }

    public synchronized void recibir(int c) {

        while (estaLlena) {
            System.out.println("Bandeja Llena");
            // Que pasa si pongo esto aqui:
            //notifyAll();
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        bandeja.add(c);
        System.out.println("Enviamos el email " + c + " a la bandeja");
        estaVacia = false;
        if (bandeja.size() == tamanho) {
            estaLlena = true;
        }
        notifyAll();
    }
}