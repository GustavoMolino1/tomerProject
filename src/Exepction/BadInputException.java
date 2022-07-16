package Exepction;

public class BadInputException extends Exception {

    public BadInputException() {
        super("Bad input - you either entered an empty text, or an impossible number (such as -1)");
    }

    public BadInputException(String errorText) {
        super(errorText);
    }

}