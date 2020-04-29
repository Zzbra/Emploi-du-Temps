package interfaces;


import application.*;
import javafx.scene.Group;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

@SuppressWarnings("rawtypes")
public class UserMenu {


	private TreeView treeView;
	private Group root;
	private Groupe groupe;
	private CreatePlanningDisplay planning;
	private EditMenu menu;
	private MainInterface main;
	private Solveur solveur;
	private Probleme instance;


	public UserMenu(Group root,MainInterface main,Solveur solveur) {
		this.root=root;
		this.groupe=null;
		this.main= main;
		this.solveur = solveur;
		this.instance = solveur.getInstance();
		this.treeView = new TreeView();
		root.getChildren().add(treeView);

		this.menu= new EditMenu(root,this);

		this.planning = new CreatePlanningDisplay(root,groupe,this,main,solveur);
		planning.getTable();


		initialize();
		createItem();
		setRoot();	        
	}


	public UserMenu(Group root, MainInterface main, Groupe groupe, Enseignant enseignant, Salle salle, Solveur solveur) {
		this.root=root;
		this.main= main;

		this.treeView = new TreeView();
		this.solveur=solveur;
		this.instance = solveur.getInstance();
		this.menu= new EditMenu(root,this);
		
		if(groupe != null) this.planning =new CreatePlanningDisplay(root,groupe,this,main,solveur);
		else if(enseignant != null) this.planning =new CreatePlanningDisplay(root,enseignant,this,main,solveur);
		else if(salle != null) this.planning =new CreatePlanningDisplay(root,salle,this,main,solveur);
		
		planning.getTable();

	

		initialize();
		createItem();
		setRoot();	 
	}
	
	
	public void setAll(Group root, MainInterface main, Groupe groupe, Enseignant enseignant, Salle salle, Solveur solveur) {
		this.root=root;
		this.main= main;
		this.solveur=solveur;
		this.instance = solveur.getInstance();
		if(groupe != null) this.planning =new CreatePlanningDisplay(root,groupe,this,main,solveur);
		else if(enseignant != null) this.planning =new CreatePlanningDisplay(root,enseignant,this,main,solveur);
		else if(salle != null) this.planning =new CreatePlanningDisplay(root,salle,this,main,solveur);
		
	}

	private void initialize() {
		new SizeDisplayOnScene(main, treeView);
	}


	@SuppressWarnings("unchecked")
	private void createItem() {
		
		

		TreeItem rootItem = new TreeItem("Person");
		TreeItem itemElev = new TreeItem("Eleves");
		treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handle(newValue));
		createGroupeList(itemElev);

		TreeItem itemEns = new TreeItem("Enseignants");
		createEnseignantList(itemEns);

		TreeItem itemSalle = new TreeItem("Salles");
		createSalleList(itemSalle);

		rootItem.getChildren().addAll(itemElev,itemEns,itemSalle);
		treeView.setRoot(rootItem);
	}


	private void handle(Object newValue) {  
		Groupe[] allGroupes = instance.getGroupe();
		for(Groupe grp : allGroupes){
			if(newValue.toString().contains(grp.toString())) {
				planning.setPlanning(this.root,grp,this,main,solveur);
			}
		}
		Enseignant [] lesEnseignants = instance.getEnseignants();
		for(Enseignant ens : lesEnseignants){
			if(newValue.toString().contains(ens.toString())) {
				planning.setPlanning(root, ens, this,main,solveur);
			}
		}
		
		Salle [] lesSalles =instance.theSalles();
		for(Salle salle : lesSalles){
			if(newValue.toString().contains(salle.toString())) {
				planning.setPlanning(root, salle, this,main,solveur);
			}
		}
		setRoot();

	}


	@SuppressWarnings("unchecked")
	private void createEnseignantList(TreeItem item) {
		Enseignant [] lesEnseignants = instance.getEnseignants();
		for(Enseignant ens : lesEnseignants){
			TreeItem ensItem = new TreeItem(ens);
			item.getChildren().add(ensItem);
		}

	}




	@SuppressWarnings("unchecked")
	private void createGroupeList(TreeItem item) {
		Groupe[] lesGroupes = instance.getGroupe();
		int nbGroupe = instance.getNbGroupes();
		int count=1;
		for(int grp=0; grp<lesGroupes.length;grp+=nbGroupe) {
			TreeItem solItem = new TreeItem("Solution "+(count));
			for(int j=0; j<nbGroupe;j++) {
					TreeItem grpItem = new TreeItem(lesGroupes[grp+j]);
					solItem.getChildren().add(grpItem);
			}
			item.getChildren().add(solItem);
			count++;
		}
	}



	@SuppressWarnings("unchecked")
	private void createSalleList(TreeItem item) {
		Salle [] lesSalles = instance.getSalles();
		for(Salle salle : lesSalles){
			TreeItem salleItem = new TreeItem(salle);
			item.getChildren().add(salleItem);
		}

	}



	public void setRoot() {
		root.getChildren().remove(treeView);
		menu.setRoot();
		root.getChildren().add(treeView);
	}


public CreatePlanningDisplay getCreatePlanningDisplay() {
	return this.planning;
}
}
