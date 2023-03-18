package Homework3.PasswordValidator.Exceptions;

import java.io.IOException;

public class WrongPasswordException extends IOException {

    public WrongPasswordException() {
    }

    public WrongPasswordException(String message) {
        super(message);
    }
}
