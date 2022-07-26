import java.util.ArrayList;

public class p5b {

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

            // args[1] Valor contador
            ClassA classA = new ClassA(Integer.parseInt(args[1]));

            ArrayList<ClassB> listClassB = new ArrayList<>(numeroHilos);
            // Inicializamos el tamaño del contenedor de corredores/hilos
            ArrayList<Thread> listHilos = new ArrayList<>(numeroHilos);
            for (int i = numeroHilos; i > 0; i--) {
                ClassB classB = new ClassB(classA, Tiniciales, Tfinales, i);
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
    String team;
    int counter;
    float ini[];
    float fin[];

    public ClassB(ClassA c, float ini[], float fin[], int i) {
        counter = 0;
        this.classA = c;
        this.ini = ini;
        this.fin = fin;
        this.team = i % 2 == 0 ? "black" : "red";

    }

    public void run() {
        ini[Integer.parseInt(Thread.currentThread().getName().split("-")[1])] = System.nanoTime();
        while (classA.EnterAndPlay(team))
            counter++;
        fin[Integer.parseInt(Thread.currentThread().getName().split("-")[1])] = System.nanoTime();
    }
}

class ClassA {
    int counter;
    String canPlay;

    public ClassA(int counter) {
        this.counter = counter;
        this.canPlay = "red";
    }


    synchronized boolean EnterAndDecrement() {
        System.out.println(Thread.currentThread().getId() + " -> Comienzo EAW");
        if (counter == 0) {

            System.out.println(Thread.currentThread().getId() + " -> Fin EAW FALSE");
            return false;
        }
        System.out.println(Thread.currentThread().getId() + " -> " + counter + " = counter--");
        counter--;
        notify();
        if (counter != 0)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        System.out.println(Thread.currentThread().getId() + " -> Fin EAW TRUE");
        return true;
    }

    private void SwitchTeam() {
        System.out.println("Team " + canPlay + " is playing");
        if (canPlay.equals("red"))
            canPlay = "black";
        else
            canPlay = "red";
    }

    public synchronized boolean EnterAndPlay(
            final String team) {
        try {
            System.out.println("EnterAndPlay entered for " + Thread.currentThread().getId());
            if (team.equals(canPlay)) {
                SwitchTeam();
                // AQUI SIGUE EL CÓDIGO de los partados ANTERIORes
                return EnterAndDecrement();
            }
        } catch (Exception E) {
            System.out.println("unexpected exception...");
        } finally {
            System.out.println("EnterAndPlay ended for " + Thread.currentThread().getId());
        }
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