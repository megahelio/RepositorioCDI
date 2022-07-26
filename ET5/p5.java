import java.util.ArrayList;

public class p5 {

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
            System.err.println("El numero de argumentos debe ser igual a dos");
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
            System.out.println("[numeroHilos][contador]");

        } else {
            int numeroHilos = Integer.parseInt(args[0]);
            float Tiniciales[] = new float[numeroHilos];
            float Tfinales[] = new float[numeroHilos];

            ClassA classA = new ClassA();

            ArrayList<ClassB> listClassB = new ArrayList<>(numeroHilos);
            // Inicializamos el tamaño del contenedor de corredores/hilos
            ArrayList<Thread> listHilos = new ArrayList<>(numeroHilos);
            for (int i = numeroHilos; i > 0; i--) {
                ClassB classB = new ClassB(classA, Tiniciales, Tfinales);
                listClassB.add(classB);
            }
            for (int i = 0; i < numeroHilos; i++) {
                Thread thread = new Thread(listClassB.get(i));
                listHilos.add(thread);
            }
            for (Thread a : listHilos)
                a.start();

            for (Thread hilo : listHilos) {
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

class ClassB implements Runnable {

    ClassA classA;
    float ini[];
    float fin[];

    public ClassB(ClassA c, float ini[], float fin[]) {
        this.classA = c;
        this.ini = ini;
        this.fin = fin;
    }

    public void run() {
        ini[Integer.parseInt(Thread.currentThread().getName().split("-")[1])] = System.nanoTime();
        classA.EnterAndWait();
        fin[Integer.parseInt(Thread.currentThread().getName().split("-")[1])] = System.nanoTime();
    }
}

class ClassA {
    int counter;

    public ClassA(int counter) {
        this.counter = counter;
    }

    void EnterAndWait() {
        System.out.println("Comienzo EAW");
        try {
            Thread.currentThread().sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Final EAW");
    }

    synchronized boolean EnterAndDecrement() {
        System.out.println("Inicio EAD");
        if (counter == 0)
            return false;
        counter--;
        System.out.println("Fin EAD");
        return true;
    }
}

class Counter {

    /* AtomicInteger */int contador;

    public Counter() {
        contador = 0;
    }

    public synchronized int increment() {
        return ++contador;
    }

    public int get() {
        return contador;
    }

}