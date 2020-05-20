package interfaces.EDT;

import interfaces.MainInterface;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CreatePlanningDisplay {

	private Group root;
	private TableView<?> tableView = new TableView<Object>();
	private MainInterface main;
	
	
	public CreatePlanningDisplay(CreateInterface createInterface) {
		this.root = createInterface.getRoot();
		this.main = createInterface.getMain();
		initialize();

		new ScheduleDisplay(root,tableView,main);
		new weekPlanning(createInterface,tableView);
		this.tableView.setStyle("-fx-color: black;-fx-text-fill: white;");
		this.tableView.getColumns().get(0).setStyle("-fx-fill: white;");
	}



	public void initialize() {
		new SizeDisplayOnScene(main,tableView,root);
	}

	
	public TableView<?> getTable() {
		return this.tableView;
	}


	public void setPlanning(CreateInterface createInterface) {
		new CreatePlanningDisplay(createInterface);		
	}
	
	
}
