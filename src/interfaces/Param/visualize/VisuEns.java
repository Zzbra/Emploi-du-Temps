package interfaces.Param.visualize;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import interfaces.Param.ParamPannel;
import interfaces.Param.FillTable.FillDispoEns;
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

public class VisuEns {

	private VBox mainBox = new VBox();
	private HBox mainH = new HBox();
	private VBox mainV = new VBox();
	private String name;
	private ParamPannel param;
	private TableView table;
	private ArrayList<Integer> dispo;
	private ArrayList<String> subject;
	private Label subjectLab = new Label();
	private Label nameLabel;

	public VisuEns(ParamPannel param, String name,ArrayList<Integer> dispo, ArrayList<String> subject) {
		this.name=name;
		this.dispo=dispo;
		this.param=param;
		this.subject=subject;
		subjectLab.setText(getListEns());

		param.getParam().getRoot().getChildren().remove(param.getMainVBox());
		mainV.setLayoutX(100);
		mainV.setLayoutY(100);

		initialize();
		initializeEns();
		mainV.getChildren().add(this.addAll());
		mainBox.getChildren().add(mainV);
		mainH.getChildren().add(mainBox);



		param.getParam().getRoot().getChildren().addAll(mainH);
	}

	private String getListEns() {
		String str="";
		int count=0;
		for(String ens : this.subject) {
			str=str+""+ens+" , ";
			if(count==3){str=str+"\n";count=0;

			}
			count++;
		}
		if(str.length()==0) return str;
		return str.substring(0,str.length()-2);
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
		mainBox.setPrefHeight(this.param.getParam().getMain().getHEIGHT());
		mainBox.setAlignment(Pos.CENTER);

		mainH.setPrefWidth(this.param.getParam().getMain().getWIDTH());
		mainH.setAlignment(Pos.CENTER);

		mainV.setBackground(new Background(new BackgroundFill(Color.rgb(202, 218, 238), CornerRadii.EMPTY, Insets.EMPTY)));
	}

	public void initializeEns() {
		nameLabel = new Label(name);
		nameLabel.setStyle("-fx-font-size:20px;");
		HBox boxName = new HBox(nameLabel);
		boxName.setAlignment(Pos.CENTER);

		this.mainV.getChildren().addAll(boxName);

	}


