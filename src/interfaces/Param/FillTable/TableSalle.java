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
import mariaDB.DBConnection;

public class TableSalle {
	private TableView<FillTableSalle> table;
	private MainInterface main;
	private DBConnection db;
	private ResultSet result;
	private ParamPannel param;
	private ObservableList<FillTableSalle> data= FXCollections.observableArrayList();	
	
	public TableSalle(ParamPannel param) {
		this.param=param;
		this.main=param.getParam().getMain();
		
		
		this.table= new TableView<FillTableSalle>();
		initialize();
		table.setStyle("-fx-border-color:black; -fx-border-radius:5px");
	}
	
	
	public void initialize(){
		TableColumn salle = new TableColumn("Salle");
		TableColumn groupe = new TableColumn("Groupe");
		TableColumn capacite = new TableColumn("Capacite");

		table.getColumns().addAll(salle,groupe,capacite);

		
		salle.setCellValueFactory(new PropertyValueFactory<FillTableGrp, String>("salle"));
		groupe.setCellValueFactory(new PropertyValueFactory<FillTableGrp, String>("groupe"));
		capacite.setCellValueFactory(new PropertyValueFactory<FillTableGrp, String>("capacite"));
		
		new SizeDisplayOnScene(main,salle);
		new SizeDisplayOnScene(main,groupe);
		new SizeDisplayOnScene(main,capacite);
		
		table.setItems(getSalleData());
	}

	private ObservableList getSalleData() {
		try {
			this.result = this.param.getParam().getDb().getFill().selectAllSalle();
			while(result.next()) {
				int salle = result.getInt("num");
				String groupe =result.getString("groupeSalle");
				int capacite= result.getInt("capacity");
				data.add(new FillTableSalle(salle,groupe,capacite));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
		return data;
	}


	public TableView<FillTableSalle> getTable() {
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
		this.table.setItems(getSalleData());
	}


}
