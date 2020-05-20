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

public class ModificateEns {

	private ParamPannel param;
	private String nameEns;
	private VBox mainBox = new VBox();
	private HBox mainHBox = new HBox();
	private VBox secBox = new VBox();
	private ArrayList<Integer> dispo = new ArrayList<Integer>();
	private ArrayList<RadioButton> dispoArray = new ArrayList<RadioButton>();
	private ArrayList<String> subject = new ArrayList<String>();
	private ArrayList<RadioButton> subjectArray = new ArrayList<RadioButton>();
	private ArrayList<String> allSubject = new ArrayList<String>();

	private VisuEns visu;

	public ModificateEns(ParamPannel param, String groupe) {
		this.nameEns=groupe;
		this.param=param;
		initWithDB();
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
		this.visu = new VisuEns(this.param,this.nameEns,this.dispo,this.subject);
		this.dispo=this.visu.getDispo();
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
		Label grp = new Label(nameEns);
		grp.setStyle("-fx-font-size:20px;");
		HBox boxGrp = new HBox(grp);
		boxGrp.setAlignment(Pos.CENTER);
		boxGrp.setPadding(new Insets(0,20,20,20));

		this.secBox.getChildren().addAll(boxGrp,setDispo(),setSubject());
	}
	
	
	public VBox setSubject() {
		VBox box = new VBox();
		Label lab = new Label("Enseignements :");
		lab.setStyle("-fx-font-weight: bold;");
		HBox boxSubject = new HBox();
		VBox left = new VBox();
		VBox right = new VBox();
		boxSubject.setPadding(new Insets(0,0,0,20));
		for(String sub : this.allSubject) {
			RadioButton butt = new RadioButton(sub);
			if(this.subject.contains(sub)) {
				butt.setSelected(true);
				left.getChildren().add(butt);
			}
			else {
				right.getChildren().add(butt);
			}
			this.subjectArray.add(butt);
		}
		
		
		Button see = new Button("Visualiser");
		see.setOnAction(e->{
			this.subject.clear();
			for(RadioButton button :subjectArray )
			{
				if(button.isSelected()) this.subject.add(button.getText());
			}
			this.visu.setSubject(this.subjectArray);
			this.updateSubject(box,left,right);
			this.visu.setDispo(this.dispoArray);
		});
		
		boxSubject.setSpacing(20);
		box.setSpacing(20);
		boxSubject.getChildren().addAll(left,right);
		box.getChildren().addAll(new HBox(lab,boxSubject),see);
		return box;
	}
	
	public void updateSubject(VBox box,VBox left,VBox right) {
		left.getChildren().clear();
		right.getChildren().clear();
		this.subjectArray.clear();
		
		for(String sub : this.allSubject) {
			RadioButton butt = new RadioButton(sub);
			if(this.subject.contains(sub)) {
				butt.setSelected(true);
				left.getChildren().add(butt);
			}
			else {
				right.getChildren().add(butt);
			}
			this.subjectArray.add(butt);
		}
		
		
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
		for(RadioButton rb : this.dispoArray) {
			//System.out.print(rb.getId() +" , ");
			if(this.dispo.contains(Integer.parseInt(rb.getId()))) {
				System.out.println("Yes :" +rb.getId());
				rb.setSelected(true);
			}
		}
		

		boxDispo.setSpacing(20);
		box.setSpacing(20);
		box.getChildren().addAll(new HBox(lab,boxDispo));
		return box;
	}
	
	
	public void addAll() {
		
		Button saveB = new Button("Sauver les changements");
		HBox buttonBox = new HBox(saveB);
		buttonBox.setAlignment(Pos.CENTER);
		saveB.setOnAction(e->{
			this.visu.saveAll();
		});
		saveB.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		
		secBox.getChildren().addAll(buttonBox);
	}
	
	
	public void initWithDB() {
		try {
			ResultSet resultDispo = this.param.getParam().getDb().getModif().selectDispoEns(this.nameEns);
			while(resultDispo.next()) this.dispo.add(resultDispo.getInt("idCreneau"));
			
			ResultSet resultSubject = this.param.getParam().getDb().getModif().selectSubjectEns(this.nameEns);
			while(resultSubject.next()) this.subject.add(resultSubject.getString("discipline"));
			
			ResultSet resultAllSubject = this.param.getParam().getDb().getModif().selectAllSubject();
			while(resultAllSubject.next()) this.allSubject.add(resultAllSubject.getString("discipline"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(this.dispo);
	}
	

}
