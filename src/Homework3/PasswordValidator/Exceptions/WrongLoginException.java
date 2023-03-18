package Homework3.PasswordValidator.Exceptions;

import java.io.IOException;

public class WrongLoginException extends IOException {
    public WrongLoginException() {
    }

    public WrongLoginException(String message) {
        super(message);
    }
}
