package interfaces.Param.visualize;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import interfaces.Param.ParamEDT;
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

public class VisuActivite {

	private VBox mainBox = new VBox();
	private HBox mainH = new HBox();
	private VBox mainV = new VBox();
	private char letter;
	private char number;
	

	private Label grp;
	private Label spec;
	private String listProf ="";
	private Label prof;
	private ParamEDT param;
	private ObservableList<FillTableGrp> data = FXCollections.observableArrayList();
	private ObservableList<FillTableEns> dataEns = FXCollections.observableArrayList();
	private TableView table;
	private ArrayList<FillTableEns> tableEns = new ArrayList<FillTableEns>();
	private Label capacite = new Label();



	public VisuActivite(ParamEDT param, char grp2, char number) {
		this.letter=grp2;
		this.number=number;
		this.param=param;
		mainV.setLayoutX(100);
		mainV.setLayoutY(100);

		initialize();
		initializeGrp(false);
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

	public void initBox() {

		mainV.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		mainV.setPadding(new Insets(20,40,20,40));
	//	mainBox.setPrefHeight(this.param.getParam().getMain().getHEIGHT());
		mainBox.setAlignment(Pos.CENTER);

		mainH.setPrefWidth(this.param.getParam().getMain().getWIDTH());
		mainH.setAlignment(Pos.CENTER);

		mainV.setBackground(new Background(new BackgroundFill(Color.rgb(202, 218, 238), CornerRadii.EMPTY, Insets.EMPTY)));
	}

	public void initializeGrp(boolean ensAss) {
		this.grp = new Label("Groupe "+ letter+number);
		grp.setStyle("-fx-font-size:20px;");
		HBox boxGrp = new HBox(grp);
		boxGrp.setAlignment(Pos.CENTER);

	
		

		this.mainV.getChildren().addAll(boxGrp);


	}

	public void initializeSubGrp() {
		Label grp = new Label("Sous-groupes");
		grp.setStyle("-fx-font-size:20px;");
		HBox boxGrp = new HBox(grp);
		boxGrp.setAlignment(Pos.CENTER);
		boxGrp.setPadding(new Insets(0,0,20,0));

		this.mainV.getChildren().addAll(boxGrp,createTable());
	}

	public TableView tableVisu() {
		table=new TableView();
		//table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		TableColumn prof = new TableColumn("Enseignants");
		TableColumn fonction = new TableColumn("Fonctions");
		//prof.prefWidthProperty().bind(table.widthProperty().divide(2));
		fonction.prefWidthProperty().bind(table.widthProperty().subtract(prof.widthProperty()).subtract(4));
		prof.setCellValueFactory(new PropertyValueFactory<FillTableEns, String>("name"));
		fonction.setCellValueFactory(new PropertyValueFactory<FillTableEns, String>("fonction"));
		

		table.getColumns().addAll(prof,fonction);
		
		table.setItems(dataEns);
		table.setPrefHeight(30*(this.dataEns.size()+1));
		return table;
	}

	public TableView createTable() {
		table = new TableView();

		TableColumn name = new TableColumn("Nom");
		TableColumn referent = new TableColumn("Referent");
		TableColumn cap = new TableColumn("Capacite");

		name.prefWidthProperty().bind(table.widthProperty().divide(3));
		cap.prefWidthProperty().bind(table.widthProperty().divide(3));
		referent.prefWidthProperty().bind(table.widthProperty().divide(3));

		name.setCellValueFactory(new PropertyValueFactory<FillTableGrp, String>("groupe"));
		referent.setCellValueFactory(new PropertyValueFactory<FillTableGrp, String>("referent"));
		cap.setCellValueFactory(new PropertyValueFactory<FillTableGrp, String>("specificite"));

		table.getColumns().addAll(name,referent,cap);




		return table;
	}

	public VBox getBox() {
		return this.mainBox;
	}

	public void setGroupe(String letter,String specificite,ArrayList<String> listEns) {
		//this.letter=letter;
		this.grp.setText("Groupe "+letter);
		this.spec.setText(specificite);

		for(String ens : listEns) {
			listProf=listProf+""+ens+" , ";
		}
		listProf=listProf.substring(0,listProf.length()-2);
		this.prof.setText("Enseignants assignés : "+this.listProf);
	}

	public VBox addAll() {
		VBox mainB = new VBox();

		HBox ensH = new HBox();
		ensH.setSpacing(20);
		Label ensL = new Label("Enseignants :");
		ensL.setStyle("-fx-font-weight: bold;");
		this.tableVisu();
		ensH.getChildren().addAll(ensL,table);
		mainB.getChildren().add(ensH);

		return mainB;
	}

	public void set(String name,boolean forAdd) {
		FillTableEns forSet =null;
		if(forAdd) {
		this.dataEns.add(new FillTableEns(name,"professeur"));
		this.tableEns.add(new FillTableEns(name,"professeur"));
		}
		else {
			this.dataEns.clear();
			for(FillTableEns ens : this.tableEns) {
				if(!name.equals(ens.getName())) {
					this.dataEns.add(ens);	
				}
				else {
					forSet=ens;
				}
			}
			this.tableEns.remove(forSet);
		}
		table.setPrefHeight(35*(this.dataEns.size()+1));
		this.table.setItems(dataEns);
	}
	
	public void setRef(String name) {
		this.dataEns.clear();
		for(FillTableEns ens : this.tableEns) {
			if(name.equals(ens.getName())) {
				ens.setFonction("referent");
			}
			else ens.setFonction("professeur");
			this.dataEns.add(ens);
		}
		this.table.setItems(dataEns);
		table.setPrefHeight(30*(this.dataEns.size()+1));
	}
	
	public void setCapAndSpec(String cap, String spec){
		this.spec.setText(spec);
		this.capacite.setText("Capacite : "+cap);
	}
	
	
	public void saveAll() {
		int capacite=0;
		if(this.capacite.getText().length()>11)capacite= Integer.parseInt(this.capacite.getText().substring(11));
		String specification = this.spec.getText();
		System.out.println(this.grp.getText()+" : "+capacite + " " + specification );
		for(FillTableEns ens : this.dataEns) System.out.println(ens.getName() + " : " +ens.getFonction());
		try {
			System.out.println(capacite +" : " + specification.isEmpty() +" : "+ this.dataEns.size());
			if(capacite!=0 && !specification.isEmpty() && this.dataEns.size()>=3) {
				this.param.getParam().getDb().getModif().setAll(this.grp.getText(), capacite, specification,this.dataEns);
				new Alert(Alert.AlertType.CONFIRMATION,"Les changements ont ete effectues").show();
			}
			else new Alert(Alert.AlertType.ERROR,"Erreur possible : Tout les champs doivent être remplis. \n Un groupe doit avoir au moins 3 enseignants.").show();
			
		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR,"Erreur possible : Tout les champs doivent être remplis. \n Un groupe doit avoir au moins 3 enseignants.").show();
		}
	}
}
