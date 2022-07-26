package CDI.ET1;
public class extends_Trhead {

    public static class Tarea extends Thread {
        @Override
    public void run(){
        for(int i =0; i<11;i++)
        System.out.println(i);
    }
    }

    public static void main(String[] args) {
        Tarea tarea1 = new Tarea();
        Tarea tarea2 = new Tarea();
        tarea1.start();
        tarea2.start();
        for(int i =0; i<11;i++)
        System.out.println(i);
    }

}