package behavioural;

/*
* Why Use the Strategy Pattern?
    Extensibility: Add new algorithms without changing existing code (adheres to the Open/Closed Principle).
    Run-time Flexibility: Switch behaviors dynamically at runtime.
    Encapsulation: Each algorithm/behavior is isolated, which encourages code maintainability and testability
    Promotes OCP and SRP from SOLID.

Use Cases in Interviews
    Logging strategy (file, DB, console)
    Sorting strategy (different comparators)
    Compression strategy (ZIP, RAR)
    Payment processing
    Promotion strategies in e-commerce

*
* */
interface SortingStrategy{
    void sort(int[] nums);
}

class BubbleSortStrategy implements SortingStrategy{
    public void sort(int[] nums){
        System.out.println("using bubble sort");
    }
}

class QuickSortStrategy implements SortingStrategy{
    public void sort(int[] nums){
        System.out.println("using quick sort");
    }
}

class MergeSortStrategy implements SortingStrategy{
    public void sort(int[] nums) {
        System.out.println("using mergesort");
    }
}
class StrategyContext {
    private SortingStrategy SortingStrategy;
    StrategyContext(SortingStrategy a){
        this.SortingStrategy = a;
    }
    public void setSortingStrategy(SortingStrategy b){
        this.SortingStrategy = b;
    }

    public void performSort(int[] nums){
        this.SortingStrategy.sort(nums);
    }
}
public class Strategy {
    public static void main(String[] args) {
        StrategyContext sc = new StrategyContext(new BubbleSortStrategy());
        int[] a = {1, 2, 3};
        sc.performSort(a);
        sc.setSortingStrategy(new MergeSortStrategy());
        sc.performSort(a);
    }
}
