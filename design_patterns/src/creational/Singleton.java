package creational;

/*
* Eager
* Lazy
* Synchronised
* 2PL
* * Use cases -
* Request Cache, Local Cache, DB connections, 3rd Party Library instances, handling threadpools
*
*
* */
public class Singleton {
    public static void main(String[] args) {
        SingletonEnum.INSTANCE.setValue(192);
        System.out.println(SingletonEnum.INSTANCE.getValue());
    }
}
enum SingletonEnum {
    INSTANCE;

    // You can add fields and methods as needed
    private int value;

    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
}

class SingletonImpl{
    /*
    * Why is it volatile?
        Do not cache the variable locally in any thread.
        Always read from main memory to get the latest value.
        Prevents instruction reordering — a key reason it's required in double-checked locking.

     Instruction Reordering
        Allocate memory
        Set instance = memory address (before constructor runs!)
        Run constructor
        ➡️ Another thread might see instance != null but the constructor hasn’t finished yet —
         leading to a partially constructed object.

     All write operation before volatile is dumped in memory
     volatile = have before
    *
    * */
    private static volatile SingletonImpl instance;


    private SingletonImpl(){

    }

    public static SingletonImpl GetInstance(){
        if (instance == null){
            synchronized (Singleton.class){
                if (instance == null){
                    instance = new SingletonImpl();
                }
            }
        }
        return instance;
    }
}

class SingletonClassLoader{
    private static class SingletonClassLoaderInner{
        private static final SingletonClassLoader INSTANCE = new SingletonClassLoader();
    }
    private SingletonClassLoader(){
    }
    public static SingletonClassLoader getInstance(){
        return SingletonClassLoaderInner.INSTANCE;
    }

}
