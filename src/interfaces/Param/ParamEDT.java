package interfaces.Param;

import interfaces.MainInterface;
import interfaces.Param.CreateData.CreateGroupe;
import interfaces.Param.FillTable.FillTableActivite;
import interfaces.Param.FillTable.FillTableGrp;
import interfaces.Param.FillTable.TableActivite;
import interfaces.Param.FillTable.TableGrp;
import interfaces.Param.Modificate.ModificateActivite;
import interfaces.Param.Modificate.ModificateGrp;
import interfaces.Param.removing.RemovingGroupe;
import interfaces.Param.visualize.VisuActivite;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ParamEDT {

	private ParamInterface param;
	private Group root;
	private MainInterface main;
	private VBox mainVBox;
	private TableActivite tableActivite;
	private HBox mainBox1;
	private ModificateActivite[] modifAct = new ModificateActivite[3];

	public ParamEDT(ParamInterface param) {
		this.param=param;
		this.root=param.getRoot();
		this.main=param.getMain();
		this.param.getRoot().getChildren().remove(this.param.getParam().getMainVBox());
		initialize();
	}

	public VBox getMainVBox() {
		return this.mainVBox;
	}

	public ParamInterface getParam() {
		return param;
	}


	public void initialize() {
		this.mainVBox = new VBox();
		//mainVBox.setSpacing(30);

		 mainBox1 = new HBox();
		mainBox1.setSpacing(50);
		mainBox1.setAlignment(Pos.CENTER_RIGHT);
		mainBox1.getChildren().addAll(createMatierePane());

		mainVBox.getChildren().addAll(mainBox1);
		new SizeDisplayOnScene(main,mainVBox);
		root.getChildren().add(mainVBox);
	}

	public HBox createMatierePane() {
		HBox pane = new HBox();
		pane.setBackground(new Background(new BackgroundFill(Color.rgb(202, 218, 238), CornerRadii.EMPTY, Insets.EMPTY)));
		pane.setStyle("-fx-border-color:black; -fx-border-radius:5px");

		this.tableActivite = new TableActivite(this.param);
		VBox paramGrp =new VBox();
		paramGrp.setPrefWidth(200);

		Button param = new Button("Parametrer");
		
		Button see = new Button("Visualiser");

		see.setOnAction(e->{
			VBox box = new VBox();
			box.setSpacing(20);
			box.setAlignment(Pos.CENTER);
			
			FillTableActivite fillTable=tableActivite.getTable().getSelectionModel().getSelectedItem();
			
			char grp = fillTable.getGroupe().charAt(0);
			char numChar = fillTable.getGroupe().charAt(1);
			VisuActivite modif = new VisuActivite(this,grp,numChar);
			box.getChildren().add(modif.getBox());

			mainBox1.getChildren().addAll(box);
		});

		param.setOnAction(e->{
			VBox MAINV = new VBox();
			
			VBox box = new VBox();
			box.setSpacing(20);
			box.setAlignment(Pos.CENTER);
			MAINV.setAlignment(Pos.CENTER);
			
			this.param.getRoot().getChildren().remove(this.getMainVBox());
			FillTableActivite fillTable=tableActivite.getTable().getSelectionModel().getSelectedItem();
			
	
			char numChar = fillTable.getGroupe().charAt(1);
			ModificateActivite modif = new ModificateActivite(this,fillTable.getGroupe().charAt(0),numChar);
			box.getChildren().add(modif.getMainBox());
			MAINV.getChildren().add(box);

			Button returnB = new Button("Retour");
			returnB.setLayoutX(50);
			returnB.setLayoutY(50);
			returnB.setOnAction(event->{
				this.getParam().getRoot().getChildren().removeAll(returnB,MAINV);
				this.param.getRoot().getChildren().add(this.getMainVBox());
				this.tableActivite.setAll();
			});

			this.param.getRoot().getChildren().addAll(MAINV,returnB);


		});



		param.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		see.setStyle("-fx-border-color:black; -fx-border-radius:5px");

		paramGrp.setAlignment(Pos.CENTER);
		paramGrp.setSpacing(20);
		paramGrp.getChildren().addAll(param,see);
		pane.getChildren().addAll(tableActivite.getTable(),paramGrp);
		return pane;
	}


}
