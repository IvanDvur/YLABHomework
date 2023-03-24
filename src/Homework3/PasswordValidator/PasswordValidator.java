package Homework3.PasswordValidator;

import Homework3.PasswordValidator.Exceptions.WrongLoginException;
import Homework3.PasswordValidator.Exceptions.WrongPasswordException;

public class PasswordValidator {

    public static boolean validate(String login, String password, String confirmPassword) {
        return validateLogin(login) && validatePassword(password, confirmPassword);
    }

    private static boolean validateLogin(String login) {
        try {
            if (!login.matches("\\w+")) {
                throw new WrongLoginException("Логин содержит недопустимые символы");
            } else if (login.length() >= 20) {
                throw new WrongLoginException("Логин слишком длинный(>19 символов)");
            }
        } catch (WrongLoginException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private static boolean validatePassword(String password, String confirmPassword) {
        try {
            if (!password.matches("\\w+")) {
                throw new WrongPasswordException("Пароль содержит недопустимые символы");
            } else if (password.length() >= 20) {
                throw new WrongPasswordException("Пароль слишком длинный(>19 символов)");
            } else if (!password.equals(confirmPassword)) {
                throw new WrongPasswordException("Пароль и подтверждение не совпадают");
            }
        } catch (WrongPasswordException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
