package view;

import listeners.ExamUIEvenListener;
import model.Question;

public interface AbstractExamView {

	void registerListener(ExamUIEvenListener listener);
	void addOpenQuestionToUI(String answer,String question);
    void showQuestionTOUI(Question q); // update the question by the label
    void getNumberOfQuestion(int size); // get the number of question and update it. 
    void uploadQuestionData();
    void newQuestionAddedView(String allQuestion); //update the view"
    void examErrorMessage(String msg); // used to deliver some system message between the MVC 
    void updateQuestionText(String newText);
    void saveDataBeforExit();
    void setTest(String testQuestion);
   

}
