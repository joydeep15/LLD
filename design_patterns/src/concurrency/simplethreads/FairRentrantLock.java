package concurrency.simplethreads;

import java.util.concurrent.locks.ReentrantLock;

class FairLock{
    private final ReentrantLock fairlock = new ReentrantLock(true);
    public void accessMethod()  {
        try {
            fairlock.lock();
            System.out.printf("%s acquired the lock\n", Thread.currentThread().getName());
            Thread.sleep(2000);
        }catch (InterruptedException e) {

        }finally {
            System.out.printf("%s released the lock\n", Thread.currentThread().getName());
            fairlock.unlock();
        }
    }
}
public class FairRentrantLock {
    public static void main(String[] args) throws InterruptedException {
        FairLock fairlock = new FairLock();
        Runnable r1 = new Runnable() {
            public void run() {
                fairlock.accessMethod();
            }
        };
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r1);
        Thread t3 = new Thread(r1);
        t1.start();
//        Thread.sleep(50);
        t2.start();
//        Thread.sleep(50);
        t3.start();
    }
}


