package interfaces.Param.CreateData;

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
import interfaces.Param.visualize.VisuMatiere;
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

public class CreateMatiere {

	private ParamPannel param;
	private String subject="";
	private char currentGrp;
	private VBox mainBox = new VBox();
	private HBox mainHBox = new HBox();
	private VBox secBox = new VBox();
	private int capacity=0;
	private ComboBox<String> addField= new ComboBox<String>();
	private TextField subjectField = new TextField();
	private ArrayList<Character> allAlpha = new ArrayList<Character>(); 
	private ComboBox<Character> grpCombo = new ComboBox<Character>();
	private ComboBox<Character> NOTgrpCombo = new ComboBox<Character>();
	private ArrayList<String> nature =new ArrayList<String>();
	private ArrayList<Character> alphaInGrp = new ArrayList<Character>(); 
	private ArrayList<Character> alphaNotInGrp = new ArrayList<Character>(); 
	private ComboBox<String> supprCombo;
	private Button returnB = new Button("Retour");
	private ArrayList<String> allNatureSalle = new ArrayList<String>();
	private ArrayList<Character> all = new ArrayList<Character>();

	private VisuMatiere visu;

	public CreateMatiere(ParamPannel param) {
		this.param=param;
		initWithDB();
		initialize();
		initializeSalle();
		addAll();

		
		returnB.setLayoutX(50);
		returnB.setLayoutY(50);
		returnB.setOnAction(e -> {
			goBack();
		});
		param.getParam().getRoot().getChildren().addAll(mainHBox,returnB);
	}

	public void goBack() {
		param.getParam().getRoot().getChildren().removeAll(returnB,mainHBox);
		param.getParam().getRoot().getChildren().add(param.getMainVBox());
		this.param.getTableSalle().setAll();
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
	}

	public void initializeSalle() {
		Label grp = new Label(subject);
		grp.setStyle("-fx-font-size:20px;");
		HBox boxGrp = new HBox(grp);
		boxGrp.setAlignment(Pos.CENTER);
		boxGrp.setPadding(new Insets(0,20,20,20));

		this.secBox.getChildren().addAll(boxGrp);
	}



	public void addAll() {

		HBox boxSubject = new HBox();
		Label subjectLabel = new Label("Sujet :");
		subjectLabel.setStyle("-fx-font-weight: bold;");
		boxSubject.setSpacing(20);
		boxSubject.getChildren().addAll(subjectLabel,subjectField);



		Button see = new Button("Visualiser");
		HBox seeBox = new HBox(see);
		seeBox.setPadding(new Insets(20,0,20,0));
		seeBox.setAlignment(Pos.CENTER);

		see.setOnAction(e->{
			this.subject=this.subjectField.getText();
			//System.out.println(this.grpCombo.getSelectionModel().getSelectedItem() + " : " + this.NOTgrpCombo.getSelectionModel().getSelectedItem());
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
			
			if(this.addField.getSelectionModel().getSelectedItem()!=null) {
				this.nature.add(this.addField.getSelectionModel().getSelectedItem());
				this.allNatureSalle.remove(this.addField.getSelectionModel().getSelectedItem());
			}
			
			if(supprCombo.getSelectionModel().getSelectedItem()!=null) {
				this.nature.remove(supprCombo.getSelectionModel().getSelectedItem());
				this.allNatureSalle.add(this.supprCombo.getSelectionModel().getSelectedItem());
			}

			NOTgrpCombo.setItems(getListAlpha(this.alphaInGrp));
			grpCombo.setItems(getListAlpha(this.alphaNotInGrp));
			
			
			this.visu.setAll(this.subject,this.alphaInGrp,this.nature);
			this.addField.getSelectionModel().clearSelection();
			
			supprCombo.setItems(getList(this.nature));
			addField.setItems(getList(this.allNatureSalle));
			grpCombo.getSelectionModel().clearSelection();
			this.supprCombo.getSelectionModel().clearSelection();
		});

		Button saveB = new Button("Sauver les changements");
		HBox buttonBox = new HBox(saveB);
		buttonBox.setAlignment(Pos.CENTER);
		saveB.setOnAction(e->{
			
			if(this.visu.insertAll())goBack();
		});
		saveB.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

		secBox.getChildren().addAll(boxSubject,nature(),groupe(),seeBox,buttonBox);
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

	public HBox nature(){
		Label natureLabel = new Label("Nature :");
		natureLabel.setStyle("-fx-font-weight: bold;");

		VBox vbox = new VBox();
		HBox addBox = new HBox();
		Label addLabel = new Label("Ajouter une nature :");
		addField.setItems(this.getList(this.allNatureSalle));
		Button see1 = new Button("Annuler");
		see1.setOnAction(e->{
			addField.getSelectionModel().clearSelection();
		});
		addBox.getChildren().addAll(addLabel,addField,see1);



		HBox supprBox = new HBox();
		Label supprLabel = new Label("Supprimer une nature :");
		supprCombo = new ComboBox();
		Button seeAdd = new Button("Annuler");
		seeAdd.setOnAction(e->{
			supprCombo.getSelectionModel().clearSelection();
		});
		supprBox.getChildren().addAll(supprLabel,supprCombo,seeAdd);

		supprCombo.setItems(getList(this.nature));

		vbox.getChildren().addAll(addBox,supprBox);
		HBox box = new HBox(natureLabel,vbox);
		box.setSpacing(20);
		addBox.setSpacing(20);
		supprBox.setSpacing(20);
		vbox.setSpacing(20);
		return box;
	}


	public ObservableList<Character> getListAlpha(){
		ObservableList<Character> list = FXCollections.observableArrayList();
		for(Character charact : this.allAlpha) list.add(charact);
		return list;
	}

	public void initWithDB() {
		try {
			ResultSet resultAllAlpha = this.param.getParam().getDb().getModif().selectAllAlphabet();
			while(resultAllAlpha.next()) this.allAlpha.add(resultAllAlpha.getString("alphabet").charAt(0));
			System.out.println(allAlpha);


			for(Character alpha : allAlpha) {
				if(!this.alphaNotInGrp.contains(alpha))alphaNotInGrp.add(alpha);
			}
			grpCombo.setItems(getListAlpha(this.alphaNotInGrp));
			

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
