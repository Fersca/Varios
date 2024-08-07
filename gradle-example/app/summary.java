public class summary {

    public static final Integer id = 22;
    public static final String name = "Fernando";
    public static final Integer age = 42;
    public static final boolean adult = true;
    public static final String degrees = "Engineering";

    public static Object getMessage() {
        return name + " is " + age + " years old, holds a degree in " + degrees + ", and is " + (adult ? "an adult" : "not an adult") + ".";
    }

    public static void main(String[] args) {
        System.out.println(getMessage());
    }
}
