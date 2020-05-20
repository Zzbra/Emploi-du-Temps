package interfaces.Param;

import java.util.Iterator;

import interfaces.MainInterface;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
	
	public SizeDisplayOnScene(MainInterface main, ScrollPane scroll, Label label) {
		setSize(main);
		double startY;

		if(main.getHEIGHT()<530) startY=(60);
		else startY=(main.getHEIGHT()/13+20);

		scroll.setLayoutY(startY);

		scroll.setLayoutX(widthRessource);
		scroll.setPrefHeight(main.getHEIGHT()-startY-80);
		scroll.setMinWidth(minWidthRessource);
		scroll.setPrefWidth(main.getWIDTH()-(widthRessource*4));
		label.setStyle("-fx-font-size:30px");
	}
	
	public SizeDisplayOnScene(MainInterface main, VBox mainVBox) {
		setSize(main);
		double startY;

		if(main.getHEIGHT()<530) startY=(60);
		else startY=(main.getHEIGHT()/13+20);

		mainVBox.setLayoutY(startY);
		mainVBox.setLayoutX(100);
		
	}

	public SizeDisplayOnScene(MainInterface main, TableColumn col) {
		setSize(main);
		col.setPrefWidth(200);
	}

}
