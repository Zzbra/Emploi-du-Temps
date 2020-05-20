package interfaces.EDT;


import application.*;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import interfaces.MainInterface;
@SuppressWarnings("rawtypes")
public class UserMenu {


	private TreeView treeView;
	public TreeView getTreeView() {
		return treeView;
	}


	private Group root;
	private CreatePlanningDisplay planning = null;
	private MainInterface main;
	private Solveur solveur;
	private Probleme instance;
	private CreateInterface createInterface;


	public UserMenu(CreateInterface createInterface) {
		this.root=createInterface.getRoot();
		this.main=createInterface.getMain();
		this.treeView = new TreeView();
		this.solveur=createInterface.getSolveur();
		this.instance = solveur.getInstance();
		this.planning=createInterface.getPlanning();
		this.createInterface= createInterface;
		initialize();
		createItem();
	}


	public void setAll(Group root, MainInterface main,Solveur solveur) {
		this.root=root;
		this.main= main;
		this.solveur=solveur;
		this.instance = solveur.getInstance();
	}

	private void initialize() {
		drawMenu();
		new SizeDisplayOnScene(main, treeView);
	}

	public void drawMenu() {
		Rectangle rect = new Rectangle(main.getWIDTH()/7,main.getHEIGHT()-60);
		rect.setFill(Color.rgb(61,105,126));
		Label ressource = new Label("LISTE DES RESSOURCES");
		AnchorPane.setLeftAnchor(ressource, 0.0);
		AnchorPane.setRightAnchor(ressource, 0.0);
		ressource.setAlignment(Pos.CENTER);
		ressource.setTextFill(Color.WHITE);
		new SizeDisplayOnScene(main,rect,ressource);
		AnchorPane anchor = new AnchorPane(rect,ressource);
		root.getChildren().add(anchor);
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
		root.getChildren().add(treeView);
	}


	private void handle(Object newValue) {  
		Groupe[] allGroupes = instance.getGroupe();
		for(Groupe grp : allGroupes){
			if(newValue.toString().contains(grp.toString())) {
				this.createInterface.setParam(grp,null,null);
				planning.setPlanning(this.createInterface);
				return;
			}
		}
		Enseignant [] lesEnseignants = instance.getEnseignants();
		for(Enseignant ens : lesEnseignants){
			if(newValue.toString().contains(ens.toString())) {
				this.createInterface.setParam(null,ens,null);
				planning.setPlanning(this.createInterface);
				return;
			}
		}

		Salle [] lesSalles =instance.theSalles();
		for(Salle salle : lesSalles){
			if(newValue.toString().contains(salle.toString())) {
				this.createInterface.setParam(null,null,salle);
				planning.setPlanning(this.createInterface);
				return;
			}
		}
		createInterface.setRoot();

	}


	@SuppressWarnings("unchecked")
	private void createEnseignantList(TreeItem item) {
		Enseignant [] lesEnseignants = instance.getEnseignants();
		for(Enseignant ens : lesEnseignants){
			TreeItem ensItem = new TreeItem(ens);
			item.getChildren().add(ensItem);
			if(this.createInterface.getEnseignant()!=null) {
				if(this.createInterface.getEnseignant().equals(ens)) {
					this.treeView.getSelectionModel().select(ensItem);
				}
			}

		}
	}




	@SuppressWarnings("unchecked")
	private void createGroupeList(TreeItem item) {
		Groupe[] lesGroupes = instance.getGroupe();
		int count=1;

		for(Groupe grp : lesGroupes) {
			TreeItem grpItem = new TreeItem(grp);
			item.getChildren().add(grpItem);
			if(this.createInterface.getGroupe()!=null) {
				if(this.createInterface.getGroupe().equals(grp)) {
					this.treeView.getSelectionModel().select(grpItem);
				}
			}

		}
	}



	@SuppressWarnings("unchecked")
	private void createSalleList(TreeItem item) {
		Salle [] lesSalles = instance.getSalles();
		for(Salle salle : lesSalles){
			TreeItem salleItem = new TreeItem(salle);
			item.getChildren().add(salleItem);
			if(this.createInterface.getSalle()!=null) {
				if(this.createInterface.getSalle().equals(salle)) {
					this.treeView.getSelectionModel().select(salleItem);
				}
			}
		}

	}


	public CreatePlanningDisplay getCreatePlanningDisplay() {
		return this.planning;
	}


}
