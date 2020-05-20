package interfaces.Param;

import javax.swing.BorderFactory;

import application.CreateProblem;
import application.Enseignant;
import application.Groupe;
import application.Probleme;
import application.Salle;
import application.Solution;
import application.SolutionEdt;
import application.Solveur;
import interfaces.EDT.SizeDisplayOnScene;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ParamMenu {

	private ParamInterface param;
	private Group root;
	private Probleme instance;
	private TreeView treeView = new TreeView();
	private MenuBar menu;
	private ParamEDT EDTParam;
	private SolutionEdt solutionEdt;

	public ParamMenu(ParamInterface param) {
		this.param=param;
		this.root=param.getRoot();
		menuBox();
	}




	public void menuBox() {
		HBox boxMenu = new HBox();
		
		Button seeEDT = new Button("Voir les EDT");
		seeEDT.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> edition());
		
		Button server = new Button("Calculer EDT");
		server.addEventFilter(MouseEvent.MOUSE_CLICKED, eventFilter -> createNewEDT());
		
		Button paramInfo = new Button("Paramètres");
		paramInfo.addEventFilter(MouseEvent.MOUSE_CLICKED, eventFilter -> {
			this.param.getRoot().getChildren().remove(this.EDTParam.getMainVBox());
			this.param.setParam(new ParamPannel(this.param));
		});
		
		Button paramEDT = new Button("Paramètres EDT");
		paramEDT.addEventFilter(MouseEvent.MOUSE_CLICKED, eventFilter -> {
			setEDTParam(new ParamEDT(this.param));
		});
		
		boxMenu.getChildren().addAll(seeEDT,server,paramInfo,paramEDT);
		
		param.getRoot().getChildren().add(boxMenu);
		
	}
		
	
	private Object createNewEDT() {
		CreateProblem createpProblem = new CreateProblem(this.param.getDb());
		Solveur solveur=createpProblem.getSolveur();
		this.solutionEdt = solveur.getSolutionEdt();
		return null;
	}



	private Object edition() {
		if(this.solutionEdt==null) {
			new Alert(AlertType.ERROR,"Vous devez générer les emplois du temps avant de pouvoir les consulter").show();
		}
		else {
			System.out.println("Dans EDT");
			param.getMain().getStage().setScene(param.getMain().getSceneEDT());
		}
		
		return null;
	}

	public TreeView getTreeView() {
		return this.treeView;
	}

	public MenuBar getMenu() {
		return this.menu;
	}



	public ParamEDT getEDTParam() {
		return EDTParam;
	}



	public void setEDTParam(ParamEDT eDTParam) {
		EDTParam = eDTParam;
	}

}
