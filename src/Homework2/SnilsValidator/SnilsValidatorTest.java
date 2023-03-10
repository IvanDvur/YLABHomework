package Homework2.SnilsValidator;

public class SnilsValidatorTest {
    public static void main(String[] args) {
        SnilsValidator sv = new SnilsValidatorImpl();
        System.out.println( sv.validate("1234567890!")); //false + сообщение о некорректном вводе
        System.out.println(sv.validate("12345")); // false + сообщение о некорректной длине
        System.out.println(sv.validate("01468870570")); //false
        System.out.println(sv.validate("90114404441")); //true
        System.out.println(sv.validate("15024126709")); //true

    }
}
