package lld.uber;

import java.util.Scanner;
import java.util.concurrent.*;

/*
*   GET /estimates/price -> Map<Product, Integer>
    {
        src, dest
    }

*   POST /requests/estimate -> Fare
*   {
*       src, dest, productId
*   }
*
*   POST /requests -> Ride
* {
*   fare_id, product_id, src, dest
* }
*
*
* */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService esx = Executors.newSingleThreadExecutor();
        Future<String> f1 = esx.submit(new DependentService());
//        f1.get();
    }
}

class DependentService implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(2000);
        return "Hello World";
    }
}