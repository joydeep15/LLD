package concurrency.simplethreads;

public class PrinterRunnable implements Runnable {
    public void run(){
        System.out.println("world runnable");
    }
}
