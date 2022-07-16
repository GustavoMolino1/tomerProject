package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import Exepction.BadInputException;
import Exepction.CantRemoveAnswerException;
import Exepction.WrongQuestionException;
import Exepction.WrongQuestionTypeException;
import listeners.ExamEvenListenter;

public class Questioneer implements Serializable {
	public static Test myCurrentTest = null;
	private Vector<Question> questions;
	private Vector<ExamEvenListenter> listeners;

	public Questioneer() {
		this.questions = new Vector<Question>();
		this.listeners = new Vector<ExamEvenListenter>();

	}

	public void registerListener(ExamEvenListenter listener) {
		listeners.add(listener);
		try {
			importFromBinrayFile();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			sendMessageToUser(e.getMessage());
		}
	}

	public boolean createMultiQuestion(String answer, String question, boolean statusAnswer)
			throws BadInputException, WrongQuestionException, FileNotFoundException, IOException {
		Question qu = fetchByQuestion(question);
		Set<Answer> answers = new Set<Answer>();
		if ((qu == null) && (answer != "stop")) {
			answers.add(new Answer(answer, statusAnswer));
			MultipleChoiceQuestion mp = new MultipleChoiceQuestion(question, answers);
			this.addQuestion(mp);
			return true;

		}
		if (!answer.equals("stop")) {
			answers = qu.getAnswers();
			MultipleChoiceQuestion mp = ((MultipleChoiceQuestion) fetchByQuestion(question));
			Answer myAnswer = new Answer(answer, statusAnswer);
			if (mp.addPossibleAnswer(myAnswer)) {
				return true;
			} else {
				this.questions.remove(qu);
				sendMessageToUser(String.valueOf(new BadInputException("answer Already Exist! im deleting this question!").getMessage()));
				return true;

			}
		} else {
			if (qu != null) {
				answers = qu.getAnswers();
			}

			if (answers.getNumberOfElement() < 4) {
				this.questions.remove(qu);
				sendMessageToUser(String.valueOf(new BadInputException("answer Already Exist!").getMessage()));
			

			} else {
				sendMessageToUser("Added!");
				questionAddedSuccess(qu);
				getQuestionCountToUI();
			}

			return false;
		}

	}

	public void createOpenQuestion(String answer, String question)
			throws BadInputException, WrongQuestionException, FileNotFoundException, IOException {

		Set<Answer> answers = new Set<Answer>();
		if ((answer != "") && (question != "")) {
			answers.add(new Answer(answer, true));
			OpenQuestion openQ = new OpenQuestion(question, answers);
			this.addQuestion(openQ);
			questionAddedSuccess(openQ);
			getQuestionCountToUI();

		} else
			sendMessageToUser(String.valueOf(new BadInputException("Bad input!").getMessage()));
		return;
	}

	public void questionAddedSuccess(Question q1) {
		for (ExamEvenListenter examEvenListenter : listeners) {
			examEvenListenter.sendQuestionToUI(this.toString());
		}
	}

	public String getAllQuestionDisplay() {
		return this.toString();
	}

	public void sendMessageToUser(String e) {
		for (ExamEvenListenter examEvenListenter : listeners) {
			examEvenListenter.iGotAnErrorMessage(e);
		}

	}

	public static boolean doesAnswerExist(Set<Answer> answers, String answer) throws BadInputException {
		if (answer == null)
			throw new BadInputException("Empty answer");
		for (int i = 0; i < answers.size(); i++) {
			if (answers.get(i) != null) {
				if (answers.get(i).getText().equals(answer))
					return true;
			}
		}
		return false;
	}

	public static boolean containsForTest(Question qu, Vector<Question> myQuestions) {
		if (myQuestions.isEmpty())
			return false;
		for (Question Myquestion : myQuestions) {
			if (Myquestion.getQuestion() == qu.getQuestion())
				return true;
		}
		return false;
	}

	

