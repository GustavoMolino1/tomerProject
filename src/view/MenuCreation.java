package view;

import java.util.Vector;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import listeners.ExamUIEvenListener;
import model.Answer;
import model.Question;
import model.Set;

public class MenuCreation implements AbstractExamView {

	private Vector<ExamUIEvenListener> mylistenter = new Vector<ExamUIEvenListener>();
	public static Label showQuestion = new Label();
	public static String selectingButton = "";
	public static int selectedQuestionID = -1;

	public BorderPane setMenu(int size, Vector<ExamUIEvenListener> listener) {
		BorderPane br = new BorderPane();
		GridPane gpRoot = new GridPane();
		gpRoot.setPadding(new Insets(10));
		gpRoot.setAlignment(Pos.CENTER_LEFT);
		gpRoot.setVgap(10);
		gpRoot.setHgap(15);
		String myChoice[] = { "Edit Question", "Edit An Answer", "Add Answer", "Remove a possible answer" };
		
		ComboBox<String> userChoice = new ComboBox<String>();
		userChoice.getItems().addAll(myChoice);
		VBox root = new VBox();
		GridPane newone = new GridPane();
		userChoice.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				root.getChildren().clear();
				newone.add(root, 100, 100);
				root.getChildren().add(allPane(userChoice.getValue(), questionCounter(size), listener));
				gpRoot.add(new HBox(allPane(userChoice.getValue(), questionCounter(size), listener)), 4, 4);
				gpRoot.add(root, 0, 3, 2, 1);
			}
		});

		gpRoot.add(new Label("Please Select which Option that you want to do"), 0, 0);
		gpRoot.add(new Label("Possible Option"), 0, 1);
		gpRoot.add(userChoice, 1, 1);
		br.setCenter(gpRoot);
		Label showing = new Label(" Menu");
		showing.setMinWidth(1000);
		showing.setMinHeight(0);
		showing.setFont(new Font(24));
		showing.setAlignment(Pos.TOP_LEFT);
		br.setTop(showing);
		gpRoot.setAlignment(Pos.TOP_LEFT);
		return br;
	}

	public ComboBox<Integer> questionCounter(int size) {
		ComboBox<Integer> questionIndex = new ComboBox<Integer>();
		Integer[] questionIndexarray = new Integer[size];
		for (int i = 0; i < questionIndexarray.length; i++) {
			questionIndexarray[i] = i + 1;
		}
		questionIndex.getItems().addAll(questionIndexarray);
		return questionIndex;
	}

	public BorderPane allPane(String value, ComboBox<Integer> myquestion, Vector<ExamUIEvenListener> lis) {
		Vector<ExamUIEvenListener> listen = lis;
		BorderPane br = new BorderPane();

		GridPane gpRoot = new GridPane();
		gpRoot.setPadding(new Insets(10));
		gpRoot.setAlignment(Pos.CENTER_LEFT);
		gpRoot.setVgap(10);
		gpRoot.setHgap(15);
		String myChoice[] = { "Edit Question", "Edit An Answer", "Add Answer", "Remove a possible answer" };
		
		ComboBox<String> userChoice = new ComboBox<String>();
		userChoice.getItems().addAll(myChoice);
		
		

		ComboBox<Integer> questionIndex = myquestion;
		Label showing = new Label("Edit Question");
		switch (value) {

		case ("Edit Question"):
			TextField newQuestionField = new TextField();
			questionIndex.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {

					gpRoot.getChildren().remove(showQuestion);

					for (ExamUIEvenListener lis : lis) {
						if (questionIndex.getValue() != null)
							lis.getQuestionByIDFromUI((questionIndex.getValue() - 1));
					}

					gpRoot.add(showQuestion, 0, 1);

				}
			});

			Button editQuestionButton = new Button("Save New Question");
			gpRoot.add(new Label("Please Select the Quesution: "), 0, 0);
			gpRoot.add(questionIndex, 1, 0);
			gpRoot.add(new Label("Enter new Question:"), 1, 1);
			gpRoot.add(newQuestionField, 3, 1); // get the question
			gpRoot.add(editQuestionButton, 1, 2);
			br.setCenter(gpRoot);
			showing.setText("Edit Question");
			showing.setMinWidth(1000);
			showing.setMinHeight(0);
			showing.setFont(new Font(24));
			showing.setAlignment(Pos.TOP_LEFT);
			br.setTop(showing);
			gpRoot.setAlignment(Pos.TOP_LEFT);
			editQuestionButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {

					for (ExamUIEvenListener lis : lis) {
						if (questionIndex.getValue() != null)
							lis.editQuestion(questionIndex.getValue() - 1, newQuestionField.getText());
						else
							lis.editQuestion(-5, newQuestionField.getText());
					}
				}
			});

			break;

		case ("Edit An Answer"): {

			VBox broot = new VBox();
			TextField changeText = new TextField();
			Button editAnswerButton = new Button("Save New Answer");
		
			questionIndex.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {

					broot.getChildren().clear();
					gpRoot.getChildren().remove(showQuestion);
					for (ExamUIEvenListener lis : lis) {
						if (questionIndex.getValue() != null) {
							lis.getQuestionByIDFromUI((questionIndex.getValue() - 1));
							MenuCreation.selectedQuestionID = questionIndex.getValue() - 1;
						}
					}
					gpRoot.add(MenuCreation.showQuestion, 0, 1);
					broot.getChildren()
							.addAll(new VBox((VBox) importQuestionAnswer(questionIndex.getValue() - 1, listen)[0]));

				}

			});

			gpRoot.add(new Label("Please Select the Quesution: "), 0, 0);
			gpRoot.add(questionIndex, 1, 0);

			broot.setSpacing(10);
			broot.setPadding(new Insets(10));
			broot.setAlignment(Pos.TOP_LEFT);

			gpRoot.add(broot, 0, 2);

			gpRoot.add(new Label("Enter new Answer:"), 1, 2);
			gpRoot.add(editAnswerButton, 3, 2); // get the question
			gpRoot.add(changeText, 2, 2);
			br.setCenter(gpRoot);
			showing.setText("Edit Answer");
			showing.setMinWidth(1000);
			showing.setMinHeight(0);
			showing.setFont(new Font(24));
			showing.setAlignment(Pos.TOP_LEFT);
			br.setTop(showing);
			gpRoot.setAlignment(Pos.TOP_LEFT);

			editAnswerButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override

				public void handle(ActionEvent arg0) {
					if (MenuCreation.selectedQuestionID != -1) {
						for (ExamUIEvenListener exam : lis) {
							exam.updateAnswerText(MenuCreation.selectedQuestionID, changeText.getText(),
									MenuCreation.selectingButton);
							if (changeText.getText() != "") {
								broot.getChildren().clear();
								gpRoot.getChildren().remove(showQuestion);
								gpRoot.add(MenuCreation.showQuestion, 0, 1);
								broot.getChildren().addAll(
										new VBox((VBox) importQuestionAnswer(questionIndex.getValue() - 1, listen)[0]));

							}
						}
					}
				}

			});
			break;
		}
		case ("Add Answer"):
			VBox broot2 = new VBox();
			TextField newAnswer = new TextField();
			Button saveNewAnswerButton = new Button("Save New Answer");
			CheckBox isTrue = new CheckBox();
			isTrue.setAlignment(Pos.CENTER_RIGHT);
			questionIndex.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					// TODO Auto-generated method stub
					gpRoot.getChildren().remove(showQuestion);
					for (ExamUIEvenListener lis : lis) {
						if (questionIndex.getValue() != null)
							lis.getQuestionByIDFromUI((questionIndex.getValue() - 1));
					}

				}
			});
			gpRoot.add(MenuCreation.showQuestion, 0, 1);
			gpRoot.add(new Label("Please Select the Quesution: "), 0, 0);
			gpRoot.add(questionIndex, 1, 0);
			questionIndex.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					broot2.getChildren().clear();
					gpRoot.getChildren().remove(showQuestion);
					for (ExamUIEvenListener lis : lis) {
						if (questionIndex.getValue() != null)
							lis.getQuestionByIDFromUI((questionIndex.getValue() - 1));
						MenuCreation.selectedQuestionID = questionIndex.getValue() - 1;
					}
					gpRoot.add(MenuCreation.showQuestion, 0, 1);
					broot2.getChildren()
							.addAll(new VBox((VBox) importQuestionAnswer(questionIndex.getValue() - 1, listen)[0]));
				}
			});
			gpRoot.add(broot2, 0, 2);

			gpRoot.add(new Label("Enter new answer that you want to add!:"), 1, 2);
			gpRoot.add(new Label("Is it currect?"), 1, 3);
			gpRoot.add(isTrue, 2, 3);
			gpRoot.add(saveNewAnswerButton, 1, 4); // get the question
			gpRoot.add(newAnswer, 2, 2);
			br.setCenter(gpRoot);
			showing.setText("Add New Answer");
			showing.setMinWidth(1000);
			showing.setMinHeight(0);
			showing.setFont(new Font(24));
			showing.setAlignment(Pos.TOP_LEFT);
			br.setTop(showing);
			gpRoot.setAlignment(Pos.TOP_LEFT);
			saveNewAnswerButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					for (ExamUIEvenListener exam : lis) {

						exam.addNewAnswerText((MenuCreation.selectedQuestionID + 1), newAnswer.getText(),
								isTrue.isSelected());
					}
					if (MenuCreation.selectedQuestionID != -1) {
						broot2.getChildren().clear();
						gpRoot.getChildren().remove(showQuestion);
						gpRoot.add(MenuCreation.showQuestion, 0, 1);
						broot2.getChildren()
								.addAll(new VBox((VBox) importQuestionAnswer(questionIndex.getValue() - 1, listen)[0]));
					}
				}
			});

			break;

		case ("Remove a possible answer"):

			VBox broot24 = new VBox();
			broot24.setSpacing(10);
			broot24.setPadding(new Insets(10));
			broot24.setAlignment(Pos.TOP_LEFT);
			Button removeButton = new Button("Remove");
			gpRoot.add(questionIndex, 1, 0);
			questionIndex.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {

					broot24.getChildren().clear();
					gpRoot.getChildren().remove(showQuestion);
					for (ExamUIEvenListener lis : lis) {
						if (questionIndex.getValue() != null)
							lis.getQuestionByIDFromUI((questionIndex.getValue() - 1));
						MenuCreation.selectedQuestionID = questionIndex.getValue();
					}

					gpRoot.add(MenuCreation.showQuestion, 0, 2);

					broot24.getChildren()
							.addAll(new VBox((VBox) importQuestionAnswer(questionIndex.getValue() - 1, listen)[0]));

				}
			});
			gpRoot.add(broot24, 0, 3);
			gpRoot.add(new Label("Select the Answer that you want to remove!"), 0, 1);
			gpRoot.add(removeButton, 1, 3); // get the question
			br.setCenter(gpRoot);
			showing.setText("Remove Answer");
			showing.setMinWidth(1000);
			showing.setMinHeight(0);
			showing.setFont(new Font(24));
			showing.setAlignment(Pos.TOP_LEFT);
			br.setTop(showing);
			gpRoot.setAlignment(Pos.TOP_LEFT);

			removeButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {

					for (ExamUIEvenListener exam : lis) {
						exam.removeAnswer(MenuCreation.selectedQuestionID - 1, MenuCreation.selectingButton);
						broot24.getChildren().clear();
						gpRoot.getChildren().remove(showQuestion);
						gpRoot.add(MenuCreation.showQuestion, 0, 2);
						broot24.getChildren()
								.addAll(new VBox((VBox) importQuestionAnswer(questionIndex.getValue() - 1, listen)[0]));
					}

				}
			});
			break;
		}
		return br;

	}

	@Override
	public void registerListener(ExamUIEvenListener listener) {
		this.mylistenter.add(listener);
	}

	@Override
	public void addOpenQuestionToUI(String answer, String question) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showQuestionTOUI(Question q) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getNumberOfQuestion(int size) {
		// TODO Auto-generated method stub

	}

	@Override
	public void uploadQuestionData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void newQuestionAddedView(String allQuestion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void examErrorMessage(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateQuestionText(String newText) {
		MenuCreation.showQuestion.setText("Question: " + newText);
	}

	public Object[] importQuestionAnswer(int index, Vector<ExamUIEvenListener> lis) {
		Object[] returning = new Object[4];
		ToggleGroup myAnswers66 = new ToggleGroup();
		VBox vb = new VBox();
		RadioButton[] mybuttons = new RadioButton[20];
	
		if (index != -1) {
			Set<Answer> tt = new Set<Answer>();
			for (ExamUIEvenListener examUIEvenListener : lis) {
				tt = examUIEvenListener.getAnserwsPerQuestionFromUI(index);
				returning[3] = tt.size();
			}

			for (int i = 0; i < tt.capacity(); i++) {
				tt.elementAt(i).getText();

				RadioButton b1 = new RadioButton(tt.elementAt(i).getText());
				mybuttons[i] = b1;
				b1.setToggleGroup(myAnswers66);
				vb.getChildren().add(b1);
			}

		}

		for (RadioButton radioButton : mybuttons) {
			if (radioButton != null) {

				radioButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						if (radioButton != null)
							MenuCreation.selectingButton = radioButton.getText();

					}
				});
			}

		}

		returning[0] = vb;
		returning[1] = myAnswers66;

		return returning;
	}

	@Override
	public void saveDataBeforExit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTest(String testQuestion) {
		// TODO Auto-generated method stub
		
	}

}
