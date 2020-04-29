package interfaces;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

import application.Enseignant;
import application.Groupe;
import application.Salle;
import application.Solveur;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

@SuppressWarnings("rawtypes")
public class DateScrollMenu {

	private Group root;
	
	private TableView table;
	private Groupe groupe;
	private UserMenu menu;
	private ScrollPane scrollPane;
	private MainInterface main;
	private weekPlanning weekPlanning;
	private Solveur solveur;
	
	public DateScrollMenu(Group root,TableView table, Groupe groupe, UserMenu menu2, MainInterface main,Solveur solveur) {
		this.root=root;
		this.table=table;
		this.groupe= groupe;
		this.menu = menu2;
		this.main = main;
		this.solveur = solveur;
//		this.probleme = solveur.getInstance();

		initialize();
		this.weekPlanning = new weekPlanning(getCurrentWeek(),table,groupe,root,main,solveur);
		root.getChildren().addAll(scrollPane);
	}





	public DateScrollMenu(Group root, TableView tableView, Enseignant ens, UserMenu menu2, MainInterface main,Solveur solveur) {
		this.root=root;
		this.table=tableView;
		this.menu = menu2;
		this.main = main;
		this.solveur = solveur;
	//	this.probleme = solveur.getInstance();

		initialize();
		this.weekPlanning = new weekPlanning(getCurrentWeek(),table,ens,root,main,solveur);
		root.getChildren().addAll(scrollPane);
	}



	public DateScrollMenu(Group root, TableView tableView, Salle salle, UserMenu menu, MainInterface main,Solveur solveur) {
		this.root=root;
		this.table=tableView;
		this.menu = menu;
		this.main = main;
		this.solveur = solveur;
	//	this.probleme = solveur.getInstance();

		initialize();
		this.weekPlanning = new weekPlanning(getCurrentWeek(),table,salle,root,main,solveur);
		root.getChildren().addAll(scrollPane);
	}





	public void initialize() {
		scrollPane = new ScrollPane();		
		
		new SizeDisplayOnScene(main,scrollPane);
		scrollPane.setContent(addDateButton());

	}



	public HBox addDateButton() {
		final HBox container = new HBox();
		LocalDate date = LocalDate.of(2019, 8, 19);
		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
		date =date.with(fieldISO, 1);

		for (int i=1;i<=53;i++) {
			Button button= new Button(date.format(DateTimeFormatter.ofPattern("MMMM dd,yyyy",Locale.FRENCH)));
			date = date.plusWeeks(1);
			button.setId(i+"");
			button.setOnMouseClicked(event -> {
				this.weekPlanning = new weekPlanning(button.getId(),table,groupe,root,main,solveur);
				
				menu.setRoot();
			});
			
			container.getChildren().add(button);
		}
		return container;

	}


	public String getCurrentWeek(){
		LocalDate date = LocalDate.now(); 
		Locale locale = Locale.FRANCE;
		int weekOfYear = date.get(WeekFields.of(locale).weekOfWeekBasedYear());
		return String.valueOf(weekOfYear);
	}
	
	public ScrollPane getScrollPane() {
		this.scrollPane.setHvalue(300);
		return this.scrollPane;
	}

	public weekPlanning getWeekPlanning() {
		return this.weekPlanning;
	}

}