	public Vector<Question> createTestManually2(Vector<Question> myquestions, int questionNumber, String subject,
			String name, int[] array, boolean isFinished, String text) throws FileNotFoundException, BadInputException,
			WrongQuestionException, IOException, ClassNotFoundException {
		Vector<Question> questionForTest = null;

		if (myquestions != null) {
			questionForTest = new Vector<Question>(myquestions);
		} else {
			questionForTest = new Vector<Question>();
		}
		Question qu = fetchByIdForTest(text);
		Vector<Question> copy = myquestions;

		if (myquestions.contains(qu)) {
			sendMessageToUser("Error, Question Already exist!");
		} else {

			Set<Answer> forQustionAnswer = new Set<Answer>();
			Question fortestQuestion = null;
			int counter = 0;
			for (int i = 0; i < array.length; i++) {
				if (array[i] != -1)
					counter++;
			}
			bubbleSort(array);

			if (qu instanceof OpenQuestion) {
				questionForTest.add(qu);
				sendMessageToUser("Your Added successfully to the test");
			} else {
				if (counter < 4) {
					sendMessageToUser("Please select at least 4 Answers!");
				
				} else {

					for (int i = 0; i < array.length; i++) {
						if (qu instanceof MultipleChoiceQuestion)
							if (array[i] != -1) {

								if (qu != null) {
									forQustionAnswer.add(new Answer(qu.getAnswers().elementAt(array[i]).getText(),
											qu.getAnswers().elementAt(array[i]).isTrue()));
								}
							}
					}
					fortestQuestion = new MultipleChoiceQuestion(qu.getQuestion(), forQustionAnswer);
					sendMessageToUser("Your Added successfully to the test");
				}

				questionForTest.add(fortestQuestion);
			}
		}
		return questionForTest;

	}

	public void updateQuestion(int questionNum, String questionText) throws WrongQuestionException, BadInputException {
		if (questionText == "") {
			sendMessageToUser(String.valueOf(new BadInputException().getMessage()));
			return;
		}
		if (questionNum > Question.getGlobalId()) {
			sendMessageToUser(String.valueOf(new BadInputException().getMessage()));
			return;
		}
		if (questionNum >= this.getQuestionsCount() || questionNum < 0) {
			sendMessageToUser(String.valueOf(new BadInputException().getMessage()));
			return;
		}
		if (!this.containsByString(questionText)) {
			sendMessageToUser(
					"Changing: " + this.questions.elementAt(questionNum).getQuestion() + " To: " + questionText);
			this.questions.elementAt(questionNum).setQuestion(questionText);

			updateView(this.questions.elementAt(questionNum));
		} else
			sendMessageToUser(String.valueOf(new BadInputException("This Question is alreay Exist!").getMessage()));
		return;
		// throw new BadInputException("This Question is alreay Exist!");
	}

	public void updateView(Question text) {
		for (ExamEvenListenter examEvenListenter : listeners) {
			examEvenListenter.updateMenu(text.getQuestion());
		}
	}

	public void updateAnswerMain(int questionNum, int answerIndex, String answerText, boolean status)
			throws BadInputException, WrongQuestionException {
		if (answerText == null || answerText == "") {
			// throw new BadInputException("Answer cannot be an empty string");
			sendMessageToUser(String.valueOf(new BadInputException("Answer cannot be an empty string").getMessage()));
			return;
		}

		if (questionNum >= this.getQuestionsCount() || questionNum < 0) {
			sendMessageToUser(String.valueOf(new BadInputException("Question doesnt Exist").getMessage()));
			return;
		}
		if (answerIndex == -1) {
			sendMessageToUser(String.valueOf(new BadInputException("Select Answer").getMessage()));
			return;

		}

		Question qu = this.questions.elementAt(questionNum);
		if (qu instanceof MultipleChoiceQuestion) {
			if (doesAnswerExist(this.questions.elementAt(questionNum).getAnswers(), answerText)) {
				sendMessageToUser(String.valueOf(
						new BadInputException("This Answer already Exist. \n didnt save the change").getMessage()));
				return;
				// throw new BadInputException("This Answer already Exist. \n didnt save the
				// change");
			} else {
				this.questions.elementAt(questionNum).updateAnswer2(answerText, answerIndex, status);
				updateView(this.questions.elementAt(questionNum));
				return;
			}

		} else {
			if (!doesQuestionExist(answerText)) {
				this.questions.elementAt(questionNum).updateAnswer2(answerText, answerIndex, status);
				updateView(this.questions.elementAt(questionNum));
				return;
			} else
				sendMessageToUser(
						String.valueOf(new BadInputException("already Exist. \n didnt save the change").getMessage()));

		}
	}

