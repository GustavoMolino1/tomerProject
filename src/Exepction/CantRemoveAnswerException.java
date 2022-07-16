package Exepction;

public class CantRemoveAnswerException extends Exception {

    public CantRemoveAnswerException() {
        super("You can't remove an answer now because the question has less than 4 total answers");
    }
    public CantRemoveAnswerException(String errorText) {
        super(errorText);
    }
    
}
