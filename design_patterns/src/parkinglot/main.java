package parkinglot;

import parkinglot.entity.Spot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class main {
    public static void main(String[] args) throws InterruptedException {
        List<Spot> spots = new ArrayList<>();
        spots.add(new Spot("s1"));
        spots.add(new Spot("s2"));

        ExecutorService executor = Executors.newFixedThreadPool(3);
        SpotFetcher spotFetcher = new SpotFetcher();
        spotFetcher.spots = spots;
        Runnable u1 = () -> spotFetcher.getSpot("joy");
        Runnable u2 = () -> spotFetcher.getSpot("joy1");
        Runnable u3 = () -> spotFetcher.getSpot("joy2");
        System.out.println("start..");
        executor.submit(u1);
        executor.submit(u2);
        executor.submit(u3);
//        executor.wait();
    }
}

class SpotFetcher{
    List<Spot> spots;
    public Spot getSpot(String user){
        for(Spot s : spots){
            if(s.tryOccupy()){
                s.userID = user;
                System.out.printf("Spot: %s, assigned to user: %s\n", s.id, s.userID);
                return s;
            }
        }
        return null;
    }
}
