package application;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Probleme implements Serializable {
	private Enseignant[] enseignants;
	private Salle[] salles;
	private Creneau[] creneaux;
	private Groupe[] groupes;
	private Matiere[] matieres;
	private Activite [][] activites;
	private int nbGroupes, nbSols;
	
	public Probleme() {}

	public Probleme(int nbGroupes, int nbSols) {
		super();
		this.nbGroupes = nbGroupes;
		this.nbSols = nbSols;
//		this.setEnseignant(Arrays.copyOfRange(theEnseignants(), 0, 1 +(nbGroupes/2)*3));
//		this.setSalle(Arrays.copyOfRange(theSalles(), 0, 3 + nbGroupes));
		this.setEnseignant(theEnseignants());
		this.setSalle(theSalles());
		this.setCreneaux(theCreneaux());
		this.setGroupe(theGroupes(nbGroupes, nbSols));
		this.setMatiere(theMatieres());
		this.setActivites(theActivites());
	}

	public Probleme(int nbGroupes, int indiceDepard, int nbSols) {
		super();
		this.nbGroupes = nbGroupes;
		this.nbSols = nbSols;
		ArrayList<Enseignant> enseignantsList = new ArrayList<>();
		enseignantsList.add(theEnseignants()[0]);
		int indiceDepardProf = 1 + (indiceDepard/2) * 3;
		for (int i = 0; i < (nbGroupes/2)*3; i++) {
			enseignantsList.add(theEnseignants()[indiceDepardProf + i]);
		}
		//this.setEnseignant(enseignantsList.toArray(new Enseignant[enseignantsList.size()]));
		this.setEnseignant(theEnseignants());

		ArrayList<Salle> sallesList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			sallesList.add(theSalles()[i]);
		}
		for (int i = 0; i < nbGroupes; i++) {
			sallesList.add(theSalles()[3 + indiceDepard + i]);
		}
		//this.setSalle(sallesList.toArray(new Salle[sallesList.size()]));
		this.setSalle(theSalles());

		this.setCreneaux(theCreneaux());
		this.setGroupe(theGroupes(nbGroupes, indiceDepard, nbSols));
		this.setMatiere(theMatieres());
		this.setActivites(theActivites());
	}

	public void printProbleme(){
		System.out.println();
		for (int i = 0; i < getEnseignants().length; i++) {
			System.out.printf("%s; ", getEnseignant(i));
		}
		System.out.println();
		for (int i = 0; i < getSalles().length; i++) {
			System.out.printf("%s; ", getSalle(i));
		}
		System.out.println();
		for (int i = 0; i < getGroupe().length; i++) {
			System.out.printf("%s; ", getGroupe()[i]);
		}
		System.out.println();
	}

	public int getNbGroupes() {
		return nbGroupes;
	}

	public int getNbSols() {
		return nbSols;
	}

	public Enseignant getEnseignant(int indice) {
		return enseignants[indice];
	}

	public Enseignant[] getEnseignants(){ return this.enseignants;}
	
	public void setEnseignant(Enseignant[] enseignants) {
		this.enseignants = enseignants;
	}
	
	public int nbEnseignants() {
		return enseignants.length;
	}

	public Salle getSalle(int indice) {
		return salles[indice];
	}

	public Salle[] getSalles(){ return salles;}

	public void setSalle(Salle[] salles) {
		this.salles = salles;
	}

	public void setNbGroupes(int nbGroupes){ this.nbGroupes = nbGroupes;}
	
	public int nbSalles() {
		return salles.length;
	}

	public Creneau[] getCreneaux() {
		return creneaux;
	}

	public void setCreneaux(Creneau[] creneaux) {
		this.creneaux = creneaux;
	}
	
	public int nbCreneaux() {
		return creneaux.length;
	}

	public Groupe[] getGroupe() {
		return groupes;
	}

	public void setGroupe(Groupe[] groupes) {
		this.groupes = groupes;
	}
	
	public int nbGroupes() {
		return groupes.length;
	}

	public Matiere[] getMatieres() {
		return matieres;
	}

	public void setMatiere(Matiere[] matieres) {
		this.matieres = matieres;
	}
	
	public int nbMatieres() {
		return matieres.length;
	}
	
	public Activite getActivite(int groupe, int indice) {
		return activites[groupe][indice];
	}

	public void setActivites(Activite [][] activites) {
		this.activites = activites;
	}
	
	public int nbActivites() {
		return activites.length;
	}



	public Salle getSalleById(int id){
		for (int i = 0; i < getSalles().length; i++) {
			if( getSalle(i).getId() == id){
				return getSalle(i);
			}
		}
		return null;
	}

	public Enseignant getEnseignantById(int id){
		for (int i = 0; i < getEnseignants().length; i++) {
			if(getEnseignant(i).getId() == id){
				return getEnseignant(i);
			}
		}
		return null;
	}

	public Enseignant [] theEnseignants() {
		Enseignant [] enseignants = new Enseignant [30];
		enseignants[0] = new Enseignant("Yang Zheng", new ArrayList<Character>(){{add('I');}},new String[] {"anglais"}, 0);

		enseignants[1] = new Enseignant("Thierry Mathias", new ArrayList<Character>(){{add('A');}},
				new String[] {"referent", "maths", "bureautique"}, 1);
		enseignants[2] = new Enseignant("Christine Caldentey", new ArrayList<Character>(){{add('A');}},
				new String[] {"referent", "sport", "arl"}, new int[]{8, 9, 10, 11}, 2);
		enseignants[3] = new Enseignant("Slimane Sadelli", new ArrayList<Character>(){{add('A');}},
				new String[] {"referent", "francais"}, new int[]{12, 13, 14, 15}, 3);

		enseignants[4] = new Enseignant("Lionel Silvy", new ArrayList<Character>(){{add('B');}},
				new String[]{"referent", "sport", "bureautique"}, new int[]{10, 11}, 4);
		enseignants[5] = new Enseignant("Diane Pietrini", new ArrayList<Character>(){{add('B');}},
				new String[]{"referent", "maths", "francais"}, 5);
		enseignants[6] = new Enseignant("Florence Exbrayat", new ArrayList<Character>(){{add('B');}},
				new String[]{"referent", "francais"}, 6);

		enseignants[7] = new Enseignant("Carole Magaud", new ArrayList<Character>(){{add('C');}},
				new String[]{"referent", "francais", "sport"}, 7);
		enseignants[8] = new Enseignant("Mokhtar Mansouri", new ArrayList<Character>(){{add('C');}},
				new String[]{"referent", "bureautique", "maths"}, 8);
		enseignants[9] = new Enseignant("Philippe Del Bianco", new ArrayList<Character>(){{add('C');}},
				new String[]{"referent", "maths", "francais"}, 9);

		enseignants[10] = new Enseignant("Pascal Marullaz", new ArrayList<Character>(){{add('E');}},
				new String[]{"referent", "maths", "bureautique"}, 10);
		enseignants[11] = new Enseignant("Serge Raysseguier", new ArrayList<Character>(){{add('E');}},
				new String[]{"referent", "francais", "sport"}, 11);
		enseignants[12] = new Enseignant("Abes Hammachi", new ArrayList<Character>(){{add('E');}},
				new String[]{"referent", "bureautique", "maths"}, 12);

		enseignants[13] = new Enseignant("Jerome Pannetier", new ArrayList<Character>(){{add('G');}},
				new String[]{"referent", "maths", "bureautique"}, 13);
		enseignants[14] = new Enseignant("Timote Ducatez", new ArrayList<Character>(){{add('G');}},
				new String[]{"referent", "bureautique", "maths"}, 14);
		enseignants[15] = new Enseignant("Agnes Chambion", new ArrayList<Character>(){{add('G');}},
				new String[]{"referent", "francais", "sport"}, 15);

		enseignants[16] = new Enseignant("Sabine Chiavassa", new ArrayList<Character>(){{add('F');}},
				new String[]{"referent", "maths", "bureautique"}, 16);
		enseignants[17] = new Enseignant("Marina Halliez", new ArrayList<Character>(){{add('F');}},
				new String[]{"referent", "francais", "sport"}, 17);
		enseignants[18] = new Enseignant("Marie Rutowski", new ArrayList<Character>(){{add('F');}},
				new String[]{"referent", "bureautique", "maths" }, 18);

		enseignants[19] = new Enseignant("Mélanie Grenier", new ArrayList<Character>(){{add('H');}},
				new String[]{"referent", "maths", "bureautique"}, 19);
		enseignants[20] = new Enseignant("Margot Vinagre", new ArrayList<Character>(){{add('H');}},
				new String[]{"referent", "francais", "sport"}, 20);
		enseignants[21] = new Enseignant("Claude Garmirian", new ArrayList<Character>(){{add('H');}},
				new String[]{"referent", "bureautique", "maths"}, 21);

		enseignants[22] = new Enseignant("Jean-Michel Antonini", new ArrayList<Character>(){{add('M');}},
				new String[]{"referent", "maths", "bureautique"}, 22);
		enseignants[23] = new Enseignant("Vanessa Kopec", new ArrayList<Character>(){{add('M');}},
				new String[]{"referent", "francais", "anglais"}, 23); // Bizarre qu'il y ait anglais.
		enseignants[24] = new Enseignant("Sandra Lajaunie", new ArrayList<Character>(){{add('M');}},
				new String[]{"referent", "anglais", "sport"}, 24);

		enseignants[25] = new Enseignant("Françoise Reina", new ArrayList<Character>(){{add('D');}},
				new String[]{"referent", "maths", "bureautique"}, 25);
		enseignants[26] = new Enseignant("Félix Saludès", new ArrayList<Character>(){{add('D');}},
				new String[]{"referent", "bureautique", "sport"}, 26);
		enseignants[27] = new Enseignant("Daniel Libourel", new ArrayList<Character>(){{add('D');}},
				new String[]{"referent", "maths", "francais"}, 27);
		enseignants[28] = new Enseignant("Enseignant fictif", new ArrayList<Character>(){{add('I');}}, new String[]{"exterieures"}, 28);
		enseignants[29] = new Enseignant("Enseignant anglais fictif", new ArrayList<Character>(){{add('I');}}, new String[]{"anglais"}, 29); // nécessaire au dessus de 10 groupes
		return enseignants;
	}

	public ArrayList<String> exclusiveDiscipline(int indexEnseignant){
		ArrayList<String> classesList = theEnseignants()[indexEnseignant].getDisciplines();
		for (int i = 0; i < theEnseignants().length; i++)
			if(i!= indexEnseignant)
				for(String s : theEnseignants()[indexEnseignant].getDisciplines())
					if(theEnseignants()[i].getDisciplines().contains(s))
						classesList.remove(s);
		return classesList;
	}
	
	public Salle [] theSalles() {
		Salle [] salles = new Salle [21];
		salles[0] = new Salle (new ArrayList<Character>(){{add('I');}}, 504, new ArrayList<String>(){{add("anglais");}}, 15, 0);
		salles[1] = new Salle(new ArrayList<Character>(){{add('I');}}, 0, new ArrayList<String>(){{add("sport");}}, 500, 1); //'I' indique que c'est pour tout le monde
		salles[2] = new Salle(new ArrayList<Character>(){{add('I');}}, 1, new ArrayList<String>(){{add("autre");}}, 500, 2);
		salles[3] = new Salle(new ArrayList<Character>(){{add('A');}}, 513, new ArrayList<String>(){{add("info");}}, 15, 3);
		salles[4] = new Salle(new ArrayList<Character>(){{add('A');}}, 611, new ArrayList<String>(){{add("cours");add("info");}}, 15, 4);
		salles[5] = new Salle(new ArrayList<Character>(){{add('B');}}, 612, new ArrayList<String>(){{add("info");}}, 15, 5);
		salles[6] = new Salle(new ArrayList<Character>(){{add('B');}}, 502, new ArrayList<String>(){{add("cours");}}, 15, 6);
		salles[7] = new Salle(new ArrayList<Character>(){{add('C');}}, 613, new ArrayList<String>(){{add("info");}}, 15, 7);
		salles[8] = new Salle(new ArrayList<Character>(){{add('C');}}, 603, new ArrayList<String>(){{add("cours");}}, 15, 8);
		salles[9] = new Salle(new ArrayList<Character>(){{add('E');}}, 91, new ArrayList<String>(){{add("info");}}, 15, 9);	// 9bis
		salles[10] = new Salle(new ArrayList<Character>(){{add('E');}}, 503, new ArrayList<String>(){{add("cours");}}, 15, 10);
		salles[11] = new Salle(new ArrayList<Character>(){{add('G');}}, 512, new ArrayList<String>(){{add("info");}}, 15, 11);
		salles[12] = new Salle(new ArrayList<Character>(){{add('G');}}, 501, new ArrayList<String>(){{add("cours");}}, 15, 12);
		salles[13] = new Salle(new ArrayList<Character>(){{add('F');}}, 1000, new ArrayList<String>(){{add("info");}}, 15, 13); // Cdr
		salles[14] = new Salle(new ArrayList<Character>(){{add('F');}}, 604, new ArrayList<String>(){{add("cours");}}, 15, 14);
		salles[15] = new Salle(new ArrayList<Character>(){{add('H');}}, 1001, new ArrayList<String>(){{add("info");}}, 15, 15); // Cdr aussi ?
		salles[16] = new Salle(new ArrayList<Character>(){{add('H');}}, 605, new ArrayList<String>(){{add("cours");}}, 15, 16);
		salles[17] = new Salle(new ArrayList<Character>(){{add('M');}}, 505, new ArrayList<String>(){{add("info");}}, 15, 17);
		salles[18] = new Salle(new ArrayList<Character>(){{add('M');}}, 1002, new ArrayList<String>(){{add("cours");}}, 15, 18); // salle fictive 1 seule salle pour M?
		salles[19] = new Salle(new ArrayList<Character>(){{add('D');}}, 9, new ArrayList<String>(){{add("info");}}, 15, 19);
		salles[20] = new Salle(new ArrayList<Character>(){{add('D');}}, 1003, new ArrayList<String>(){{add("cours");}}, 15, 20); // D partage une salle avec H ? c'est la 605 normalement
//		salles[13] = new Salle('G', 511, new String[]{"cours"}, 15);
		return salles;
	}
	
	public Creneau [] theCreneaux() {
		Creneau [] creneaux = new Creneau [20];
		for(Jour jour : Jour.values()) {
			creneaux[jour.ordinal() * 4] = new Creneau(jour, 840, 1010);
			creneaux[jour.ordinal() * 4 + 1] = new Creneau(jour, 1030, 1200);
			creneaux[jour.ordinal() * 4 + 2] = new Creneau(jour, 1340, 1510);
			creneaux[jour.ordinal() * 4 + 3] = new Creneau(jour, 1530, 1700);
		}
		return creneaux;
	}
	
	public Groupe [] theGroupes() {
		Groupe [] groupes = new Groupe [18];
		groupes[0] = new Groupe('A', 1, 15, "generique", 1);
		groupes[1] = new Groupe('A', 2, 15, "generique", 2);
		//groupes[2] = new Groupe('A', 3, 15, "generique");
		groupes[2] = new Groupe( 'B', 3, 15, "generique", 1);
		groupes[3] = new Groupe( 'B', 4, 15, "generique", 2);
		groupes[4] = new Groupe( 'C', 5, 15, "generique", 1);
		groupes[5] = new Groupe( 'C', 6, 15, "generique", 2);
		groupes[6] = new Groupe( 'E', 7, 15, "generique", 1);
		groupes[7] = new Groupe( 'E', 8, 15, "generique", 2);
		groupes[8] = new Groupe( 'G', 9, 15, "generique", 1);
		groupes[9] = new Groupe( 'G', 10, 15, "generique", 2);
		groupes[10] = new Groupe( 'F', 11, 15, "generique", 1);
		groupes[11] = new Groupe( 'F', 12, 15, "generique", 2);
		groupes[12] = new Groupe( 'H', 13, 15, "generique", 1);
		groupes[13] = new Groupe( 'H', 14, 15, "generique", 2);
		groupes[14] = new Groupe( 'M', 15, 15, "generique", 1);
		groupes[15] = new Groupe( 'M', 16, 15, "generique", 2);
		groupes[16] = new Groupe( 'D', 17, 15, "generique", 2);
		groupes[17] = new Groupe( 'D', 18, 15, "generique", 2);
		return groupes;
	}
	public Groupe[] theGroupes(int nbSousGroupes, int nbSolutions){
		Groupe[] groupes = new Groupe[nbSousGroupes * nbSolutions];
		for (int i = 0; i < nbSousGroupes * nbSolutions; i++) {
			groupes[i] = new Groupe(theGroupes()[i%theGroupes().length].getAlphabet(), i+1, 15, "generique", (i%2)+1);
		}
		return groupes;
	}

	public Groupe[] theGroupes(int nbSousGroupes, int indiceDepard, int nbSolutions){
		Groupe[] groupes = new Groupe[nbSousGroupes * nbSolutions];
		for (int i = 0; i < nbSousGroupes * nbSolutions; i++) {
			groupes[i] = new Groupe(theGroupes()[(indiceDepard + i) % theGroupes().length].getAlphabet(), i+1, 15, "generique", (i%2)+1);
		}
		return groupes;
	}
	
	public Matiere [] theMatieres() {
			Matiere [] matieres = new Matiere [20];
			matieres[0] = new Matiere("Temps referent", new ArrayList<String>(){{add("cours");add("info");}}, 0,new int[]{0, 1, 18, 19});
			matieres[1] = new Matiere("Temps referent", new ArrayList<String>(){{add("cours");add("info");}}, 1, new int[]{0, 1, 18, 19});
			matieres[2] = new Matiere("Temps referent", new ArrayList<String>(){{add("cours");add("info");}}, 2,new int[]{0, 1, 18, 19});
			matieres[3] = new Matiere("Temps referent", new ArrayList<String>(){{add("cours");add("info");}}, 3, new int[]{0, 1, 18, 19});

//		matieres[4] = new Matiere("Projet pedagogique", new String[]{"cours", "autre"}, 0, Color.WHITE);
//		matieres[5] = new Matiere("Projet pedagogique", new String[]{"cours", "autre"}, 1, Color.WHITE);
			matieres[4] = new Matiere("Projet pedagogique referent", new ArrayList<String>(){{add("autre");}}, 0);
			matieres[5] = new Matiere("Projet pedagogique referent", new ArrayList<String>(){{add("autre");}}, 1);

			matieres[6] = new Matiere("EDA francais", new ArrayList<String>(){{add("info");}}, 0);

			matieres[7] = new Matiere("Remediation francais", new ArrayList<String>(){{add("cours");}}, 0);
			//matieres[3] = new Matiere("EDA francais - micro e/se", new String[]{"info"}, 0);

			matieres[8] = new Matiere("Francais", new ArrayList<String>(){{add("cours");}}, 0);

			matieres[9] = new Matiere("Atelier lecture/ecriture francais", new ArrayList<String>(){{add("cours");}}, 0);
			matieres[10] = new Matiere("EDA maths", new ArrayList<String>(){{add("info");}}, 0);
			//matieres[8] = new Matiere("EDA maths - micro e/se", "info", 0);
			matieres[11] = new Matiere("Remediation maths", new ArrayList<String>(){{add("cours");}}, 0);
			matieres[12] = new Matiere("Mathematique maths", new ArrayList<String>(){{add("cours");}}, 0);
			//matieres[13] = new Matiere("ARL", new String[]{"cours"}, 0);
			matieres[13] = new Matiere("EDA bureautique", new ArrayList<String>(){{add("info");}}, 0);
			//matieres[13] = new Matiere("EDA bureautique - micro e/se", "info", 0);
			matieres[14] = new Matiere("Remedition bureautique", new ArrayList<String>(){{add("cours");}}, 0);
			//matieres[15] = new Matiere("Bureautique", "info", 0);
			matieres[15] = new Matiere("Anglais", new ArrayList<String>(){{add("anglais");}}, 0);
			matieres[16] = new Matiere("Sport", new ArrayList<String>(){{add("sport");}}, 0, new int[]{3, 10, 9, 12, 13, 14, 15});
			matieres[17] = new Matiere("Sport", new ArrayList<String>(){{add("sport");}}, 1, new int[]{2, 3, 10, 11, 12, 13, 14, 15});
			matieres[18] = new Matiere("Demarches exterieures", new ArrayList<String>(){{add("autre");}}, 0, new int[]{6, 7});
			//matieres[19] = new Matiere("Null", "null", 0);
			matieres[19] = new Matiere("Demarches exterieures", new ArrayList<String>(){{add("autre");}}, 1, new int[]{6, 7});

		return matieres;
	}

	public Activite [][] theActivites() {
		Activite [][] activites = new Activite [getNbGroupes()][20];
		for (int i = 0; i < getNbGroupes(); i++) {
			for (int j = 0; j < 20; j++) {
				activites[i][j] = new Activite(groupes[i], matieres[j]);
			}
//			activites[i][0] = new Activite(groupes[i], matieres[0]);//referent
//			activites[i][1] = new Activite(groupes[i], matieres[20]);
//			activites[i][2] = new Activite(groupes[i], matieres[21]);
//			activites[i][3] = new Activite(groupes[i], matieres[22]);
//			activites[i][4] = new Activite(groupes[i], matieres[2]);//EDA
//			activites[i][5] = new Activite(groupes[i], matieres[3]);
//			activites[i][6] = new Activite(groupes[i], matieres[7]);
//			activites[i][7] = new Activite(groupes[i], matieres[12]);
//			activites[i][8] = new Activite(groupes[i], matieres[18]);//demarche ext
//			activites[i][9] = new Activite(groupes[i], matieres[25]);
//
//			activites[i][10] = new Activite(groupes[i], matieres[4]);//francais
//			activites[i][11] = new Activite(groupes[i], matieres[5]);
//			activites[i][12] = new Activite(groupes[i], matieres[6]);
//
//			activites[i][13] = new Activite(groupes[i], matieres[9]);//maths
//			activites[i][14] = new Activite(groupes[i], matieres[10]);
//
//			activites[i][15] = new Activite(groupes[i], matieres[11]);//ARL
//
//			activites[i][16] = new Activite(groupes[i], matieres[14]);//bureautique
//
//			activites[i][17] = new Activite(groupes[i], matieres[16]);//anglais
//
//			activites[i][18] = new Activite(groupes[i], matieres[17]);//sport
//			activites[i][19] = new Activite(groupes[i], matieres[24]);
		}
		return activites;
	}
}
