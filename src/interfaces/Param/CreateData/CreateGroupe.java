package interfaces.Param.CreateData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import interfaces.Param.ParamPannel;
import interfaces.Param.FillTable.FillTableEns;
import interfaces.Param.FillTable.FillTableGrp;
import interfaces.Param.visualize.VisuGrp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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
import javafx.scene.paint.Paint;

public class CreateGroupe {
	private ParamPannel param;
	private TextField name;
	private TextField specificity;
	private ArrayList<String> nameEns = new ArrayList<String>();
	private ArrayList<RadioButton> radioArray = new ArrayList<RadioButton>();
	private VisuGrp visu;
	private ComboBox combo[] = new ComboBox[3];
	private TextField enterCap[] = new TextField[3];
	private Button returnB = new Button("Retour");
	private HBox mainB = new HBox();

	public CreateGroupe(ParamPannel paramPannel) {
		initialize();
		this.param=paramPannel;
		this.visu = new VisuGrp(paramPannel,"",combo,enterCap);
		paramPannel.getParam().getRoot().getChildren().remove(paramPannel.getMainVBox());

		
		returnB.setLayoutX(50);
		returnB.setLayoutY(50);
		
		mainB.setLayoutX(100);
		mainB.setLayoutY(100);
		mainB.setSpacing(70);
		
		mainB.setPrefHeight(700);
		mainB.getChildren().addAll(create(),visu.getBox());
		
		paramPannel.getParam().getRoot().getChildren().addAll(returnB,mainB);
		returnB.setOnAction(e -> {
			goBack();
		});

	}
	
	public void goBack() {
		param.getParam().getRoot().getChildren().removeAll(returnB,mainB);
		param.getParam().getRoot().getChildren().add(param.getMainVBox());
		this.param.getTableGrp().setAll();
		this.param.getTableEns().setAll();
	}
	
	public void initialize() {
		for(int i=0; i<3;i++) {
			this.enterCap[i] = new TextField();
			this.combo[i] = new ComboBox();
		}
	}


	public VBox create() {
		Label mainLabel = new Label("CREATION");
		mainLabel.setStyle("-fx-font-size:30px; -fx-font-weight: bold;");
		HBox boxVisu = new HBox(mainLabel);
		boxVisu.setAlignment(Pos.CENTER);
		boxVisu.setPadding(new Insets(0,0,20,0));
		
		VBox mainVBox = new VBox();
		mainVBox.setBackground(new Background(new BackgroundFill(Color.rgb(202, 218, 238), CornerRadii.EMPTY, Insets.EMPTY)));
		mainVBox.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		
		HBox mainBox = new HBox();
		mainBox.setLayoutX(100);
		mainBox.setLayoutY(100);
		mainBox.setSpacing(70);
		
	
		VBox box = new VBox();
		box.setPadding(new Insets(20,0,20,20));
		box.setSpacing(30);
		
		Label titleLab = new Label("Groupe");
		HBox title = new HBox(titleLab);
		title.setAlignment(Pos.CENTER);
		titleLab.setStyle("-fx-font-size:20px;");
		
		box.getChildren().addAll(title,nameGrp(),ensGrp(),specifGrp(),createGrpButton());
		

		mainBox.getChildren().addAll(box,subGrp());
		
		mainVBox.getChildren().addAll(boxVisu,mainBox);
		return mainVBox;
	}

	private HBox createGrpButton() {
		HBox box = new HBox();
		Button createGrp = new Button("Creer le groupe");
		createGrp.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(createGrp);
		createGrp.setOnAction(e->{
			
			try {
				
					nameEns.clear();
					for(RadioButton b : radioArray) {
						if(b.isSelected()) nameEns.add(b.getId());
					}
					if(nameEns.size()>=3 && !this.specificity.getText().isEmpty()) {
						this.param.getParam().getDb().getFill().insertIntoGroupe(this.name.getText(), this.specificity.getText(), nameEns);
						this.visu.setGroupe(this.name.getText(),this.specificity.getText(),nameEns);
						ObservableList<String> data= FXCollections.observableArrayList();
						for(String ens : this.nameEns) {
							data.add(ens);
						}
						combo[1].setItems(data);
						combo[2].setItems(data);
						combo[0].setItems(data);
						new Alert(Alert.AlertType.CONFIRMATION,"Le groupe " + this.name.getText()+" a ete créé").show();
						
					}
					else {
						String error = "Erreurs possibles : \n Tout les champs ne sont pas remplis. \n Le groupe " + this.name.getText()+" existe deja. \n Il faut cocher au moins 3 enseignants.";
						new Alert(Alert.AlertType.ERROR,error).show();
					}
				
			} catch (SQLException e1) {
				System.out.println("Ce groupe existe deja");
				String error = "Erreurs possibles : \n Tout les champs ne sont pas remplis. \n Le groupe " + this.name.getText()+" existe deja.\\n Il faut cocher au moins 3 enseignants.";
				new Alert(Alert.AlertType.ERROR,error).show();
			}
		});
		return box;
	}
	