	public void addAnswer(int questionId, String answer, boolean isTrue)
			throws BadInputException, WrongQuestionException {
		if (answer.equals("") || answer == null) {
			sendMessageToUser(String.valueOf(new BadInputException("Answer cannot be an empty string").getMessage()));
			return;
		}
		for (int i = 0; i < questions.capacity(); i++) {
			if (questions.elementAt(i).getQuestionId() == questionId) {
				if (!(questions.elementAt(i) instanceof OpenQuestion))
					try {
						questions.elementAt(i).addAnswer(answer, isTrue);
					} catch (Exception e) {
						sendMessageToUser(String.valueOf(new BadInputException("Answer Already Exist").getMessage()));
					}
				else
					sendMessageToUser(String.valueOf(new BadInputException("Cant Add to open question").getMessage()));
				return;
			}
		}

		// If we have reached this far, the user has entered an input which does not
		// correspond to any id
		throw new WrongQuestionException();
	}

	public void removeAnswer(int questionNum, int answerNum)
			throws CantRemoveAnswerException, WrongQuestionTypeException, WrongQuestionException, BadInputException {
		if (questionNum >= this.getQuestionsCount() || questionNum < 0)
			throw new BadInputException("Question doesnt Exist");
		Question que = this.questions.elementAt(questionNum);

		if (que instanceof MultipleChoiceQuestion) {
			MultipleChoiceQuestion myQuestion = (MultipleChoiceQuestion) que;
			if (myQuestion.getAnswers().getNumberOfElement() <= 4) {
				// throw new CantRemoveAnswerException();
				sendMessageToUser(String
						.valueOf(new CantRemoveAnswerException("Cant delete answer when u got less than 4 answers.")
								.getMessage()));
				return;

			}
			myQuestion.deleteAnswer(answerNum);
			return;
		} else
			sendMessageToUser(String
					.valueOf(new CantRemoveAnswerException("You cant delete an answer that belongs to open question")
							.getMessage()));
		return;

	}

	public void addQuestion(Question myQuestion) throws BadInputException {
		if (doesQuestionExist(myQuestion.getQuestion())) {
		//	sendMessageToUser(
		//			String.valueOf(new BadInputException("Bad input!, Question is already exist! Deleting.").getMessage()));
			throw new BadInputException();
		} else {
		//	sendMessageToUser("Success adding Question"); // here to delete
		}
		this.questions.add(myQuestion);

	}

	public int getQuestionsCount() {

		return this.questions.capacity();
	}

	public void getQuestionCountToUI() {
		for (ExamEvenListenter examEvenListenter : listeners) {
			examEvenListenter.getNumberOfQuestionFromModel(this.getCurrectCapacity());

		}
	}

	public int getCurrectCapacity() {
		int count = 0;
		for (Question myquestion : questions) {
			if (myquestion != null)
				count++;
		}
		return count;
	}

	public boolean containsByString(String question) {
		Iterator<Question> questionIt = this.questions.iterator();
		while (questionIt.hasNext()) {
			if (questionIt.next().getQuestion().equals(question))
				return true;
		}
		return false;
	}

	public void loadQuestion() {
		try {
			importFromBinrayFile();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			sendMessageToUser(e.getMessage());
		}
	}

	public boolean doesQuestionExist(String question) throws BadInputException {
		if (question == null)
			throw new BadInputException("Empty question");
		return containsByString(question);
	}

	public void display() {
		int multiCounter = 0, openCounter = 0;
		for (Question q1 : this.questions) {
			multiCounter = (q1 instanceof MultipleChoiceQuestion) ? +1 : +0;
			openCounter = (q1 instanceof OpenQuestion) ? +1 : +0;
		}
		System.out.println("You got: " + multiCounter + " Multiple Choice Question(s) \n" + "You got: " + openCounter
				+ " Open Question(s) \n");
		System.out.println(this.toString());
	}

	public Vector<Question> getQuestions() {
		return questions;
	}

