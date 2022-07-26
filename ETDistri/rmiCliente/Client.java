package rmiCliente;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

import rmiInterfaz.Wordle;
import rmiInterfaz.funcionesQueDeberiaRecopilarEIrConstruyendoMiPropiaLibreriaDeCositas;

/**
 * [0]Direccion IP [localhost o xxx.xxx.xxx.xxx donde xxx no sea
 * superior a 255]
 * [1]Puerto [numérico entre 0 y 65536]
 * [2]Nombre del Servidor [String sin espacios]
 */
public class Client {

    static ArrayList<String> palabrasAlmacenadas;

    public static boolean validarPalabra(String a) {
        String palabra = a.toLowerCase();
        boolean palabraValida = true;
        if (palabra.length() != 5) {
            System.out.println("Longuitud distinta de 5");
            palabraValida = false;
        }
        // en caso de que queramos jugar sin tildes, como en el archivo hay palabras con
        // tildes y en las pruebas el código funcionó correctamente con palabras con
        // tilde, permito jugar con tildes
        /*
         * String caracteresProhibidos[] = { " ", "á", "é", "í", "ó", "ú", "#", "@" };
         * for (int i = 0; i < caracteresProhibidos.length; i++) {
         * if (palabra.contains(caracteresProhibidos[i]))
         * System.out.println("Contiene  , á, é, í, ó, ú, #, @ ");
         * palabraValida = false;
         * }
         */

        // System.out.println("Verificacion diccionario: ");

        /*
         * for (int i = 0; i < palabrasAlmacenadas.size() && palabraValida == false;
         * i++) {
         * // System.out.println(palabrasAlmacenadas.get(i) + "<->" + palabra);
         * palabraValida = (palabrasAlmacenadas.get(i).equals(palabra));
         * }
         */

        if (!palabrasAlmacenadas.contains(palabra)) {
            palabraValida = false;
        }

        // System.out.println("Fin Verificacion diccionario");

        return palabraValida;

    }

    /**
     * * [0]Direccion IP [localhost o xxx.xxx.xxx.xxx donde xxx no sea
     * superior a 255]
     * [1]Puerto [numérico entre 0 y 65536]
     * [2]Nombre del Servidor [String sin espacios]
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        if (funcionesQueDeberiaRecopilarEIrConstruyendoMiPropiaLibreriaDeCositas.validarArgumentos(args))

            try {
                // Iniciamos conexion
                Registry reg = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));

                Wordle stub = (Wordle) reg.lookup(args[2]);

                int numeroIntentos = 0;
                long startTime;
                Scanner entrada = new Scanner(System.in);
                String palabraObjetivo, palabraRespuestaActual, progresoActual = "xxxxx";
                ArrayList<String> palabrasRespuestas = new ArrayList<>();
                ArrayList<String> progresos = new ArrayList<>();
                palabrasAlmacenadas = stub.getDiccionario();
                ArrayList<String> palabrasUsadas = new ArrayList<>();

                boolean seguir = true;
                // Primera palabra
                palabraObjetivo = stub.getNewPalabraPropuesta(palabrasUsadas);
                palabrasUsadas.add(palabraObjetivo);

                // Iniciamos interaccion
                System.out.println("Bienvenido al PUTO WORDLE DE HACENDADO: ");
                startTime = System.currentTimeMillis();

                System.out.println(
                        "x -> No contiene esta letra\n- -> Contiene esta letra pero en otra posicion\no -> La letra y la posicion es correcta");
                System.out.println(progresoActual);

                System.out.println("Inserte una palabra para comenzar: ");
                do {

                    do {
                        // 1min = 60000ms
                        
                        System.out.println("Tiempo: " + (System.currentTimeMillis() - startTime) + "ms -> " + (System
                                .currentTimeMillis() - startTime) / 60000 + "min /// Al 1:30 min CAMBIARÁ LA PALABRA!");
                        if (System.currentTimeMillis() - startTime >= 90000 || numeroIntentos >= 5
                                || progresoActual.equals("ooooo")) {
                            palabraObjetivo = stub.getNewPalabraPropuesta(palabrasUsadas);
                            if (palabraObjetivo == null) {
                                throw new NullPointerException();
                            }
                            palabrasUsadas.add(palabraObjetivo);
                            numeroIntentos = 0;
                            progresos.clear();
                            palabrasRespuestas.clear();
                            startTime = System.currentTimeMillis();
                        }
                        //System.out.println("PalabraObjetivo: " + palabraObjetivo);

                        palabraRespuestaActual = entrada.nextLine();

                    } while (!validarPalabra(palabraRespuestaActual) || System.currentTimeMillis()
                            - startTime >= 90000);
                    // salimos del bucle con una palabra valida para contabilizar como intento

                    progresoActual = stub.comprobarIntento(palabraRespuestaActual, palabraObjetivo);
                    System.out.println(progresoActual);
                    progresos.add(progresoActual);
                    palabrasRespuestas.add(palabraRespuestaActual);
                    for (int i = 0; i <= numeroIntentos; i++) {
                        System.out.println(progresos.get(i) + " <-> " + palabrasRespuestas.get(i));
                    }
                    System.out
                            .println("Incrementado el número de intentos de: " + numeroIntentos++ + " a "
                                    + numeroIntentos);
                    if (progresoActual.equals("ooooo")) {
                        System.out.println("La palabra era: " + palabraObjetivo);
                        System.out.print("Palabra acertada. Nueva palabra? S/n : ");
                        String foo = entrada.nextLine();
                        if (foo.charAt(0) != 'S' && foo.charAt(0) != 's') {
                            seguir = false;
                        }
                    }
                    if (numeroIntentos >= 5) {
                        System.out.println("La palabra era: " + palabraObjetivo);
                        System.out.print("Máximo de intentos alcanzado. Nueva palabra? S/n : ");
                        String foo = entrada.nextLine();
                        if (foo.charAt(0) != 'S' && foo.charAt(0) != 's') {
                            seguir = false;
                        }
                    }
                } while (seguir);

            } catch (RemoteException e) {

                System.out.println("Host not rechable!!" + e);

            } catch (NotBoundException e) {

                System.out.println("Name not bound to any object!! " + e);
            } catch (NullPointerException e) {

                System.out.println("Archivo de palabras finalizado, el programa finalizará.");
            }

    }
}
