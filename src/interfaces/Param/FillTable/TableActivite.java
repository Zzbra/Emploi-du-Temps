package interfaces.Param.FillTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import interfaces.MainInterface;
import interfaces.Param.ParamEDT;
import interfaces.Param.ParamInterface;
import interfaces.Param.ParamPannel;
import interfaces.Param.SizeDisplayOnScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableActivite {
	
	private TableView<FillTableActivite> table;
	private MainInterface main;
	private ResultSet result;
	private ParamInterface param;
	private ObservableList<FillTableActivite> data = FXCollections.observableArrayList();	

	public TableActivite(ParamInterface paramEDT) {
		this.param=paramEDT;
		this.main=param.getMain();
		this.table= new TableView<FillTableActivite>();
		
		initialize();
		table.setStyle("-fx-border-color:black; -fx-border-radius:5px");
	}
	
	
	public void initialize(){

		 this.table= new TableView<FillTableActivite>();
		
		TableColumn subG = new TableColumn("Groupe");
		TableColumn nbAct = new TableColumn("Nombre d'activite");

		table.getColumns().addAll(subG,nbAct);

		new SizeDisplayOnScene(main,subG);
	
		subG.setPrefWidth(100);
		nbAct.setPrefWidth(200);
		
		subG.setCellValueFactory(new PropertyValueFactory<FillTableActivite, String>("groupe"));
		nbAct.setCellValueFactory(new PropertyValueFactory<FillTableActivite, String>("nbSeq"));

		
		table.setItems(getGroupeData());
	}

	private ObservableList getGroupeData() {
			try {
				ResultSet result = this.param.getDb().getFill().selectAllActivite();
				while(result.next()) {
					String groupe = result.getString("alphabet")+""+result.getString("num");
					int matiere = result.getInt("NbSeq");
					
					data.add(new FillTableActivite(groupe,matiere));
				}
				
				ResultSet resultWO = this.param.getDb().getFill().selectAllActiviteWOSeq();
				while(resultWO.next()) {
					String groupe = resultWO.getString("alphabet")+""+resultWO.getString("num");
					
					data.add(new FillTableActivite(groupe,0));
				}
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return data;
	}


	public TableView<FillTableActivite> getTable() {
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
