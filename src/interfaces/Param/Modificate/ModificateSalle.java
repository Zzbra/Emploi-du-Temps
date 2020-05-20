package interfaces.Param.Modificate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.CaseEdTGroupe;
import interfaces.EDT.CreneauxPlanning;
import interfaces.EDT.SizeDisplayOnScene;
import interfaces.Param.ParamPannel;
import interfaces.Param.FillTable.FillDispoEns;
import interfaces.Param.FillTable.FillTableEns;
import interfaces.Param.visualize.VisuEns;
import interfaces.Param.visualize.VisuGrp;
import interfaces.Param.visualize.VisuSalle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ModificateSalle {

	private ParamPannel param;
	private String numSalle;
	private char currentGrp;
	private VBox mainBox = new VBox();
	private HBox mainHBox = new HBox();
	private VBox secBox = new VBox();
	private int capacity=0;
	private ArrayList<String> nature =new ArrayList<String>();
	private ComboBox<String> supprCombo;
	private TextField addField;
	private ArrayList<Character> allAlpha = new ArrayList<Character>(); 
	private ComboBox<Character> grpCombo = new ComboBox<Character>();
	private Button returnB = new Button("Retour");

	private VisuSalle visu;

	public ModificateSalle(ParamPannel param, String groupe) {
		this.numSalle=groupe;
		this.param=param;
		initWithDB();
		initialize();
		initializeSalle();
		addAll();
		
		
		returnB.setLayoutX(50);
		returnB.setLayoutY(50);
		goBack();
		
		param.getParam().getRoot().getChildren().addAll(mainHBox,returnB);
	}
	
	public void goBack() {
		returnB.setOnAction(e -> {
			param.getParam().getRoot().getChildren().removeAll(returnB,mainHBox);
			param.getParam().getRoot().getChildren().add(param.getMainVBox());
			this.param.getTableSalle().setAll();
		});
	}


	public void initialize() {
		initBox();
		param.getParam().getRoot().getChildren().remove(param.getMainVBox());
		
		Label mainLabel = new Label("PARAMETRER");
		mainLabel.setStyle("-fx-font-size:30px; -fx-font-weight: bold;");
		HBox boxVisu = new HBox(mainLabel);
		boxVisu.setAlignment(Pos.CENTER);
		
		secBox.getChildren().addAll(boxVisu);
		mainBox.getChildren().addAll(secBox);
		
		this.visu = new VisuSalle(this.param,this.numSalle,this.capacity,this.nature,currentGrp);
		mainHBox.getChildren().addAll(mainBox,visu.getBox());
	//	mainHBox.getChildren().add(mainBox);
	}

	public void initBox() {

		mainBox.setPrefHeight(this.param.getParam().getMain().getHEIGHT());
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(30);
		
		secBox.setBackground(new Background(new BackgroundFill(Color.rgb(202, 218, 238), CornerRadii.EMPTY, Insets.EMPTY)));
		secBox.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		secBox.setPadding(new Insets(20,20,20,20));
		secBox.setSpacing(20);
		mainHBox.setPrefWidth(this.param.getParam().getMain().getWIDTH());
		mainHBox.setAlignment(Pos.CENTER);
		mainHBox.setSpacing(100);
	}

	public void initializeSalle() {
		Label grp = new Label("Salle "+numSalle);
		grp.setStyle("-fx-font-size:20px;");
		HBox boxGrp = new HBox(grp);
		boxGrp.setAlignment(Pos.CENTER);
		boxGrp.setPadding(new Insets(0,20,20,20));

		this.secBox.getChildren().addAll(boxGrp);
	}
	

	
	public void addAll() {
		Label capLabel = new Label("Capacite :");
		capLabel.setStyle("-fx-font-weight: bold;");
		TextField capEnter = new TextField(String.valueOf(this.capacity));
		HBox capBox = new HBox(capLabel,capEnter);
		capBox.setSpacing(20);
		
		
		
		Button see = new Button("Visualiser");
		HBox seeBox = new HBox(see);
		seeBox.setPadding(new Insets(20,0,20,0));
		seeBox.setAlignment(Pos.CENTER);
		
		see.setOnAction(e->{
			if(this.grpCombo.getSelectionModel().getSelectedIndex()!=-1) this.currentGrp=this.grpCombo.getSelectionModel().getSelectedItem(); 
			if(!this.addField.getText().isEmpty())this.nature.add(this.addField.getText());
			if(supprCombo.getSelectionModel().getSelectedItem()!=null) this.nature.remove(supprCombo.getSelectionModel().getSelectedItem());
			this.visu.setAll(Integer.parseInt(capEnter.getText()),this.nature,this.currentGrp);
			this.addField.clear();
			supprCombo.setItems(getList());
			grpCombo.getSelectionModel().clearSelection();
			this.supprCombo.getSelectionModel().clearSelection();
		});
		
		Button saveB = new Button("Sauver les changements");
		HBox buttonBox = new HBox(saveB);
		buttonBox.setAlignment(Pos.CENTER);
		saveB.setOnAction(e->{
			this.visu.saveAll();
			
		});
		saveB.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		
		secBox.getChildren().addAll(capBox,nature(),groupe(),seeBox,buttonBox);
	}
	
	public HBox groupe() {
		HBox box = new HBox();
		Label natureLabel = new Label("Groupe :");
		natureLabel.setStyle("-fx-font-weight: bold;");
		
		
		grpCombo.setItems(getListAlpha());
		
		box.getChildren().addAll(natureLabel,grpCombo);
		box.setSpacing(20);
		return box;
	}
	
	public HBox nature(){
		Label natureLabel = new Label("Nature :");
		natureLabel.setStyle("-fx-font-weight: bold;");
		
		VBox vbox = new VBox();
		HBox addBox = new HBox();
		Label addLabel = new Label("Ajouter une nature :");
		addField = new TextField();
		
		addBox.getChildren().addAll(addLabel,addField);
		
		
		
		HBox supprBox = new HBox();
		Label supprLabel = new Label("Supprimer une nature :");
		supprCombo = new ComboBox();
		Button seeAdd = new Button("Annuler");
		seeAdd.setOnAction(e->{
			supprCombo.getSelectionModel().clearSelection();
		});
		supprBox.getChildren().addAll(supprLabel,supprCombo,seeAdd);
		
		supprCombo.setItems(getList());
		
		vbox.getChildren().addAll(addBox,supprBox);
		HBox box = new HBox(natureLabel,vbox);
		box.setSpacing(20);
		addBox.setSpacing(20);
		supprBox.setSpacing(20);
		vbox.setSpacing(20);
		return box;
	}
	
	public ObservableList<String> getList(){
		ObservableList<String> list = FXCollections.observableArrayList();
		for(String str : this.nature) list.add(str);
		return list;
	}
	
	public ObservableList<Character> getListAlpha(){
		ObservableList<Character> list = FXCollections.observableArrayList();
		for(Character charact : this.allAlpha) list.add(charact);
		return list;
	}
	
	public void initWithDB() {
		try {
			ResultSet resultCap =this.param.getParam().getDb().getModif().selectCapInSalle(this.numSalle);
			while(resultCap.next()) this.capacity= resultCap.getInt("capacity");
			
			ResultSet resultNature = this.param.getParam().getDb().getModif().selectNatureSalle(numSalle);
			while(resultNature.next()) this.nature.add(resultNature.getString("nature"));
			
			ResultSet resultAllAlpha = this.param.getParam().getDb().getModif().selectAllAlphabet();
			while(resultAllAlpha.next()) this.allAlpha.add(resultAllAlpha.getString("alphabet").charAt(0));
			
			ResultSet resultGrp = this.param.getParam().getDb().getModif().selectGrpSalle(Integer.parseInt(this.numSalle));
			while(resultGrp.next()) this.currentGrp = resultGrp.getString("groupeSalle").charAt(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
