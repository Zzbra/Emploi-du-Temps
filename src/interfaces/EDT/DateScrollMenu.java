package interfaces.EDT;

import interfaces.MainInterface;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

import com.sun.glass.ui.Screen;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import net.bytebuddy.asm.Advice.Local;


public class DateScrollMenu {

	private Group root;

	private ScrollPane scrollPane;
	private MainInterface main;
	private weekPlanning weekPlanning;
	private CreateInterface createInterface;
	private Button numberButton;



	public DateScrollMenu(CreateInterface createInterface) {
		this.root=createInterface.getRoot();
		this.main = createInterface.getMain();
		createInterface.getSolveur();
		createInterface.getEnseignant();
		createInterface.getSalle();

		initialize();
		this.createInterface=createInterface;
		this.createInterface.setCurrentWeek(getCurrentWeek(),getCurrentYear());
		root.getChildren().addAll(scrollPane);
	}





	public void initialize() {
		scrollPane = new ScrollPane();		

		new SizeDisplayOnScene(main,scrollPane);
		scrollPane.setContent(addDateButton());


	}
	public HBox addDateButton() {
		final HBox container = new HBox();
		LocalDate date = LocalDate.now().minusWeeks(3);
		Locale locale = Locale.FRANCE;
		TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
		date =date.with(fieldISO, 1);


		for (int i=1;i<=53;i++) {
			Button button= new Button(date.format(DateTimeFormatter.ofPattern("MMMM dd,yyyy",Locale.FRENCH)));
			final int numberWeek = date.get(WeekFields.of(locale).weekOfWeekBasedYear());
			final int year = date.getYear();
			date = date.plusWeeks(1);
			button.setId(i+"");

			
			if(numberWeek==LocalDate.now().get(WeekFields.of(locale).weekOfWeekBasedYear()) && year == LocalDate.now().getYear()) {
				this.numberButton=button;
				button.setStyle("-fx-background-color: #cadaee ; -fx-border-color:black; -fx-border-radius:5px");
			
			}
			else {
				button.setStyle("-fx-background-color : #dcdcdc ; -fx-border-color:black; -fx-border-radius:5px");
			}
			

			button.setOnMouseClicked(event -> {
				this.numberButton.setStyle("-fx-background-color : #dcdcdc ; -fx-border-color:black; -fx-border-radius:5px");
				this.numberButton=button;
				button.setStyle("-fx-background-color: #cadaee ; -fx-border-color:black; -fx-border-radius:5px");

				this.createInterface.setCurrentWeek(String.valueOf(numberWeek),String.valueOf(year));
				this.createInterface.getPlanning().setPlanning(this.createInterface);
				this.createInterface.setRoot();
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

	public String getCurrentYear() {
		int year = LocalDate.now().getYear();
		return String.valueOf(year);
	}

	public ScrollPane getScrollPane() {
		this.scrollPane.setHvalue(300);
		return this.scrollPane;
	}

	public weekPlanning getWeekPlanning() {
		return this.weekPlanning;
	}


	@SuppressWarnings("unchecked")
	public void setScroll() {
		 
//			double a = 30;
	//		double b = 50;
		//	double result = a/b;
			
			
//			scrollPane.hvalueProperty().addListener(new ChangeListener<Number>() {
	//	        @Override
		//        public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
		 //       	System.out.println(oldValue+" / "+newValue);
		   //     	if((double)oldValue==1.0 && (double) newValue==0.0){
		     //   		 scrollPane.setHvalue((double) result);
		       // 	}
		        		
		     //   	else  scrollPane.setHvalue((double) newValue);
		       // 	return;
		 //       }
		 //   }) ;
	}

}

