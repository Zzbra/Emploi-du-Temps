package interfaces;


import application.CaseEdTGroupe;
import application.Enseignant;
import application.Groupe;
import application.Probleme;
import application.Salle;
import application.Solveur;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

@SuppressWarnings("rawtypes")
public class setNewPlanning {


	private Group root;
	private Groupe groupe;
	private String day;

	private TableColumn column;
	private Enseignant enseignant;
	private Salle salle;
	private MainInterface main;
	private Probleme instance;
	private CaseEdTGroupe[][] edt;
	public setNewPlanning(Group root, Groupe groupe, String day, TableColumn dayColumn, MainInterface main,Solveur solveur) {
		this.root=root;
		this.day=day;
		this.groupe=groupe;
		this.column=dayColumn;
		this.main=main;
		this.instance = solveur.getInstance();

		this.edt = solveur.getResultat();
		getGrpPlanning();
	}


	public setNewPlanning(Group root, Enseignant ens,String day, TableColumn dayColumn, MainInterface main,Solveur solveur) {
		this.root=root;
		this.day=day;
		this.column=dayColumn;
		this.enseignant=ens;
		this.main=main;
		this.instance = solveur.getInstance();

		this.edt = solveur.getResultat();
		getEnsPlanning();


	}

	public setNewPlanning(Group root, Salle salle,String day, TableColumn dayColumn, MainInterface main,Solveur solveur) {
		this.root=root;
		this.day=day;
		this.column=dayColumn;
		this.salle=salle;
		this.main=main;
		this.instance = solveur.getInstance();

		this.edt = solveur.getResultat();
		getSallePlanning();

	}


	public void getEnsPlanning() {
		for(int grp = 0; grp < 2 ; grp++) {
			for(int cre = 0 ; cre < instance.nbCreneaux() ; cre++) {
				if(edt[grp][cre].getEnseignant().getName().equals(enseignant.getName())) {
					main.setParam(null,enseignant,null);
					if (instance.getCreneaux()[cre].toString().contains(day)) {
						boolean duplicate = searchDuplicate(grp,cre,false);
						if(!duplicate) {
							getPlanning(edt[grp][cre],getCurrentCreneau(cre),false,true,true,false,0);
						}
						else {
							getPlanning(edt[grp][cre],getCurrentCreneau(cre),false,true,true,true,1);
						}
					}
				}
			}
		}
	}

	public void getGrpPlanning() {



		for(int cre=0; cre<instance.nbCreneaux(); cre++) {
			if (instance.getCreneaux()[cre].toString().contains(day)) {
				if(edt[groupe.getNumber()-1][cre]!=null)
					main.setParam(groupe,null,null);
				getPlanning(edt[groupe.getNumber()-1][cre],getCurrentCreneau(cre),true,false,true,false,0);
			}
		}	

	}

	public void getSallePlanning() {
		for(int grp = 0; grp < 2 ; grp++) {
			for(int cre = 0 ; cre < instance.nbCreneaux() ; cre++) {
				if(edt[grp][cre].getSalle().getNumber() == salle.getNumber()) {
					main.setParam(null,null,salle);
					if (instance.getCreneaux()[cre].toString().contains(day)) {
						boolean duplicate = searchDuplicate(grp,cre,true);
						if(!duplicate) {
							getPlanning(edt[grp][cre],getCurrentCreneau(cre),true,true,false,false,0);
						}
						else {
							getPlanning(edt[grp][cre],getCurrentCreneau(cre),true,true,false,true,1);
						}
					}
				}
			}
		}
	}




	public int getCurrentCreneau(int creneau) {
		int returnCreneau = 0;
		switch(instance.getCreneaux()[creneau].getStarttime()) {
		case 840 : 
			returnCreneau= 0;
			break;
		case 1030 :
			returnCreneau= 1;
			break;
		case 1340 :
			returnCreneau= 2;
			break;
		case 1530 :
			returnCreneau= 3;
			break;
		}
		return returnCreneau;
	}


	public void getPlanning(CaseEdTGroupe edt,int count,boolean dispEns,boolean dispGrp,boolean dispSalle,boolean duplic,int numDuplic) {

		Rectangle rect = new Rectangle(100,100);
		rect.setFill(main.getMatCol().getColor(edt.getActivite().getMatiere()));


		Label matiere = new Label();

		matiere.setText(edt.getActivite().getMatiere().getSubject().toString());
		matiere.setTextFill(Color.BLACK);


		Label label1 = new Label();
		label1.setTextFill(Color.BLACK);

		Label label2 = new Label();
		label2.setTextFill(Color.BLACK);

		if(dispEns&& dispSalle) {
			label1.setText(edt.getEnseignant().getName().toString());
			label2.setText(String.valueOf(edt.getSalle().getNumber()));
		}
		else if(dispGrp && dispSalle) {
			label1.setText("Groupe "+edt.getActivite().getGroupe().toString());
			label2.setText(String.valueOf(edt.getSalle().getNumber()));
		}
		else {
			label1.setText(edt.getEnseignant().getName().toString());
			label2.setText("Groupe "+edt.getActivite().getGroupe().toString());
		}

		if(numDuplic==1) {
			label2.setText("Plusieurs Groupes");
		}


		new SizeDisplayOnScene(matiere,label1,label2,rect,count,duplic, numDuplic,day,column,main);

		matiere.setMaxWidth(Double.MAX_VALUE);
		AnchorPane.setLeftAnchor(matiere, rect.getX());
		AnchorPane.setRightAnchor(matiere, 0.0);
		matiere.setAlignment(Pos.CENTER);

		label1.setMaxWidth(Double.MAX_VALUE);
		AnchorPane.setLeftAnchor(label1, rect.getX());
		AnchorPane.setRightAnchor(label1, 0.0);
		label1.setAlignment(Pos.CENTER);

		label2.setMaxWidth(Double.MAX_VALUE);
		AnchorPane.setLeftAnchor(label2, rect.getX());
		AnchorPane.setRightAnchor(label2, 0.0);
		label2.setAlignment(Pos.CENTER);

		AnchorPane anchor = new AnchorPane(rect,matiere,label1,label2);
		root.getChildren().add(anchor);

	}




	public  boolean searchDuplicate(int groupe, int creneau,boolean searchSalle) {
		for(int grp = 0; grp < 2 ; grp++) {
			for(int cre = 0 ; cre < instance.nbCreneaux() ; cre++) {
				if(searchSalle && edt[grp][cre].getSalle().getNumber() == edt[groupe][creneau].getSalle().getNumber()  && edt[grp][cre].getActivite().getGroupe()!=edt[groupe][creneau].getActivite().getGroupe() && cre ==creneau){
					return true;
				}
				else if(edt[grp][cre].getEnseignant().equals(this.enseignant) && edt[groupe][creneau].getEnseignant().equals(this.enseignant) && edt[grp][cre].getSalle()==edt[groupe][creneau].getSalle() && cre==creneau && edt[grp][cre].getActivite().getGroupe()==edt[groupe][creneau].getActivite().getGroupe())
					return true;
			}
		}
		return false;
	}
}

