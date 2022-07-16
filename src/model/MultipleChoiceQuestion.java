package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import Exepction.BadInputException;
import Exepction.WrongQuestionException;



@SuppressWarnings("serial")
public class MultipleChoiceQuestion extends Question {

	public MultipleChoiceQuestion(String question, Set<Answer> answers)
			throws BadInputException, WrongQuestionException, FileNotFoundException, IOException {
		super(question, answers);
		if (question.equals(""))
			throw new BadInputException("Question cannot be empty");

	}

	public void updateAnswer2(String newAnswer, int answerIndex, boolean status)
			throws BadInputException, IndexOutOfBoundsException {
		if (!newAnswer.equals("") && newAnswer != null) {
			if (this.answers.size() >= answerIndex && answerIndex >= 0) {
				this.answers.elementAt(answerIndex).setText(newAnswer);
				this.answers.elementAt(answerIndex).setIsTrue(status);
			} else {
				throw new IndexOutOfBoundsException();
			}
		} else {
			throw new BadInputException();
		}
	}

	public boolean addPossibleAnswer(Answer answer) throws BadInputException {
		if (answers.contains(answer)) {
		//	System.out.println("Answer already exists: " + answer.getText());
			throw new BadInputException();
			//return false;
		}
		this.answers.add(answer);
		this.numOfAnswers++;
		return true;
	}

	public String findTheTruth(Vector<Answer> answerVector) {
		Iterator<Answer> answersIt = answerVector.iterator();
		while (answersIt.hasNext()) {
			if (answersIt.next().isTrue())
				return answersIt.next().getText();
		}
		return null;
	}

	public String toString() {
		StringBuffer bf = new StringBuffer();
		bf.append(" -------------\n");

		bf.append("Question ID: " + this.questionId + " " + this.question + "\n");
		bf.append("Answers:\n");

		for (int i = 0; i < this.answers.length(); ++i) {
			if (this.answers.get(i) != null) {
				bf.append("\n" + i + " " + this.answers.get(i).toString());
			}
		}
		bf.append("\n -------------");

		return bf.toString();
	}

	// The difference between this and the toString method, is that this one is
	// meant for the test, and not the user/developer - the output of this does not
	// contain the answers to the questions.

	public String exportQuestionWithoutAnswer() {
		int counter = 0;
		String export = "";
		export += question + "\n";
		export += "-------------- \n";
		// Questions
		for (int i = 0; i < this.answers.getNumberOfElement(); ++i) {
			export += counter + ". " + this.answers.get(i).getText() + "\n";
			// export += "\n";
			// export += "answer is: " + this.answers.get(i).isTrue();
			counter++;
		}
		return export;
	}

	public String exportQuestionWithAnswer() {
		int counter = 0;
		String export = "";
		export += question + "\n";
		export += "-------------- \n";
		// Questions
		for (int i = 0; i < this.answers.getNumberOfElement(); ++i) {
			export += counter + ". " + this.answers.get(i).getText() + " ";
			// export += "\n";
			export += "(" + this.answers.get(i).isTrue() + ") \n";
			counter++;
		}
		return export;

	}

	@Override
	public Question clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exportQuestion() {
		// TODO Auto-generated method stub
		return null;
	}

}
