package interfaces.EDT;

import java.time.LocalDate;
import interfaces.MainInterface;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import application.CaseEdTGroupe;
import application.Creneau;
import application.Enseignant;
import application.Groupe;
import application.Horaire;
import application.Matiere;
import application.Salle;
import application.Solveur;
import interfaces.EDT.CreneauxPlanning;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;

@SuppressWarnings("rawtypes")
public class weekPlanning {


	private String weekNumber;
	private String year;
	private TableView table;


	private Groupe groupe;
	private Group root;
	private MainInterface main;
	private Enseignant ens;
	private Salle salle;
	private Solveur solveur;



	public weekPlanning(CreateInterface createInterface, TableView tableView) {

		this.solveur = createInterface.getSolveur();
		this.weekNumber=createInterface.getCurrentWeek();
		this.year= createInterface.getCurrentYear();
		this.table=tableView;
		this.groupe=createInterface.getGroupe();
		this.ens=createInterface.getEnseignant();
		this.salle=createInterface.getSalle();
		this.root=createInterface.getRoot();
		this.main=createInterface.getMain();
		setCreneauPlanning();

	}


	@SuppressWarnings("unchecked")
	public void setCreneauPlanning() {

		LocalDate date = createPlanningDisplay();


		table.getColumns().clear();

		Text text = new Text("");

		if(this.groupe!=null) text.setText("Groupe "+this.groupe.getAlphabet()+""+this.groupe.getNumber());
		else if(this.ens!=null) text.setText(this.ens.getName());
		else if(this.salle!=null) text.setText(String.valueOf(this.salle.getNumber()));

		TableColumn name = new TableColumn(text.getText());
		table.getColumns().add(name);

		TableColumn horaire = new TableColumn("Horaire");
		TableColumn lundi = new TableColumn(date.format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy",Locale.FRENCH)));
		TableColumn mardi = new TableColumn(date.plusDays(1).format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy",Locale.FRENCH)));
		TableColumn mercredi =  new TableColumn(date.plusDays(2).format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy",Locale.FRENCH)));
		TableColumn jeudi =  new TableColumn(date.plusDays(3).format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy",Locale.FRENCH)));
		TableColumn vendredi =  new TableColumn(date.plusDays(4).format(DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy",Locale.FRENCH)));

		name.getColumns().addAll(horaire,lundi,mardi,mercredi,jeudi,vendredi);



		horaire.setCellValueFactory(new PropertyValueFactory<CreneauxPlanning, String>("name"));
		lundi.setCellValueFactory(new PropertyValueFactory<CreneauxPlanning, CaseEdTGroupe>("lundi"));
		lundi.setPrefWidth(200);
		mardi.setCellValueFactory(new PropertyValueFactory<CreneauxPlanning, CaseEdTGroupe>("mardi"));
		mercredi.setCellValueFactory(new PropertyValueFactory<CreneauxPlanning, CaseEdTGroupe>("mercredi"));
		jeudi.setCellValueFactory(new PropertyValueFactory<CreneauxPlanning, CaseEdTGroupe>("jeudi"));
		vendredi.setCellValueFactory(new PropertyValueFactory<CreneauxPlanning, CaseEdTGroupe>("vendredi"));

		new SizeDisplayOnScene(this.main,lundi);
		new SizeDisplayOnScene(this.main,mardi);
		new SizeDisplayOnScene(this.main,mercredi);
		new SizeDisplayOnScene(this.main,jeudi);
		new SizeDisplayOnScene(this.main,vendredi);

		if(groupe!=null) table.setItems(getGroupeData());
		else if(ens!=null)  getEnsData();//table.setItems(getEnsData());


		table.setRowFactory(tv -> {
			TableRow row = new TableRow();
			new SizeDisplayOnScene(table,row,main);
			return row ;
		});

		Callback factory = new Callback<TableColumn<CreneauxPlanning, Object>, TableCell<CreneauxPlanning, Object>>() {
			private int columns = table.getColumns().size();
			@Override
			public TableCell<CreneauxPlanning, Object> call(TableColumn<CreneauxPlanning, Object> param) {
				return new TableCell<CreneauxPlanning, Object>() {
					private int columnIndex = param.getColumns().indexOf(param);
					@Override
					public void updateIndex(int i) {
						super.updateIndex(i);
						if (i >= 0) {
							if(param.getCellData(i)!=null) {
								String color =getColorFromColumn(param.getCellData(i).toString());
								this.setStyle("-fx-background-color: " + color + "; -fx-font-size: 17px;  -fx-alignment: CENTER; -fx-border-color:black");
								// System.out.println(getStyle());
							}
						}
					}
					@Override
					protected void updateItem(Object item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
						} else {
							setText(item.toString());
						}
					}
				};
			}
		};
		//horaire.setCellValueFactory(new PropertyValueFactory<>("value"));
		horaire.setCellFactory(factory);
		lundi.setCellFactory(factory);
		mardi.setCellFactory(factory);
		mercredi.setCellFactory(factory);
		jeudi.setCellFactory(factory);
		vendredi.setCellFactory(factory);
		name.setStyle("-fx-font-size: 17px;  -fx-alignment: CENTER; -fx-border-color:black ;");



	}

	private String getColorFromColumn(String text) {
		Matiere[] mats = this.solveur.getInstance().getMatieres();
		if(text.indexOf('\n', 0)!=-1)	text=text.substring(0, text.indexOf('\n', 0));
		for(Matiere mat : mats) {		
			if(mat.toString().contains(text)) {
				double red = this.main.getMatCol().getColor(mat).getRed()*255;
				double green =this.main.getMatCol().getColor(mat).getGreen()*255;
				double blue = this.main.getMatCol().getColor(mat).getBlue()*255;
				String color="rgb("+red+","+green+","+blue+")";
				return color;
			}
		}
		return null;	
	}



	public ObservableList<CreneauxPlanning> getGroupeData() {
		CaseEdTGroupe[][] edt = this.solveur.getResultat();
		ObservableList<CreneauxPlanning> data = FXCollections.observableArrayList(
				new CreneauxPlanning("Sequence 1",edt[groupe.getNumber()-1][0],edt[groupe.getNumber()-1][4],edt[groupe.getNumber()-1][8],edt[groupe.getNumber()-1][12],edt[0][16]),
				new CreneauxPlanning("Sequence 2",edt[groupe.getNumber()-1][1],edt[groupe.getNumber()-1][5],edt[groupe.getNumber()-1][9],edt[groupe.getNumber()-1][13],edt[groupe.getNumber()-1][17]),
				new CreneauxPlanning("Sequence 3",edt[groupe.getNumber()-1][2],edt[groupe.getNumber()-1][6],edt[groupe.getNumber()-1][10],edt[groupe.getNumber()-1][14],edt[groupe.getNumber()-1][18]),
				new CreneauxPlanning("Sequence 4",edt[groupe.getNumber()-1][3],edt[groupe.getNumber()-1][7],edt[groupe.getNumber()-1][11],edt[groupe.getNumber()-1][15],edt[groupe.getNumber()-1][19])
				);
		return data;
	}



	public ObservableList<CreneauxPlanning> getEnsData() {
		CaseEdTGroupe[][] edt = this.solveur.getResultat();
		for(int grp=0; grp<2; grp++)
			for(int i=0; i<4;i++) {
				if(edt[grp][i].getEnseignant().getName().equals(ens.getName())) { 
				}
			}
		return null;
	}




	public LocalDate createPlanningDisplay() {
		LocalDate date = LocalDate.now().minusWeeks(3);
		Locale locale = Locale.FRANCE;
		int weekOfYear = date.get(WeekFields.of(locale).weekOfWeekBasedYear());
		int yearOfWeek = date.getYear();
		while(weekOfYear != Integer.parseInt(this.weekNumber)) {
			if( yearOfWeek==Integer.parseInt(this.year) && weekOfYear == Integer.parseInt(this.weekNumber)) {
				break;

			}
			else {
				date = date.plusWeeks(1);
				weekOfYear = date.get(WeekFields.of(locale).weekOfWeekBasedYear());
			}
		}
		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
		return date.with(fieldISO,1);
	}



	public TableView getTable() {
		return table;
	}



}
