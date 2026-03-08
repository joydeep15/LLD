package concurrency.simplethreads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ReadWrite {

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();
    volatile int value =0;
    ReadWrite() {}

    void increment() {
        try {
            writeLock.lock();
            value++;
            System.out.printf("%s write value as: %d\n", Thread.currentThread().getName(), value);
        }catch(Exception e) {

        }finally {
            writeLock.unlock();
        }
    }

    void get() {
        try {
            readLock.lock();
            System.out.printf("%s read value as: %d\n", Thread.currentThread().getName(), value);
        }catch(Exception e) {

        }finally {
            readLock.unlock();
        }
    }
}

public class ReadWriteLock {
    public static void main(String[] args) throws InterruptedException {
        ReadWrite rw = new ReadWrite();
        Runnable reader = new Runnable() {
            public void run() {
                for(int i = 0; i < 10; i++) {
                    rw.get();
                }
            }
        };
        Runnable writer = new Runnable() {
            public void run() {
                for(int i = 0; i < 10; i++) {
                    rw.increment();
                }
            }
        };
        Thread t1 = new Thread(reader);
        Thread t2 = new Thread(reader);
        Thread t3 = new Thread(writer);
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }
}
