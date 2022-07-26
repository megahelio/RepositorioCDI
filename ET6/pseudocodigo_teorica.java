/*
productor produce rellenando un buffer, consumidor consume del buffer


Interfaz Condition
ArrayBlockingQueu -> buffer
*/
class pseudocodigo_teorica {
    // Productor
    public synchronized void put() {
        // si el buffer está lleno pues no producciomos
        while (buffer.size() == max) {
            // esperamos
            wait(); //falta el trycatch
        }
        // si el buffer no está lleno "produccimos"
        buffer.add(a);
        notifyall();
    }
    // consumidor
    public synchronized void get(){
        while(buffer.size()==0){
            try{
            wait();
            }catch(){}
        }
        buffer.get(a);
        notifyall();
    }
}