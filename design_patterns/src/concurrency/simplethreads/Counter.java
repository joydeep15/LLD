package concurrency.simplethreads;

public class Counter {
    int count;
    public Counter() {
        this.count = 0;
    }

    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
