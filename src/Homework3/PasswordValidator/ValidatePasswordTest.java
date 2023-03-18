package Homework3.PasswordValidator;

public class ValidatePasswordTest {
    public static void main(String[] args) {
        System.out.println(PasswordValidator.validate("Иван123","собака","кошка"));
        System.out.println(PasswordValidator.validate("Ivan123123123123123112123","собака","кошка"));
        System.out.println(PasswordValidator.validate("Ivan12312","собака","кошка"));
        System.out.println(PasswordValidator.validate("Ivan12312","sobakи","sobakи"));
        System.out.println(PasswordValidator.validate("Ivan12312","sobaka","sobaka_"));
        System.out.println(PasswordValidator.validate("Ivan12312","sobaka_1","sobaka_1"));

    }
}
