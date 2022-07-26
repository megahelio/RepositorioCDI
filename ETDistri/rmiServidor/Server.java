package rmiServidor;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.channels.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import rmiInterfaz.Wordle;
import rmiInterfaz.funcionesQueDeberiaRecopilarEIrConstruyendoMiPropiaLibreriaDeCositas;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Server implements Wordle {

    static ArrayList<String> palabrasAlmacenadas;

    public Server() {
        super();
    }

    /**
     * x -> No contiene esta letra
     * - -> Contiene esta letra pero en otra posicion
     * o -> La letra y la posicion es correcta
     * 
     * @param palabraRespuesta
     *                         la palabra que suministra el usuario como intento
     * @param palabraPregunta
     *                         la palabra problema que es oculta al usuario
     * @return
     *         combinacion de 5 caracteres [x-o] en funcion de la respuesta
     */

    public String comprobarIntento(String palabraRespuesta, String palabraPregunta) throws RemoteException {
        // inicialmente son todo errores
        String[] estado = { "x", "x", "x", "x", "x" };
        // para cada letra de la palabra respuesta
        for (int i = 0; i < 5; i++) {
            // miramos si la letra existe en la palabra objetivo
            System.out.println(palabraPregunta + " contiene " + Character.toString(palabraRespuesta.charAt(i)) + "? "
                    + palabraPregunta.contains(Character.toString(palabraRespuesta.charAt(i))));

            if (palabraPregunta.contains(Character.toString(palabraRespuesta.charAt(i)))) {
                estado[i] = "-";
            }
            // miramos si la letra está en la posicion correcta
            System.out.println(Character.toString(palabraPregunta.charAt(i))
                    + " == " + Character.toString(palabraRespuesta.charAt(i)) + " "
                    + (palabraPregunta.charAt(i) == palabraRespuesta.charAt(i)));
            if (palabraPregunta.charAt(i) == palabraRespuesta.charAt(i)) {

                estado[i] = "o";
            }
        }
        // convertimos en String el resultado
        String toret = "";
        for (int i = 0; i < 5; i++) {
            System.out.println(estado[i]);
            toret = toret.concat(estado[i]);
        }
        System.out.println(toret);
        return toret;
    }

    public ArrayList<String> getDiccionario() throws RemoteException {
        return palabrasAlmacenadas;
    }

    /**
     * Toma las palabras usadas, hace la diferencia con las posibles palabras a
     * jugar y envia una que no se usó si es posible @return String palabra a jugar
     * 
     * 
     * En caso contrario @return null
     */
    public String getNewPalabraPropuesta(ArrayList<String> palabrasUsadas) throws RemoteException {
        String toret = "";

        ArrayList<String> posiblesPropuestas = palabrasAlmacenadas;

        for (int i = 0; i < palabrasUsadas.size(); i++) {
            posiblesPropuestas.remove(palabrasUsadas.get(i));
        }
        if (posiblesPropuestas.size() > 0) {
            Random random = new Random();
            toret = posiblesPropuestas.get(random.nextInt(posiblesPropuestas.size()));
            return toret;
        }
        return null;

    }

    /**
     * 
     * @param args
     *             [0]Direccion IP [localhost o xxx.xxx.xxx.xxx donde xxx no sea
     *             superior a 255]
     *             [1]Puerto [numérico entre 0 y 65536]
     *             [2]Nombre del Servidor [String sin espacios]
     * @throws java.rmi.AlreadyBoundException
     */
    public static void main(String[] args) throws java.rmi.AlreadyBoundException {

        if (funcionesQueDeberiaRecopilarEIrConstruyendoMiPropiaLibreriaDeCositas.validarArgumentos(args)) {
            palabrasAlmacenadas = new ArrayList<>();
            File doc = new File(

                    "rmiServidor/palabras5Letras.txt");// ruta relativa desde el direcctorio actual

            try (Scanner obj = new Scanner(doc)) {
                while (obj.hasNextLine())
                    palabrasAlmacenadas.add(obj.nextLine());
            } catch (FileNotFoundException e) {
                System.out.println("ERROR: No se encontró en fichero de palabras en la ruta especificada: "
                        + doc.getAbsolutePath());
            }

            Server obj = new Server();

            try {

                Wordle stub = (Wordle) UnicastRemoteObject.exportObject(obj, 0);
                // 0; puerto por default, solo
                // debemos
                // exportar la parte del codigo que esta
                // marcada para uso remoto

                Registry reg = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
                // Se puede usar sin
                // agrumentos.
                reg.bind(args[2], stub);
                // Se usa rebind en muchos ejemplos, aunque podemos pisar métodos.

                System.out.println("Server up and running!");
                long startTime = System.currentTimeMillis();
                while (true) {
                    // 1 min = 60000 ms
                    if (System.currentTimeMillis() - startTime == 300000) {
                        System.out.println("Tiempo máximo alcanzado, el servidor se apagará.");
                        UnicastRemoteObject.unexportObject(obj, true);
                    }
                }
            } catch (AlreadyBoundException e) {

                System.out.println("Name already bound to another object!!" + e);
            } catch (RemoteException e) {

                System.out.println("Host not rechable//communication faillure!! " + e);

            }
        }

    }

}
