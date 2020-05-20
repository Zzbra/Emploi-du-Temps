package interfaces.Param.FillTable;

import java.sql.ResultSet;
import java.sql.SQLException;

import interfaces.MainInterface;
import interfaces.Param.ParamPannel;
import interfaces.Param.SizeDisplayOnScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableGrp {
	private TableView<FillTableGrp> table;
	private MainInterface main;
	private ResultSet result;
	private ParamPannel param;
	private ObservableList<FillTableGrp> data = FXCollections.observableArrayList();
	
	public TableGrp(ParamPannel param) {
		this.param=param;
		this.main=param.getParam().getMain();
		
		
		this.table= new TableView<FillTableGrp>();
		table.setStyle("-fx-border-color:black; -fx-border-radius:5px");
		initialize();
	}
	
	
	@SuppressWarnings("unchecked")
	public void initialize(){
		TableColumn<FillTableGrp, String> grp = new TableColumn<FillTableGrp, String>("Groupe");
		TableColumn<FillTableGrp, String> sgrp = new TableColumn<FillTableGrp, String>("Referent");
		TableColumn<FillTableGrp, String> specificite = new TableColumn<FillTableGrp, String>("Specificite");

		table.getColumns().addAll(grp,sgrp,specificite);
		
		
		grp.setCellValueFactory(new PropertyValueFactory<FillTableGrp, String>("groupe"));
		sgrp.setCellValueFactory(new PropertyValueFactory<FillTableGrp, String>("referent"));
		specificite.setCellValueFactory(new PropertyValueFactory<FillTableGrp, String>("specificite"));
		
		new SizeDisplayOnScene(main,grp);
		new SizeDisplayOnScene(main,sgrp);
		new SizeDisplayOnScene(main,specificite);
		
		table.setItems(getGroupeData());
	}

	private ObservableList<FillTableGrp> getGroupeData() {
		
		try {
			this.result = this.param.getParam().getDb().getFill().selectAllGroupe();
			while(result.next()) {
				String groupe = result.getString("alphabet")+""+ result.getInt("num");
				String referent = result.getString("name");
				String specificite = result.getString("specificity");
				data.add(new FillTableGrp(groupe,referent,specificite));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}


	public TableView<FillTableGrp> getTable() {
		return table;
	}
	
	public int getResultSize(ResultSet result) {
		int lenght=0;
		try {
			result.last();
			lenght = result.getRow();
			result.beforeFirst();
			
		} catch (SQLException e) {
			System.out.println("Erreur : Taille result groupe");
		}
		return lenght;
		
	}
	
	public void setAll() {
		this.data.clear();
		this.table.setItems(getGroupeData());
	}


}
