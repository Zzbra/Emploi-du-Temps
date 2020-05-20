package interfaces.EDT;

import application.*;
import application.Solveur;
import interfaces.MainInterface;
import javafx.scene.Group;

public class CreateInterface {
	private boolean canSetScrollDate = true;
	
	private Groupe groupe =null;
	private Enseignant enseignant =null;
	private Salle salle= null;
	private CreatePlanningDisplay planning=null;
	private Group root;
	private MainInterface main;
	private Solveur solveur;
	private String currentWeek;
	private String currentYear;
	private UserMenu userMenu =null;

	private EditMenu editMenu =null;
	

	public CreateInterface(Group root, MainInterface main,Solveur solveur) {
		this.root=root;
		this.main=main;
		this.solveur=solveur;
		this.editMenu = new EditMenu(this);
		new SearchBar(this);
		DateScrollMenu dateScroll = new DateScrollMenu(this);
		this.planning =new CreatePlanningDisplay(this);
		this.userMenu = new UserMenu(this);
		dateScroll.setScroll();
		setRoot();
	}
	
	
	public void setRoot() {
		root.getChildren().remove(userMenu.getTreeView());
		root.getChildren().remove(editMenu.getMenu());
		root.getChildren().add(editMenu.getMenu());
		root.getChildren().add(userMenu.getTreeView());
		
	}
	
	public void setUserMenu() {
		this.userMenu = new UserMenu(this);
	}


	public CreatePlanningDisplay getPlanning() {
		return this.planning;
	}

	public Groupe getGroupe() {
		return groupe;
	}


	public Enseignant getEnseignant() {
		return enseignant;
	}
	
	public UserMenu getUserMenu() {
		return userMenu;
	}



	public Salle getSalle() {
		return salle;
	}
	
	public Group getRoot() {
		return root;
	}


	public MainInterface getMain() {
		return main;
	}


	public Solveur getSolveur() {
		return solveur;
	}
	public void setParam(Groupe groupe,Enseignant ens, Salle salle) {
		this.groupe=groupe;
		this.enseignant=ens;
		this.salle=salle;
	}


	public String getCurrentWeek() {
		return currentWeek;
	}


	public void setCurrentWeek(String currentWeek, String currentYear) {
		this.currentWeek = currentWeek;
		this.currentYear=currentYear;
	}
	
	public String getCurrentYear() {
		return currentYear;
	}
	
	public void setInterface(Group root, MainInterface main,Solveur solveur) {
		this.root=root;
		this.setCanSetScrollDate(true);
		this.main=main;
		this.solveur=solveur;
		this.editMenu = new EditMenu(this);
		new SearchBar(this);
		DateScrollMenu dateScroll=new DateScrollMenu(this);
		this.planning =new CreatePlanningDisplay(this);
		this.userMenu = new UserMenu(this);
		dateScroll.setScroll();
		setRoot();
	}


	/**
	 * @return the canSetScrollDate
	 */
	public boolean isCanSetScrollDate() {
		return canSetScrollDate;
	}


	/**
	 * @param canSetScrollDate the canSetScrollDate to set
	 */
	public void setCanSetScrollDate(boolean canSetScrollDate) {
		this.canSetScrollDate = canSetScrollDate;
	}

}
