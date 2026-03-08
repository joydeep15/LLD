package creational;


/*
Use builder when a class has many optional and/or mandatory fields.
It prevents the telescopic constructor anti-pattern.
It’s useful for making objects immutable.
When you want better readability and maintainability in object creation.
Separates in mandatory and non mandatory constructor params

Eg
HttpClient, ObjectMapper

*
*
* */
class User{
    private final String name;
    private final String phoneNumber;
    private final String email;
    private final String address;
    private final int empId;
    private User(UserBuilder u){
        this.name = u.name;
        this.phoneNumber  = u.phoneNumber;
        this.email = u.email;
        this.address = u.address;
        this.empId = u.empId;
    }

    @Override
    public String toString() {
       return String.format("name: %s\nphoneNumber: %s\nemail: %s\naddress: %s\nempId: %d ",
               name, phoneNumber, email, address, empId);
    }

    /*
    * Why is inner class static?
    * If members are not static then to access this we need an object of user
    * Because the builder is responsible for creating an instance of the outer class.
    * It doesn’t operate on an existing instance. Making it static removes the implicit reference to the enclosing object,
    *  avoids unnecessary coupling, and supports cleaner and more memory-efficient design.
    *
    * Why is it an inner class?
    * The builder is an inner class because it's closely tied to the outer class it's constructing.
    * Keeping it inside improves encapsulation and gives it direct access to private fields of the outer class, which supports immutability and avoids exposing unnecessary APIs.
    * It also prevents misuse and keeps the code clean and modular.
    *
    * Reason	Explanation
🧩 Tight cohesion	The builder is specific to its enclosing class (e.g., UserBuilder only builds User),
* so grouping them together makes the relationship clear and prevents misuse.
🔐 Access to private fields	As a nested class, the builder can access private fields or constructors of the outer
* class without needing setters or making them public. This supports immutability.
📦 Encapsulation	Keeping the builder inside the class hides the construction logic from the outside world,
*  exposing only a clean, fluent API.
📖 Improved readability and maintainability	You can keep the object and its builder in one file.
* Developers immediately see how to create the object.

    *
    * */
    public static class UserBuilder{
        private final String name;
        private String phoneNumber;
        private String email;
        private String address;
        private int empId;

        UserBuilder(String name){
            this.name = name;
        }
        public UserBuilder withPhoneNumber(String ph){
            this.phoneNumber = ph;
            return this;
        }
        public UserBuilder withEmail(String email){
            this.email = email;
            return this;
        }
        public UserBuilder withEmpId(int empId){
            this.empId = empId;
            return this;
        }

        public UserBuilder withAddress(String address){
            this.address = address;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}

public class Builder {
    public static void main(String[] args) {
        User.UserBuilder a = new User.UserBuilder("Joydeep").withPhoneNumber("123456")
                .withAddress("kolkata")
                .withEmail("j@h.com")
                .withEmpId(69);
        System.out.println(a.build());

    }
}
