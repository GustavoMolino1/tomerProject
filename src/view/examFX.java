package view;

import java.io.IOException;
import java.util.Vector;
import javax.swing.JOptionPane;
import Exepction.BadInputException;
import Exepction.WrongQuestionException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import listeners.ExamUIEvenListener;
import model.Answer;
import model.Question;
import model.Set;

public class examFX implements AbstractExamView {

	// all of those things are helping me to create the menu.
	// this class also create test, copy test etc.. 
	private Vector<ExamUIEvenListener> mylistenter = new Vector<ExamUIEvenListener>();
	public int questionCounter; // counter all questions
	public Label showinglabel = new Label("Coutner Question:" + questionCounter); // for showing label. controlled by the controller.
	public int globalSize = 0; // the global size of question. like the question counter.
	public Label questionViewLabel = new Label(""); // Question label, controlled by the controller too
	public static int globalIndex = -1;
	public Label testLabel = new Label(""); // label for test. controlled by the controller too, showing the text of the test.
	public static Label showQuestion = new Label(); // show question, for checking some shit.. 
	public static String copyQuestion; // copy of 
	public static String selectingButton = "";
	public static int selectedQuestionID = -1; // do i selected a question? telling me. 
	public static int[] myAnswerSelction = new int[100]; // an array of the all the answers that i choiced per question.
	public VBox root = null; // a global Vbox
	


	public examFX(Stage stage) throws Exception {

		Scene scene = new Scene(createBorderPane(), 1000, 600);
		stage.setTitle("Tomers' Project");
		stage.setScene(scene);
		stage.show();
		for (int i = 0; i < myAnswerSelction.length; i++) {
			myAnswerSelction[i] = -1;
		}
	}

