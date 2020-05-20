package interfaces.EDT;

import interfaces.MainInterface;
import application.Horaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

@SuppressWarnings("rawtypes")

public class ScheduleDisplay {


	private TableColumn horaire;
	private MainInterface main;
	
	public ScheduleDisplay(Group root, TableView table,MainInterface main) {
		horaire = new TableColumn("Horaire");
		new SizeDisplayOnScene(table,horaire,main);
		displayHoraire(table); 
	}


	@SuppressWarnings("unchecked")
	public void displayHoraire(TableView table) {

		table.getColumns().add(horaire);
		setHoraire(table,horaire);

		table.setRowFactory(tv -> {
			TableRow row = new TableRow();
			new SizeDisplayOnScene(table,row,main);
			return row ;
		});
	}



	@SuppressWarnings("unchecked")
	public void setHoraire(TableView tableView, TableColumn column) {
		ObservableList<Horaire> data = FXCollections.observableArrayList(
				new Horaire("Sequence 1",840,1010),
				new Horaire("Sequence 2",1030,1200),
				new Horaire("Sequence 3",1340,1510),
				new Horaire("Sequence 4",1530,1700)
				);
		
		column.setCellValueFactory(new PropertyValueFactory<Horaire, String>("name"));
		tableView.setItems(data);

	}


}
