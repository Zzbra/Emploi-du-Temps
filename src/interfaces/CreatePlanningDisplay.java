package interfaces;

import application.Enseignant;
import application.Groupe;
import application.Salle;
import application.Solveur;
import javafx.scene.Group;
import javafx.scene.control.TableView;

public class CreatePlanningDisplay {

	private Group root;
	private UserMenu menu;
	private TableView<?> tableView;
	private MainInterface main;
	private DateScrollMenu dsm;
	@SuppressWarnings("rawtypes")	
	public CreatePlanningDisplay(Group root,Groupe groupe, UserMenu menuUsers,MainInterface main,Solveur solveur) {
		this.root=root;
		this.menu = menuUsers;
		this.main=main;
		this.tableView = new TableView();
		
		initialize();

		new ScheduleDisplay(root,tableView,main);
		this.dsm =new DateScrollMenu(root,tableView,groupe,menu,main,solveur);
		this.tableView.setStyle("-fx-color: black");
		menu.setRoot();
	}
	
	
	@SuppressWarnings("rawtypes")
	public CreatePlanningDisplay(Group root,Enseignant ens, UserMenu menuUsers,MainInterface main,Solveur solveur) {
		this.root=root;
		this.main=main;
		this.menu = menuUsers;
		this.tableView = new TableView();
		initialize();
		this.tableView.setStyle("-fx-color: black");
		new ScheduleDisplay(root,tableView,main);
		this.dsm=new DateScrollMenu(root,tableView,ens,menu,main,solveur);

		menu.setRoot();
	}


	@SuppressWarnings("rawtypes")
	public CreatePlanningDisplay(Group root, Salle salle, UserMenu menuUsers,MainInterface main,Solveur solveur) {
		this.root=root;
		this.main=main;
		this.menu = menuUsers;
		this.tableView = new TableView();
		initialize();
		this.tableView.setStyle("-fx-color: black");
		new ScheduleDisplay(root,tableView,main);
		this.dsm=new DateScrollMenu(root,tableView,salle,menu,main,solveur);

		menu.setRoot();
	}


	public void initialize() {

		new SizeDisplayOnScene(main,tableView,root);
	}

	@SuppressWarnings("rawtypes")
	public TableView getTable() {
		return tableView;
	}


	public void setPlanning(Group root, Groupe groupe2,UserMenu menu,MainInterface main,Solveur solveur) {
		new CreatePlanningDisplay(root,groupe2,menu,main,solveur);		
	}


	public void setPlanning(Group root, Enseignant ens, UserMenu menu,MainInterface main,Solveur solveur) {
		new CreatePlanningDisplay(root,ens,menu,main,solveur);
		
	}


	public void setPlanning(Group root, Salle salle, UserMenu menu,MainInterface main,Solveur solveur) {
		new CreatePlanningDisplay(root,salle,menu,main,solveur);
		
	}
	
	public DateScrollMenu getDateScrollMenu() {
		return dsm;
	}
}
