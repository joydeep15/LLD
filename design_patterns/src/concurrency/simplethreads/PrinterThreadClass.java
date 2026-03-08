package concurrency.simplethreads;

public class PrinterThreadClass extends Thread {
    @Override
    public void run() {
        System.out.println("world");
    }
}