	public BorderPane createBorderPane() { // the mean menu,

		multiQuestioncreator mc = new multiQuestioncreator();
		MenuCreation menuC = new MenuCreation();
		BorderPane borderPane = new BorderPane();

		borderPane.autosize();
		Button addQuestionButton = new Button("Add An Open Question");
		Button openQuestion = new Button("Add MultiChoice question");
		Button menuShow = new Button("Main Menu");
		Button createTest = new Button("Create Test");
		Button showQuestions = new Button("Show all my Questions");
		Button exitAndSaveDataButton = new Button("Exit and Save");
		exitAndSaveDataButton.setTextFill(Color.RED);

		addQuestionButton.setOnAction(new EventHandler<ActionEvent>() {
			BorderPane pp = createScene();

			@Override
			public void handle(ActionEvent arg0) {
				borderPane.setCenter(pp);
			}
		});

		ToolBar toolbar = new ToolBar(addQuestionButton, openQuestion, new Separator(), showQuestions, createTest,
				menuShow, new Separator(), exitAndSaveDataButton);
		VBox vbox = new VBox();
		vbox.getChildren().addAll(toolbar);
		TabPane tabPaneLeft = new TabPane();

		TabPane tabPaneRight = new TabPane();
		showinglabel.setText("Counter Question:" + questionCounter);
		borderPane.setTop(vbox);
		borderPane.setLeft(tabPaneLeft);
		borderPane.setCenter(new TextArea());
		borderPane.setRight(tabPaneRight);
		borderPane.setBottom(showinglabel);

		openQuestion.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				borderPane.setCenter(mc.createOpenQuestionPane(mylistenter));

			}
		});

		menuShow.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub

				borderPane.setCenter(menuC.setMenu(globalSize, mylistenter));
			}
		});
		createTest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				ScrollPane cc2 = new ScrollPane();
				// cc2.setContent(creatTestScene());
				cc2.setContent(setMenu());
				borderPane.setCenter(cc2);

			}
		});

		showQuestions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {

				ScrollPane cc = new ScrollPane();
				for (ExamUIEvenListener examUIEvenListener : mylistenter) {
					questionViewLabel.setText(examUIEvenListener.getAllQuestionFromModel());
				}

				cc.setContent(questionViewLabel);
				borderPane.setCenter(cc);

			}
		});

		exitAndSaveDataButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				saveDataBeforExit();
		

			}
		});

		return borderPane;

	}

	public BorderPane creatTestScene() { // create automatic test

		BorderPane testPane = new BorderPane();
		ComboBox<Integer> questionIndex = new ComboBox<Integer>();

		Integer[] questionIndexarray = new Integer[globalSize];
		for (int i = 0; i < questionIndexarray.length; i++) {
			questionIndexarray[i] = i + 1;
		}
		questionIndex.getItems().addAll(questionIndexarray);

		GridPane gpRoot = new GridPane();
		gpRoot.setPadding(new Insets(10));
		gpRoot.setAlignment(Pos.CENTER_LEFT);
		gpRoot.setVgap(10);
		gpRoot.setHgap(15);
		TextField subjectField = new TextField();
		TextField nameField = new TextField();
		Button createTest = new Button("Create Test");

		subjectField.setMinWidth(120);

		gpRoot.add(new Label("Enter Subject"), 0, 0);
		gpRoot.add(subjectField, 1, 0);
		gpRoot.add(new Label("Select how much answer(s) Do you want:"), 0, 1);
		gpRoot.add(questionIndex, 1, 1);
		gpRoot.add(new Label("Enter your name:"), 0, 2);
		gpRoot.add(nameField, 1, 2);

		gpRoot.add(createTest, 0, 3, 2, 1);
		testPane.setCenter(gpRoot);

		createTest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				for (ExamUIEvenListener lis : mylistenter) {
					if (questionIndex.getValue() != null) {
						if ((questionIndex.getValue() != null) || (subjectField.getText() != "")
								|| (nameField.getText() != "")) {
							gpRoot.getChildren().remove(testLabel);
							lis.genreateAutoTest(subjectField.getText(), questionIndex.getValue(), nameField.getText());
							gpRoot.add(testLabel, 0, 5);
						}
					} else {
						lis.genreateAutoTest("", 0, "");
					}
				}

			}
		});

		return testPane;
	}

	public BorderPane createScene() { // create the design of the scene for the test.
		BorderPane br = new BorderPane();

		GridPane gpRoot = new GridPane();
		gpRoot.setPadding(new Insets(10));
		gpRoot.setAlignment(Pos.CENTER_LEFT);
		gpRoot.setVgap(10);
		gpRoot.setHgap(15);
		TextField questionfield = new TextField();
		TextField Answerfield = new TextField();
		Button addQuestion = new Button("Add Question");

		questionfield.setMinWidth(500);

		gpRoot.add(new Label("Question"), 0, 0);
		gpRoot.add(Answerfield, 1, 0);
		gpRoot.add(new Label("Answer:"), 0, 1);
		gpRoot.add(questionfield, 1, 1);

		gpRoot.add(addQuestion, 0, 3, 2, 1);
		br.setCenter(gpRoot);
		Label showing = new Label("Add an Open Question");
		showing.setMinWidth(1000);
		showing.setMinHeight(0);
		showing.setFont(new Font(24));
		showing.setAlignment(Pos.TOP_LEFT);
		br.setTop(showing);
		gpRoot.setAlignment(Pos.TOP_LEFT);
		addQuestion.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				for (ExamUIEvenListener examUIEvenListener : mylistenter) {
					try {
						examUIEvenListener.addOpenQuestionFromUI(questionfield.getText(), Answerfield.getText());
					} catch (BadInputException | WrongQuestionException | IOException e) {
						// TODO Auto-generated catch block
					examErrorMessage("Not a Valid Question");
					}
				}
			}
		});
		return br;
	}

	public BorderPane setMenu() { // the main menu of the "Test generator area"
		BorderPane br = new BorderPane();
		GridPane gpRoot = new GridPane();
		gpRoot.setPadding(new Insets(10));
		gpRoot.setAlignment(Pos.CENTER_LEFT);
		gpRoot.setVgap(10);
		gpRoot.setHgap(15);
		String myChoice[] = { "Automatic test creation", "Create a manual test", "A copy of a previous test" };

		ComboBox<String> userChoice = new ComboBox<String>();
		userChoice.getItems().addAll(myChoice);
		VBox root = new VBox();
		GridPane newone = new GridPane();
		userChoice.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

			}
		});

		gpRoot.add(new Label("Please Select which Option you want to do"), 0, 0);
		gpRoot.add(new Label("Options:"), 0, 1);
		gpRoot.add(userChoice, 1, 1);
		br.setCenter(gpRoot);
		Label showing = new Label("Test Genreator!");
		showing.setMinWidth(1000);
		showing.setMinHeight(0);
		showing.setFont(new Font(24));
		showing.setAlignment(Pos.TOP_LEFT);

		br.setTop(showing);
		gpRoot.setAlignment(Pos.TOP_LEFT);
		userChoice.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				root.getChildren().clear();
				newone.add(root, 100, 100);
				root.getChildren().add(allPane(userChoice.getValue(), mylistenter));

				gpRoot.add(root, 0, 3, 2, 1);

			}
		});
		return br;
	}

	public BorderPane allPane(String value, Vector<ExamUIEvenListener> lis) { // treat which case is selected, automatic / manual test / copy of
		Vector<ExamUIEvenListener> listen = lis;
		BorderPane br = new BorderPane();

		GridPane gpRoot = new GridPane();
		gpRoot.setPadding(new Insets(10));
		gpRoot.setAlignment(Pos.CENTER_LEFT);
		gpRoot.setVgap(10);
		gpRoot.setHgap(15);
		String myChoice[] = { "Automatic test creation", "Create a manual test" };

		ComboBox<String> userChoice = new ComboBox<String>();
		userChoice.getItems().addAll(myChoice);

		switch (value) {

		case ("Automatic test creation"):
			br = creatTestScene();
			return br;

		case ("Create a manual test"):
			// return createManualTest(0);
			return setManualTest2(globalSize, listen, examFX.showQuestion.getText());

		case ("A copy of a previous test"):
			if (testLabel.getText() != "") {
				return setCopyTest(listen);
			} else
				examErrorMessage("You have not previous test");
			break;
		}

		return br;

	}

	public BorderPane setCopyTest(Vector<ExamUIEvenListener> lis) { // copy test 
		VBox broot = new VBox();
		Vector<ExamUIEvenListener> listen = lis;
		BorderPane br = new BorderPane();
		GridPane gpRoot = new GridPane();
		gpRoot.setPadding(new Insets(10));
		gpRoot.setAlignment(Pos.CENTER_LEFT);
		gpRoot.setVgap(10);
		gpRoot.setHgap(15);
		TextField subjectField = new TextField();
		TextField nameField = new TextField();
		Button createTest = new Button("Create Test");

		subjectField.setMinWidth(120);

		gpRoot.add(new Label("Enter Subject"), 0, 0);
		gpRoot.add(subjectField, 1, 0);

		gpRoot.add(new Label("Enter your name:"), 0, 1);
		gpRoot.add(nameField, 1, 1);
		// gpRoot.add(createTest, 0, 3, 2, 1);
		gpRoot.add(new Label("Please Select the Question: "), 0, 2);
		broot.setSpacing(10);
		broot.setPadding(new Insets(10));
		broot.setAlignment(Pos.TOP_LEFT);
		gpRoot.add(broot, 0, 6);

		createTest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				for (ExamUIEvenListener examUIEvenListener : listen) {
					examUIEvenListener.cloneMyTest(nameField.getText(), subjectField.getText());
				}
				gpRoot.getChildren().remove(testLabel);

				gpRoot.add(testLabel, 0, 5);

			}
		});

		gpRoot.add(createTest, 8, 0); // get the question

		br.setCenter(gpRoot);
		gpRoot.setAlignment(Pos.TOP_LEFT);
		return br;
	}

	public BorderPane setManualTest2(int size, Vector<ExamUIEvenListener> lis, String questions) { // manual test creation

		Vector<ExamUIEvenListener> listen = lis;
		BorderPane br = new BorderPane();
		GridPane gpRoot = new GridPane();
		gpRoot.setPadding(new Insets(10));
		gpRoot.setAlignment(Pos.CENTER_LEFT);
		gpRoot.setVgap(10);
		gpRoot.setHgap(15);

		ComboBox<Integer> questionIndex = questionCounter(size);
		VBox broot = new VBox();
		Button addQuestionToTest = new Button("Add Question");

		questionIndex.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				broot.getChildren().clear();

				gpRoot.getChildren().remove(showQuestion);
				for (ExamUIEvenListener lis : lis) {
					if (questionIndex.getValue() != null) {
						lis.getQuestionByIDFromUI((questionIndex.getValue() - 1));

						examFX.selectedQuestionID = questionIndex.getValue();
					}
				}
				gpRoot.add(examFX.showQuestion, 0, 4);
				broot.getChildren()
						.addAll(new VBox((VBox) importQuestionAnswer(questionIndex.getValue() - 1, listen)[0]));

			}

		});

		TextField subjectField = new TextField();
		TextField nameField = new TextField();
		Button createTest = new Button("Create Test");

		subjectField.setMinWidth(120);
		gpRoot.add(new Label("Enter Subject"), 0, 0);
		gpRoot.add(subjectField, 1, 0);
		gpRoot.add(new Label("Enter your name:"), 0, 1);
		gpRoot.add(nameField, 1, 1);
		gpRoot.add(new Label("Please Select the Question: "), 0, 2);
		gpRoot.add(questionIndex, 1, 2);
		broot.setSpacing(10);
		broot.setPadding(new Insets(10));
		broot.setAlignment(Pos.TOP_LEFT);
		gpRoot.add(broot, 0, 6);
		gpRoot.add(new Label("Add Question to test"), 5, 0);
		gpRoot.add(addQuestionToTest, 6, 0); // get the question

		gpRoot.add(createTest, 8, 0); // get the question

		br.setCenter(gpRoot);
		gpRoot.setAlignment(Pos.TOP_LEFT);
		addQuestionToTest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				for (ExamUIEvenListener examUIEvenListener : lis) {

					examUIEvenListener.addQuestionManully(examFX.selectedQuestionID, examFX.myAnswerSelction,
							examFX.copyQuestion, nameField.getText(), subjectField.getText());
				}
			}
		});

		createTest.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				for (ExamUIEvenListener lis : mylistenter) {
					if ((questionIndex.getValue() != null) || (subjectField.getText() != "")
							|| (nameField.getText() != "")) {
						gpRoot.getChildren().remove(testLabel);
						lis.createManuallyTest(nameField.getText(), subjectField.getText());
						gpRoot.add(testLabel, 0, 8);

					} else {
						lis.genreateAutoTest("", 0, "");
					}
				}

			}
		});

		return br;
	}

	public Object[] importQuestionAnswer(int index, Vector<ExamUIEvenListener> lis) { // this is for the radio buttons that return radio-buttons of all answers per question.
		Object[] returning = new Object[100];
		// ToggleGroup myAnswers66 = new ToggleGroup();
		VBox vb = new VBox();
		RadioButton[] mybuttons = new RadioButton[100];

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
				// b1.setToggleGroup(myAnswers66);
				vb.getChildren().add(b1);
			}

		}
		for (RadioButton radioButton : mybuttons) {
			if (radioButton != null) {
				radioButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						if (radioButton != null)
							examFX.selectingButton = radioButton.getText();
						for (int i = 0; i < mybuttons.length; i++) {
							if (mybuttons[i] != null)
								if (mybuttons[i].getText() == radioButton.getText()) {
									{
										myAnswerSelction[i] = i;
									}
								}
						}
					}
				});
			}
		}
		returning[0] = vb;
		return returning;
	}

	public void clearSelection() { // clear the selection of answer.
		for (int i = 0; i < myAnswerSelction.length; i++) {
			myAnswerSelction[i] = -1;
		}
	}

	@Override
	public void registerListener(ExamUIEvenListener listener) {
		mylistenter.add(listener);

	}

	@Override
	public void addOpenQuestionToUI(String answer, String question) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showQuestionTOUI(Question q) {

	}

	@Override
	public void getNumberOfQuestion(int size) {
		globalSize = size;
		questionCounter = size;
		showinglabel.setText("Counter is: " + size);

	}

	@Override
	public void uploadQuestionData() {
		for (ExamUIEvenListener abstractExamView : mylistenter) {
			abstractExamView.uploadQuestionData();
		}
	}

	@Override
	public void newQuestionAddedView(String allQuestion) {
		questionViewLabel.setText(allQuestion);
	}

	@Override
	public void examErrorMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg);

	}

	@Override
	public void updateQuestionText(String newText) {
		examFX.showQuestion.setText("Question: " + newText);
		examFX.copyQuestion = newText;
	}

	@Override
	public void saveDataBeforExit() {
		for (ExamUIEvenListener list : mylistenter) {
			list.saveData();

		}

	}

	public ComboBox<Integer> questionCounter(int size) { // return Question counter; 
		ComboBox<Integer> questionIndex = new ComboBox<Integer>();
		Integer[] questionIndexarray = new Integer[size];
		for (int i = 0; i < questionIndexarray.length; i++) {
			questionIndexarray[i] = i + 1;
		}
		questionIndex.getItems().addAll(questionIndexarray);
		return questionIndex;
	}

	@Override
	public void setTest(String testQuestion) { // update the test by the controller.
		if (testQuestion == "")
			examErrorMessage("Cant create an empty test");
		else

			this.testLabel.setText(testQuestion);
	}

}
