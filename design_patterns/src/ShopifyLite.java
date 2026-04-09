import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class ShopifyLite {

    public static void main(String[] args) {
        Marketplace m = new Marketplace();
        Store s = m.createStore();

        long p1 = s.addProduct("TShirt", 100, 10);
        long p2 = s.addProduct("Hoodie", 200, 5);

        String cart = s.createCart();
        s.addToCart(cart, p1, 2);
        s.addToCart(cart, p2, 1);

        System.out.println(s.checkout(cart, new NoOpPayment()));
    }

    // -------- Marketplace --------
    static class Marketplace {
        Map<Long, Store> stores = new ConcurrentHashMap<>();
        AtomicLong idGen = new AtomicLong(1);

        Store createStore() {
            Store s = new Store(idGen.getAndIncrement());
            stores.put(s.id, s);
            return s;
        }
    }

    // -------- Store --------
    static class Store {
        long id;
        Map<Long, Product> products = new ConcurrentHashMap<>();
        Map<String, Cart> carts = new ConcurrentHashMap<>();
        AtomicLong pidGen = new AtomicLong(1);
        ReentrantLock lock = new ReentrantLock();

        Store(long id) { this.id = id; }

        long addProduct(String name, int price, int stock) {
            long id = pidGen.getAndIncrement();
            products.put(id, new Product(id, name, price, stock));
            return id;
        }

        String createCart() {
            String cid = UUID.randomUUID().toString();
            carts.put(cid, new Cart());
            return cid;
        }

        void addToCart(String cid, long pid, int qty) {
            carts.get(cid).add(pid, qty);
        }

        Order checkout(String cid, PaymentProcessor p) {
            lock.lock();
            try {
                Cart cart = carts.get(cid);
                int total = 0;

                // validate
                for (var e : cart.items.entrySet()) {
                    Product pr = products.get(e.getKey());
                    if (pr.stock < e.getValue()) throw new RuntimeException("No stock");
                }

                // deduct
                for (var e : cart.items.entrySet()) {
                    Product pr = products.get(e.getKey());
                    pr.stock -= e.getValue();
                    total += pr.price * e.getValue();
                }

                Order o = new Order(total);
                if (!p.pay(o)) throw new RuntimeException("Payment failed");

                cart.clear();
                return o;

            } finally {
                lock.unlock();
            }
        }
    }

    // -------- Product --------
    static class Product {
        long id;
        String name;
        int price;
        int stock;

        Product(long id, String name, int price, int stock) {
            this.id = id; this.name = name; this.price = price; this.stock = stock;
        }
    }

    // -------- Cart --------
    static class Cart {
        Map<Long, Integer> items = new HashMap<>();

        void add(long pid, int qty) {
            items.put(pid, items.getOrDefault(pid, 0) + qty);
        }

        void clear() { items.clear(); }
    }

    // -------- Order --------
    static class Order {
        int total;
        Order(int total) { this.total = total; }

        public String toString() { return "Order total=" + total; }
    }

    // -------- Payment --------
    interface PaymentProcessor {
        boolean pay(Order o);
    }

    static class NoOpPayment implements PaymentProcessor {
        public boolean pay(Order o) { return true; }
    }
}