package concurrency.simplethreads;

import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierImpl {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        DependentService oddPrinter = new DependentService(cyclicBarrier, 10, true);
        DependentService evenPrinter = new DependentService(cyclicBarrier, 10, false);
        executorService.submit(evenPrinter);
        Thread.sleep(50);
        executorService.submit(oddPrinter);



    }
}

class DependentService implements Callable<String> {

    private final CyclicBarrier barrier;
    private final int maxCount;
    private final boolean isOdd;

    public DependentService(CyclicBarrier barrier, int maxCount, boolean isOdd) {
        this.barrier = barrier;
        this.maxCount = maxCount;
        this.isOdd = isOdd;
    }

    @Override
    public String call() throws Exception {
        System.out.printf("%s is starting\n",Thread.currentThread().getName());
        int start = 0;
        if (isOdd) {
            start = 1;
        }
        for (int i = start; i < this.maxCount; i+=2) {
            System.out.printf("%s printed: %d\n",Thread.currentThread().getName(), i);
            barrier.await();
        }
        return "done";

    }
}
