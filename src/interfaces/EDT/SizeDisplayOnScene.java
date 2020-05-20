package interfaces.EDT;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.shape.Rectangle;
import interfaces.MainInterface;
public class SizeDisplayOnScene {

	private double widthRessource;
	private double minWidthRessource;
	private double tableViewStartX;
	private double tableViewMinStartX;
	private double tableViewWidth;
	private double tableViewHeight;
	private double tableViewMinSceneWidth;
	private double prefWidthColumn;

	private void setSize(MainInterface main) {
		this.widthRessource = main.getWIDTH()/150;
		this.minWidthRessource=170;
		tableViewStartX=main.getWIDTH()/5.2;
		tableViewMinStartX=200;
		tableViewWidth=main.getWIDTH()/1.27;
		tableViewHeight=main.getHEIGHT()-100;
		tableViewMinSceneWidth=1000;
		prefWidthColumn = (tableViewWidth/16);
	}


	public SizeDisplayOnScene(MainInterface main, Rectangle rect, Label ressource) {
		setSize(main);
		// RessourcePanel
		rect.setX(widthRessource);
		if(main.getWIDTH()<1200) rect.setWidth(minWidthRessource);

		double startY;

		if(main.getHEIGHT()<530) startY=(40);
		else startY=(main.getHEIGHT()/13);

		rect.setY(startY);
		rect.setHeight(main.getHEIGHT()-startY-80);
		ressource.setLayoutY((rect.getY()));
	}


	public SizeDisplayOnScene(MainInterface main, TreeView<?> treeView) {
		//UserMenu
		setSize(main);
		double startY;

		if(main.getHEIGHT()<530) startY=(60);
		else startY=(main.getHEIGHT()/13+20);

		treeView.setLayoutY(startY);

		treeView.setLayoutX(widthRessource);
		treeView.setPrefHeight(main.getHEIGHT()-startY-80);
		treeView.setMinWidth(minWidthRessource);
		treeView.setPrefWidth(main.getWIDTH()/7);
		treeView.setShowRoot(false);
	}

	public SizeDisplayOnScene(MainInterface main, TableView<?> tableView,Group root) {
		// CreatePlanningDisplay
		setSize(main);
		double startY = 30;
		double width = tableViewWidth;
		double height = tableViewHeight;

		tableView.setLayoutY(startY);

		tableView.setLayoutX(tableViewStartX);
		if(main.getWIDTH()<tableViewMinSceneWidth) tableView.setLayoutX(tableViewMinStartX);


		tableView.setPrefWidth(width);
		tableView.setPrefHeight(height);
		tableView.setStyle("-fx-background-color : #CCD8F0; ");


	//	Line separator = new Line(tableViewStartX,(startY+height)/1.8,tableViewStartX+width,(startY+height)/1.8); //Line(startX,startY,endX,endY)  
		//separator.setStrokeWidth(5);
		//separator.setStroke(Color.rgb(61,105,126));
		root.getChildren().addAll(tableView);
	}

	public SizeDisplayOnScene(TableView<?> table,TableColumn<?, ?> horaire, MainInterface main) {
		// ScheduleDisplay
		setSize(main);
		horaire.setPrefWidth(tableViewWidth/16);
	}

	public SizeDisplayOnScene(TableView<?> table , TableRow<?> row, MainInterface main) {
		// ScheduleDisplay
		row.setPrefHeight(table.getHeight()/4.4);
	}

	public SizeDisplayOnScene(MainInterface main, ScrollPane scrollPane) {
		// DataSrollMenu
		double startX = main.getWIDTH()/5.2;
		double width = main.getWIDTH()/1.27;

		scrollPane.setPrefSize(width, 50);

		scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		scrollPane.setPrefViewportWidth(10);
		scrollPane.setLayoutY(main.getHEIGHT()-50);
		scrollPane.setLayoutX(startX);
		scrollPane.setHvalue(200);
		scrollPane.setVvalue(200);
		if(main.getWIDTH()<1000) scrollPane.setLayoutX(200);


	}

	public SizeDisplayOnScene(MainInterface main, TableColumn<?, ?> dayColumn) {
		// WeekPlanning
		setSize(main);
		dayColumn.setPrefWidth(prefWidthColumn*3);
		dayColumn.setStyle("-fx-font-size:18px ");
	}

	public SizeDisplayOnScene(Label label,Label ens, Label salle,Rectangle rect,int creneau,boolean duplic,int numDuplic,String day,TableColumn<?, ?> column,MainInterface main) {
		// SetNewPlanning
		setSize(main);
		rect.setWidth(column.getWidth()-2);

		double cellHeight;

		cellHeight=((main.getHEIGHT()-100)/4.4);

		rect.setHeight(cellHeight);

		rect.setLayoutY(85+(creneau*cellHeight));
		label.setLayoutY(100+(creneau*cellHeight));
		ens.setLayoutY(150+(creneau*cellHeight));
		salle.setLayoutY(200+(creneau*cellHeight));





		double rectX=0;
		switch(day){
		case  "LUNDI" : 
			if(main.getWIDTH()<=tableViewMinSceneWidth)rectX=(tableViewMinStartX+prefWidthColumn);
			else rectX=(tableViewStartX+prefWidthColumn+3);
			break;
		case  "MARDI" : 
			if(main.getWIDTH()<=tableViewMinSceneWidth)rectX=(tableViewMinStartX+prefWidthColumn*4);
			else rectX=(tableViewStartX+prefWidthColumn*4+4);
			break;
		case  "MERCREDI" : 
			if(main.getWIDTH()<=tableViewMinSceneWidth) rectX=(tableViewMinStartX+prefWidthColumn*7);
			else rectX=(tableViewStartX+prefWidthColumn*7+5);
			break;
		case  "JEUDI" : 
			if(main.getWIDTH()<=tableViewMinSceneWidth) rectX=(tableViewMinStartX+prefWidthColumn*10);
			else rectX=(tableViewStartX+prefWidthColumn*10+6);
			break;
		case  "VENDREDI" :
			if(main.getWIDTH()<=tableViewMinSceneWidth) rectX=(tableViewMinStartX+prefWidthColumn*13);
			else rectX=(tableViewStartX+prefWidthColumn*13+7);
			break;
		}
		rect.setX(rectX);

	}

	public SizeDisplayOnScene(TextField searchText, Button searchB, MainInterface main) {
		// SearchBar
		searchText.setLayoutY(main.getHEIGHT()-60);
		searchText.setLayoutX(10);
		searchText.setPrefWidth(150);
		searchB.setLayoutY(main.getHEIGHT()-60);
		searchB.setLayoutX(170);
	}


	public SizeDisplayOnScene(MainInterface main, TableView table) {
		setSize(main);
		double startY;

		if(main.getHEIGHT()<530) startY=(60);
		else startY=(main.getHEIGHT()/13+20);

		table.setLayoutY(startY);

		table.setLayoutX(widthRessource);
		table.setPrefHeight(main.getHEIGHT()-startY-80);
		table.setMinWidth(minWidthRessource);
		table.setPrefWidth(main.getWIDTH()-widthRessource*2);
	}






}
