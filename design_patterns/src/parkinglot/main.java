package parkinglot;

public class main {
    public static void main(String[] args) {
    }
}

class SingleTon{
    private SingleTon instance;
    private SingleTon(){}
    public SingleTon getInstance(){
        if(instance == null){
            synchronized (SingleTon.class){
                if(instance == null){
                    synchronized (SingleTon.class){
                        instance = new SingleTon();
                    }
                }
            }
        }
        return instance;
    }
}