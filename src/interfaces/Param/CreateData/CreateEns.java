package interfaces.Param.CreateData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import interfaces.Param.ParamPannel;
import interfaces.Param.FillTable.FillTableEns;
import interfaces.Param.FillTable.FillTableGrp;
import interfaces.Param.visualize.VisuEns;
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

public class CreateEns {
	private ParamPannel param;
	private TextField name = new TextField();
	private TextField specificity;
	private ArrayList<String> nameEns = new ArrayList<String>();
	private ArrayList<RadioButton> radioArray = new ArrayList<RadioButton>();
	private VisuEns visu;
	private ComboBox combo[] = new ComboBox[3];
	private TextField enterCap[] = new TextField[3];
	private ArrayList<Integer> dispo = new ArrayList<Integer>();
	private ArrayList<RadioButton> dispoArray = new ArrayList<RadioButton>();
	private ArrayList<String> subject = new ArrayList<String>();
	private ArrayList<RadioButton> subjectArray = new ArrayList<RadioButton>();
	private ArrayList<String> allSubject = new ArrayList<String>();
	private Button returnB = new Button("Retour");
	private HBox mainB = new HBox();

	public CreateEns(ParamPannel paramPannel) {
		
		this.param=paramPannel;
		initialize();
		initWithDB();
		this.visu = new VisuEns(paramPannel,name.getText(),dispo,subject);
		paramPannel.getParam().getRoot().getChildren().remove(paramPannel.getMainVBox());

		
		returnB.setLayoutX(50);
		returnB.setLayoutY(50);
		
		
		
		mainB.setPadding(new Insets(20,20,20,20));
		mainB.setLayoutX(100);
		mainB.setLayoutY(100);
		mainB.setSpacing(70);
		mainB.setPrefHeight(500);
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
		box.setPadding(new Insets(20,20,20,20));
		box.setSpacing(30);

		
		Button see = new Button("Visualiser");
		see.setOnAction(e->{
			this.visu.setName(this.name.getText());
			this.visu.setDispo(this.dispoArray);
			this.visu.setSubject(this.subjectArray);
		});
		
		HBox seeBox = new HBox(see);
		seeBox.setAlignment(Pos.CENTER);
		
		box.getChildren().addAll(nameEns(),ensGrp(),setDispo(),setSubject(),seeBox,createEnsButton());
		

		mainBox.getChildren().addAll(box);
		
		mainVBox.getChildren().addAll(boxVisu,mainBox);
		return mainVBox;
	}
	
	public VBox setDispo() {
		VBox box = new VBox();
		
		Label lab = new Label("NON DISPONIBLE :");
		lab.setStyle("-fx-font-weight: bold;");
		VBox boxDispo = new VBox();
		boxDispo.setPadding(new Insets(0,0,0,20));
		for(int i=1;i<5;i++) {
			RadioButton butt1 = new RadioButton("LUNDI Sequence "+i);
			RadioButton butt2 = new RadioButton("MARDI Sequence "+i);
			RadioButton butt3 = new RadioButton("MERCREDI Sequence "+i);
			RadioButton butt4 = new RadioButton("JEUDI Sequence "+i);
			RadioButton butt5 = new RadioButton("VENDREDI Sequence "+i);
			butt1.setId(String.valueOf(i));
			butt2.setId(String.valueOf(i+4));
			butt3.setId(String.valueOf(i+8));
			butt4.setId(String.valueOf(i+12));
			butt5.setId(String.valueOf(i+16));
			this.dispoArray.add(butt1);
			this.dispoArray.add(butt2);
			this.dispoArray.add(butt3);
			this.dispoArray.add(butt4);
			this.dispoArray.add(butt5);
			HBox tempH = new HBox(butt1,butt2,butt3,butt4,butt5);
			tempH.setSpacing(20);
			boxDispo.getChildren().add(tempH);
		}

		
		boxDispo.setSpacing(20);
		box.setSpacing(20);
		box.getChildren().addAll(new HBox(lab,boxDispo));
		return box;
	}
	
	
	public VBox setSubject() {
		VBox box = new VBox();
		Label lab = new Label("Enseignements :");
		lab.setStyle("-fx-font-weight: bold;");
		HBox boxSubject = new HBox();
		VBox left = new VBox();
		VBox right = new VBox();
		boxSubject.setPadding(new Insets(0,0,0,20));
		int count=0;
		for(String sub : this.allSubject) {
			RadioButton butt = new RadioButton(sub);
			this.subjectArray.add(butt);
			if(count==0) {
				left.getChildren().add(butt);
				count++;
			}
			else {
				right.getChildren().add(butt);
				count--;
			}
			
		}
		boxSubject.setSpacing(20);
		left.setSpacing(10);
		right.setSpacing(10);
		box.setSpacing(20);
		boxSubject.getChildren().addAll(left,right);
		box.getChildren().addAll(new HBox(lab,boxSubject));
		return box;
	}

	private HBox createEnsButton() {
		HBox box = new HBox();
		Button createGrp = new Button("Creer l'enseignant");
		createGrp.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		
		createGrp.setOnAction(e->{
			try {
				if(this.subject.size()>0) {
					this.param.getParam().getDb().getModif().insertEns(this.name.getText(), dispo, subject);
					new Alert(Alert.AlertType.CONFIRMATION,"L'enseignant " + this.name.getText()+" a ete créé").show();
					goBack();
				}
				else {
					String error = "Erreurs possibles :\n L'enseignant " + this.name.getText()+" existe deja. \n Il faut cocher au moins 1 enseignement";
					new Alert(Alert.AlertType.ERROR,error).show();

				}
				
			} catch (SQLException e1) {
				String error = "Erreurs possibles :\n L'enseignant " + this.name.getText()+" existe deja. \n Il faut cocher au moins 1 enseignement";
				new Alert(Alert.AlertType.ERROR,error).show();
			}
		});
		
		box.setPadding(new Insets(0,0,0,0));
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(createGrp);
			
		return box;
	}



	public HBox nameEns() {
		HBox box = new HBox();
		box.setSpacing(20);

		Label nom = new Label("Nom de l'enseignant :");

		
		
		box.getChildren().addAll(nom,this.name);
		return box;
	}

	public HBox ensGrp() {
		HBox box = new HBox();
		box.setSpacing(20);

		return box;
	}
	
	public void initWithDB() {
		try {
			ResultSet resultAllSubject = this.param.getParam().getDb().getModif().selectAllSubject();
			while(resultAllSubject.next()) this.allSubject.add(resultAllSubject.getString("discipline"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
