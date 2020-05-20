package interfaces.Param;


import interfaces.MainInterface;
import interfaces.Param.CreateData.CreateEns;
import interfaces.Param.CreateData.CreateGroupe;
import interfaces.Param.CreateData.CreateMatiere;
import interfaces.Param.CreateData.CreateSalle;
import interfaces.Param.FillTable.FillTableEns;
import interfaces.Param.FillTable.FillTableGrp;
import interfaces.Param.FillTable.FillTableMatiere;
import interfaces.Param.FillTable.FillTableSalle;
import interfaces.Param.FillTable.TableEns;
import interfaces.Param.FillTable.TableGrp;
import interfaces.Param.FillTable.TableMatiere;
import interfaces.Param.FillTable.TableSalle;
import interfaces.Param.Modificate.ModificateEns;
import interfaces.Param.Modificate.ModificateGrp;
import interfaces.Param.Modificate.ModificateMatiere;
import interfaces.Param.Modificate.ModificateSalle;

import interfaces.Param.removing.RemovingEns;
import interfaces.Param.removing.RemovingGroupe;
import interfaces.Param.removing.RemovingMatiere;
import interfaces.Param.removing.RemovingSalle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ParamPannel {

	private Group root;
	private MainInterface main;
	private ParamInterface param;
	private VBox mainVBox;
	private TableGrp fillgrp ;
	private TableEns fillEns ;
	private TableSalle fillSalle;
	private TableMatiere fillMatiere;



	public ParamPannel(ParamInterface param) {
		this.setParam(param);
		this.root=param.getRoot();
		this.main=param.getMain();

		initialize();
	}

	public VBox getMainVBox() {
		return this.mainVBox;
	}


	public void initialize() {
		this.mainVBox = new VBox();
		mainVBox.setSpacing(30);

		HBox mainBox1 = new HBox();
		mainBox1.setSpacing(50);
		mainBox1.setAlignment(Pos.CENTER_RIGHT);
		mainBox1.getChildren().addAll(createGrpPane(),CreateEnsPane());

		HBox mainBox2 = new HBox();
		mainBox2.setSpacing(50);
		mainBox2.setAlignment(Pos.CENTER_RIGHT);
		mainBox2.getChildren().addAll(createSallePane(),createMatierePane());

		mainVBox.getChildren().addAll(mainBox1,mainBox2);
		new SizeDisplayOnScene(main,mainVBox);
		root.getChildren().add(mainVBox);
	}

	public HBox createGrpPane() {
		HBox pane = new HBox();
		pane.setBackground(new Background(new BackgroundFill(Color.rgb(202, 218, 238), CornerRadii.EMPTY, Insets.EMPTY)));
		pane.setStyle("-fx-border-color:black; -fx-border-radius:5px");

		this.fillgrp = new TableGrp(this);

		VBox paramGrp =new VBox();
		paramGrp.setPrefWidth(200);

		Button param = new Button("Parametrer & Visualiser");
		Button create = new Button("Creer un groupe");
		Button suppress = new Button("Supprimer");

		suppress.setOnAction(e->{
			if(!fillgrp.getTable().getSelectionModel().getSelectedItems().isEmpty()) {
				FillTableGrp grp =fillgrp.getTable().getSelectionModel().getSelectedItem();
				new RemovingGroupe(grp,this);
			}
			
			else new Alert(Alert.AlertType.ERROR,"Vous devez selectionner un groupe.").show();
		});
		
		create.setOnAction(e -> new CreateGroupe(this));
		
		param.setOnAction(e-> {
			if(!fillgrp.getTable().getSelectionModel().getSelectedItems().isEmpty()) {
				FillTableGrp grp =fillgrp.getTable().getSelectionModel().getSelectedItem();
				 new ModificateGrp(this,grp.getGroupe());
			}
			
			else new Alert(Alert.AlertType.ERROR,"Vous devez selectionner un groupe.").show();
		});

		param.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		create.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		suppress.setStyle("-fx-border-color:black; -fx-border-radius:5px");

		paramGrp.setAlignment(Pos.CENTER);
		paramGrp.setSpacing(20);
		paramGrp.getChildren().addAll(param,create,suppress);
		pane.getChildren().addAll(fillgrp.getTable(),paramGrp);
		return pane;
	}


	
	public HBox CreateEnsPane() {
		HBox pane = new HBox();
		pane.setBackground(new Background(new BackgroundFill(Color.rgb(202, 218, 238), CornerRadii.EMPTY, Insets.EMPTY)));
		pane.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		fillEns = new TableEns(this);

		VBox paramGrp =new VBox();
		paramGrp.setPrefWidth(200);
		Button param = new Button("Parametrer & Visualiser");
		Button create = new Button("Ajouter un enseignant");
		Button suppress = new Button("Supprimer");

		param.setOnAction(e-> {
			if(!fillEns.getTable().getSelectionModel().getSelectedItems().isEmpty()) {
				FillTableEns ens =this.fillEns.getTable().getSelectionModel().getSelectedItem();
				new ModificateEns(this,ens.getName());
			}
			
			else new Alert(Alert.AlertType.ERROR,"Vous devez selectionner un enseignant.").show();
		
		});
		
		create.setOnAction(e-> new CreateEns(this));
		suppress.setOnAction(e->{
			if(!fillEns.getTable().getSelectionModel().getSelectedItems().isEmpty()) {
				FillTableEns ens =fillEns.getTable().getSelectionModel().getSelectedItem();
				new RemovingEns(ens,this);
			}
			
			else new Alert(Alert.AlertType.ERROR,"Vous devez selectionner un enseignant.").show();
			
		});

		param.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		create.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		suppress.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		
		paramGrp.setAlignment(Pos.CENTER);
		paramGrp.setSpacing(20);
		paramGrp.getChildren().addAll(param,create,suppress);
		pane.getChildren().addAll(fillEns.getTable(),paramGrp);
		return pane;
	}

	public HBox createSallePane() {
		HBox pane = new HBox();
		pane.setBackground(new Background(new BackgroundFill(Color.rgb(202, 218, 238), CornerRadii.EMPTY, Insets.EMPTY)));
		pane.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		fillSalle = new TableSalle(this);

		VBox paramGrp =new VBox();
		paramGrp.setPrefWidth(200);
		Button param = new Button("Parametrer & Visualiser");
		param.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		Button create = new Button("Ajouter une salle");
		create.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		Button suppress = new Button("Supprimer");

		param.setOnAction(e->{
			if(!fillSalle.getTable().getSelectionModel().getSelectedItems().isEmpty()) {
				FillTableSalle ens =this.fillSalle.getTable().getSelectionModel().getSelectedItem();
				new ModificateSalle(this,String.valueOf(ens.getSalle()));
			}
			
			else new Alert(Alert.AlertType.ERROR,"Vous devez selectionner une salle.").show();
			
		});
		
		create.setOnAction(e-> new CreateSalle(this));
		
		suppress.setOnAction(e->{
			if(!fillSalle.getTable().getSelectionModel().getSelectedItems().isEmpty()) {
				FillTableSalle salle =this.fillSalle.getTable().getSelectionModel().getSelectedItem();
				new RemovingSalle(salle,this);
			}
			
			else new Alert(Alert.AlertType.ERROR,"Vous devez selectionner une salle.").show();
		});
		

		suppress.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		paramGrp.setAlignment(Pos.CENTER);
		paramGrp.setSpacing(20);
		paramGrp.getChildren().addAll(param,create,suppress);
		pane.getChildren().addAll(fillSalle.getTable(),paramGrp);

		return pane;
	}

	public HBox createMatierePane() {
		fillMatiere = new TableMatiere(this);
		
		HBox pane = new HBox();
		pane.setBackground(new Background(new BackgroundFill(Color.rgb(202, 218, 238), CornerRadii.EMPTY, Insets.EMPTY)));
		pane.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		
		VBox paramGrp =new VBox();
		paramGrp.setPrefWidth(200);
		Button param = new Button("Parametrer & Visualiser");
		param.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		Button create = new Button("Ajouter une matiere");
		create.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		Button suppress = new Button("Supprimer");

		param.setOnAction(e->{
			if(!fillMatiere.getTable().getSelectionModel().getSelectedItems().isEmpty()) {
				FillTableMatiere ens =this.fillMatiere.getTable().getSelectionModel().getSelectedItem();
				new ModificateMatiere(this,ens.getSubject());
			}
			
			else new Alert(Alert.AlertType.ERROR,"Vous devez selectionner une matiere.").show();
			
		});
		
		create.setOnAction(e-> new CreateMatiere(this));
		
		suppress.setOnAction(e->{
			if(!fillMatiere.getTable().getSelectionModel().getSelectedItems().isEmpty()) {
				FillTableMatiere ens =this.fillMatiere.getTable().getSelectionModel().getSelectedItem();
				new RemovingMatiere(ens.getSubject(),this);
			}
			
			else new Alert(Alert.AlertType.ERROR,"Vous devez selectionner une salle.").show();
		});

		suppress.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		paramGrp.setAlignment(Pos.CENTER);
		paramGrp.setSpacing(20);
		paramGrp.getChildren().addAll(param,create,suppress);
		pane.getChildren().addAll(fillMatiere.getTable(),paramGrp);

		return pane;
	}



	/**
	 * @return the param
	 */
	public ParamInterface getParam() {
		return param;
	}



	/**
	 * @param param the param to set
	 */
	public void setParam(ParamInterface param) {
		this.param = param;
	}
	
	public TableGrp getTableGrp() {
		return fillgrp;
	}
	
	public TableEns getTableEns() {
		return fillEns;
	}

	public TableSalle getTableSalle() {
		return this.fillSalle;
	}
	
	public TableMatiere getTableMatiere() {
		return fillMatiere;
	}

}
