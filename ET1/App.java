package CDI.ET1;
public class App extends Thread{
    public App (String pepe){
        super(pepe);
    }
    public void run(){
      System.out.println("A: "+Thread.currentThread().getId()+" "+Thread.currentThread().getName());
    }
    public static void main(String[] args) throws Exception {
      App thr = new App(args[0]);
      thr.start();
    }
  }
