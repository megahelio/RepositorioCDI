import java.util.ArrayList;

public class Escenario1 implements Runnable {

    /*
     * n-> "número de dorsal"/identificador del corredor/hilo
     * tiempos[n][0]-> salida
     * tiempos[n][1]-> finalización
     */
    static public float[][] tiempos;

    //Número de corredores. Se pide por parámetro y es args[0]
    static public int numeroHilos = 0; 

    /*Variable global utilizada para pasar la distancia al run().
     Debería ser con el constructor pero lo hicimos así para ahorrar dolores de cabeza
     Segundo argumento pedido por parámetro
     Se utiliza en el run() para controlar cuantas veces duermen los hilos (num iteraciones)
     args[1]
     */
    static private int distanciaRecorrido;

    /**
     * Excepcion para comprobar los argumentos introducidos, para cualquier problema con el formato de los argumentos
     * Al final no la usamos, sobra
     */
    public class InputException extends Exception {
        public InputException(String errorMasaje) {
            super(errorMasaje);
        }
    }

    /**
     * Función para comprobar los argumentos introducidos.
     * 
     * @param args
     * @throws InputException
     */
    public static boolean comprobarArgs(String args[]) {

        //Comprobamos que el número de argumentos sea igual a 2, corredores y distancia
        if (args.length != 2) {
            System.err.println("El número de argumentos debe ser igual a dos");
            return false;
        }
        /* Integer.parseInt() genera NumberFormarException cuando se introducen valores que no se pueden parsear.
         Controlamos que los argumentos siempre sean positivos y numéricos
        */
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
     * El run()
     */
    public void run() {

        //Cronometro de salida
        //Guarda tiempo con nanoTime();
        //Colle o número n do Thread-n
        //Nos aseguramos de que cada uno se guarde en una posición diferente para el cálculo de la media
        //Nos permite reciclar el array de tiempos, en caso contrario habría que hacer varios para cada iteración para la media u otras opciones
        tiempos[Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % numeroHilos][0] = System.nanoTime();

        // Corredor n empieza la carrera:
        // System.out.println("Started" + " " +
        // Integer.parseInt(Thread.currentThread().getName().split("-")[1]));
        System.out.println("Started" + " " + Thread.currentThread().getName().split("-")[1] + " " + "");

        //Los corredores se dividen en 3 categorias según su velocidad
        //Para simular distintas velocidades entre los corredores
        //No entra en las excepciones porque no se interrumpen los hilos desde el main()
        if (Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % 3 == 0) {
            for (int i = 0; i < distanciaRecorrido; i += 1) {
                try {
                    Thread.sleep((long) Math.rint(Math.random() * 10));
                } catch (InterruptedException e) {
                    System.out.println("Interrumpido" + " " + Thread.currentThread().getId() + " " + "");
                    break;
                }
            }
        } else if (Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % 3 == 1) {
            for (int i = 0; i < distanciaRecorrido; i += 1) {
                try {
                    Thread.sleep((long) Math.rint(Math.random() * 20));
                } catch (InterruptedException e) {
                    System.out.println("Interrumpido" + " " + Thread.currentThread().getId() + " " + "");
                    break;
                }
            }
        } else {
            for (int i = 0; i < distanciaRecorrido; i += 1) {
                try {
                    Thread.sleep((long) Math.rint(Math.random() * 50));
                } catch (InterruptedException e) {
                    System.out.println("Interrumpido" + " " + Thread.currentThread().getId() + " " + "");
                    break;
                }
            }
        }

        for (int i = 1; i < distanciaRecorrido; i += 1) {
            try {
                Thread.sleep((long) Math.rint(Math.random() * 10));
            } catch (InterruptedException e) {
                System.out.println("Interrumpido" + " " + Thread.currentThread().getId() + " " + "");
                break;
            }
        }

        // Corredor n acaba la carrera:
        // System.out.println("Finished" + " " + Thread.currentThread().getId() + " " +
        // "");
        System.out.println("Finished" + " " + Thread.currentThread().getName().split("-")[1] + " " + "");
        // Cronometro finalizacion
        tiempos[Integer.parseInt(Thread.currentThread().getName().split("-")[1]) % numeroHilos][1] = System.nanoTime();
    }

    /**
     * Devuelve el valora más grande de un array.
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
     * Sobra, no se usa
     * @throws InterruptedException
     */

    public static void main(String[] args) throws InterruptedException {

        //Comprobamos que se introduccieron argumentos de consola válidos
        if (!comprobarArgs(args)) {

            // Si los argumentos del consola no son correctos...
            System.out.print("[numeroHilos][distanciaRecorrido]");

        } else {
            //Esto no hace falta pero lo hemos hecho así para no tener que cambiarlo en todas las ocurrencias del código
            numeroHilos = Integer.parseInt(args[0]);

            //Inicializamos la variable muestrasParaMedia que define el tamaño del array de duraciones de la carrera
            int muestrasParaMedia = 4;
            /* Inicializo el tamaño porque si no lo hago escucho a Floro en mi cabeza */
            tiempos = new float[Integer.parseInt(args[0])][3];

            /*Array de cada una de las iteraciones para hacer la media para un número único de hilos siendo estos los mismos.
            Son los que se pasan por args[0]
            */
            float duracion[] = new float[muestrasParaMedia];

            /*Bucle iteraciones
            Dentro de este bucle se lleva a cabo la carrera
            Una única carrera se repite 4 veces
            Se hace la media de estas 4 iteraciones
            */
            for (int iteracionMedia = 0; iteracionMedia < muestrasParaMedia; iteracionMedia++) {

                /* Inicializo el tamaño porque si no lo hago escucho a Floro en mi cabeza
                Es el cronometro
                Tiene dos dimensiones porque hay dos tiempos que controlar
                Tiempo de salida para cada corredor
                Tiempo de finalización para cada corredor
                */
                tiempos = new float[numeroHilos][2];

                /* Ocurre lo mismo que con numeroHilos
                Inicializamos la distancia de recorrido para no tener que hacerlo en todas las ocurrencias Integer.parseInt()
                Se debería hacer fuera del bucle, porque aquí se está instanciando 4 veces cuando en realidad hace falta solo una
                */
                distanciaRecorrido = Integer.parseInt(args[1]);

                /* Instanciamos la clase Escenario1()
                Se hace para poder generar los hilos/corredores
                */
                Escenario1 pepe = new Escenario1();

                /* Inicializamos el tamaño del contenedor de corredores/hilos
                Utilizamos ArrayList como estructura de datos para contener al número de corredores args[0]
                */
                ArrayList<Thread> listhilos = new ArrayList<Thread>(numeroHilos);
            // pasa el tiempo
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

                /*Los corredores en la parrilla de salida
                Se inicializan los hilos
                */
                for (int i = numeroHilos; i > 0; i--) {
                    /*Los corredores/hilos se crean
                    Porque no utilizamos extends Thread, utilizamos implements Runnable
                    */
                    Thread thread = new Thread(pepe);
                    //Y se apuntan en la lista de participantes
                    listhilos.add(thread);
                }

                /*Pistoletazo de salida
                Se inicializa cada thread
                Aquí se llama al run() para cada uno
                */

                for (Thread a : listhilos)
                    a.start();

                /* Para simular retraso entre los distintos corredores
                Aquí habrá que poner cosas que pasen en la carrera para todos
                sleep()
                */

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                
                //Simula las caídas, pero en este escenario no se caen
                // for (Thread hilo : listhilos) {
                // try {
                // hilo.interrupt();
                // } catch (SecurityException exception) {
                // System.err.println("SecurityException");

                // }
                // }

                /* Aquí hay que esperar a que acaben todos para mostrar los resultados
                Con join() se utiliza para salir de la parte concurrente y entrar en la secuencial otra vez
                Espera a que todos acaben
                */
                for (Thread hilo : listhilos) {
                    try {
                        hilo.join();

                    //Puede lanzar la interrupción pero como no se interrumpen, nunca se va a dar ese caso.
                    //En escenario4 podría pasar
                    } catch (InterruptedException excepcion) {
                        System.out.println(hilo.getId() + " InterruptedException :)");
                    }
                }

                //Finaliza la carrera
                float tiempos_iniciales[] = new float[numeroHilos];
                float tiempos_finales[] = new float[numeroHilos];

                for (int i = 0; i < tiempos.length; i++) {
                    //Nos dimos cuenta de que podríamos reutilizar estos arrays en vez de los arrays de tiempos para inicio y fin
                    tiempos_iniciales[i] = tiempos[i][0];
                    tiempos_finales[i] = tiempos[i][1];
                }
                duracion[iteracionMedia] = mayor(tiempos_finales) - menor(tiempos_iniciales);

            }

            System.out.print(args[0] + " ");
            System.out.println(media(duracion));
            for (Thread hilo : listhilos) {
                System.out.print(Integer.parseInt(hilo.getName().split("-")[1])+" -> ");
                tiempos[Integer.parseInt(hilo.getName().split("-")[1])][2] = tiempos[Integer
                        .parseInt(hilo.getName().split("-")[1])][1]
                        - tiempos[Integer.parseInt(hilo.getName().split("-")[1])][0];
                System.out.println(tiempos[Integer.parseInt(hilo.getName().split("-")[1])][2]);
            }

        }

    }
}
