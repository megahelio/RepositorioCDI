import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
/*
1º-registro (no sé lo que es el registro jajaja salu2)
2º-server
3º-cliente
*/

public class server {

    public static void main(){
        Server obj= new Server();
        try{
            Hello stub=(Hello) UnicastRemoteObject.exportObject(obj, 0);
            Registry registro=LocateRegistry.getRegistry();
            registro.bind(Names.SERVERNAME, stub);
            System.out.println("servidor listo");

        }catch(/*no se pude conectar */){
            System.err.println("caca");
        }catch(/*no se pude contectar porque ya está creado el puente */){
System.err.println("caca");
        }    }

}
