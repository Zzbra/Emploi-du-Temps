package interfaces;

import application.Enseignant;
import application.Probleme;
import application.Salle;
import application.Solveur;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SearchBar {


	private Group root;
	private MainInterface main;
	private Solveur solveur;
	private Probleme instance;

	public SearchBar(Group root, MainInterface main,Solveur solveur) {
		this.main=main;
		this.root=root;
		this.solveur=solveur;
		this.instance = solveur.getInstance();

		TextField searchText = new TextField();
		
		Button searchB = new Button ("Rechercher");
		searchB.setOnMouseClicked(event -> {
			System.out.println("Tu as recherché : " + searchText.getText());
			searchInfo(searchText.getText());
		});
		
		new SizeDisplayOnScene(searchText,searchB,main);
		
		root.getChildren().addAll(searchText,searchB);
	}



	public void searchInfo(String search) {
		Enseignant[] enseignants = instance.getEnseignants();
		Salle[] salles = instance.getSalles();
		for(Enseignant ens : enseignants) {
			if(ens.getName().toString().contains(search)) {
				new CreatePlanningDisplay(root,ens,new UserMenu(root,main,solveur),main,solveur);
				return ;
			}
		}
		for(Salle salle : salles) {
			if(salle.toString().contains(search)) {
				new CreatePlanningDisplay(root,salle,new UserMenu(root,main,solveur),main,solveur);
				return ;
			}
		}

	}
}
