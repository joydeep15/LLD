package structural;

/*
* The Decorator Pattern is a structural design pattern that allows behavior to be added to an individual object,
* dynamically, without affecting the behavior of other objects from the same class.
It is a flexible alternative to subclassing for extending functionality.
*
* component interface + concrete
* abstract decorator + concrete decorator
*
* Making the base decorator abstract is a guardrail. It prevents accidental usage without added behavior,
* maintains a clean design, and ensures decorators are always meaningful extensions.
*
* | Reason           | Abstract Decorator                   | Concrete Decorator |
| ---------------- | ------------------------------------ | ------------------ |
| Prevent misuse   | ✅ Cannot instantiate directly        | ❌ Can be misused   |
| Shared logic     | ✅ Provided centrally                 | ✅ Inherits         |
| Expresses intent | ✅ Only extended, never used directly | ❌ Can confuse devs |
| Adds behavior    | ❌ No                                 | ✅ Yes              |

Adds no value
Coffee base = new BasicCoffee();
Coffee noOp = new CoffeeDecorator(base);
System.out.println(noOp.getDescription()); // Same as base
System.out.println(noOp.cost()); // Same as base

*
*
*
*
* */

interface Pizza{
    String getDescription();
    int getCost();
}

class SimplePizza implements Pizza{

    @Override
    public String getDescription() {
        return "SimplePizza";
    }

    @Override
    public int getCost() {
        return 100;
    }

}

abstract class PizzaDecorator implements Pizza{
    protected Pizza pizza;
    PizzaDecorator(Pizza pizza){
        this.pizza = pizza;
    }
}

class CheeseDecorator extends PizzaDecorator{
    CheeseDecorator(Pizza pizza){
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", cheese";
    }
    @Override
    public int getCost() {
        return pizza.getCost() + 50;
    }
}

class PepperoniDecorator extends PizzaDecorator{
    PepperoniDecorator(Pizza pizza){
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", pepperoni";
    }
    @Override
    public int getCost() {
        return pizza.getCost() + 80;
    }
}
public class Decorator {
    public static void main(String[] args) {
        Pizza p = new SimplePizza();
        p = new CheeseDecorator(p);
        p = new PepperoniDecorator(p);
        System.out.println(p.getDescription());
        System.out.println(p.getCost());
    }


}
