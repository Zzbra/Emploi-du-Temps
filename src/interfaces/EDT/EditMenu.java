package interfaces.EDT;

import interfaces.MainInterface;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class EditMenu {

	private MenuBar menu;

	public EditMenu(CreateInterface createInterface) {
		Label edition = new Label("Menu edition");
		Menu menu = new Menu("",edition);
		edition.setTextFill(Color.BLACK);
		edition.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> edition(createInterface));
		
		
		Label labelPrint = new Label("Imprimer");
		Menu printMenu = new Menu("",labelPrint);
		labelPrint.setTextFill(Color.BLACK);
		labelPrint.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> print(createInterface.getRoot()));

		MenuBar menuBar2 = new MenuBar();
		menuBar2.getMenus().addAll(menu,printMenu);

		this.menu=menuBar2;
		createInterface.getRoot().getChildren().add(menuBar2);



	}

	private Object edition(CreateInterface createInterface ) {
		System.out.println("Dans edition");
		createInterface.getMain().getStage().setScene(createInterface.getMain().getSceneParam());
		return null;
	}

	
	private void print(Node node) {
		System.out.println("Creating a printer job...");

		PrinterJob job = PrinterJob.createPrinterJob();
		if (job != null) {
			System.out.println(job.jobStatusProperty().asString());

			boolean printed = job.printPage(node);
			if (printed) {
				job.endJob();
			} else {
				System.out.println("Printing failed.");
			}
		} else {
			System.out.println("Could not create a printer job.");
		}
	}


	public MenuBar getMenu() {
		return this.menu;
	}
}
