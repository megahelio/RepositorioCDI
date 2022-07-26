package rmiInterfaz;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Wordle extends Remote {

    public String comprobarIntento(String palabraRespuesta, String palabraPregunta) throws RemoteException;

    public ArrayList<String> getDiccionario() throws RemoteException;

    public String getNewPalabraPropuesta(ArrayList<String> a) throws RemoteException;

}
