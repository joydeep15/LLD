package behavioural;

/*
* Observer
* Subject
*
*
* The Observer pattern defines a one-to-many dependency between objects so that when one object (the subject)
*  changes state, all its dependents (observers) are notified and updated automatically.
*  It’s commonly used in event-driven systems.
*
* Q: How is it different from Pub/Sub?
Answer:

In Observer, the subject and observers are typically tightly coupled — they know about each other.
* In Pub/Sub (like Kafka), communication is via a broker, making it more decoupled and suitable for distributed systems.
*
* How to make thread safe?
* Use thread safe collections - copy on write array, concurrent hashmap/set
*
* */

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

interface ObserverI{
    void update(String state);
}

interface Subject{
    void addObserver(ObserverI o);
    void removeObserver(ObserverI o);
    void notifyObservers();
}

class PhoneObserver implements ObserverI{
    String state;
    public void update(String state){
        this.state = state;
        display();
    }

    private void display(){
        System.out.printf("Phone Observer, state: %s\n", this.state);
    }

}
class TVObserver implements ObserverI{
    String state;
    public void update(String state){
        this.state = state;
        display();
    }

    private void display(){
        System.out.printf("TV Observer, state: %s\n", this.state);
    }
}

class WeatherSubject implements Subject{
    /*
    * If equals and hashcode not defined, it uses default java impl that checks if it points
    * to same address in memory
    * */
    Set<ObserverI> observers;
    private String state;
    WeatherSubject(){
        observers = new HashSet<>();
    }
    public void setState(String state){
        this.state = state;
        notifyObservers();
    }
    public void addObserver(ObserverI o){
        observers.add(o);
    }

    public void removeObserver(ObserverI o){
        observers.remove(o);
    }

    public void notifyObservers(){
        for (ObserverI obs : observers){
            obs.update(this.state);
        }
    }
}

public class Observer {
    public static void main(String[] args) {
        WeatherSubject ws = new WeatherSubject();
        ObserverI phone = new PhoneObserver();
        ObserverI tv = new TVObserver();

        ws.addObserver(phone);
        ws.addObserver(tv);
        ws.setState("sunny");
        ws.removeObserver(tv);
        ws.setState("rainy");

    }
}
