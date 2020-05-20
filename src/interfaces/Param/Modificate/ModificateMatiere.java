package interfaces.Param.Modificate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import interfaces.Param.ParamPannel;
import interfaces.Param.FillTable.FillTableEns;
import interfaces.Param.visualize.VisuGrp;
import interfaces.Param.visualize.VisuMatiere;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ModificateMatiere {

	private ParamPannel param;
	private String subject;
	private VBox mainBox = new VBox();
	private HBox MAINBOX = new HBox();
	private HBox mainHBox = new HBox();
	private VBox secBox = new VBox();
	private ArrayList<Character> alphaInGrp = new ArrayList<Character>(); 
	private ArrayList<Character> alphaNotInGrp = new ArrayList<Character>(); 
	private VisuMatiere visu;
	private ComboBox<Character> grpCombo = new ComboBox<Character>();
	private ComboBox<Character> NOTgrpCombo = new ComboBox<Character>();
	private ArrayList<String> nature =new ArrayList<String>();
	private ComboBox<String> supprCombo;
	private ComboBox<String> addField= new ComboBox<String>();
	private ArrayList<String> allNatureSalle = new ArrayList<String>();
	private ArrayList<Character> all = new ArrayList<Character>();


	public ModificateMatiere(ParamPannel param, String subject) {
		this.subject=subject;
		this.param=param;
		initWithDB();
		System.out.println("Modification du groupe : "+subject);
		initialize();
		initializeGrp();
		addAll();

		Button returnB = new Button("Retour");
		returnB.setLayoutX(50);
		returnB.setLayoutY(50);
		returnB.setOnAction(e -> {
			param.getParam().getRoot().getChildren().removeAll(returnB,MAINBOX);
			param.getParam().getRoot().getChildren().add(param.getMainVBox());
			this.param.getTableMatiere().setAll();
		});
		MAINBOX.getChildren().add(mainHBox);
		param.getParam().getRoot().getChildren().addAll(MAINBOX,returnB);
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
		this.visu = new VisuMatiere(this.param,this.subject,this.alphaInGrp,this.nature);
		mainHBox.getChildren().addAll(mainBox,visu.getBox());
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
		
		MAINBOX.setAlignment(Pos.CENTER);
	}

	public void initializeGrp() {
		Label grp = new Label(subject);
		grp.setStyle("-fx-font-size:20px;");
		HBox boxGrp = new HBox(grp);
		boxGrp.setAlignment(Pos.CENTER);
		boxGrp.setPadding(new Insets(0,20,20,20));

		this.secBox.getChildren().addAll(boxGrp);
	}

	public void addAll() {

		Button see = new Button("Visualiser");
		HBox seeBox = new HBox(see);
		seeBox.setAlignment(Pos.CENTER);
		seeBox.setPadding(new Insets(0,0,10,0));
		see.setOnAction(e->{
			
			
			if(this.grpCombo.getSelectionModel().getSelectedItem()!=null) {
				if(this.grpCombo.getSelectionModel().getSelectedItem().equals('I')) {  //Si on ajoute I
					this.alphaInGrp.clear();
					this.alphaNotInGrp.clear();
					for(Character charac : this.all) this.alphaInGrp.add(charac);
					
				}else {
					this.alphaInGrp.add(this.grpCombo.getSelectionModel().getSelectedItem());
					this.alphaNotInGrp.remove(this.grpCombo.getSelectionModel().getSelectedItem());
				}
		
			}
			if(this.NOTgrpCombo.getSelectionModel().getSelectedItem()!=null) {
				if(this.NOTgrpCombo.getSelectionModel().getSelectedItem().equals('I')) {//Si on supprimer I
					this.alphaInGrp.clear();
					for(Character charac : this.all) this.alphaInGrp.add(charac);
					
				}
				else {
					this.alphaNotInGrp.add(this.NOTgrpCombo.getSelectionModel().getSelectedItem());
					this.alphaInGrp.remove(this.NOTgrpCombo.getSelectionModel().getSelectedItem());
				}
				
				
			}
			if(addField.getSelectionModel().getSelectedItem()!=null) {
				
				this.nature.add(addField.getSelectionModel().getSelectedItem());
				this.allNatureSalle.remove(addField.getSelectionModel().getSelectedItem());
			}
			if(supprCombo.getSelectionModel().getSelectedItem()!=null) {
				
				this.allNatureSalle.add(supprCombo.getSelectionModel().getSelectedItem());
				this.nature.remove(supprCombo.getSelectionModel().getSelectedItem());
			}
			this.supprCombo.setItems(getList(this.nature));
			
			addField.getSelectionModel().clearSelection();
			this.supprCombo.getSelectionModel().clearSelection();
			this.NOTgrpCombo.getSelectionModel().clearSelection();
			this.grpCombo.getSelectionModel().clearSelection();
			NOTgrpCombo.setItems(getListAlpha(this.alphaInGrp));
			grpCombo.setItems(getListAlpha(this.alphaNotInGrp));
			addField.setItems(getList(this.allNatureSalle));
			
			this.visu.setAll(this.alphaInGrp,this.nature);
		});

		Button saveB = new Button("Sauver les changements");
		HBox buttonBox = new HBox(saveB);
		buttonBox.setAlignment(Pos.CENTER);
		saveB.setOnAction(e->{
			this.visu.saveAll();

		});
		saveB.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

		secBox.getChildren().addAll(groupe(),nature(),seeBox,buttonBox);
	}

	

	public HBox groupe(){
		HBox boxGrp = new HBox();
		Label natureLabel = new Label("Groupe :");
		natureLabel.setStyle("-fx-font-weight: bold;");

		VBox addSupprBox = new VBox();
		HBox addBox = new HBox();
		Label addLab = new Label("Ajouter :");
		addBox.getChildren().addAll(addLab,grpCombo);

		Label supprLab = new Label("Supprimer");
		HBox supprBox = new HBox();
		supprBox.getChildren().addAll(supprLab,NOTgrpCombo);

		NOTgrpCombo.setItems(getListAlpha(this.alphaInGrp));
		grpCombo.setItems(getListAlpha(this.alphaNotInGrp));

		boxGrp.setSpacing(20);
		addSupprBox.setSpacing(20);
		addBox.setSpacing(20);
		supprBox.setSpacing(20);
		addSupprBox.getChildren().addAll(addBox,supprBox);
		boxGrp.getChildren().addAll(natureLabel,addSupprBox);
		return boxGrp;
	}
	

	public HBox nature(){
		Label natureLabel = new Label("Nature :");
		natureLabel.setStyle("-fx-font-weight: bold;");

		VBox vbox = new VBox();
		HBox addBox = new HBox();
		Label addLabel = new Label("Ajouter une nature :");
		
		addField.setItems(getList(this.allNatureSalle));
		Button annulAdd = new Button("Annuler");
		annulAdd.setOnAction(e->{
			addField.getSelectionModel().clearSelection();
		});

		addBox.getChildren().addAll(addLabel,addField,annulAdd);



		HBox supprBox = new HBox();
		Label supprLabel = new Label("Supprimer une nature :");
		supprCombo = new ComboBox();
		Button annulSuppr = new Button("Annuler");
		annulSuppr.setOnAction(e->{
			supprCombo.getSelectionModel().clearSelection();
		});
		supprBox.getChildren().addAll(supprLabel,supprCombo,annulSuppr);

		supprCombo.setItems(getList(this.nature));

		vbox.getChildren().addAll(addBox,supprBox);
		HBox box = new HBox(natureLabel,vbox);
		box.setSpacing(20);
		addBox.setSpacing(20);
		supprBox.setSpacing(20);
		vbox.setSpacing(20);
		return box;
	}

	public ObservableList<Character> getListAlpha(ArrayList<Character> listArray){
		ObservableList<Character> list = FXCollections.observableArrayList();
		for(Character charact : listArray) list.add(charact);
		return list;
	}

	public ObservableList<String> getList(ArrayList<String> arrayList){
		ObservableList<String> list = FXCollections.observableArrayList();
		for(String str : arrayList) list.add(str);
		return list;
	}

	public void initWithDB() {
		try {
			ResultSet resultAllAlpha = this.param.getParam().getDb().getModif().selectGrpMatiere(this.subject);
			while(resultAllAlpha.next()) this.alphaInGrp.add(resultAllAlpha.getString("alphabet").charAt(0));

			ResultSet resultNotAlpha = this.param.getParam().getDb().getModif().selectNOTGrpMatiere(this.subject);
			while(resultNotAlpha.next()) this.alphaNotInGrp.add(resultNotAlpha.getString("alphabet").charAt(0));

			ResultSet resultNature = this.param.getParam().getDb().getModif().selectNatureMatiere(this.subject);
			while(resultNature.next()) this.nature.add(resultNature.getString("nature"));
			
			ResultSet resultAllNature = this.param.getParam().getDb().getModif().selectAllNatureSalle();
			while(resultAllNature.next()) {
				if(!this.nature.contains(resultAllNature.getString("nature")))
				this.allNatureSalle.add(resultAllNature.getString("nature"));
			}
			ResultSet resultAll = this.param.getParam().getDb().getModif().selectAllGrpWOI();
			while(resultAll.next()) {
				all.add(resultAll.getString("alphabet").charAt(0));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
