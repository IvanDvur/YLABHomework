package Homework2.ComplexNumbers;

public class ComplexNumbersTest {
    public static void main(String[] args) {
        ComplexNumber num1 = new ComplexNumber(1.0,7.5);
        ComplexNumber num2 = new ComplexNumber(17.2,10);
        System.out.println(num1.sum(num2));
        System.out.println(num1.subtract(num2));
        System.out.println(num1.multiply(num2));
        System.out.println(num1.abs());
        System.out.println(num1);
    }
}
