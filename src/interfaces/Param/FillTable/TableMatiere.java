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

public class TableMatiere {
	private TableView<FillTableMatiere> table;
	private MainInterface main;
	private ResultSet result;
	private ParamPannel param;
	private ObservableList<FillTableMatiere> data = FXCollections.observableArrayList();	
	
	public TableMatiere(ParamPannel param) {
		this.param=param;
		this.main=param.getParam().getMain();
		this.table= new TableView<FillTableMatiere>();
		
		initialize();
		table.setStyle("-fx-border-color:black; -fx-border-radius:5px");
	}
	
	
	public void initialize(){

		 this.table= new TableView<FillTableMatiere>();
		
		TableColumn subject = new TableColumn("Matiere");
		TableColumn nature = new TableColumn("Nature");
		TableColumn grp = new TableColumn("Groupe");

		table.getColumns().addAll(subject,nature,grp);

		new SizeDisplayOnScene(main,subject);
		nature.setPrefWidth(100);
		grp.setPrefWidth(300);
		
		
		subject.setCellValueFactory(new PropertyValueFactory<FillTableMatiere, String>("subject"));
		nature.setCellValueFactory(new PropertyValueFactory<FillTableMatiere, String>("natures"));
		grp.setCellValueFactory(new PropertyValueFactory<FillTableMatiere, String>("groupes"));

		
		table.setItems(getGroupeData());
	}

	private ObservableList getGroupeData() {
		try {
			this.result = this.param.getParam().getDb().getFill().selectAllMatiere();
			while(result.next()) {
				int idMatiere = result.getInt("idMatiere");
				String subject = result.getString("subject");
				ArrayList<String> natures = new ArrayList<String>();
				ArrayList<Character> groupes = new ArrayList<Character>();
				
				ResultSet resultNature = this.param.getParam().getDb().getFill().selectNatureMatiere(idMatiere);
				while(resultNature.next()) natures.add(resultNature.getString("nature"));
				
				ResultSet resultGroupes = this.param.getParam().getDb().getFill().selectGroupesMatiere(idMatiere);
				while(resultGroupes.next()) groupes.add(resultGroupes.getString("alphabet").charAt(0));
				
				
				data.add(new FillTableMatiere(subject,natures,groupes));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}


	public TableView<FillTableMatiere> getTable() {
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
