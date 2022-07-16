package view;

import java.util.Vector;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import listeners.ExamUIEvenListener;

public class multiQuestioncreator extends BorderPane {

	public VBox root = null;
	String message = null;
	public static int numberSelected = 0;

	public BorderPane createOpenQuestionPane(Vector<ExamUIEvenListener> listener) {
		BorderPane br = new BorderPane();

		GridPane gpRoot = new GridPane();
		gpRoot.setPadding(new Insets(10));
		gpRoot.setAlignment(Pos.CENTER_LEFT);
		gpRoot.setVgap(10);
		gpRoot.setHgap(15);
		TextField questionfield = new TextField();
		questionfield.setPrefWidth(350);
	
		Button addQuestion = new Button("Add Question");
		Button resetButton = new Button("Reset Answers");
		ComboBox<Integer> answersNumber = new ComboBox<Integer>();
		answersNumber.getItems().addAll(4, 5);

		GridPane newone = new GridPane();
		gpRoot.add(addQuestion, 0, 4, 2, 1);
		gpRoot.add(resetButton, 1, 4, 2, 1);
	
		answersNumber.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				final int numTextFields = answersNumber.getValue();
				multiQuestioncreator.numberSelected = answersNumber.getValue();
				TextField[] textFields = new TextField[numTextFields];
				CheckBox[] isTrue = new CheckBox[numTextFields];
				root = new VBox();
				
				newone.add(root, 100, 100);
				for (int i = 1; i <= numTextFields; i++) {
					TextField tf = new TextField();
					CheckBox c = new CheckBox();
					c.setAlignment(Pos.CENTER_RIGHT);
					root.getChildren().add(tf);
					root.getChildren().addAll(new Label("is it currect?"), c);
					root.getChildren().add(new Separator());
					isTrue[i - 1] = c;
					textFields[i - 1] = tf;
				}

				gpRoot.add(root, 0, 3, 2, 1);

				answersNumber.setDisable(true);
				resetButton.setVisible(true);
				

			}
		});
		addQuestion.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				TextField[] textFields = new TextField[multiQuestioncreator.numberSelected];
				CheckBox[] isTrue = new CheckBox[multiQuestioncreator.numberSelected];
				int index = 0;
				for (Node fielddd : root.getChildren()) {
					if (fielddd instanceof TextField) {
						if (((TextField) fielddd).getText() != "")
							message += ((TextField) fielddd).getText() + "\n";
						textFields[index] = (TextField) fielddd;
					} else if (fielddd instanceof CheckBox) {
						isTrue[index] = (CheckBox) fielddd;
						index++;
					}

				}
				for (ExamUIEvenListener lis : listener) {
					lis.addMultiQuestion(questionfield.getText(),textFields, isTrue);
				}
				
			}
		});
		resetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				newone.getChildren().clear();
				root.getChildren().clear();
				answersNumber.setDisable(false);
				resetButton.setVisible(false);
				message = "";
			}
		});
		gpRoot.add(new Label("Question"), 0, 0);
		gpRoot.add(questionfield, 1, 0);
		gpRoot.add(new Label("Possible Answers that you want:"), 0, 1);
		gpRoot.add(answersNumber, 1, 1);
		br.setCenter(gpRoot);
		Label showing = new Label("Add MultiChoice Question");
		showing.setMinWidth(1000);
		showing.setMinHeight(0);
		showing.setFont(new Font(24));
		showing.setAlignment(Pos.TOP_LEFT);
		br.setTop(showing);
		gpRoot.setAlignment(Pos.TOP_LEFT);

		return br;
	}
	

}
