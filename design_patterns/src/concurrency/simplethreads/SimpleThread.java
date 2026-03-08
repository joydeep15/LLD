package concurrency.simplethreads;

public class SimpleThread extends Thread {
    private Counter counter;
    public SimpleThread(Counter counter) {
        this.counter = counter;
    }
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            counter.increment();
        }
    }
}
