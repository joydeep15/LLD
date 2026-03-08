package concurrency.simplethreads;

import java.time.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Test {
    public static void main(String[] args) throws InterruptedException {
//        PrinterThreadClass printerThreadClass = new PrinterThreadClass();
//        printerThreadClass.run();
//
//        Thread t1 = new Thread(new PrinterRunnable());
//        t1.start();
//
//        System.out.println("hello");
//        Thread.sleep(10000);

//        Counter c = new Counter();
//        SimpleThread s1 = new SimpleThread(c);
//        SimpleThread s2 = new SimpleThread(c);
//        s1.start();
//        s2.start();
//        s1.join();
//        s2.join();
//        System.out.println(c.getCount());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime later = now.plusMinutes(30);
        later = later.plusHours(3);
        Duration duration = Duration.between(now, later);
        System.out.println(duration.toMinutes());
    }
}
