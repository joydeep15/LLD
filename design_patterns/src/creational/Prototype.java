package creational;

/*
* Used to make a copy / clone of existing object. usually done when creation of base object is very expensive
Object creation is costly or complex
For example, if an object involves heavy computation, database calls, or complex configuration.
Instead of recreating it, you clone a preconfigured instance.
*
* */

interface Proto<T>{
    T Clone();
}

class Circle implements Proto<Circle>{
    int radius;
    Circle(int radius){
        this.radius = radius;
    }

    public Circle Clone(){
        return new Circle(this.radius);
    }
}

public class Prototype {
    public static void main(String[] args) {
        Circle circle = new Circle(10);
        Circle circle2 = circle.Clone();
        System.out.println(circle2.radius);
    }
}
