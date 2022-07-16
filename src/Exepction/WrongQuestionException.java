package Exepction;

public class WrongQuestionException extends Exception {

    public WrongQuestionException() {
        super("The question number does not exist, it is either smaller than 0, or bigger than the last question");
    }
}
