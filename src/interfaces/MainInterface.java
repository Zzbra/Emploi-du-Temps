package interfaces;


import application.*;
import interfaces.ColorMat.CreateMatiereColor;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
public class MainInterface extends Application {

	private double WIDTH = Screen.getPrimary().getBounds().getWidth();
	private double HEIGHT = Screen.getPrimary().getVisualBounds().getHeight()-40;
	
	private MainInterface mainInterface;
	private CreateMatiereColor matCol;
	private Groupe groupe=null;
	private Enseignant enseignant=null;
	private Salle salle=null;
	private Solveur solveur;
	private UserMenu userMenu = null;

	public static void main(String[] args) {
		Application.launch(MainInterface.class, args);
	}



	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Emploi du temps : Ecole de la deuxieme chance Marseille");
		
		Group root = new Group();
		Solution solution = new Solution();
		this.solveur=solution.getSolveur();
		this.matCol=new CreateMatiereColor(solveur);

		
		ScrollPane scroll = new ScrollPane();
		scroll.setStyle("-fx-background: rgb(76, 130, 154);");
		scroll.setContent(root);
		Scene scene = new Scene(scroll, WIDTH,HEIGHT);
		
		
		primaryStage.widthProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
				primaryStage.setWidth((double) newSceneWidth);
				setWIDTH(newSceneWidth);
				setWindow(root);
			}
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
				setHEIGHT(newSceneHeight);
				setWindow(root);
			}
		});




		this.mainInterface=this;
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public void setWindow(Group root) {
		root.getChildren().clear();
		new ResourcePanel(root,mainInterface,solveur);
		if(this.groupe!=null)  this.userMenu.setAll(root,mainInterface,this.groupe,null,null,solveur);
		else if(this.enseignant!=null) this.userMenu.setAll(root,mainInterface,null,this.enseignant,null,solveur);
		else if(this.salle!=null)  this.userMenu.setAll(root,mainInterface,null,null,this.salle,solveur);
		else if(this.groupe==null && this.enseignant==null && this.salle==null) this.userMenu = new UserMenu(root,mainInterface,solveur);
	}
	
	

	public void setParam(Groupe grp, Enseignant ens, Salle salle) {
		this.groupe=grp;
		this.enseignant=ens;
		this.salle=salle;
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






}
