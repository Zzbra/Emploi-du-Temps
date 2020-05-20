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

public class VisuMatiere {

	private VBox mainBox = new VBox();
	private HBox mainH = new HBox();
	private VBox mainV = new VBox();
	private String subject;
	private ParamPannel param;
	private Label grpMat = new Label();
	private Label natMat = new Label();
	private ArrayList<Character> allGroupe = new ArrayList<Character>();
	private ArrayList<String> allNature = new ArrayList<String>();
	private Label mat = new Label();
	


	public VisuMatiere(ParamPannel param,String subject,ArrayList<Character> grp,ArrayList<String> natures) {
		this.subject=subject;
		this.param=param;
		this.grpMat.setText(this.getStringFromArrayChar(grp));
		this.natMat.setText(getStringFromArrayString(natures));
		this.allGroupe=grp;
		this.allNature=natures;
		
		
		param.getParam().getRoot().getChildren().remove(param.getMainVBox());
		mainV.setLayoutX(100);
		mainV.setLayoutY(100);

		initialize();
		initMat();
		
		mainV.getChildren().add(this.addAll());
		mainBox.getChildren().add(mainV);
		mainH.getChildren().add(mainBox);

		

		param.getParam().getRoot().getChildren().addAll(mainH);
	}

	public void initialize() {
		initBox();

		Label mainLabel = new Label("VISUALISATION");
		mainLabel.setStyle("-fx-font-size:30px; -fx-font-weight: bold;");
		HBox boxVisu = new HBox(mainLabel);
		boxVisu.setAlignment(Pos.CENTER);
		this.mainV.getChildren().add(boxVisu);
	}
	
	public void initMat() {
		
		mat.setStyle("-fx-font-size:20px;");
		HBox boxGrp = new HBox(mat);
		boxGrp.setAlignment(Pos.CENTER);
		boxGrp.setPadding(new Insets(5,0,20,0));
		this.mainV.getChildren().addAll(boxGrp);
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
		Label ensL = new Label("Groupes : ");
		ensL.setStyle("-fx-font-weight: bold;");
		ensH.getChildren().addAll(ensL,grpMat);
		
		HBox natH = new HBox();
		natH.setSpacing(20);
		Label natL = new Label("Nature : ");
		natL.setStyle("-fx-font-weight: bold;");
		natH.getChildren().addAll(natL,natMat);

		mainB.getChildren().addAll(ensH,natH);
		return mainB;
	}

	
	
	public void saveAll() {

		try {
			if(this.subject.length()>0  && !this.allNature.isEmpty()) {
				this.param.getParam().getDb().getModif().updateMatiere(this.subject,this.allGroupe,this.allNature);
				new Alert(Alert.AlertType.CONFIRMATION,"La matière "+this.subject+ " a bien été modifiée.").show();
			}
			else new Alert(Alert.AlertType.ERROR,"Erreurs possibles : \n Vous n'avez pas visualiser. \n Il doit y avoir au moins une nature.").show();
			
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR,"Erreurs possible : Vous n'avez pas visualiser. \n Il doit y avoir au moins une nature.").show();
		}

	}
	
	public void setAll(ArrayList<Character> alphaNotInGrp,ArrayList<String> natures) {
		this.grpMat.setText(getStringFromArrayChar(alphaNotInGrp));
		this.natMat.setText(getStringFromArrayString(natures));
		this.allGroupe=alphaNotInGrp;
		this.allNature=natures;
	}
	
	private String getStringFromArrayChar(ArrayList<Character> list) {
		String str = "";
		for(Character s : list) str=str+""+s+" , ";
		if(str.length()>2) return str.substring(0,str.length()-2);
		else return str;
	}
	
	private String getStringFromArrayString(ArrayList<String> list) {
		String str = "";
		for(String s : list) str=str+""+s+" , ";
		if(str.length()>2) return str.substring(0,str.length()-2);
		else return str;
	}

	public boolean insertAll() {
		try {
			if(this.subject.length()>0  && !this.allNature.isEmpty()) {
				this.param.getParam().getDb().getModif().insertMatiere(this.subject,this.allGroupe,this.allNature);
				new Alert(Alert.AlertType.CONFIRMATION,"La matière "+this.subject+ " a bien été ajoutée.").show();
				this.param.getTableMatiere().setAll();
				return true;
			}
			else new Alert(Alert.AlertType.ERROR,"Erreurs possibles : \n Vous n'avez pas visualiser. \n Il doit y avoir au moins une nature.").show();
			
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR,"Erreur possible : La matiere "+this.subject+" existe deja. \n Il doit y avoir au moins une nature.").show();
		}
		return false;
	}
	

	public void setAll(String subject, ArrayList<Character> alphaNotInGrp,ArrayList<String> natures) {
		this.grpMat.setText(getStringFromArrayChar(alphaNotInGrp));
		this.natMat.setText(getStringFromArrayString(natures));
		this.allGroupe=alphaNotInGrp;
		this.allNature=natures;
		mat.setText(subject);
		this.subject=subject;
	}
}
