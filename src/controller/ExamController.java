package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import Exepction.BadInputException;
import Exepction.CantRemoveAnswerException;
import Exepction.WrongQuestionException;
import Exepction.WrongQuestionTypeException;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import listeners.ExamEvenListenter;
import listeners.ExamUIEvenListener;
import model.Answer;
import model.Question;
import model.Questioneer;
import view.AbstractExamView;
import model.Set;

public class ExamController implements ExamEvenListenter, ExamUIEvenListener {

	private Questioneer model;
	private AbstractExamView view;
	public static Vector<Question> forTest = new Vector<Question>();

	public ExamController(Questioneer manger, AbstractExamView view) {
		this.model = manger;
		this.view = view;

		this.model.registerListener(this);
		this.view.registerListener(this);

		 this.model.loadQuestion();
		this.model.getQuestionCountToUI();

	}

	@Override
	public void toDisplayFromModel(Question q1) {
		this.view.showQuestionTOUI(q1);

	}

	@Override
	public void uploadQuestionData() {
		uploadQuestionDataFromModel();
	}

	@Override
	public void uploadQuestionDataFromModel() {
		try {
			this.model.importFromBinrayFile();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			iGotAnErrorMessage(e.getMessage());
		}

	}

	@Override
	public void addOpenQuestionFromUI(String answer, String question)
			throws FileNotFoundException, BadInputException, WrongQuestionException, IOException {
		this.model.createOpenQuestion(answer, question);

	}

	@Override
	public void getNumberOfQuestionFromModel(int size) {
		// TODO Auto-generated method stub
		this.view.getNumberOfQuestion(size);
	}

	@Override
	public void sendQuestionToUI(String q1) {
		this.view.newQuestionAddedView(q1);
	}


	@Override
	public String getAllQuestionFromModel() {
		return this.model.getAllQuestionDisplay();

	}

	@Override
	public void iGotAnErrorMessage(String msg) {
		this.view.examErrorMessage(msg);

	}

	@Override
	public void getQuestionByIDFromUI(int index) {
		this.model.fetchById2(index);

	}

	@Override
	public void getQuestionByIDFromModel(Question q1) {
		this.view.showQuestionTOUI(q1);

	}

	@Override
	public void editQuestion(Integer index, String newQuestion) {
		try {
			this.model.updateQuestion(index, newQuestion);

		} catch (WrongQuestionException | BadInputException e) {
			// TODO Auto-generated catch block
			iGotAnErrorMessage(e.getMessage());
		}

	}

	@Override
	public void updateMenu(String newtext) {
		this.view.updateQuestionText(newtext);

	}

	@Override
	public Set<Answer> getAnserwsPerQuestionFromUI(int index) {
		return this.model.fetchQuestionNew(index).getAnswers();

	}

	@Override
	public void updateAnswerText(int id, String newAnswer, String lastAnswer) {
		// this.model.fetchQuestionNew(id).getMyAnswerIndex(newAnswer);
		int idToUpdate = this.model.fetchQuestionNew(id).getMyAnswerIndex(lastAnswer);
		try {
			this.model.updateAnswerMain(id, idToUpdate, newAnswer, false);
		} catch (BadInputException | WrongQuestionException e) {
			// TODO Auto-generated catch block
			iGotAnErrorMessage(e.getMessage());
		}

	}

	@Override
	public void addNewAnswerText(int questionId, String answer, boolean isTrue) {
		try {
			this.model.addAnswer(questionId, answer, isTrue);
		} catch (BadInputException | WrongQuestionException e) {
			// TODO Auto-generated catch block
			iGotAnErrorMessage(e.getMessage());
		}

	}

	@Override
	public void removeAnswer(int questionNum, String answerText) {
		try {
			int idToUpdate = this.model.fetchQuestionNew(questionNum).getMyAnswerIndex(answerText);

			this.model.removeAnswer(questionNum, idToUpdate);
		} catch (CantRemoveAnswerException | WrongQuestionTypeException | WrongQuestionException
				| BadInputException e) {
			// TODO Auto-generated catch block
		iGotAnErrorMessage(e.getMessage());
		}

	}

	@Override
	public void addMultiQuestion(String question, TextField[] tF, CheckBox[] cB) { // this function is like a console
																					// Method
		int totalTimes = tF.length;
		for (int i = 0; i < totalTimes; i++) {
			try {
				this.model.createMultiQuestion(tF[i].getText(), question, cB[i].isSelected());
			} catch (BadInputException | WrongQuestionException | IOException e) {
				// TODO Auto-generated catch block
				iGotAnErrorMessage("Not a valid Question.");
				return;
			}
		}
		try {
			this.model.createMultiQuestion("stop", question, false);
		} catch (BadInputException | WrongQuestionException | IOException e) {
			// TODO Auto-generated catch block
			iGotAnErrorMessage(e.getMessage());
		}

	}

	@Override
	public void saveData() {
		this.model.saveToBinrayFile();

	}

	@Override
	public String genreateAutoTest(String sub, int size, String name) {
		String text = null;
		try {
			text = this.model.generateTestAutomatically(sub, name, size);
			setTestScrollPane(text);

		} catch (BadInputException e) {
			// TODO Auto-generated catch block
			iGotAnErrorMessage(e.getMessage());
		}
		return null;
	}

	@Override
	public void setTestScrollPane(String allTest) {

		this.view.setTest(allTest);
	}

	@Override
	public String getQuestionByIDString(int index) {
		return this.model.fetchStringQuestion(index);

	}

	@Override
	public void addQuestionManully(int index, int[] array, String text, String name, String subject) {
		try {
			forTest = this.model.createTestManually2(forTest, index, subject, name, array, false, text);
		} catch (BadInputException | WrongQuestionException | IOException e) {
			
			iGotAnErrorMessage(e.getMessage());
		} catch (ClassNotFoundException e) {
			
			iGotAnErrorMessage(e.getMessage());
		}
	}

	@Override
	public void createManuallyTest(String name, String subject) {
		String text = this.model.generateTest(subject, name, forTest);
		setTestScrollPane(text);

	}

	@Override
	public void cloneMyTest(String name, String subject) {
		try {
		setTestScrollPane(this.model.cloneMyTest(name, subject));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			iGotAnErrorMessage(e.getMessage());
		}
		
	}

}
