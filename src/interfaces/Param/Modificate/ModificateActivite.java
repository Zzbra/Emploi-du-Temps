package interfaces.Param.Modificate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import interfaces.Param.ParamEDT;
import interfaces.Param.FillTable.FillTableActivite;
import interfaces.Param.visualize.VisuEns;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ModificateActivite {

	private char alpha;
	private int subgroupe;
	private VBox mainBox = new VBox();
	private ParamEDT param;
	private TableView<FillTableActivite> table = new TableView<FillTableActivite>();
	private ObservableList<FillTableActivite> data = FXCollections.observableArrayList();
	private ArrayList<Integer> dispo = new ArrayList<Integer>();
	private ArrayList<RadioButton> dispoArray = new ArrayList<RadioButton>();
	
	public ModificateActivite(ParamEDT param,char alpha, char subgroupe) {
		this.alpha =alpha;
		this.param=param;
		
		this.subgroupe = Integer.parseInt(String.valueOf(subgroupe));
		System.out.println(subgroupe +" = " + this.subgroupe);
		System.out.println("Groupe "+alpha+""+subgroupe);
		
		init();
		
	}
	
	private void init() {
		initBox();
		Label mainLabel = new Label("PARAMETRER");
		mainLabel.setStyle("-fx-font-size:30px; -fx-font-weight: bold;");
		HBox boxVisu = new HBox(mainLabel);
		boxVisu.setAlignment(Pos.CENTER);
		
		Label grp = new Label("Groupe "+alpha+""+subgroupe);
		grp.setStyle("-fx-font-size:20px;");
		HBox boxGrp = new HBox(grp);
		boxGrp.setAlignment(Pos.CENTER);
		boxGrp.setPadding(new Insets(0,20,20,20));
		
		Button valid = new Button("Valider");
		
		valid.setOnAction(e->{
			int nbSeq=0;
			for(FillTableActivite fill: data) nbSeq+=fill.getComboSeq().getSelectionModel().getSelectedItem();
			
			if(nbSeq>20) {
				new Alert(Alert.AlertType.ERROR,"Un groupe ne peut pas avoir plus de 20 activités par semaine").show();
			}
			else {
				for(FillTableActivite fill: data) {
					System.out.println(fill.getMatiere() +" : "+ fill.getComboSeq().getSelectionModel().getSelectedItem()+" " + fill.getDispoArray());
					try {
						this.param.getParam().getDb().getModif().updateSeq(fill.getMatiere(), this.alpha, this.subgroupe,fill.getComboSeq().getSelectionModel().getSelectedIndex());
						this.param.getParam().getDb().getModif().updateCreAct(fill.getMatiere(),this.alpha, this.subgroupe ,fill.getDispoArray());
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
			
			
			
		});
		
		mainBox.getChildren().addAll(boxVisu,boxGrp,createTable(),valid);
	}
	
	
	
	private TableView<FillTableActivite> createTable(){
		
		TableColumn matiere = new TableColumn("Matiere");
		TableColumn nbSeq = new TableColumn("Séquence");
		TableColumn cre = new TableColumn("Creneaux Obligatoires");
		
		table.getColumns().addAll(matiere,nbSeq,cre);
		
		
		
		matiere.setCellValueFactory(new PropertyValueFactory<FillTableActivite, String>("matiere"));
		nbSeq.setCellValueFactory(new PropertyValueFactory<FillTableActivite, String>("comboSeq"));
		cre.setCellValueFactory(new PropertyValueFactory<FillTableActivite, VBox>("box"));
		
		table.setItems(getData());
		cre.setPrefWidth(1100);
		table.setPrefWidth(matiere.getWidth() + nbSeq.getWidth()+cre.getPrefWidth());
		return table;
	}
	
	
	
	private ObservableList getData() {
		try {
			ResultSet result = this.param.getParam().getDb().getFill().selecActiviteSub(this.alpha,this.subgroupe);
			String matiere="";
			while(result.next()) {
				String groupe = this.alpha+""+this.subgroupe;
				matiere = result.getString("subject");
				
				ResultSet resSeq = this.param.getParam().getDb().getFill().selectSeqAct(this.alpha, this.subgroupe, matiere);
				int nbSeq=0;
				while(resSeq.next()) nbSeq=resSeq.getInt("NbSeq");
				
				data.add(new FillTableActivite(matiere,nbSeq,this.param));
				this.dispoArray.clear();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
}
	public void initBox() {

		mainBox.setBackground(new Background(new BackgroundFill(Color.rgb(202, 218, 238), CornerRadii.EMPTY, Insets.EMPTY)));
		mainBox.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		mainBox.setPadding(new Insets(20,20,20,20));
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setSpacing(30);
		mainBox.setTranslateY(100);
		
	}
	
	
	
	
	public VBox getMainBox() {
		return this.mainBox;
	}
	
}
