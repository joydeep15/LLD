import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class ShopifyNonLite {

    public static void main(String[] args) {
        Marketplace marketplace = new Marketplace();

        Store fashion = marketplace.createStore("Fashion");
        Store electronics = marketplace.createStore("Electronics");

        Product tshirt = marketplace.addProduct(fashion.getId(), "T-Shirt", new BigDecimal("20.00"), 10);
        Product hoodie = marketplace.addProduct(fashion.getId(), "Hoodie", new BigDecimal("50.00"), 5);
        Product headphones = marketplace.addProduct(electronics.getId(), "Headphones", new BigDecimal("99.00"), 3);

        String cartId = marketplace.createCart(fashion.getId());
        marketplace.addToCart(fashion.getId(), cartId, tshirt.getId(), 2);
        marketplace.addToCart(fashion.getId(), cartId, hoodie.getId(), 1);

        Order order = marketplace.placeOrder(
                fashion.getId(),
                cartId,
                PaymentProcessorFactory.getProcessor(PaymentMethod.NO_OP)
        );

        System.out.println(order);
        System.out.println("T-Shirt inventory: " + tshirt.getInventoryCount());
        System.out.println("Hoodie inventory: " + hoodie.getInventoryCount());
        System.out.println("Headphones inventory: " + headphones.getInventoryCount());
    }

    static class Marketplace {
        private final Map<Long, Store> stores = new ConcurrentHashMap<>();
        private final AtomicLong storeIdSeq = new AtomicLong(1);

        Store createStore(String name) {
            long id = storeIdSeq.getAndIncrement();
            Store store = new Store(id, name);
            stores.put(id, store);
            return store;
        }

        Product addProduct(long storeId, String name, BigDecimal price, int inventoryCount) {
            return getStore(storeId).addProduct(name, price, inventoryCount);
        }

        String createCart(long storeId) {
            return getStore(storeId).createCart();
        }

        void addToCart(long storeId, String cartId, long productId, int quantity) {
            getStore(storeId).addToCart(cartId, productId, quantity);
        }

        Order placeOrder(long storeId, String cartId, PaymentProcessor paymentProcessor) {
            return getStore(storeId).placeOrder(cartId, paymentProcessor);
        }

        private Store getStore(long storeId) {
            Store store = stores.get(storeId);
            if (store == null) throw new NotFoundException("Store not found: " + storeId);
            return store;
        }
    }

    static class Store {
        private final long id;
        private final String name;

        private final Map<Long, Product> products = new ConcurrentHashMap<>();
        private final Map<String, Cart> carts = new ConcurrentHashMap<>();
        private final Map<Long, Order> orders = new ConcurrentHashMap<>();

        private final AtomicLong productIdSeq = new AtomicLong(1);
        private final AtomicLong orderIdSeq = new AtomicLong(1);
        private final ReentrantLock checkoutLock = new ReentrantLock();

        Store(long id, String name) {
            this.id = id;
            this.name = name;
        }

        long getId() {
            return id;
        }

        Product addProduct(String productName, BigDecimal price, int inventoryCount) {
            validatePrice(price);
            validateNonNegativeInventory(inventoryCount);

            long productId = productIdSeq.getAndIncrement();
            Product product = new Product(productId, productName, price, inventoryCount);
            products.put(productId, product);
            return product;
        }

        String createCart() {
            String cartId = "CART-" + id + "-" + UUID.randomUUID().toString().substring(0, 8);
            carts.put(cartId, new Cart(cartId, id));
            return cartId;
        }

        void addToCart(String cartId, long productId, int quantity) {
            Cart cart = getCart(cartId);
            getProduct(productId); // validate product belongs to this store
            if (cart.getStoreId() != id) {
                throw new InvalidCartException("Cart does not belong to this store.");
            }
            cart.addItem(productId, quantity);
        }

        Order placeOrder(String cartId, PaymentProcessor paymentProcessor) {
            Cart cart = getCart(cartId);
            if (cart.isEmpty()) throw new InvalidCartException("Cart is empty.");

            checkoutLock.lock();
            try {
                // Re-read cart under lock to avoid mutation races
                List<CartItem> snapshotItems = cart.snapshotItems();

                List<OrderItem> orderItems = new ArrayList<>();
                Map<Long, Integer> deductions = new LinkedHashMap<>();
                BigDecimal total = BigDecimal.ZERO;

                for (CartItem item : snapshotItems) {
                    Product product = getProduct(item.getProductId());
                    if (product.getInventoryCount() < item.getQuantity()) {
                        throw new InsufficientInventoryException(
                                "Insufficient inventory for product: " + product.getName()
                        );
                    }

                    orderItems.add(new OrderItem(
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            item.getQuantity()
                    ));

                    deductions.put(product.getId(), item.getQuantity());
                    total = total.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                }

                // Atomic inventory deduction inside the same lock
                for (Map.Entry<Long, Integer> e : deductions.entrySet()) {
                    getProduct(e.getKey()).decreaseInventory(e.getValue());
                }

                Order order = new Order(
                        orderIdSeq.getAndIncrement(),
                        id,
                        cartId,
                        orderItems,
                        total,
                        OrderStatus.CREATED
                );

                boolean paid = paymentProcessor.process(order);
                if (!paid) {
                    // Roll back inventory on payment failure
                    for (Map.Entry<Long, Integer> e : deductions.entrySet()) {
                        getProduct(e.getKey()).increaseInventory(e.getValue());
                    }
                    order.setStatus(OrderStatus.FAILED);
                    throw new PaymentFailedException("Payment failed.");
                }

                order.setStatus(OrderStatus.PAID);
                orders.put(order.getId(), order);
                cart.clear();
                return order;
            } finally {
                checkoutLock.unlock();
            }
        }

        Product getProduct(long productId) {
            Product product = products.get(productId);
            if (product == null) throw new NotFoundException("Product not found: " + productId);
            return product;
        }

        private Cart getCart(String cartId) {
            Cart cart = carts.get(cartId);
            if (cart == null) throw new NotFoundException("Cart not found: " + cartId);
            return cart;
        }

        private void validatePrice(BigDecimal price) {
            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be positive.");
            }
        }

        private void validateNonNegativeInventory(int inventoryCount) {
            if (inventoryCount < 0) {
                throw new IllegalArgumentException("Inventory cannot be negative.");
            }
        }
    }

    static class Product {
        private final long id;
        private final String name;
        private final BigDecimal price;
        private int inventoryCount;

        Product(long id, String name, BigDecimal price, int inventoryCount) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.inventoryCount = inventoryCount;
        }

        long getId() { return id; }
        String getName() { return name; }
        BigDecimal getPrice() { return price; }

        synchronized int getInventoryCount() {
            return inventoryCount;
        }

        synchronized void decreaseInventory(int quantity) {
            if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
            if (inventoryCount < quantity) throw new InsufficientInventoryException("Inventory would go negative.");
            inventoryCount -= quantity;
        }

        synchronized void increaseInventory(int quantity) {
            if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
            inventoryCount += quantity;
        }

        @Override
        public String toString() {
            return "Product{id=" + id + ", name='" + name + "', price=" + price +
                    ", inventoryCount=" + inventoryCount + "}";
        }
    }

    static class Cart {
        private final String id;
        private final long storeId;
        private final Map<Long, CartItem> items = new LinkedHashMap<>();

        Cart(String id, long storeId) {
            this.id = id;
            this.storeId = storeId;
        }

        long getStoreId() {
            return storeId;
        }

        synchronized void addItem(long productId, int quantity) {
            if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
            CartItem existing = items.get(productId);
            if (existing == null) {
                items.put(productId, new CartItem(productId, quantity));
            } else {
                existing.setQuantity(existing.getQuantity() + quantity);
            }
        }

        synchronized boolean isEmpty() {
            return items.isEmpty();
        }

        synchronized List<CartItem> snapshotItems() {
            return new ArrayList<>(items.values());
        }

        synchronized void clear() {
            items.clear();
        }

        @Override
        public synchronized String toString() {
            return "Cart{id='" + id + "', storeId=" + storeId + ", items=" + items.values() + "}";
        }
    }

    static class CartItem {
        private final long productId;
        private int quantity;

        CartItem(long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        long getProductId() {
            return productId;
        }

        int getQuantity() {
            return quantity;
        }

        void setQuantity(int quantity) {
            if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "CartItem{productId=" + productId + ", quantity=" + quantity + "}";
        }
    }

    static class Order {
        private final long id;
        private final long storeId;
        private final String cartId;
        private final List<OrderItem> items;
        private final BigDecimal totalAmount;
        private OrderStatus status;

        Order(long id, long storeId, String cartId, List<OrderItem> items, BigDecimal totalAmount, OrderStatus status) {
            this.id = id;
            this.storeId = storeId;
            this.cartId = cartId;
            this.items = new ArrayList<>(items);
            this.totalAmount = totalAmount;
            this.status = status;
        }

        long getId() {
            return id;
        }

        void setStatus(OrderStatus status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Order{id=" + id +
                    ", storeId=" + storeId +
                    ", cartId='" + cartId + '\'' +
                    ", items=" + items +
                    ", totalAmount=" + totalAmount +
                    ", status=" + status + "}";
        }
    }

    static class OrderItem {
        private final long productId;
        private final String productName;
        private final BigDecimal unitPrice;
        private final int quantity;

        OrderItem(long productId, String productName, BigDecimal unitPrice, int quantity) {
            this.productId = productId;
            this.productName = productName;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "OrderItem{productId=" + productId +
                    ", productName='" + productName + '\'' +
                    ", unitPrice=" + unitPrice +
                    ", quantity=" + quantity + "}";
        }
    }

    enum OrderStatus { CREATED, PAID, FAILED }

    enum PaymentMethod { NO_OP, FAILING }

    interface PaymentProcessor {
        boolean process(Order order);
    }

    static class NoOpPaymentProcessor implements PaymentProcessor {
        @Override
        public boolean process(Order order) {
            return true;
        }
    }

    static class FailingPaymentProcessor implements PaymentProcessor {
        @Override
        public boolean process(Order order) {
            return false;
        }
    }

    static class PaymentProcessorFactory {
        static PaymentProcessor getProcessor(PaymentMethod method) {
            switch (method) {
                case FAILING:
                    return new FailingPaymentProcessor();
                case NO_OP:
                default:
                    return new NoOpPaymentProcessor();
            }
        }
    }

    static class NotFoundException extends RuntimeException {
        NotFoundException(String message) { super(message); }
    }

    static class InvalidCartException extends RuntimeException {
        InvalidCartException(String message) { super(message); }
    }

    static class InsufficientInventoryException extends RuntimeException {
        InsufficientInventoryException(String message) { super(message); }
    }

    static class PaymentFailedException extends RuntimeException {
        PaymentFailedException(String message) { super(message); }
    }
}