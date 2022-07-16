package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

import Exepction.BadInputException;
import Exepction.WrongQuestionException;

public abstract class Question implements Serializable {

	private static int globalQuestionID = (setGlobalId());
	protected String question;
	protected int questionId;
	protected int numOfAnswers;
	protected Set<Answer> answers;

	public Question(String question, Set<Answer> answers2)
			throws BadInputException, WrongQuestionException, FileNotFoundException, IOException {
		if (!question.equals("") && answers2.size() != 0) {
			this.question = question;
			Set<Answer> copyAnswers = new Set<Answer>(answers2);
			if (checkingAnswersArray(answers2)) {
				this.answers = copyAnswers;
			}
			this.numOfAnswers = answers.capacity();
			this.questionId = ++globalQuestionID;

		} else {
			throw new BadInputException("Question cannot be empty");
		}
	}

	public static int getGlobalId() {
		return globalQuestionID;
	}

	public void addAnswer(String answer, boolean isTrue) throws BadInputException {
		this.addAnswer(new Answer(answer, isTrue));
	}

	public boolean contains(Answer answer) {
		if (this.answers.get(answer) != null)
			return true;
		else
			return false;
	}

	public boolean containsByString(String answer) {
		for (int i = 0; i < this.answers.size(); i++) {
			if (this.answers.get(i).getText().equals(answer))
				return true;
		}
		return false;

	}

	public void addAnswer(Answer newAnswer) throws BadInputException {
		if (this.answers.contains(newAnswer)) {
			throw new BadInputException("answer already exists \n");
		}
		this.answers.add(newAnswer);
	}

	public boolean setQuestion(String question) throws BadInputException {
		if (!question.equals("") && question != null) {
			this.question = question;
			return true;
		} else {
			throw new BadInputException("Question cannot be empty");
		}
	}

	public String getQuestion() {
		return this.question;
	}

	public int getQuestionId() {
		return this.questionId;
	}

	public abstract void updateAnswer2(String newAnswer, int answerIndex, boolean status)
			throws BadInputException, IndexOutOfBoundsException;

	public String toString() {

		String returnText = "-------- \n Question ID: (" + this.questionId + ")"+"\n " + this.question + "\n" + "Answers:\n";
		for (int i = 0; i < this.answers.length(); ++i) {
			returnText = returnText + this.answers.get(i).getText() + "\n-------- \n";
		}

		return returnText;
	}

	public Set<Answer> getAnswers() {
		return this.answers;
	}

	public abstract String exportQuestion();

	public boolean equals(Question q1) throws NullPointerException {
		if (q1 == null) {
			throw new NullPointerException();
		} else if (!q1.getQuestion().equals(this.question)) {
			return false;
		} else {
			for (int i = 0; i < this.answers.length(); ++i) {
				if (!this.answers.get(i).equals(q1.getAnswers().get(i))) {
					return false;
				}
			}

			return true;
		}
	}

	public boolean checkingAnswersArray(Set<Answer> answer3) throws BadInputException, WrongQuestionException {
		Set<Answer> copy = answer3;
		int index = 1;
		Answer first = answer3.firstElement();
		Answer last = answer3.lastElement();
		@SuppressWarnings("unused")
		Answer answersIt = null;
		if (answer3.isEmpty())
			throw new BadInputException("Empty answer(s)");
		else {
			if (this.getClass().getName() != "project.OpenQuestion") {

				if (first.getText().equals(last.getText()) && (answer3.getNumberOfElement() != 1)) {
					throw new BadInputException("Cant create this question. \n" + last.getText() + " is already exist");
				}
				for (int i = 0; i < answer3.length(); i++) {
					for (int j = 0; j < copy.length(); j++) {
						if (answer3.get(i) != null)
							if (answer3.get(i).getText() == copy.get(i).getText() && answer3.get(i) != copy.get(i))
								throw new BadInputException("Cant create this question. \n" + answer3.get(i).getText()
										+ " is already exist");
					}
				}
				return true;
			} else
				return true;
		}
	}

	public void deleteAnswer(int answerToRemove) throws WrongQuestionException {
		try {

			this.answers.deleteObject(answerToRemove);
		} catch (Exception e) {
			throw new WrongQuestionException();
		}

	}

	public static Answer[] removeElement(Answer[] original, int element) {
		Answer[] n = new Answer[original.length - 1];
		System.arraycopy(original, 0, n, 0, element);
		System.arraycopy(original, element + 1, n, element, original.length - element - 1);
		return n;
	}

	@SuppressWarnings("unused")
	public Set<Answer> reduceAnswersCount(int count) throws BadInputException {
		Set<Answer> myAnswerForTest = new Set<Answer>();
		if (this.answers.getNumberOfElement() > count) {
			Random random = new Random();
			Set<Answer> newAnswers = new Set<Answer>();
			myAnswerForTest = answers.getRandomElement(this.answers.getNumberOfElement());

			return myAnswerForTest;
		}
		return this.getAnswers();
	}

		public int getMyAnswerIndex(String text) {
		return this.answers.getMyIndex(text);
	}

	public static int setGlobalId() {
		try {
			ObjectInputStream inFile = new ObjectInputStream(new FileInputStream("binaryInformation\\questions.txt"));
			Vector<Question> myvector = (Vector<Question>) inFile.readObject();
			inFile.close();
			return myvector.lastElement().getQuestionId();
		} catch (ClassNotFoundException e) {
			return 0;
			// TODO Auto-generated catch block

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return 0;
		}

	}

}
