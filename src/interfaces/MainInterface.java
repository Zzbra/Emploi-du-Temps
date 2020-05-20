package interfaces;

import java.sql.SQLException;

import application.*;
import interfaces.ColorMat.CreateMatiereColor;
import interfaces.EDT.CreateInterface;
import interfaces.Param.ParamInterface;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mariaDB.DBConnection;
public class MainInterface extends Application {

	private double WIDTH = Screen.getPrimary().getBounds().getWidth();
	private double HEIGHT = Screen.getPrimary().getVisualBounds().getHeight()-40;

	private MainInterface mainInterface;
	private CreateMatiereColor matCol;
	private ParamInterface paramInterface;
	private Solveur solveur;
	private CreateInterface creationInterface;
	private Scene sceneEDT;
	private Scene sceneParam;
	private Stage stage;
	


	public Scene getScene() {
		return sceneEDT;
	}


	public static void main(String[] args) {
		Application.launch(MainInterface.class, args);
	}


	@Override
	public void start(Stage primaryStage) throws ClassNotFoundException, SQLException {
		this.stage =primaryStage;
		
	//	this.matCol=new CreateMatiereColor(solveur);
		primaryStage.setTitle("Emploi du temps : Ecole de la deuxieme chance Marseille");

		Group rootEDT = new Group();
		ScrollPane scroll = new ScrollPane();
		scroll.setStyle("-fx-background: rgb(76, 130, 154);");
		scroll.setContent(rootEDT);
		scroll.setMaxHeight(HEIGHT);
		this.sceneEDT = new Scene(scroll, WIDTH,HEIGHT);
		
		this.mainInterface=this;
		
		
		Group rootParam = new Group();
		ScrollPane scroll2 = new ScrollPane();
		scroll2.setStyle("-fx-background: rgb(76, 130, 154);");
		scroll2.setContent(rootParam);
		scroll2.setMaxHeight(HEIGHT);
		
		this.sceneParam = new Scene(scroll2,WIDTH,HEIGHT);
		this.paramInterface = new ParamInterface(rootParam,mainInterface,solveur);
		
	//	this.creationInterface= new CreateInterface(rootEDT,mainInterface,solveur);

		primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				primaryStage.setWidth((double) newSceneWidth);
				setWIDTH(newSceneWidth);
				setWindow(rootEDT);
			}
		});
		primaryStage.heightProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
				setHEIGHT(newSceneHeight);
				setWindow(rootEDT);
			}
		});


	//	primaryStage.setScene(sceneEDT);
		primaryStage.setScene(sceneParam);
		primaryStage.setResizable(true);
		primaryStage.show();

	}

	public Scene getSceneEDT() {
		return sceneEDT;
	}


	public Scene getSceneParam() {
		return sceneParam;
	}


	public void setWindow(Group root) {
		root.getChildren().clear();
	//	this.creationInterface.setInterface(root, this, solveur);
	}



	public void setParam(Groupe grp, Enseignant ens, Salle salle) {
	}


	public double getWIDTH() {
		return WIDTH;
	}




	public void setWIDTH(Number newSceneWidth) {
		WIDTH = (double) newSceneWidth;
	}



	public double getHEIGHT() {
		return HEIGHT;
	}



	public void setHEIGHT(Number newSceneHeight) {
		HEIGHT = (double) newSceneHeight;
	}

	public CreateMatiereColor getMatCol() {
		return matCol;
	}

	public Stage getStage() {
		return this.stage;
	}

}
