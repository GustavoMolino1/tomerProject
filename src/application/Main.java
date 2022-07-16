package application;



import controller.ExamController;
import javafx.application.Application;
import javafx.stage.Stage;
import view.AbstractExamView;
import view.MenuCreation;
import model.*;
import view.examFX;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
		

	}

	@Override
	public void start(Stage myStage) throws Exception {
							
				Questioneer theModel = new Questioneer();
		AbstractExamView theView1 = new examFX (myStage);
	
		@SuppressWarnings("unused")
		ExamController controller1=new ExamController(theModel, theView1);
		
		AbstractExamView viewMenu=new MenuCreation();
		@SuppressWarnings("unused")
		ExamController controller2=new ExamController(theModel,viewMenu);
		
		
	}

}

	
