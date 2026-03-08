package creational;
/*
* Factory of factory
* */
interface Car{
    public int getTopSpeed();
}
class EconomyCar1 implements Car{
    public int getTopSpeed() {
        return 10;
    }
}
class EconomyCar2 implements Car{
    public int getTopSpeed() {
        return 15;
    }
}

class SportsCar1 implements Car{
    public int getTopSpeed() {
        return 100;
    }
}

class SportsCar2 implements Car{
    public int getTopSpeed() {
        return 150;
    }
}
interface CarFactory {
    Car getCar(int price);
}
class EconomicCarFactory implements CarFactory {
    public Car getCar(int price){
        if(price <= 400){
            return new EconomyCar1();
        }else{
            return new EconomyCar2();
        }
    }
}

class SportsCarFactory implements CarFactory{
    public Car getCar(int price){
        if(price > 5000){
            return new SportsCar2();
        }else {
            return new SportsCar1();
        }
    }
}
class AbstractFactoryProducer{
    public static CarFactory getFactory(String type){
        if(type.equals("Economy")){
            return new EconomicCarFactory();
        }else return new SportsCarFactory();
    }
}

public class AbstractFactory {
    public static void main(String[] args) {
        AbstractFactoryProducer abs = new AbstractFactoryProducer();
        CarFactory carFactory = abs.getFactory("Economy");
        Car car1 = carFactory.getCar(50);
        car1.getTopSpeed();
    }
}