	public TableView tableVisu() {
		table = new TableView();

		TableColumn seq = new TableColumn("Seq");
		TableColumn lundi = new TableColumn("Lundi");
		TableColumn mardi = new TableColumn("Mardi");
		TableColumn mercredi = new TableColumn("Mercredi");
		TableColumn jeudi = new TableColumn("Jeudi");
		TableColumn vendredi = new TableColumn("Vendredi");

		table.getColumns().addAll(seq,lundi,mardi,mercredi,jeudi,vendredi);

		table.setPrefWidth(500);


		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		seq.setCellValueFactory(new PropertyValueFactory<FillDispoEns, Boolean>("name"));
		lundi.setCellValueFactory(new PropertyValueFactory<FillDispoEns, Boolean>("lundi"));
		mardi.setCellValueFactory(new PropertyValueFactory<FillDispoEns, Boolean>("mardi"));
		mercredi.setCellValueFactory(new PropertyValueFactory<FillDispoEns, Boolean>("mercredi"));
		jeudi.setCellValueFactory(new PropertyValueFactory<FillDispoEns, Boolean>("jeudi"));
		vendredi.setCellValueFactory(new PropertyValueFactory<FillDispoEns, Boolean>("vendredi")); 

		table.setItems(getDispoData());

		table.setRowFactory(tv -> {
			TableRow row = new TableRow();
			row.setPrefHeight(40);
			return row ;
		});

		table.setPrefHeight(193);

		Callback factory = new Callback<TableColumn<FillDispoEns, Object>, TableCell<FillDispoEns, Object>>() {
			@Override
			public TableCell<FillDispoEns, Object> call(TableColumn<FillDispoEns, Object> param) {
				return new TableCell<FillDispoEns, Object>() {
					@Override
					public void updateIndex(int i) {
						super.updateIndex(i);
						if (i >= 0) {
							if(param.getCellData(i)!=null) {
								if(param.getCellData(i).equals(false)) {
									String color ="green";
									setText("");
									this.setStyle("-fx-background-color: " + color + "; -fx-font-size: 17px;  -fx-alignment: CENTER; -fx-border-color:black");

								}
								else if (param.getCellData(i).equals(true)){
									String color ="red";
									setText("");
									this.setStyle("-fx-background-color: " + color + "; -fx-font-size: 17px;  -fx-alignment: CENTER; -fx-border-color:black");
								}
							}

						}
					}
					@Override
					protected void updateItem(Object item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
						} else {
							setText(item.toString());
						}
					}
				};
			}
		};
		//horaire.setCellValueFactory(new PropertyValueFactory<>("value"));
		seq.setCellFactory(factory);
		lundi.setCellFactory(factory);
		mardi.setCellFactory(factory);
		mercredi.setCellFactory(factory);
		jeudi.setCellFactory(factory);
		vendredi.setCellFactory(factory);
		return table;
	}

	public ObservableList<FillDispoEns> getDispoData() {
		ObservableList<FillDispoEns> data = FXCollections.observableArrayList(
				new FillDispoEns("Seq 1",this.dispo.contains(1),this.dispo.contains(5),this.dispo.contains(9),this.dispo.contains(13),this.dispo.contains(17)),
				new FillDispoEns("Seq 2",this.dispo.contains(2),this.dispo.contains(6),this.dispo.contains(10),this.dispo.contains(14),this.dispo.contains(18)),
				new FillDispoEns("Seq 3",this.dispo.contains(3),this.dispo.contains(7),this.dispo.contains(11),this.dispo.contains(15),this.dispo.contains(19)),
				new FillDispoEns("Seq 4",this.dispo.contains(4),this.dispo.contains(8),this.dispo.contains(12),this.dispo.contains(16),this.dispo.contains(20))
				);
		return data;
	}

	public VBox getBox() {
		return this.mainBox;
	}



	public VBox addAll() {
		Label lab=new Label("DISPONIBILITE :");
		lab.setStyle("-fx-font-weight: bold;");

		Label ens = new Label("ENSEIGNEMENTS :");
		ens.setStyle("-fx-font-weight: bold;");

		VBox mainB = new VBox(lab,this.tableVisu(),ens,subjectLab);
		mainB.setSpacing(20);
		mainB.setPadding(new Insets(30,10,30,10));
		return mainB;
	}


	public HBox getSubject() {
		HBox box = new HBox();


		return box;
	}

	public TableView getTable() {
		return this.table;
	}

	public ArrayList<Integer> getDispo(){
		return this.dispo;
	}


	public void setDispo(ArrayList<RadioButton> buttonArray) {
		this.dispo.clear();
		for(RadioButton button : buttonArray) {
			if(button.isSelected()) this.dispo.add(Integer.parseInt(button.getId()));
			table.setItems(getDispoData());
		}

	}

	public void setSubject(ArrayList<RadioButton> buttonArray) {
		this.subject.clear();
		String str="";
		for(RadioButton button : buttonArray) {
			if(button.isSelected()) {
				this.subject.add(button.getText());
				str=str+""+button.getText()+" , ";
			}
		}
		if(str.length()>2) this.subjectLab.setText(str.substring(0,str.length()-2));
		else this.subjectLab.setText("");
	}

	public void saveAll() {
		System.out.println("SAVE ALL");
		try {
			this.param.getParam().getDb().getModif().deleteInfoEns(this.name);
			this.param.getParam().getDb().getModif().addInfoEns(this.name, this.dispo,this.subject);
			new Alert(Alert.AlertType.CONFIRMATION,"Les changements ont ete effectues").show();

		} catch (SQLException e) {
			new Alert(Alert.AlertType.ERROR,"").show();
		}
	}
	
	
	public void setName(String name) {
		this.nameLabel.setText(name);
	}
}
