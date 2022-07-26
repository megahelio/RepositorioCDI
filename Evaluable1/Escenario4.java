package CDI.Evaluable1;

import java.util.ArrayList;

public class Escenario4 implements Runnable {

    /*
     * n-> "número de dorsal"/identificador del corredor/hilo
     * tiempos[n][0]-> salida
     * tiempos[n][1]-> finalización
     */
    static public float tiempos[][];

    static public int numeroHilos = 0;

    static private int distanciaRecorrido;

    /**
     * Excepcion para comprobar los argumentos introducidos
     */
    public class InputException extends Exception {
        public InputException(String errorMasaje) {
            super(errorMasaje);
        }
    }

    /**
     * Funcion para comprobar los argumentos introducidos.
     * 
     * @param args
     * @throws InputException
     */
    public static boolean comprobarArgs(String args[]) {
        if (args.length != 2) {
            System.err.println("El número de argumentos debe ser igual a uno");
            return false;
        }
        try {
            if (numeroHilos <= 0 && Integer.parseInt(args[1]) <= 0) {
                System.err.println("Los argumentos deben ser positivos.");
                return false;
            }
        } catch (NumberFormatException exc) {
            System.err.println("Los argumentos deben de ser numéricos");
            return false;
        }
        return true;
    }

    /**
     * El run
     */
    public void run() {
        // Cronometro de salida
        tiempos[Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % numeroHilos][0] = System.nanoTime();

        // Los corredores se dividen en 3 categorias según su velocidad
        if (Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % 3 == 0) {
            for (int i = 0; i < distanciaRecorrido; i += 1) {
                try {
                    Thread.sleep((long) Math.rint(Math.random() * 10));
                } catch (InterruptedException e) {
                    //System.out.println("Interrumpido" + " " + Thread.currentThread().getId());
                    break;
                }
            }
        } else if (Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % 3 == 1) {
            for (int i = 0; i < distanciaRecorrido; i += 1) {
                try {
                    Thread.sleep((long) Math.rint(Math.random() * 20));
                } catch (InterruptedException e) {
                    //System.out.println("Interrumpido" + " " + Thread.currentThread().getId());
                    break;
                }
            }
        } else {
            for (int i = 0; i < distanciaRecorrido; i += 1) {
                try {
                    Thread.sleep((long) Math.rint(Math.random() * 50));
                } catch (InterruptedException e) {
                    //System.out.println("Interrumpido" + " " + Thread.currentThread().getId());
                    break;
                }
            }
        }

        for (int i = 1; i < distanciaRecorrido; i += 1) {
            try {
                //El cast es necesario porque sleep requiere un argumento de tipo long
                Thread.sleep((long) Math.rint(Math.random() * 10));
            } catch (InterruptedException e) {
                //System.out.println("Interrumpido" + " " + Thread.currentThread().getId());

                //En el bucle se está iterando el sleep
                //Si llega la llamada sleep, break rompe el for, y solo el for, simulando las caídas o fin de carrera para algunos
                //Sleep lanzó la interrupción
                break;
            }
        }

        tiempos[Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % numeroHilos][1] = System.nanoTime();
    }

    /**
     * Devuelve el valor más grande de un array.
     * 
     * @param array
     * @return
     */
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

    public static float media(float array[]) {
        float sum = 0;
        for (int i = 0; i < array.length; i++)
            sum += array[i];

        return sum / array.length;

    }

    /**
     * Devuelve los tiempos de ejecución de una secuencia de instrucciones definida.
     * 
     * args[0]-> Número de Hilos.
     * args[1]-> Distacia total del recorrido.
     * 
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        // comprobamos que se introduccieron argumentos de consola válidos
        if (!comprobarArgs(args)) {

            // Si los argumentos del consola no son correctos...
            System.out.print("[numeroHilos][distanciaRecorrido]");

        } else {
            numeroHilos = Integer.parseInt(args[0]);
            int muestrasParaMedia = 4;
            float duracion[] = new float[muestrasParaMedia];
            for (int iteracionMedia = 0; iteracionMedia < muestrasParaMedia; iteracionMedia++) {

                /* Inicializo el tamaño porque si no lo hago escucho a Floro en mi cabeza */
                tiempos = new float[numeroHilos][2];

                // Si son correctos ejecutamos la carrera

                distanciaRecorrido = Integer.parseInt(args[1]);

                Escenario4 pepe = new Escenario4();

                // Inicializamos el tamaño del contenedor de corredores/hilos
                ArrayList<Thread> listhilos = new ArrayList<Thread>(Integer.valueOf(args[0]));

                // Los corredores en la parrilla de salida
                for (int i = Integer.valueOf(args[0]); i > 0; i--) {
                    // Los corredores/hilos se crean
                    Thread thread = new Thread(pepe);
                    // Y se apuntan en la lista de participantes
                    listhilos.add(thread);
                }

                // Pistoletazo de salida
                for (Thread a : listhilos)
                    a.start();

                // aquí habrá que poner cosas que pasen en la carrera para todos
                try {
                    Thread.sleep((long) Math.rint(Math.random() * 500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (Thread hilo : listhilos) {
                    try {
                        //Si no quiero que se caigan todos, le pongo alguna condición
                        //Por ejemplo con nombreHilo%n
                        //Se caen todos
                        hilo.interrupt();

                        //Interrupt puede lanzar una SecurityException y hay que cogerla porque si no Java peta
                    } catch (SecurityException exception) {
                        System.err.println("SecurityException");
                    }
                }

                // al salir del bucle todos los hilos habrán acabado
                for (Thread hilo : listhilos) {
                    try {
                        hilo.join();
                    } catch (InterruptedException excepcion) {
                        System.out.println(hilo.getId() + " InterruptedException :)");
                    }
                }

                // acaba la carrera
                float tiempos_iniciales[] = new float[numeroHilos];
                float tiempos_finales[] = new float[numeroHilos];

                for (int i = 0; i < tiempos.length; i++) {
                    tiempos_iniciales[i] = tiempos[i][0];
                    tiempos_finales[i] = tiempos[i][1];
                }
                duracion[iteracionMedia] = mayor(tiempos_finales) - menor(tiempos_iniciales);

            }

            System.out.print(args[0] + " ");
            System.out.println(media(duracion));

        }

    }
}
