package creational;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
/*
*
* When to Use Factory Pattern?
    When the object creation is complex or involves multiple steps.
    When you have a common interface, and you want to create different subclasses based on input or configuration.
    When the code needs to be decoupled from specific class implementations.

Why is it Useful?
    Promotes loose coupling between client and implementation.
    Supports Open/Closed Principle (open for extension, closed for modification).
    Helps centralize and manage object creation logic in one place.
*
* *
* */

interface Shape {
    String draw();
}

class Square implements Shape{
    public String draw(){
        return "I am Square";
    }
}
class Rhombus implements Shape{
    public String draw(){
        return "I am Rhombus";
    }
}

class Default implements Shape{
    public String draw(){
        return "I am shape less";
    }
}
public class Factory {
    Map<String, Supplier<Shape>> mp;
    public Shape createShape(String shape){
        if(shape.toLowerCase(Locale.ROOT).equals("square")){
            return new Square();
        }else if(shape.toLowerCase(Locale.ROOT).equals("rhombus")){
            return new Rhombus();
        }else return new Default();
    }

    public void initMap(){
        mp = new HashMap<>();
        mp.put("square", Square::new);
        mp.put("rhombus", Rhombus::new);
    }
    public Shape createShapeViaSupplier(String shape){
        return mp.get(shape).get();
    }

    public static void main(String[] args) {
        Factory f = new Factory();
        f.initMap();
        Shape s1 = f.createShape("square");
        Shape s2 = f.createShape("rhombus");
        System.out.println(s1.draw());
        System.out.println(s2.draw());

        Shape s3 = f.createShapeViaSupplier("square");
        Shape s4 = f.createShapeViaSupplier("square");
        System.out.println(s3.draw());
        System.out.printf("s3 and s4 are equal: %b", s3 == s4);
    }
}
