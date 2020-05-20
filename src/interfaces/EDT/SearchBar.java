package interfaces.EDT;

import application.Enseignant;
import application.Groupe;
import application.Probleme;
import application.Salle;
import application.Solveur;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import interfaces.MainInterface;
public class SearchBar {

	private Group root;
	private MainInterface main;
	private Solveur solveur;
	private Probleme instance;
	private CreateInterface createInterface;

	public SearchBar(CreateInterface createInterface) {
		this.main=createInterface.getMain();
		this.root=createInterface.getRoot();
		this.solveur=createInterface.getSolveur();
		this.instance = solveur.getInstance();
		this.createInterface=createInterface;

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
		Groupe[] groupes = instance.getGroupe();
		
		for(Groupe grp : groupes) {
			Text text = new Text(grp.getAlphabet()+""+grp.getNumber());
			if(text.getText().contains(search)) {
				this.createInterface.setParam(grp,null,null);
				this.createInterface.getPlanning().setPlanning(this.createInterface);
				this.createInterface.setUserMenu();
				this.createInterface.setRoot();
				return ;
			}
		}
		
		for(Enseignant ens : enseignants) {
			if(ens.getName().toString().contains(search)) {
				this.createInterface.setParam(null,ens,null);
				this.createInterface.getPlanning().setPlanning(this.createInterface);
				this.createInterface.setUserMenu();
				this.createInterface.setRoot();
				return ;
			}
		}
		for(Salle salle : salles) {
			if(salle.toString().contains(search)) {
				this.createInterface.setParam(null,null,salle);
				this.createInterface.getPlanning().setPlanning(this.createInterface);
				this.createInterface.setUserMenu();
				this.createInterface.setRoot();
				return ;
			}
		}

	}
}