	public String generateTestAutomatically(String subject, String name, int amount) throws BadInputException {
		if (subject == "" || name == "") {
			sendMessageToUser(String.valueOf(new BadInputException("Please Fill all the empty Field(s)").getMessage()));
			return null;
		}

		if (amount <= this.questions.size() && amount >= 0) {
			if (amount == this.questions.size()) {
				return this.generateTest(subject, name, this.questions);
			} else {
				Collections.shuffle(questions);
				Vector<Question> questionForTest = new Vector<Question>();
				for (int i = 0; i < amount; i++) {
					questionForTest.add(this.questions.elementAt(i));
				}
							return this.generateTest(subject, name, questionForTest);
			}
		} else {
			sendMessageToUser(String.valueOf(new BadInputException("You didnt choiced any questions..").getMessage()));

			throw new BadInputException("You cannot choose less than 0 questions, or more than the amount available");
		}
	}

	public int howMuchTrue(Question questionForTest) {
		int counter = 0;
		for (int i = 0; i < questionForTest.getAnswers().getNumberOfElement(); i++) {
			if (questionForTest.getAnswers().get(i) != null)
				if (questionForTest.getAnswers().get(i).isTrue())
					counter++;
		}
		return counter;
	}

	public Set<Answer> shufflyMyArray(Set<Answer> values) throws BadInputException {
		int size = values.size();
		Answer[] shufflyArray = new Answer[size];
		List<Answer> myListToShuffly = Arrays.asList(values.getMyValues());
		Collections.shuffle(myListToShuffly);
		myListToShuffly.toArray(shufflyArray);
		shufflyArray = Arrays.copyOfRange(shufflyArray, 0, 4);
		Set<Answer> newValues = new Set<Answer>(values);
		newValues.setArray(shufflyArray);
		return newValues;

	}

	public String generateTest(String subject, String name, Vector<Question> receivedQuestions) {

		Test myTest = new Test(name, subject, null);
		if (subject == "" || name == "") {
			sendMessageToUser("Please fill in all the missing details.");
			return "";
		}
		if (receivedQuestions == null) {
			sendMessageToUser("Please fill in all the missing details.");
			return "";
		}

		Vector<Question> rawQuestions = receivedQuestions;
		Vector<Question> questionsForTestVector = new Vector<Question>();
		Set<Answer> answerForTest = null;
		Question questionForTest = null;
		questionsForTestVector.setSize(rawQuestions.size());
		try { // Forced by the language to use it, because the constructor of MultipleChoice
				// has a "throw exception" expression

			for (Question questionTest : rawQuestions) {

				Question newQuestion = questionTest;
				if (newQuestion instanceof MultipleChoiceQuestion) {
					answerForTest = newQuestion.reduceAnswersCount(4);
					questionForTest = new MultipleChoiceQuestion(newQuestion.getQuestion(), answerForTest);

					int trueAnswersCount = howMuchTrue(questionForTest);

					// Add "more than 1 correct answer"
					if (trueAnswersCount >= 2) {
						// Mark all other answers as false, since this is the correct one

						for (int k = 0; k < questionForTest.getAnswers().getNumberOfElement(); k++) {
							if (questionForTest.getAnswers().elementAt(k) != null)
								questionForTest.getAnswers().elementAt(k).setIsTrue(false);
						}
						questionForTest.addAnswer(new Answer("More than 1 correct answer", true));
						questionForTest.addAnswer(new Answer("None of the answers is correct", false));
					} else {
						questionForTest.addAnswer(new Answer("More than 1 correct answer", false));

						// Add "no correct answers"
						if (trueAnswersCount == 0) {
							questionForTest.addAnswer(new Answer("None of the answers is correct", true));
						} else {
							questionForTest.addAnswer(new Answer("None of the answers is correct", false));
						}
					}
				} else
					questionForTest = questionTest;
				questionsForTestVector.add(questionForTest);
			}
			myTest.setQuestions(questionsForTestVector);
			Questioneer.myCurrentTest = myTest;
			sendMessageToUser(name + " Your test is ready!");
			return myTest.toString();
		} catch (Exception ex) {
			sendMessageToUser("Error trying to do it, try again mate");
			return null;
		}
	}

