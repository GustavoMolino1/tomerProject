package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import Exepction.BadInputException;

public class Test {

	String name;
	String subject;

	Vector<Question> question;

	public Test(String name, String subject, Vector<Question> questions) {
		this.name = name;
		this.subject = subject;
		this.question = questions;
	}

	public Test clone() {
		if (this.name == null || this.subject == null || this.question.isEmpty()) {
			return null;
		} else {
			String testName = this.name;
			String mySubject = this.subject;
			Vector<Question> myQuestions = new Vector<Question>(this.question);
			return new Test(testName, mySubject, myQuestions);
		}
	}

	public boolean setQuestions(Vector<Question> questions) throws FileNotFoundException {
		this.question = new Vector<Question>(questions);
		return true;
	}
	public Vector<Question> getMyQuestion()
	{
		return this.question;
	}
	

	public String toString() {
		int questionCounter = 1;
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH");
		String formatDateTime = now.format(format);
		String testWithAnswers = "";
		testWithAnswers += "------------------------ \n";
		testWithAnswers += "Subject: " + subject + "\n";
		testWithAnswers += "Test name: " + name + "\n";
		testWithAnswers += "Date: " + formatDateTime + "\n"; // Need to a find a more
		testWithAnswers += "------------------------ \n\n"; // beautiful way to format

		String test = "";
		test += "------------------------ \n";
		test += "Subject: " + subject + "\n";
		test += "Test name: " + name + "\n";

		test += "Date: " + formatDateTime + "\n"; // Need to a find a more
		test += "------------------------ \n\n"; // beautiful way to format
		MultipleChoiceQuestion myques = null;
		OpenQuestion myquest2 = null;
		for (int i = 0; i < question.capacity(); i++) {
			if (question.elementAt(i) != null)
				if (question.elementAt(i) instanceof MultipleChoiceQuestion) {
					myques = (MultipleChoiceQuestion) question.elementAt(i);
					test += "Question Number:(" + (questionCounter) + ") \n" + myques.exportQuestionWithoutAnswer()
							+ "\n\n";
					testWithAnswers += "Question Number:(" + (questionCounter) + ") \n"
							+ myques.exportQuestionWithAnswer() + "\n\n";
					questionCounter++;
				} else {
					myquest2 = (OpenQuestion) question.elementAt(i);
					test += "Question Number:(" + (questionCounter) + ") \n" + myquest2.exportQuestionWithoutAnswer()
							+ "\n\n";

					testWithAnswers += "Question Number:(" + (questionCounter) + ") \n"
							+ myquest2.exportQuestionWithAnswer() + "\n\n";

					questionCounter++;
				}

		}
		test += "Good luck!";
		PrintWriter pwWithoutAnswer = null;
		PrintWriter pwWithAnswer = null;

		try {
			pwWithoutAnswer = new PrintWriter(new File("testList\\exam" + formatDateTime + ".txt"));
			pwWithAnswer = new PrintWriter(new File("testList\\solution " + formatDateTime + ".txt"));
			pwWithoutAnswer.print(test);
			pwWithAnswer.print(testWithAnswers);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pwWithoutAnswer.close();
		pwWithAnswer.close();
		
		return testWithAnswers;

	}

	public static int comparing(String s1, String s2) {
		int len1 = s1.length();
		int len2 = s2.length();
		int limit = Math.min(len1, len2);
		char[] v1 = s1.toCharArray();
		char[] v2 = s2.toCharArray();
		int i = 0;
		while (i < limit) {

			char ch = Character.toLowerCase(v1[i]);
			char ch2 = Character.toLowerCase(v2[i]);
			if (ch != ch2) {
				return ch - ch2;
			}
			i++;
		}
		return len1 - len2;
	}

	public static Question[] sortArrayQuestion(Question[] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j + i < ((arr.length - 1)); j++) {
				boolean sorted = false;
				for (int k = 0; !sorted && k < arr.length; k++) {

					// System.out.println("Checking " + arr[j] + " to " + " " + arr[j + 1]);
					if (comparing(arr[j].getQuestion(), arr[j + 1].getQuestion()) <= 0) {
						sorted = true;
						continue;
					}
					if (comparing(arr[j].getQuestion(), arr[j + 1].getQuestion()) > 0) {
						sorted = true;
						Question temp = arr[j];
						arr[j] = arr[j + 1];
						arr[j + 1] = temp;
					}
				}
			}
		}
		return arr;
	}

	public static Vector<Question> sortArrayQuestion21(Vector<Question> arr) throws BadInputException {
		for (int i = 0; i < arr.size(); i++) {
			for (int j = 0; j + i < ((arr.size() - 1)); j++) {
				boolean sorted = false;
				for (int k = 0; !sorted && k < arr.size(); k++) {

					// System.out.println("Checking " + arr[j] + " to " + " " + arr[j + 1]);
					if (comparing(arr.elementAt(j).getQuestion(), arr.elementAt(j + 1).getQuestion()) <= 0) {
						sorted = true;
						continue;
					}
					if (comparing(arr.elementAt(j).getQuestion(), arr.elementAt(j + 1).getQuestion()) > 0) {
						sorted = true;
						Question temp = arr.elementAt(j);
						arr.elementAt(j).setQuestion(arr.elementAt(j + 1).getQuestion());
						arr.elementAt(j + 1).setQuestion(temp.getQuestion());
					}
				}
			}
		}
		return arr;
	}

}
