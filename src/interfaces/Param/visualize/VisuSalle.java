package interfaces.Param.visualize;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import interfaces.Param.ParamPannel;
import interfaces.Param.FillTable.FillTableEns;
import interfaces.Param.FillTable.FillTableGrp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class VisuSalle {

	private VBox mainBox = new VBox();
	private HBox mainH = new HBox();
	private VBox mainV = new VBox();
	private String salle;
	private Label numLabel = new Label("Salle ");
	private Label capLabel = new Label();
	private Label natLabel = new Label();
	private ParamPannel param;
	private Label grpLabel = new Label();
	private ArrayList<String> natures = new ArrayList<String>();
	
	public VisuSalle(ParamPannel param, String groupe,int capacity,ArrayList<String> natures, char currentGrp) {
		System.out.println(natures);
		this.natures=natures;
		this.salle=groupe;
		this.param=param;
		capLabel.setText(String.valueOf(capacity));
		natLabel.setText(getListNature(natures));
		grpLabel.setText(String.valueOf(currentGrp));
		param.getParam().getRoot().getChildren().remove(param.getMainVBox());
		mainV.setLayoutX(100);
		mainV.setLayoutY(100);

		initialize();
		mainV.getChildren().add(this.addAll());
		mainBox.getChildren().add(mainV);
		mainH.getChildren().add(mainBox);

		

		param.getParam().getRoot().getChildren().addAll(mainH);
	}
	
	private String getListNature(ArrayList<String> natures) {
		String str="";
		for(String nature : natures) str=str+""+nature+" , ";
		
		if(str.length()>2) return str.substring(0,str.length()-2);
		return str;
	}

	public void initialize() {
		initBox();

		Label mainLabel = new Label("VISUALISATION");
		mainLabel.setStyle("-fx-font-size:30px; -fx-font-weight: bold;");
		HBox boxVisu = new HBox(mainLabel);
		boxVisu.setAlignment(Pos.CENTER);
		
		
		numLabel.setStyle("-fx-font-size:20px;");
		HBox boxGrp = new HBox(numLabel);
		boxGrp.setAlignment(Pos.CENTER);
		boxGrp.setPadding(new Insets(0,20,20,20));
		
		this.mainV.getChildren().addAll(boxVisu,boxGrp);
	}

	public void initBox() {

		mainV.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		mainV.setPadding(new Insets(20,40,20,40));
		mainBox.setPrefHeight(this.param.getParam().getMain().getHEIGHT());
		mainBox.setAlignment(Pos.CENTER);

		mainH.setPrefWidth(this.param.getParam().getMain().getWIDTH());
		mainH.setAlignment(Pos.CENTER);

		mainV.setBackground(new Background(new BackgroundFill(Color.rgb(202, 218, 238), CornerRadii.EMPTY, Insets.EMPTY)));
	}



	public VBox getBox() {
		return this.mainBox;
	}

	
	public VBox addAll() {
		VBox mainB = new VBox();
		mainB.setSpacing(20);

		HBox ensH = new HBox();
		ensH.setSpacing(20);
		Label ensL = new Label("Capacite :");
		ensL.setStyle("-fx-font-weight: bold;");
		ensH.getChildren().addAll(ensL,this.capLabel);
		
		HBox natureBox = new HBox();
		natureBox.setSpacing(20);
		Label natureL = new Label("Nature :");
		natureL.setStyle("-fx-font-weight: bold;");
		natureBox.getChildren().addAll(natureL,natLabel);
		
		HBox grpBox = new HBox();
		grpBox.setSpacing(20);
		Label grpLab = new Label("Groupe :");
		grpLab.setStyle("-fx-font-weight: bold;");
		grpBox.getChildren().addAll(grpLab,this.grpLabel);
		
		mainB.getChildren().addAll(ensH,natureBox,grpBox);
		
		
		return mainB;
	}

	
	
	
	public void saveAll() {
		int capacite = Integer.parseInt(this.capLabel.getText());
		ArrayList<String> nature = this.natures;
		char grp = this.grpLabel.getText().charAt(0);
		
		try {
			if(capacite>=0 && ! nature.isEmpty()) {
				this.param.getParam().getDb().getModif().updateSalle(Integer.valueOf(this.salle), capacite,nature,grp);
				new Alert(Alert.AlertType.CONFIRMATION,"La salle "+this.salle+ " a bien été modifiée.").show();
			}
			else {
				new Alert(Alert.AlertType.ERROR,"Vous devez remplir tout les champs.").show();
			}
			
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR,"Erreur possible : Tout les champs ne sont pas remplis.").show();
		}
		
	}
	
	public void setAll(int capacity,ArrayList<String> nat,char currentGrp) {
		this.capLabel.setText(String.valueOf(capacity));
		natLabel.setText(getListNature(nat));
		grpLabel.setText(String.valueOf(currentGrp));
	}

	public boolean insertAll() {
		int capacite = Integer.parseInt(this.capLabel.getText());
		ArrayList<String> nature = this.natures;
		String grp = this.grpLabel.getText();
		try {
			if(!this.salle.isEmpty() &&capacite>=0 && ! nature.isEmpty()) {
				this.param.getParam().getDb().getModif().insertSalle(Integer.parseInt(this.salle), capacite, nature, grp.charAt(0));
				new Alert(Alert.AlertType.CONFIRMATION,"La salle "+this.salle+ " a bien été ajoutée.").show();
				return true;
			}
			else {
				new Alert(Alert.AlertType.ERROR,"Vous devez remplir tout les champs.").show();
			}
			
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR,"Erreur possible : La salle "+salle+" existe deja. \n Tout les champs ne sont pas remplis.").show();
		}
		return false;
	}

	public void setAll(String text, int capacity, ArrayList<String> nat, char currentGrp) {
		this.capLabel.setText(String.valueOf(capacity));
		natLabel.setText(getListNature(nat));
		grpLabel.setText(String.valueOf(currentGrp));
		this.salle=text;
		this.numLabel.setText("Salle "+salle);
		
	}
}
