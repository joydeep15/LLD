package concurrency.simplethreads;

import java.sql.Time;
import java.util.Scanner;
import java.util.concurrent.*;

public class CountDownLatchImpl {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        DependentService1 ds1 = new DependentService1(countDownLatch);
        DependentService1 ds2 = new DependentService1(countDownLatch);
        DependentService1 ds3 = new DependentService1(countDownLatch);
        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.submit(ds1);
        executor.submit(ds2);
        executor.submit(ds3);

        countDownLatch.await();
        System.out.println("all background work is complete");

    }
}

class DependentService1 implements Runnable {
    private final CountDownLatch latch;
    DependentService1(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            latch.countDown();
            latch.await();
            System.out.printf("%s: DependentService is running: \n", Thread.currentThread().getName());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