	public String cloneMyTest(String name, String subject) throws Exception {
		try {
			Test cloneTest = new Test(name, subject, GetMyTest().getMyQuestion());
			Questioneer.myCurrentTest = cloneTest;
			sendMessageToUser(name + "your clone test with a new name/subject is ready!");
			return cloneTest.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block

			sendMessageToUser("No previous test available");
		}
		return null;
	}

	public Test GetMyTest() throws Exception {
		if (Questioneer.myCurrentTest == null)
			throw new Exception("No previous test available");

		return Questioneer.myCurrentTest;
	}

	public boolean get4Answers(int indexOfAnswer, Question qu) throws BadInputException {
		Set<Answer> myAnswerForTest = new Set<Answer>();
		if (indexOfAnswer < 0 || indexOfAnswer > qu.getAnswers().getNumberOfElement()) {
			throw new BadInputException("Answer Doesnt exist");
		}
		if (indexOfAnswer != -1) {
			if (myAnswerForTest.isEmpty()) {
				myAnswerForTest.add(qu.getAnswers().elementAt(indexOfAnswer));
				return true;
			} else if (!myAnswerForTest.contains(qu.getAnswers().elementAt(indexOfAnswer))) {
				myAnswerForTest.add(qu.getAnswers().elementAt(indexOfAnswer));
			}

		}
		return false;

	}

	public int counterQuestion(Vector<Question> checkingVecotr) {
		int counter = 0;
		for (Question question : questions) {
			if (question != null)
				counter++;
		}
		return counter;

	}

	public Question fetchByQuestion(String question) {
	
		for (Question question2 : this.questions) {
			if (question2.getQuestion() == question)
				return question2;
		}
		return null;
	}

	public Question fetchById(int id) {
		Iterator<Question> questionIt = this.questions.iterator();
		while (questionIt.hasNext()) {
			if (id != 0) {
				if (questionIt.next().getQuestionId() == (id - 1))
					return questionIt.next();
			} else {
				if (questionIt.next().getQuestionId() == 0)
					return questionIt.next();
			}
		}
		return null;
	}

	public Question fetchByIdForTest(String id) {

		for (Question myquestions : this.questions) {
			if (myquestions.getQuestion() == id)
				return myquestions;
		}
		return null;
	}

	public Question fetchQuestionNew(int id) {
		return this.questions.get(id);
	}

	public void fetchById2(int id) {
		updateView(this.questions.get(id));

	}

	public String fetchStringQuestion(int id) {
		return this.questions.get(id).getQuestion();
	}

	public Question sendToUIQuestion(Question q1) {
		for (ExamEvenListenter examEvenListenter : listeners) {
			examEvenListenter.getQuestionByIDFromModel(q1);
		}
		return null;
	}

	public Vector<Question> eliminateNulls(Vector<Question> questionsToCleanse) {
		Vector<Question> newQuestions = new Vector<Question>();
		newQuestions.setSize(questionsToCleanse.size());
		newQuestions = new Vector<Question>(this.questions.subList(0, this.questions.size()));

		return newQuestions;

	}

	public void saveToBinrayFile() {
		try {
			ObjectOutputStream outFile = new ObjectOutputStream(
					new FileOutputStream("binaryInformation\\questions.txt"));
			outFile.writeObject(questions);
			outFile.close();
			sendMessageToUser(String.valueOf("All the Data saved you can exit the program"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			sendMessageToUser(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void importFromBinrayFile() throws ClassNotFoundException {
		try {
			ObjectInputStream inFile = new ObjectInputStream(new FileInputStream("binaryInformation\\questions.txt"));
			// this.questions = ( Vector<Question>) inFile.readObject();
			this.questions = new Vector<Question>((Vector<Question>) inFile.readObject());

			inFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			sendMessageToUser(e.getMessage());
		}
	}

	public String toString() {
		String returnString = "";
		Iterator<Question> questionIt = questions.iterator();
		while (questionIt.hasNext()) {
			returnString += "\n" + questionIt.next().toString() + "\n";
		}
		return returnString;
	}

	public void bubbleSort(int[] arr) {
		int n = arr.length;
		int temp = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 1; j < (n - i); j++) {
				if (arr[j - 1] > arr[j]) {
					// swap elements
					temp = arr[j - 1];
					arr[j - 1] = arr[j];
					arr[j] = temp;
				}

			}
		}
	}
}
