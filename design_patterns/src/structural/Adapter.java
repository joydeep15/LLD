package structural;

/*
* The Adapter design pattern is a structural pattern that enables objects with incompatible
* interfaces to work together. Instead of modifying existing code, you introduce an Adapter class that translates
*  one interface into another, allowing the client to interact seamlessly with the adapted class
*
* Making legacy code fit into newer libs/interfaces
*
* Common analogy: Think of a power socket adapter that lets you plug a device with a US plug into a European socket.
* Bridging New and Old Systems:
*
* */
interface Vehicle{
    void run();
}

class Boat{
    void sail(){
        System.out.println("boat is sailing");
    }
}

class BoatAdapter implements Vehicle{

    Boat b;
    BoatAdapter(){
        b = new Boat();
    }
    public void run(){
        b.sail();
    }
}

public class Adapter {
    public static void main(String[] args) {
        Vehicle vaahan = new BoatAdapter();
        vaahan.run();
    }
}
