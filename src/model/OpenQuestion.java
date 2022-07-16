package model;

import java.io.FileNotFoundException;
import java.io.IOException;

import Exepction.BadInputException;

import Exepction.WrongQuestionException;



public class OpenQuestion extends Question {

	public OpenQuestion(String question, Set<Answer> answer)
			throws BadInputException, WrongQuestionException, FileNotFoundException, IOException {
		super(question, answer);

	}

	public String exportQuestionWithoutAnswer() {
		String export = "";
		export += question + "\n";
		export += "-------------\n ";
		// export += "Answer: "+answers.elementAt(0).getText();

		return export;
	}

	public String exportQuestionWithAnswer() {
		String export = "";
		export += question + "\n ---------";
		export += "\n Answer: " + answers.elementAt(0).getText() + " \n";
		return export;
	}

	@Override
	public void updateAnswer2(String newAnswer, int answerIndex, boolean status)
			throws BadInputException, IndexOutOfBoundsException {
		if (!newAnswer.equals("") && newAnswer != null) {
			if (this.answers.size() >= answerIndex && answerIndex >= 0) {
				this.answers.elementAt(answerIndex).setText(newAnswer);
				this.answers.elementAt(answerIndex).setIsTrue(true);
			} else {
				throw new IndexOutOfBoundsException();
				
			}
		} else {
			throw new BadInputException();
		}
	}

	@Override
	public String exportQuestion() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
