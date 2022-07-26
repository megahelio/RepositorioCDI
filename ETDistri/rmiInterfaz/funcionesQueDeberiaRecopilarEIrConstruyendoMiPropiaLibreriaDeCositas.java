package rmiInterfaz;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class funcionesQueDeberiaRecopilarEIrConstruyendoMiPropiaLibreriaDeCositas {
    // Function to validate the IPs address.
    private static boolean isValidIPAddress(String ip) {

        String zeroTo255 = "(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])";

        String regex = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;

        Pattern p = Pattern.compile(regex);

        if (ip == null) {
            return false;
        }

        Matcher m = p.matcher(ip);

        return m.matches();
    }

    public static float mayorArray(float array[]) {
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

    public static float menorArray(float array[]) {
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
     * Proporciona un numero entero pseudoaleatorio de tantos digitos como se pida
     * 
     * @param numeroDeDigitos decimal entero
     * @return (int) (Math.random() * (10 ^ numeroDeDigitos))
     */
    public static int randomEntero(int numeroDeDigitos) {
        return (int) (Math.random() * (10 ^ numeroDeDigitos));
    }

    /**
     * Ejemplo excepción
     */
    public class InputException extends Exception {
        public InputException(String errorMasaje) {
            super(errorMasaje);
        }
    }

    /**
     * 
     * @param args
     * @return
     */
    public static boolean validarArgumentos(String[] args) {
        boolean toret = true;
        if (args.length < 3) {
            System.out.println("Debe introduccir 3 argumentos");
            toret = false;
        }

        // Direccion IP
        if (!isValidIPAddress(args[0]) && !args[0].equals("localhost")) {
            System.out.println("La direccion IP no es valida: " + args[0]);
            toret = false;
        }

        // Puerto
        try {
            int puerto = Integer.parseInt(args[1]);
            if (puerto < 0 || puerto > 65536) {
                System.out.println("El puerto no es un número valido: " + args[1]);
                toret = false;
            }
        } catch (NumberFormatException e) {
            System.out.println("El puerto no es un argumento numerico: " + args[1]);
            toret = false;
        }

        // Nombre servidor
        if (args[2].contains(" ")) {
            System.out.println("El nombre de servidor tiene espacios: " + args[2]);
            toret = false;
        }

        return toret;
    }
}