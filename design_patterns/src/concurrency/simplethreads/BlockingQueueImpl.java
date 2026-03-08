package concurrency.simplethreads;

import java.util.UUID;
import java.util.concurrent.*;

public class BlockingQueueImpl {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        ExecutorService executor = Executors.newFixedThreadPool(5);
        executor.submit(new Producer(queue));
        executor.submit(new Producer(queue));
        executor.submit(new Consumer(queue));
    }
}

class Producer implements Runnable {
    private BlockingQueue<Integer> queue;
    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            queue.offer(i);
        }
    }
}

class Consumer implements Runnable {
    private BlockingQueue<Integer> queue;
    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        while (true) {
            try {
                Integer i = queue.take();
                System.out.println(i);
            } catch (InterruptedException e) {}
        }
    }
}