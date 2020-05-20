package interfaces.Param.Modificate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import interfaces.Param.ParamPannel;
import interfaces.Param.FillTable.FillTableEns;
import interfaces.Param.visualize.VisuGrp;
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

public class ModificateGrp {

	private ParamPannel param;
	private String groupe;
	private VBox mainBox = new VBox();
	private HBox mainHBox = new HBox();
	private VBox secBox = new VBox();
	private String specialite;
	private int capacity;
	private String referent;
	private ArrayList<String> listEns =new ArrayList<String>();
	private ArrayList<String> listNotEnsGrp = new ArrayList<String>();
	private VisuGrp visu;
	private ComboBox listEnsForAdd= new ComboBox();
	private ComboBox<String> listEnsForSuppr= new ComboBox();
	ObservableList<String> canSuppr = FXCollections.observableArrayList();
	ObservableList<String> canAdd = FXCollections.observableArrayList();

	public ModificateGrp(ParamPannel param, String groupe) {
		this.groupe=groupe;
		this.param=param;
		initWithDB();
		System.out.println("Modification du groupe : "+groupe);
		initialize();
		initializeGrp();
		addAll();
		
		Button returnB = new Button("Retour");
		returnB.setLayoutX(50);
		returnB.setLayoutY(50);
		returnB.setOnAction(e -> {
			param.getParam().getRoot().getChildren().removeAll(returnB,mainHBox);
			param.getParam().getRoot().getChildren().add(param.getMainVBox());
			this.param.getTableGrp().setAll();
			this.param.getTableEns().setAll();
		});
		
		param.getParam().getRoot().getChildren().addAll(mainHBox,returnB);
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
		this.visu = new VisuGrp(this.param,this.groupe,this.referent,this.specialite);
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

	public void initializeGrp() {
		Label grp = new Label("Groupe "+ groupe);
		grp.setStyle("-fx-font-size:20px;");
		HBox boxGrp = new HBox(grp);
		boxGrp.setAlignment(Pos.CENTER);
		boxGrp.setPadding(new Insets(0,20,20,20));

		this.secBox.getChildren().addAll(boxGrp);
	}
	
	public void addAll() {
		Label capLabel = new Label("Capacite :");
		TextField capEnter = new TextField(String.valueOf(this.capacity));
		HBox capBox = new HBox(capLabel,capEnter);
		capBox.setSpacing(20);
		
		Label specLabel = new Label("Specification :");
		TextField specEnter = new TextField(String.valueOf(this.specialite));
		HBox specBox = new HBox(specLabel,specEnter);
		specBox.setSpacing(20);
		
		Button see = new Button("Visualiser");
		HBox seeBox = new HBox(see);
		seeBox.setAlignment(Pos.CENTER);
		seeBox.setPadding(new Insets(0,0,10,0));
		see.setOnAction(e->{
			this.visu.setCapAndSpec(capEnter.getText(), specEnter.getText());
		});
		
		Button saveB = new Button("Sauver les changements");
		HBox buttonBox = new HBox(saveB);
		buttonBox.setAlignment(Pos.CENTER);
		saveB.setOnAction(e->{
			this.visu.saveAll();
			
		});
		saveB.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		
		secBox.getChildren().addAll(capBox,specBox,seeBox,modifSupr(),modifAdd(),modiRef(),buttonBox);
	}
	
	
	public VBox modifSupr() {
		
		canSuppr.add(this.referent);
		for(String ens : this.listEns) canSuppr.add(ens);
		
		Label suppr = new Label("Supprimer :");
		
		listEnsForSuppr.setItems(canSuppr);
		Button see = new Button("Visualiser");
		see.setOnAction(e->{
			String suppres = listEnsForSuppr.getSelectionModel().getSelectedItem().toString(); 
			System.out.println("Suppresion de "+suppres);
			this.visu.set(suppres,false);
			canSuppr.remove(suppres);
			listEnsForSuppr.setItems(canSuppr);
			listEnsForSuppr.getSelectionModel().clearSelection();
			canAdd.add(suppres);
			listEnsForAdd.setItems(canAdd);
			
		});
		
		HBox box = new HBox(suppr,listEnsForSuppr, see);
		box.setSpacing(10);
		return new VBox(box);
	}
	
	public VBox modifAdd() {
		for(String ens : this.listNotEnsGrp) canAdd.add(ens);
		
		Label addLab = new Label("Ajouter :");
		
		listEnsForAdd.setItems(canAdd);
		Button see = new Button("Visualiser");
		
		see.setOnAction(e->{
			String addStr = listEnsForAdd.getSelectionModel().getSelectedItem().toString();
			System.out.println("Ajout de "+addStr);
			this.visu.set(addStr,true);
			canAdd.remove(addStr);
			listEnsForAdd.setItems(canAdd);
			listEnsForAdd.getSelectionModel().clearSelection();
			canSuppr.add(addStr);
			listEnsForSuppr.setItems(canSuppr);
		});
		
		HBox box = new HBox(addLab,listEnsForAdd, see);
		box.setSpacing(10);
		return new VBox(box);
	}
	
	public VBox modiRef() {
		Label addLab = new Label("Nouveau Referent :");
		ComboBox listEnsForRef= new ComboBox();
		listEnsForRef.setItems(this.canSuppr);
		Button see = new Button("Visualiser");
		see.setOnAction(e->{
			String select = listEnsForRef.getSelectionModel().getSelectedItem().toString();
			System.out.println("Nouveau referent :" + select);
			this.visu.setRef(select);
		});
		
		HBox box = new HBox(addLab,listEnsForRef, see);
		box.setSpacing(10);
		return new VBox(box);
	}
	
	public void initWithDB() {
		try {
			ResultSet resultMod =this.param.getParam().getDb().getModif().selectAllInGroupe(this.groupe.charAt(0),this.groupe.charAt(1));
			ResultSet resultFill1 = this.param.getParam().getDb().getFill().selectEnsJustRef(this.groupe);
			ResultSet resultFill2 = this.param.getParam().getDb().getFill().selectEnsWORef(this.groupe);
			ResultSet resultMod2 = this.param.getParam().getDb().getFill().selectAllEnsWithoutGrp();
			while(resultMod.next()) {
				this.specialite=resultMod.getString("specificity");
				this.capacity=resultMod.getInt("capacity");
			}
			while(resultFill1.next()) {
				this.referent = resultFill1.getString("name");
			}
			while(resultFill2.next()) {
				this.listEns.add(resultFill2.getString("name"));
			}
			while(resultMod2.next()) {
				this.listNotEnsGrp.add(resultMod2.getString("name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getList() {
		String str = new String();
		for(String ens : this.listEns) {
			str=str+""+ens+" , ";
		}
		return str.substring(0,str.length()-2);
	}
}
