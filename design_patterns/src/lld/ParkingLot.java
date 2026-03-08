package lld;


class ParkingSpot{
    int id;
    boolean isEmpty;
    int price;
    Vehicle vehicle;

    boolean parkVehicle(Vehicle v){
        this.vehicle = v;
        isEmpty = false;
        return true;
    }

    boolean removeVehicle(){
        this.vehicle = null;
        isEmpty = true;
        return true;
    }
}

class Vehicle{
    String vno;
    VType vType;
}
enum VType{TWO_WHEELER, FOUR_WHEELER}

public class ParkingLot {
}
