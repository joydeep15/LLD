package structural;

/*
The Facade Design Pattern is a structural design pattern that provides a simplified interface to a complex subsystem.
It's like creating a front desk for multiple complex backend operations — the client deals with only one object,
 not all the inner complexities.

 Intent: “Provide a unified interface to a set of interfaces in a subsystem.
 Facade defines a higher-level interface that makes the subsystem easier to use.”

 Think of a hotel concierge (Facade). Instead of contacting the housekeeper, restaurant,
 and taxi separately, you ask the concierge, who coordinates with everyone behind the scenes.



*
* */

class Lights{
    public void on(){
        System.out.println("lights switched on");
    }

    public void off(){
        System.out.println("lights switched off");
    }

    public void dim(){
        System.out.println("lights dimmed");
    }
}

class Movie {
    String getName(){
        return "Matrix";
    }
    void on(){
        System.out.println("movie started");
    }
    void off(){
        System.out.println("movie ended");
    }
    void play(){
        System.out.println("movie playing");
    }
}
class Projector{
    void setMovie(Movie m){
        System.out.println("movie set: " +  m.getName());
    }
    void on(){
        System.out.println("projector switched on");
    }
    void off(){
        System.out.println("projector switched off");
    }
}

class HomeTheatreFacade{
    private Lights lights;
    private Movie movie;
    private Projector projector;
    public HomeTheatreFacade(Lights l, Movie m, Projector p){
        this.lights = l;
        this.movie = m;
        this.projector = p;
    }

    public void start(){
        lights.dim();
        projector.on();
        movie.on();
    }

    public void stop(){
        lights.on();
        movie.off();
        projector.off();
    }
}

public class Facade {
    public static void main(String[] args) throws InterruptedException {
        Lights l = new Lights();
        Movie m = new Movie();
        Projector p = new Projector();
        HomeTheatreFacade hm = new HomeTheatreFacade(l, m, p);
        hm.start();
        Thread.sleep(5000);
        hm.stop();
    }
}