	private HBox createSubGrpButton() {
		HBox box = new HBox();
		Button createGrp = new Button("Creer les sous-groupe");
		createGrp.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(createGrp);
		createGrp.setOnAction(e->{
				try {
					boolean canInsert=true;
					for(int i=0;i<3;i++) {
						if(this.enterCap[i].getText().isEmpty() && this.combo[i].getSelectionModel().isEmpty()) canInsert=false;
					}
					if(canInsert) {
						this.param.getParam().getDb().getFill().insertIntoSubGroupe(this.name.getText(), this.enterCap, this.combo);
						new Alert(Alert.AlertType.CONFIRMATION,"Les sous-groupes ont ete créés").show();
						this.visu.setSousGroupe(this.combo, this.enterCap);
						goBack();
					}
					else new Alert(Alert.AlertType.ERROR,"Erreur :  Veuillez remplir tout les champs").show();
					
				} catch (SQLException e1) {
					new Alert(Alert.AlertType.ERROR,"Erreur :  Veuillez remplir tout les champs").show();
				}
			
		});
		return box;
	}


	public HBox nameGrp() {
		HBox box = new HBox();
		box.setSpacing(20);

		Label nom = new Label("Nom du groupe :");
		this.name = new TextField();

		box.getChildren().addAll(nom,this.name);
		return box;
	}

	public HBox ensGrp() {
		HBox box = new HBox();
		box.setSpacing(20);

		Label nom = new Label("Choix des enseignants :");
		ComboBox<String> controllAdd = new ComboBox<String>();
		ObservableList<FillTableEns> data = FXCollections.observableArrayList();
		
		TableView table = new TableView();
		TableColumn ens = new TableColumn("Enseignants");
		TableColumn sel = new TableColumn("Selection");
		
		table.getColumns().addAll(ens,sel);
		
		ens.setCellValueFactory(new PropertyValueFactory<FillTableEns, String>("name"));
		sel.setCellValueFactory(new PropertyValueFactory<FillTableEns, String>("button"));
		
		ResultSet result;
		
		try {
			result = this.param.getParam().getDb().getFill().selectAllEnsWithoutGrp();
			while(result.next()) {
				String name = result.getString("name");
				RadioButton button = new RadioButton();
				button.setId(name);
				radioArray.add(button);
				data.add(new FillTableEns(name,button));
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		table.setItems(data);

		
		
		
		box.getChildren().addAll(nom, table);
		return box;
	}
	
	public HBox specifGrp() {
		HBox box = new HBox();
		box.setSpacing(20);
		
		Label label = new Label("Specificite du groupe : ");
		this.specificity = new TextField();
		box.getChildren().addAll(label,this.specificity);
		
		return box;
		
	}
	
	
	public VBox subGrp() {
		VBox box =new VBox();
		
		VBox box2 = new VBox();
		box2.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		box2.setSpacing(30);
		box2.setPadding(new Insets(20,20,20,20));
		box.setAlignment(Pos.CENTER);
		
		Label sub = new Label("Sous-groupes");
		sub.setStyle("-fx-font-size:20px;");
		HBox subBox = new HBox(sub);
		subBox.setAlignment(Pos.CENTER);
		
		box2.getChildren().addAll(subBox,sub1(),sub2(),sub3(),createSubGrpButton());
		box.setPadding(new Insets(20,20,20,20));
		box.getChildren().add(box2);
		return box;
	}
	
	public TableView fiche() {
		return new TableView();
	}
	
	public VBox sub1() {
		VBox box = new VBox();
		
		HBox numSub = new HBox();
		Label num = new Label("Sous groupe 1");
		num.setStyle("-fx-font-weight: bold;");
		numSub.getChildren().addAll(num);
		
		HBox capSub = new HBox();
		Label cap = new Label("Capacite du sous groupe :");
		enterCap[0] = new TextField();
		capSub.getChildren().addAll(cap,enterCap[0]);
		
		HBox refSub = new HBox();
		Label ref = new Label("Referent du sous groupe :");
		refSub.getChildren().addAll(ref,combo[0]);
		
		box.getChildren().addAll(numSub,capSub,refSub);
		return box;
	}
	
	public VBox sub2() {
		VBox box = new VBox();
		
		HBox numSub = new HBox();
		Label num = new Label("Sous groupe 2");
		num.setStyle("-fx-font-weight: bold;");
		numSub.getChildren().addAll(num);
		
		HBox capSub = new HBox();
		Label cap = new Label("Capacite du sous groupe :");
		enterCap[1] = new TextField();
		capSub.getChildren().addAll(cap,enterCap[1]);
		
		HBox refSub = new HBox();
		Label ref = new Label("Referent du sous groupe :");
		refSub.getChildren().addAll(ref,combo[1]);
		
		box.getChildren().addAll(numSub,capSub,refSub);
		return box;
	}
	
	public VBox sub3() {
		VBox box = new VBox();

		HBox numSub = new HBox();
		Label num = new Label("Sous groupe 3");
		num.setStyle("-fx-font-weight: bold;");
		numSub.getChildren().addAll(num);
		
		HBox capSub = new HBox();
		Label cap = new Label("Capacite du sous groupe :");
		enterCap[2] = new TextField();
		capSub.getChildren().addAll(cap,enterCap[2]);
		
		HBox refSub = new HBox();
		Label ref = new Label("Referent du sous groupe :");
		refSub.getChildren().addAll(ref,combo[2]);
		
		box.getChildren().addAll(numSub,capSub,refSub);
		return box;
	}


}
