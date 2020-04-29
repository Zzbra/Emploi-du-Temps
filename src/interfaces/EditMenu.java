package interfaces;


import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class EditMenu {

	private Group root;
	private MenuBar menu;

	public EditMenu(Group root, UserMenu userMenu) {
		this.root=root; 

		Menu menu = new Menu("Editer");
		Menu subMenu = new Menu("Enseignant");
		MenuItem menuItem11 = new MenuItem("Ajouter un enseignant");
		subMenu.getItems().add(menuItem11);
		Menu subMenu2 = new Menu("S'identifier");
		menu.getItems().addAll(subMenu,subMenu2);
		MenuItem menuItem1 = new MenuItem("Item 1");
		menu.getItems().add(menuItem1); 	
		MenuItem menuItem2 = new MenuItem("Item 2");
		menu.getItems().add(menuItem2);
		Label labelPrint = new Label("Imprimer");
		Menu printMenu = new Menu("",labelPrint);
		//printMenu.setOnAction(e -> print(root));
		labelPrint.setTextFill(Color.BLACK);
		labelPrint.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> print(root));

		MenuBar menuBar2 = new MenuBar();
		menuBar2.getMenus().addAll(menu,printMenu);

		this.menu=menuBar2;
		root.getChildren().add(menuBar2);



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


	public void setRoot() {
		root.getChildren().remove(menu);
		root.getChildren().add(menu);

	}
}
