package concurrency.simplethreads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicOps {
    public static void main(String[] args) throws InterruptedException {
        Seat s = new Seat();
        Runnable user1 = s::tryReserve;
        Runnable user2 = s::tryReserve;
        List<Integer> list = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.execute(user1);
        executor.execute(user2);
        Thread.sleep(1000);
        executor.shutdown();
    }
}

class Seat {
    private AtomicBoolean available;
    public Seat() {
        available = new AtomicBoolean(true);
    }

    public void tryReserve() {
        if (available.compareAndSet(true, false)){
            System.out.printf("Seat is reserved by %s\n", Thread.currentThread().getName());
        }else{
            System.out.printf("Seat was already reserved\n");
        }
    }
}
