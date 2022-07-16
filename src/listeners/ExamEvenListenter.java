package listeners;

import model.Question;

public interface ExamEvenListenter {

	

	public void toDisplayFromModel(Question q1);

	void uploadQuestionDataFromModel();

	void getNumberOfQuestionFromModel(int size);

	public void sendQuestionToUI(String allQueston);

	public void iGotAnErrorMessage(String msg);

	public void getQuestionByIDFromModel(Question q1);

	public void updateMenu(String newtext);




}
