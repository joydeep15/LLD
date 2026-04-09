package parkinglot.entity;

import java.util.concurrent.atomic.AtomicBoolean;

public class Spot {
    AtomicBoolean available;
    public String userID;
    public String id;

    public Spot(String id){
        available = new AtomicBoolean(false);
        userID = null;
        this.id = id;
    }

    public boolean tryOccupy(){
        return  available.compareAndSet(false, true);
    }



}
