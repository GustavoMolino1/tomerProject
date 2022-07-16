package Exepction;

public class WrongQuestionTypeException extends Exception {

    public WrongQuestionTypeException() {
        super("The question type is incorrect");
    }
}
