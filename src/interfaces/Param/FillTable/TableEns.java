package interfaces.Param.FillTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import interfaces.MainInterface;
import interfaces.Param.ParamPannel;
import interfaces.Param.SizeDisplayOnScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import mariaDB.DBConnection;

public class TableEns {
	private TableView<FillTableEns> table;
	private MainInterface main;
	private ResultSet result;
	private ParamPannel param;
	private ObservableList<FillTableEns> data = FXCollections.observableArrayList();	
	
	public TableEns(ParamPannel param) {
		this.param=param;
		this.main=param.getParam().getMain();
		this.table= new TableView<FillTableEns>();
		
		initialize();
		table.setStyle("-fx-border-color:black; -fx-border-radius:5px");
	}
	
	
	public void initialize(){

		 this.table= new TableView<FillTableEns>();
		
		TableColumn ens = new TableColumn("Enseignant");
		TableColumn grp = new TableColumn("Groupe");
		TableColumn disc = new TableColumn("Discipline");

		table.getColumns().addAll(ens,grp,disc);

		new SizeDisplayOnScene(main,ens);
		grp.setPrefWidth(100);
		disc.setPrefWidth(300);
		
		
		ens.setCellValueFactory(new PropertyValueFactory<FillTableGrp, String>("name"));
		grp.setCellValueFactory(new PropertyValueFactory<FillTableGrp, String>("groupe"));
		disc.setCellValueFactory(new PropertyValueFactory<FillTableGrp, String>("disciplines"));

		
		table.setItems(getGroupeData());
	}

	private ObservableList getGroupeData() {
		
		try {
			this.result = this.param.getParam().getDb().getFill().selectAllEns();
			while(result.next()) {
				int id = result.getInt("idEns");
				String name = result.getString("name");
				String groupe = result.getString("alphabet");
				
				ResultSet resultDisc = this.param.getParam().getDb().getFill().selectDiscicplineForEns(id);
				ArrayList<String> arrayDis = new ArrayList<String>();
				while(resultDisc.next()) {
					arrayDis.add(resultDisc.getString("discipline"));
				}
				data.add(new FillTableEns(name,groupe,arrayDis));
				
			}
			this.result = this.param.getParam().getDb().getFill().selectAllEnsWithoutGrp();
			while(result.next()) {
				int id = result.getInt("idEns");
				String name = result.getString("name");
				String groupe = "-";
				
				ResultSet resultDisc = this.param.getParam().getDb().getFill().selectDiscicplineForEns(id);
				ArrayList<String> arrayDis = new ArrayList<String>();
				while(resultDisc.next()) {
					arrayDis.add(resultDisc.getString("discipline"));
				}
				data.add(new FillTableEns(name,groupe,arrayDis));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
		return data;
	}


	public TableView<FillTableEns> getTable() {
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
