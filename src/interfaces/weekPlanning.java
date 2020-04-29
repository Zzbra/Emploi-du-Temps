package interfaces;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;


import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import application.Enseignant;
import application.Groupe;
import application.Salle;
import application.Solveur;
import javafx.scene.Group;

@SuppressWarnings("rawtypes")
public class weekPlanning {


	private String weekNumber;
	private TableView table;


	private Groupe groupe;
	private Group root;
	private MainInterface main;
	private Enseignant ens;
	private Salle salle;
	
	private Solveur solveur;
	
	public weekPlanning(String weekNumber,TableView table2, Groupe groupe,Group root,MainInterface main, Solveur solveur) {
		solveur.getInstance();
		this.solveur = solveur;
		this.weekNumber=weekNumber;
		this.table=table2;
		this.groupe=groupe;
		this.root=root;
		this.main=main;
		Text text = new Text("Groupe "+groupe);
		TableColumn<Text,String> columnWeek = new TableColumn<Text,String>(text.getText());
		columnWeek.setText("bonjour");
		setPlanning(columnWeek);

	}


	public weekPlanning(String weekNumber, TableView table, Enseignant ens, Group root,MainInterface main, Solveur solveur) {
		solveur.getInstance();
		this.solveur = solveur;
		this.weekNumber=weekNumber;
		this.table=table;
		this.ens=ens;
		this.root=root;
		this.main=main;


		TableColumn<String,String> week = new TableColumn<String, String>(ens.getName());
		setPlanning(week);


	}


	public weekPlanning(String weekNumber, TableView table, Salle salle, Group root,MainInterface main, Solveur solveur) {
		solveur.getInstance();
		this.solveur = solveur;
		this.weekNumber=weekNumber;
		this.table=table;
		this.salle=salle;
		this.root=root;
		this.main=main;
		TableColumn<String,String> week = new TableColumn<String, String>(salle.getNature() + " : " + salle.getNumber());
		
		setPlanning(week);

	}

	@SuppressWarnings("unchecked")
	public void setPlanning(TableColumn column) {
		table.getColumns().clear();
		new ScheduleDisplay(root,table,main);
		
		LocalDate date = createPlanningDisplay().minusDays(1);
		LocalDate newDate = date;
		
		while(newDate.isBefore(date.plusDays(5))) {
			newDate = newDate.plusDays(1);
			TableColumn dayColumn = new TableColumn(newDate.format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy",Locale.FRENCH)));
			new SizeDisplayOnScene(main,dayColumn);
			
			
			column.getColumns().addAll(dayColumn);
			if(groupe!=null) {
				new setNewPlanning(root,groupe,newDate.format(DateTimeFormatter.ofPattern("EEEE",Locale.FRANCE)).toUpperCase(),dayColumn,main,solveur);
			}
			else if(ens!=null) {
				new setNewPlanning(root,ens,newDate.format(DateTimeFormatter.ofPattern("EEEE",Locale.FRANCE)).toUpperCase(),dayColumn,main,solveur);
			}
			else if(salle!=null)new setNewPlanning(root,salle,newDate.format(DateTimeFormatter.ofPattern("EEEE",Locale.FRANCE)).toUpperCase(),dayColumn,main,solveur);
		}
		table.getColumns().addAll(column);
	}
	

	
	
	

	public LocalDate createPlanningDisplay() {
		LocalDate date = LocalDate.of(2020, 01, 01);
		Locale locale = Locale.FRANCE;
		int weekOfYear = date.get(WeekFields.of(locale).weekOfWeekBasedYear());

		while(weekOfYear != Integer.parseInt(this.weekNumber)) {
			date = date.plusWeeks(1);
			weekOfYear = date.get(WeekFields.of(locale).weekOfWeekBasedYear());
		}
		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
		return date.with(fieldISO,1);
	}

	public TableView getTable() {
		return table;
	}


}
