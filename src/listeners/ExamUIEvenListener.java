package listeners;

import java.io.FileNotFoundException;
import java.io.IOException;

import Exepction.BadInputException;
import Exepction.WrongQuestionException;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import model.Answer;
import model.Set;

public interface ExamUIEvenListener {

	void addOpenQuestionFromUI(String answer, String question)
			throws FileNotFoundException, BadInputException, WrongQuestionException, IOException;

	void uploadQuestionData();

	String getAllQuestionFromModel();

	public void getQuestionByIDFromUI(int index);
	public String getQuestionByIDString(int index);

	public void editQuestion(Integer index, String newQuestion);

	public Set<Answer> getAnserwsPerQuestionFromUI(int index);
	public void addQuestionManully(int index,int[]array,String text,String name,String subject);
	
	public void createManuallyTest(String name,String subject);

	public void updateAnswerText(int questionID, String newAnswer, String lastAnswer);

	public void addNewAnswerText(int questionId, String answer, boolean isTrue);

	public void removeAnswer(int questionNum, String answerText);

	public void addMultiQuestion(String question, TextField[] tF, CheckBox[] cB);

	public void saveData();

	public String genreateAutoTest(String sub, int size,String name);

	void setTestScrollPane(String allTest);
	public void cloneMyTest(String name,String subject);
	
}
