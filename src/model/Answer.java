package model;

import java.io.Serializable;


public class Answer implements Serializable {


	private String text;
    private Boolean isTrue;

    public Answer(String text, Boolean isTrue) {
        this.text = text;
        this.isTrue = isTrue;
    }

    public String getText() {
        return text;
    }

    public boolean isTrue() {
        return this.isTrue;
    }

    public boolean setIsTrue(boolean isTrue) {
        this.isTrue = isTrue;
        return true;
    }

    public boolean equals(Answer answer) throws NullPointerException {
        if (answer == null)
            throw new NullPointerException();

        if (!this.text.equals(answer.getText()))
            return false;

        return this.isTrue == answer.isTrue();

    }

    public boolean setText(String newAnswer) {
        this.text = newAnswer;
        return true;

    }

    public Answer clone() {
        return new Answer(text, isTrue);
    }

    @Override
    public String toString() {
        StringBuffer bf = new StringBuffer();
        bf.append(text + " ---> " + isTrue);
        return bf.toString();

    }

}
