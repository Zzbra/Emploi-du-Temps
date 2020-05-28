package application;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.search.limits.FailCounter;
import org.chocosolver.solver.search.loop.lns.INeighborFactory;
import org.chocosolver.solver.search.loop.move.MoveLNS;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.selectors.variables.InputOrder;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;


import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
	Contraintes à implémenter:
		- Certains cours doivent être donnés par bloc de 2
		- Contrainte qui fait en sorte que l'enseignant référent d'un sous groupe lui enseigne les cours référent

	J'ai besoin de savoir si les discipline ayant pour salle x/y (ex cours/info)
	peuvent être donnés dans l'une ou l'autre en fonction des disponibilités ou
	doivent être donnés certaine semaine dans l'une et d'autres dans l'autre
	Pour l'instant c'est la première option qui est implémentée




 */

public class Solveur {
	private Probleme instance;
	private Model model;
	private int nbEnseignants, nbCreneaux, nbSalles, nbActivites, nbGroupes;
	private Activite[][] activitesMat;
	private IntVar[][] heures, enseignants, salles;
	private CaseEdTGroupe[][] resultat, modele;
	private IntVar deviation_totale;
	private IntVar[][] deviation;
	private SolutionEdt solutionEdt;
	private int nbSol;
	private IntVar[] finalDataCat;
	private int[] finalDataCatSol;

	public Solveur(Probleme instance) {
		this.instance = instance;
		this.model = new Model("EDT");
		this.nbEnseignants = instance.nbEnseignants();
		this.nbCreneaux = instance.nbCreneaux();
		this.nbSalles = instance.nbSalles();
		this.nbActivites = 20;
		this.nbGroupes = instance.getNbGroupes();
		this.activitesMat = instance.theActivites();
		this.nbSol = instance.getNbSols();
		this.deviation_totale = model.intVar("deviation_totale", 0, IntVar.MAX_INT_BOUND);
		this.deviation = new IntVar[3][nbGroupes*nbActivites];
		for (int i = 0; i < 3; i++) {
			this.deviation[i] = IntStream
					.range(0, nbGroupes*nbActivites)
					.mapToObj(j -> model.intVar("deviation #" + j, 0, 1, false))
					.toArray(IntVar[]::new);
		}

		// On récupère les dommaines de définitions des salles et enseignants
		// des différents groupes.
		ArrayList<int[]> iDSallesGroupes = getIdSallesParGroupes();
		ArrayList<int[]> iDEnseignantsGroupes = getIdEnseignantsParGroupes();

		// Ici, on instancie les variables avec les domaines de définitions qui leur correspond
		this.salles = new IntVar[nbGroupes][nbActivites];
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				this.salles[i][j] = model.intVar("salle: " + i + " " + j, iDSallesGroupes.get(i));
			}
		}

		this.enseignants = new IntVar[nbGroupes][nbActivites];
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				this.enseignants[i][j] = model.intVar("enseignants: " + i + " " + j, iDEnseignantsGroupes.get(i));
			}
		}

		this.heures = new IntVar[nbGroupes][nbActivites];
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				this.heures[i][j] = model.intVar("heure: " + i + " " + j, 0, nbCreneaux - 1);
			}
		}

		/*
		   C'est ici qu'il faut préciser la solution de référence.
		   Il est possible d'utiliser une solution de référence pour plus de sous
		   groupes que sur le jeu de donné en entré en ajustant le nombre de sous
		   groupes sur la solution de référence.
		   Par exemple on exécute sur new Probleme(10);
		   faire: this.solutionEdt.setNbGroupes(10);
		   Uniquement si nbGroupes de solutionEdt > 10
		*/

		File fichier = new File("src/Solutions_Serialisees/sol18.ser");
		ObjectInputStream ois = null;
		this.solutionEdt = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fichier));
			this.solutionEdt = (SolutionEdt)ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}


	// Cette contrainte implémente le calcul de déviation par rapport à un modèle
	// comme fait sur le tutoriel sur les avions du site de choco solver
	// Elle est rendue obsolète par le LNS et LDS
	private void contrainteCalculDeviation(){
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				deviation[0][i*nbActivites + j].eq(heures[i][j].sub(this.solutionEdt.getHeures()[i][j]).abs()).post();
			}
		}
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				deviation[1][i*nbActivites + j].eq(enseignants[i][j].sub(this.solutionEdt.getEnseignants()[i][j]).abs()).post();
			}
		}
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				deviation[2][i*nbActivites + j].eq(salles[i][j].sub(this.solutionEdt.getSalles()[i][j]).abs()).post();
			}
		}

		int[] coeff = new int[3 * nbGroupes * nbActivites];
		for (int i = 0; i < 3 * nbGroupes * nbActivites; i++) {
			coeff[i] = 1;
		}


		model.scalar(ArrayUtils.append(deviation[0], deviation[1], deviation[2]), coeff, "=", deviation_totale).post();
	}

	public Probleme getInstance() {
		return instance;
	}

	public CaseEdTGroupe[][] getResultat() {
		return resultat;
	}

	// Cette fonction recherche les domaines de définition des variables de modélisation.
	// Utilisé pour instancier enseignant[][]
	private ArrayList<int[]> getIdEnseignantsParGroupes(){
		ArrayList<int[]> resultat = new ArrayList<>();
		for (int i = 0; i < nbGroupes; i++) {
			ArrayList<Integer> ligne = new ArrayList<>();
			for (int j = 0; j < nbEnseignants; j++) {
				char groupe = this.instance.getGroupe()[i].getAlphabet();
				if(this.instance.getEnseignant(j).getGroupe().contains(groupe) ||
						this.instance.getEnseignant(j).getGroupe().contains('I')){
					ligne.add(this.instance.getEnseignant(j).getId());
				}
			}
			resultat.add(ligne.stream().mapToInt(k -> k).toArray());
		}
		for(int[] tab : resultat){
			for (int i = 0; i < tab.length; i++) {
				System.out.printf("%d; ", tab[i]);
			}
			System.out.println();
		}
		return resultat;
	}

	// Pareil que celle d'avant mais pour les salles
	private ArrayList<int[]> getIdSallesParGroupes(){
		ArrayList<int[]> resultat = new ArrayList<>();
		for (int i = 0; i < nbGroupes; i++) {
			ArrayList<Integer> ligne = new ArrayList<>();
			for (int j = 0; j < nbSalles; j++) {
				char groupe = this.instance.getGroupe()[i].getAlphabet();
				if(this.instance.getSalle(j).getGroupe().contains(groupe) ||
						this.instance.getSalle(j).getGroupe().contains('I')){
					ligne.add(this.instance.getSalle(j).getId());
				}
			}
			resultat.add(ligne.stream().mapToInt(k -> k).toArray());
		}
		int j = 1;
		for(int[] tab : resultat){
			System.out.println("Groupe" + j);
			for (int i = 0; i < tab.length; i++) {
				System.out.printf("%d; ", tab[i]);
			}
			System.out.println();
			j++;
			System.out.println();
		}
		return resultat;
	}

	// Cette fonction sert à vérifier si les activités i et j peuvent avoir lieu dans la même salle en même temps
	// C'est le cas de sport ou démarche extérieur. i représente l'activité activiteMat[i / nbActivites][i % nbActivites]
	// On pourrais imaginer créer un champ paralélisable sur les matières ou gérer en fonction de la capacité des salles
	// Mais celà pourrait créer des problèmes ou plusieurs groupes ont cour dans la même salle.
	private boolean activiteCompatible(int i, int j){
		ArrayList<String> natureI = activitesMat[i / nbActivites][i % nbActivites].getMatiere().getNature();
		ArrayList<String> natureJ = activitesMat[j / nbActivites][j % nbActivites].getMatiere().getNature();

		for(String s : natureI)
			s = s.toLowerCase();

		for(String s : natureJ)
			s = s.toLowerCase();

		if((natureI.contains("autre") && natureJ.contains("autre")) || (natureI.contains("sport") && natureJ.contains("sport"))){
			return true;
		}
		return false;
	}

	// Deux activité ne peuvent avoir lieu dans la même salle au même moment
	private void contrainteActiviteTempsSalle(){
		for(int i = 0; i < nbActivites*nbGroupes; i++) {
			for(int j = i+1; j < nbActivites*nbGroupes; j++) {
				if(activitesMat[i/nbActivites][i%nbActivites].getGroupe().getAlphabet() == activitesMat[j/nbActivites][j%nbActivites].getGroupe().getAlphabet()) {
					if (i != j && !activiteCompatible(i, j)) {
						model.addClauses(LogOp.or(model.arithm(heures[i / nbActivites][i % nbActivites], "!=", heures[j / nbActivites][j % nbActivites]).reify(),
								(model.arithm(salles[i / nbActivites][i % nbActivites], "!=", salles[j / nbActivites][j % nbActivites]).reify())));
					}
				}
			}
		}
	}
	// Deux Activites de meme groupe ne peuvent pas se passer au meme moment.
	private void contrainteActiviteMemeGroupeMemeTemps(){
		for (int i = 0; i < nbGroupes; i++) {
			model.allDifferent(heures[i]).post();
		}
	}


	/*
	 	Le prof d'anglais ne donne que des cours d'anglais
	   	Cette contrainte pourrait être amené à changer pour prendre en compte les
	    profs d'anglais des sous groupes de 10 à 18. En effet, ils n'enseignent plus
	    uniquement les cours d'anglais. Pour l'instant un prof d'anglais fictif à été
	    ajouté pour améliorer le temps de résolution quand le probème devient grand.
	 */

	private void contrainteProfAnglais(){
		ArrayList<Integer> idProfAnglais = new ArrayList<>();
		for(int i = 0; i < instance.getEnseignants().length; i++){
			if(instance.getEnseignants()[i].getDisciplines().contains("anglais") && instance.getEnseignants()[i].getGroupe().contains('I'))
				idProfAnglais.add(instance.getEnseignants()[i].getId());
		}
		for(int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				// On s'assure que le prof d'anglais ne donne que le cour d'anglais
				for(Integer id : idProfAnglais) {
					if (!activitesMat[i][j].getMatiere().getSubject().equals("Anglais")) {
						model.arithm(enseignants[i][j], "!=", id).post();
					}
				}
			}
		}
	}
	// Deux Activit��s ne peuvent pas avoir un meme prof en meme temps.
	private void contrainteProfMemeTemps(){
		for(int i = 0; i < nbActivites*nbGroupes; i++) {
			for(int j = 0; j < nbActivites*nbGroupes; j++) {
				if(i != j) {
					String natI = activitesMat[i/nbActivites][i%nbActivites].getMatiere().getSubject();
					String natJ = activitesMat[j/nbActivites][j%nbActivites].getMatiere().getSubject();
					if(!natI.contains("exterieures") || !natJ.contains("exterieures") ) {
						model.addClauses(LogOp.or(model.arithm(heures[i / nbActivites][i % nbActivites], "!=", heures[j / nbActivites][j % nbActivites]).reify(),
								model.arithm(enseignants[i / nbActivites][i % nbActivites], "!=", enseignants[j / nbActivites][j % nbActivites]).reify()));
					}
				}
			}
		}
	}

	/*
		Cette contrainte s'assure qu'un enseignant n'enseigne pas une matière qu'il n'est pas habilité à enseigner.
		Elle exclut les matières de type EDA qui peuvent être enseignées par tout les enseignants.
	 */
	private void contrainteCourImpropre(){
		for (int i = 0; i < instance.getEnseignants().length; i++) {
			Enseignant enseignant = instance.getEnseignant(i);
			for (int j = 0; j < nbActivites*nbGroupes; j++) {
				Activite activite = activitesMat[j/nbActivites][j%nbActivites];
				// si le prof enseigne dans ce groupe
				if(enseignant.getGroupe().contains(activite.getGroupe().getAlphabet())
					|| enseignant.getGroupe().contains('I')){
					boolean peutEnseigner = false;
					for(String s : enseignant.getDisciplines()){
						//System.out.println(activite.getMatiere().getSubject() + "   " + s);
						if(activite.getMatiere().getSubject().toLowerCase().contains(s.toLowerCase())
							|| activite.getMatiere().getSubject().startsWith("EDA")){
							if(activite.getMatiere().getSubject().startsWith("EDA")){
								if(!enseignant.getGroupe().contains('I')){
									peutEnseigner = true;
								}
							}else {
								//System.out.println(activite.getMatiere().getSubject() + "   " + s);
								peutEnseigner = true;
							}
						}
					}
					if(!peutEnseigner){
						model.arithm(enseignants[j/nbActivites][j%nbActivites], "!=", enseignant.getId()).post();
					}
				}
			}
		}
	}

	/*
		Cette contrainte fait en sorte qu'un enseignant enseigne les matières qu'il est le seul à pouvoir
		enseigner dans son sous groupe.
		Elle date de précédentes choix de médilation et devrait ne plus jamais servir.
		Elle peut donc être retiré. La fonction Probleme.exclusiveDiscipline() peut également être retirée.
	 */
	private void contrainteCourMatiereExclusive(){
		// On s'assure que les profs enseignent leurs matières exclusives
		// Pour chaque activité de chaque groupe
		for(int i = 0; i < nbActivites*nbGroupes; i++) {
			// On extrait le chaine qui décrit la matière
			String nature = activitesMat[i/nbActivites][i%nbActivites].getMatiere().getSubject();
			// Pour chaque enseignant
			for(int j = 0; j < nbEnseignants; j++){
				// On extrait une liste de matières qui lui sont exclusive
				ArrayList<String> matieresExclusives = instance.exclusiveDiscipline(j);
				System.out.println(instance.theEnseignants()[j]);
				System.out.println(matieresExclusives + "\n");
				// On vérifie qu'elle n'est pas précédée par EDA car tout les proffesseurs sont
				// Habilités à enseigner des matières dont l'intitulé commence par EDA
				if(!nature.startsWith("EDA"))
					// Pour charque matière exclusive de chaque proffesseur
					for(String matiere :matieresExclusives)
						if(nature.toLowerCase().contains(matiere.toLowerCase()))
							// On s'assure que l'activité qui correspond est bien enseignée par le proffesseur en question
							// Ici il faudra préciser la méthode avec laquelle on assicie les intitulés des matières
							// avec ceux des cours donné par les profs. Ils diffèrent, ce qui peux causer des problèmes
							model.arithm(enseignants[i/nbActivites][i%nbActivites], "=", instance.getEnseignant(j).getId()).post();
			}
		}
	}

	// On vérifie que les cours sont donnés dans le type de salle qui leur correspond
	private void contraintesCoursSalles(){
		/*
			La structure mapSalles recense les index des salles en fonction de leur nature
			par exemple le clef "info" retournera la liste des indices des salles d'info
			dans le table des salles fournie en entrée
		 */
		HashMap<String, ArrayList<Integer>> mapSalles = new HashMap<>();
		for (int i = 0; i < this.nbSalles; i++) {
			for(String key : instance.getSalles()[i].getNature()){
				if(mapSalles.containsKey(key)){
					mapSalles.get(key).add(instance.getSalle(i).getId());
				}else{
					ArrayList<Integer> entry = new ArrayList<>();
					entry.add(instance.getSalles()[i].getId());
					mapSalles.put(key, entry);
				}
			}
		}

		for(Map.Entry<String, ArrayList<Integer>> entry : mapSalles.entrySet()){
			System.out.println(entry.getKey() +": "+ entry.getValue());
		}

		// Pour chaque activité de chaque groupe
		for (int i = 0; i < nbGroupes; i++) {
			for(int j = 0; j < nbActivites; j++){
				ArrayList<BoolVar> boolList = new ArrayList<>();
				// On regarde la nature de l'activité (peut être multiple: info;cour par ex)
				for(String s : activitesMat[i][j].getMatiere().getNature()){
					// Et on créé la contrainte qui assigne le cour avec une
					// salle de même nature (il peut il y avoir plusieurs salles)
					if(mapSalles.get(s) != null) {
						for (int k = 0; k < mapSalles.get(s).size(); k++) {
							boolList.add(model.arithm(salles[i][j], "=", mapSalles.get(s).get(k)).reify());
						}
					}
				}
				if(boolList.size() > 0) {
					BoolVar[] boolVars = boolList.toArray(new BoolVar[boolList.size()]);
					model.addClausesBoolOrArrayEqualTrue(boolVars);
				}
			}
		}
	}
	// Gestion des créneaux préférentiels des cours
	private void contrainteCreneauPreferentiel(){
		for(int i = 0; i < nbGroupes; i++) {
			for(int j = 0; j < nbActivites; j++) {
				if (activitesMat[i][j].getMatiere().hasCreneau()) {
					ArrayList<Integer> creneaux = activitesMat[i][j].getMatiere().getCreneaux();
					BoolVar[] boolArray = new BoolVar[creneaux.size()];
					for (int k = 0; k < creneaux.size(); k++) {
						boolArray[k] = model.arithm(heures[i][j], "=", creneaux.get(k)).reify();
					}
					model.addClausesBoolOrArrayEqualTrue(boolArray);
				}
			}
		}
	}
	// Contrainte pour briser un type de symétrie (explicité dans compute())
	private void contrainteSymetrieSequence(){
		ArrayList<HashMap<String, Integer>> nbSequenceMatiere = new ArrayList<>();
		for(int i = 0; i < nbGroupes; i++) {
			nbSequenceMatiere.add( new HashMap<>());
			for (int j = 0; j < nbActivites; j++) {
				String key = activitesMat[i][j].getMatiere().getSubject();
				int numSeq = activitesMat[i][j].getMatiere().getSequence();
				// Si la matière est déjà présente, on met à jour le numéro de séquence
				// si il est supérieur à celui déjà présent.
				if (nbSequenceMatiere.get(i).get(key) != null) {
					if (nbSequenceMatiere.get(i).get(key) < numSeq) {
						nbSequenceMatiere.get(i).replace(key, numSeq);
					}
					// Sinon, on ajoute la matière si son numéro de séquence est supérieur à 0.
				} else if (numSeq > 0) {
					nbSequenceMatiere.get(i).put(key, numSeq);
				}
			}
		}

		// Par groupe
		for(int i = 0; i < nbGroupes; i++){
			// Par matière ayant plusieurs séquences
			for(Map.Entry<String, Integer> matiereMap : nbSequenceMatiere.get(i).entrySet()){
				String matiere = matiereMap.getKey();
				int seqMax = matiereMap.getValue();
				// On s'assure que les séquences se déroulent dans le bon ordre
				for(int j = 0; j < seqMax; j++){
					// On trouve les indices des deux séquence qui doivent se suivre
					int indice1 = getIndiceSeq(matiere, j, i);
					int indice2 = getIndiceSeq(matiere, j+1, i);
					// Et on créé la contrainte dans la structure heures[][]
					model.arithm(heures[i][indice1], "<", heures[i][indice2]).post();
				}
			}
		}

		// Partie impression de la structure nbSequencesMatiere.
		int i = 1;
		for(HashMap<String, Integer> h : nbSequenceMatiere){
			System.out.printf("Groupe %d", i);
			System.out.println(h);
			i++;
		}
	}

	// Contrainte symetrie classe obsolète depuis l'assignation rigide entre salle et sous groupe
	private void contrainteSymetrieClasses(){
		// Pour chaque actvité (créneau)
		for (int i = 0; i < nbActivites; i++) {
			// Pour tout les groupes deux à deux
			for (int j = 0; j < nbGroupes-1; j++) {
				for (int k = j+1; k < nbGroupes; k++) {
					// Si les groupe sont de même nature, on définit un ordre sur les salles
					if(activitesMat[j][i].getMatiere().getNature().equals(activitesMat[k][i].getMatiere().getNature())){
						model.arithm(salles[j][i], "<=", salles[k][i]).post();
					}
				}
			}
		}
	}

	// Pareil que la contrainte précédente mais avec les enseignants.
	private void contrainteSymetrieEnseignants(){
		// Pour chaque actvité (créneau)
		for (int i = 0; i < nbActivites; i++) {
			// Pour tout les groupes deux à deux
			for (int j = 0; j < nbGroupes-1; j++) {
				for (int k = j+1; k < nbGroupes; k++) {
					// Si les groupe sont de même nature, on définit un ordre sur les enseignants
					if(activitesMat[j][i].getMatiere().getNature().equals(activitesMat[k][i].getMatiere().getNature())){
						model.arithm(enseignants[j][i], "<=", enseignants[k][i]).post();
					}
				}
			}
		}
	}


	// Briser un type de symétrie sur les salles mais induit un ordre dans les salles qui n'est pas nécessairement voulu
	private void contrainteSymetrieSallePourUnGroupe(){
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				for (int k = j+1; k < nbActivites; k++) {
					if(activitesMat[i][j].getMatiere().getNature().equals(activitesMat[i][k])){
						model.arithm(salles[i][j], "<", salles[i][k]).post();
					}
				}
			}
		}
	}

	// Pareil que la conrainte précédente mais pur les enseignants
	private void contrainteSymetrieProfPourUnGroupe(){
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				for (int k = j+1; k < nbActivites; k++) {
					if(activitesMat[i][j].getMatiere().getNature().equals(activitesMat[i][k])){
						model.arithm(enseignants[i][j], "<", enseignants[i][k]).post();
					}
				}
			}
		}
	}

	private boolean hasCommonElement(ArrayList<String> arr1, ArrayList<String> arr2){
		for(String s : arr1){
			if(arr2.contains(s))
				return true;
		}
		return false;
	}

	// Cette contrainte est obsolète depuis la redéfinition des domaines de définition des variables dans IntVar salle[][]
	private void contrainteSalleGroupe(){
		// Pour chaque activité de chaque groupe
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				ArrayList<BoolVar> boolList = new ArrayList<>();
				for (int k = 0; k < nbSalles; k++) {
					// On s'assure que le cour est donné dans une salle corespondant au groupe
					if(hasCommonElement(activitesMat[i][j].getMatiere().getNature(), instance.getSalle(k).getNature())){

						if(getInstance().getSalle(k).getGroupe().contains(activitesMat[i][j].getGroupe().getAlphabet()) ||
							getInstance().getSalle(k).getGroupe().contains('I')){
							System.out.println(activitesMat[i][j].getMatiere() + "G: " + activitesMat[i][j].getGroupe().getAlphabet() +
									" " + instance.getSalle(k) + ": " + instance.getSalle(k).getNature());
							boolList.add(model.arithm(salles[i][j], "=", k).reify());
						}
						System.out.println();
					}
				}
				if(boolList.size() > 0)
					model.addClausesBoolOrArrayEqualTrue(boolList.toArray(new BoolVar[boolList.size()]));
			}
		}
	}

	// Pareil que la contrainte précédente mais sur IntVar enseignants[][]
	private void contrainteEnseignantGroupe(){
		// Pour chaque activité de chaque groupe
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				ArrayList<BoolVar> boolList = new ArrayList<>();
				for (int k = 0; k < nbEnseignants; k++) {
					// On s'assure que le cour est donné dans une salle corespondant au groupe
					if(hasCommonElement(activitesMat[i][j].getMatiere().getNature(), instance.getEnseignant(k).getDisciplines())){
						if(getInstance().getEnseignant(k).getGroupe().contains(activitesMat[i][j].getGroupe().getAlphabet()) ||
							getInstance().getEnseignant(k).getGroupe().contains('I')){
							boolList.add(model.arithm(enseignants[i][j], "=", k).reify());
						}
					}
				}
				if(boolList.size() > 0)
					model.addClausesBoolOrArrayEqualTrue(boolList.toArray(new BoolVar[boolList.size()]));
			}
		}
	}

	// Fonction qui pose toute les contraintes
	public void definirContraintes(){

		// Deux activité ne peuvent avoir lieu dans la même salle au même moment
		this.contrainteActiviteTempsSalle();

		// Deux Activites de meme groupe ne peuvent pas se passe au meme temps.
		this.contrainteActiviteMemeGroupeMemeTemps();

		// Le prof d'anglais ne donne que des cours d'anglais
		this.contrainteProfAnglais();

		// On vérifie que les cours sont donnés dans le type de salle qui leur correspond
		this.contraintesCoursSalles();

		// Deux Activités ne peuvent pas avoir un meme prof en meme temps.
		this.contrainteProfMemeTemps();

		// Un prof ne peut pas enseigner les cours qu'il n'est pas habilité à enseigner
		this.contrainteCourImpropre();

		// Gestion des creneaux offs
		this.contrainteCreneauxOffs();

		// Gestion des créneaux préférentiels des cours
		// L'emplois du temps ne fonctionne pas avec les contraintes actuelles sur le sport
		// J'ai donc rajouté quelques créneaux disponibles aux deux scéances de sport pour qu'il fonctionne
		this.contrainteCreneauPreferentiel();

		// Contrainte qui s'assure que les cours ayant lieux plusieurs fois se déroule selon la même séquence
		// pour briser les symétries
		this.contrainteSymetrieSequence();

//		this.contrainteSymetrieClasses();
//
//		this.contrainteSymetrieEnseignants();

//		this.contrainteSalleGroupe();
//
//		this.contrainteEnseignantGroupe();

//		this.contrainteCalculDeviation();


	}

	/*
		Cette fonction résoud le problème en optimisant la variable qui recense la divergence avec le modèle
		tel que fait dans le tutoriel sur les avions de choco solver. Rendu obsolète par LNS et LDS
	 */
	public void solveWithModel(){
		this.nbSol = 1;
		Solver solver = model.getSolver();
		this.contrainteCalculDeviation();
		solver.showShortStatistics();
		solver.findOptimalSolution(deviation_totale, false);
		resultat = new CaseEdTGroupe[nbGroupes][nbCreneaux];
		fillResultat(0);
	}

	// Limited Discrepancy Search
	public void LDS(){
		this.setReferenceSolution();
		Map<IntVar, Integer> map = IntStream.range(0,nbGroupes*nbCreneaux*3).boxed().collect(Collectors.toMap(i-> finalDataCat[i], i -> finalDataCatSol[i]));
		model.getSolver().setSearch(Search.intVarSearch(
				new InputOrder<>(model), // variable selection: from ticks[0] to ticks[m-1]
				var -> { // value selection, choose value from solution if possible
					if(var.contains(map.get(var))){
						return map.get(var);
					}
					return var.getLB(); // otherwise, select the current lower bound
				},
				finalDataCat
		));
		this.printModele();
		model.getSolver().setLDS(30); // discrepancy is set to 12
		this.solve();
		this.printDifferenceAvecModele();
	}

	// Large Neighbourhood Search
	public void LNS(){
		this.setReferenceSolution();
		Solution solution = new Solution(model, finalDataCat);
		IntStream.range(0, nbGroupes * nbCreneaux * 3).forEach(i -> solution.setIntVal(finalDataCat[i], finalDataCatSol[i]));
		MoveLNS lns = new MoveLNS(model.getSolver().getMove(), INeighborFactory.random(finalDataCat), new FailCounter(model, 100));
		lns.loadFromSolution(solution, model.getSolver());
		model.getSolver().setMove(lns);
		this.solve();
	}

	// Fonction utilisé par LNS et LDS pour charger la solution de référence
	private void setReferenceSolution(){
		IntVar[] dataCat =null;
		int[] dataCatSol = null;
		for (int i = 0; i < nbGroupes; i++) {
			dataCat = ArrayUtils.append(dataCat, heures[i]);
			dataCatSol = ArrayUtils.append(dataCatSol, solutionEdt.getHeures()[i]);
		}
		for (int i = 0; i < nbGroupes; i++) {
			dataCat = ArrayUtils.append(dataCat, enseignants[i]);
			dataCatSol = ArrayUtils.append(dataCatSol, solutionEdt.getEnseignants()[i]);
		}
		for (int i = 0; i < nbGroupes; i++) {
			dataCat = ArrayUtils.append(dataCat, salles[i]);
			dataCatSol = ArrayUtils.append(dataCatSol, solutionEdt.getSalles()[i]);
		}
		finalDataCat = dataCat;
		finalDataCatSol = dataCatSol;
	}

	// Fonction qui résoud le problème
	public void solve(){
		Solver solver = model.getSolver();
		solver.showSolutions();
		//solver.findAllSolutions();
		resultat = new CaseEdTGroupe[nbGroupes * nbSol][nbCreneaux];
		int k = 0;
		while (k < nbSol) {
			//System.out.println(k);

			int offset;
			if (solver.solve()) {
				offset = k * nbGroupes;
				Creneau[] creneaux = instance.theCreneaux();


				int[][] heuresSol = new int[nbGroupes][nbActivites];
				int[][] enseignantsSol = new int[nbGroupes][nbActivites];
				int[][] sallesSol = new int[nbGroupes][nbActivites];

				fillResultat(0);
				for (int i = 0; i < nbActivites * nbGroupes; i++) {
					heuresSol[i/nbActivites][i%nbActivites] = heures[i/nbActivites][i%nbActivites].getValue();
					enseignantsSol[i/nbActivites][i%nbActivites] = enseignants[i/nbActivites][i%nbActivites].getValue();
					sallesSol[i/nbActivites][i%nbActivites] = salles[i/nbActivites][i%nbActivites].getValue();

					//System.out.println(activite.getMatiere() + " groupe " + activite.getGroupe() + " : " + creneaux[heures[i].getValue()] + " salle " + lesSalles[salles[i].getValue()] + " avec " + lesEnseignants[enseignants[i].getValue()]);
				}

				SolutionEdt solutionEdt = new SolutionEdt(heuresSol, enseignantsSol, sallesSol, nbGroupes);
				//
				//serializaSolution(solutionEdt, "src/Solutions_Serialisees/sol10_a_18.ser");


				k++;
			}else{
				nbSol = k;
			}
		}
	}

	// Gestion des creneaux offs
	private void contrainteCreneauxOffs(){
		for (int i = 0; i < nbEnseignants; i++) {
			// Pour chaque activité de chaque groupe
			for(int j = 0; j < nbActivites*nbGroupes; j++){
				// Pour chaque creneau off (de chaque prof)
				for(Integer creneau : instance.getEnseignants()[i].getCreneauxOff()){
					// Si l'activité est enseignée par ce prof,
					// alors sont creneau ne correspond pas au creaneau off
					model.ifThen(
							model.arithm(enseignants[j/nbActivites][j%nbActivites], "=", i),
							model.arithm(heures[j/nbActivites][j%nbActivites], "!=", creneau)
					);
				}
			}
		}
	}

	/*
		Fonction qui sert a remplir la structure CaseEdtGroupe[][]
		Contrairement à solutionEdt, cette structure contient des objets enseignant salles ect donc leur
		nom. solutionEdt ne comporte que les indices rendu par le solver (le contenu de enseignants[][] salles[]][] et
		heures[][] qui contient les id des enseignants salles et le créneau entre 0 et 19.
		A priori, avec le système des id unique et la base de donnée, la structure caseEdtGroupe[][] ne devrait plus servir.
		On se sert du offset lorsque l'on veut imprimer plus d'une solution.
	 */
	private void fillResultat(int offset){
		Salle[] lesSalles = instance.getSalles();
		Enseignant[] lesEnseignants = instance.getEnseignants();
		for (int i = 0; i < nbActivites * nbGroupes; i++) {
			Activite activite = instance.getActivite(i / nbActivites, i % nbActivites);
			resultat[(i / nbActivites)+offset][heures[i / nbActivites][i % nbActivites].getValue()] =
					new CaseEdTGroupe(activite, instance.getSalleById(salles[i / nbActivites][i % nbActivites].getValue()),
							instance.getEnseignantById(enseignants[i / nbActivites][i % nbActivites].getValue()));
			//System.out.println(activite.getMatiere() + " groupe " + activite.getGroupe() + " : " + creneaux[heures[i].getValue()] + " salle " + lesSalles[salles[i].getValue()] + " avec " + lesEnseignants[enseignants[i].getValue()]);
		}
	}

	/*
		Cette fonction sert à l'impression du modèle. Elle remplit une structure
		CaseEdtGroupe[][] pour charger les noms des enseignants et salles à partir de leur
		ids qui sont donné par le modèle.
	 */
	public static CaseEdTGroupe[][] fillModele(SolutionEdt solutionEdt, Probleme instance){
		CaseEdTGroupe[][] modele = new CaseEdTGroupe[solutionEdt.getNbGroupes()][20];
		Salle[] lesSalles = instance.getSalles();
		Enseignant[] lesEnseignants = instance.getEnseignants();
		for (int i = 0; i < 20 * solutionEdt.getNbGroupes(); i++) {
			Activite activite = instance.getActivite(i / 20, i % 20);
			modele[(i / 20)][solutionEdt.getHeures()[i / 20][i % 20]] =
					new CaseEdTGroupe(activite, instance.getSalleById(solutionEdt.getSalles()[i / 20][i % 20]),
							instance.getEnseignantById(solutionEdt.getEnseignants()[i / 20][i % 20]));
		}
		return modele;
	}
 	// Cette fonction imprime le modèle
	public void printModele(){
		System.out.println("Impression du modele:");
		this.modele = fillModele(this.solutionEdt, this.getInstance());
		for (int i = 0; i < solutionEdt.getNbGroupes(); i++) {
			System.out.println("Groupe " + (i+1));
			for (int j = 0; j < nbCreneaux; j++) {
				System.out.println(instance.getCreneaux()[j] + " : " + modele[i][j]);
			}
		}
	}

	// Cette fonction imprile la ou les solutions
	public void printSolution(){
		System.out.println("Impression solution(s):");
		for(int l = 0; l < nbSol; l++) {
			System.out.println("Solution n: " + (l+1));
			for (int i = 0; i < nbGroupes; i++) {
				System.out.println("Groupe " + (i+1));
				for (int j = 0; j < nbCreneaux; j++)
					System.out.println(instance.getCreneaux()[j] + " : " + resultat[i+nbGroupes*l][j]);
			}
		}
		System.out.println("\n");
	}

	// Cette fonction imprime les différence entre la solution et le modèle
	public void printDifferenceAvecModele(){
		CaseEdTGroupe[][] data = new CaseEdTGroupe[nbGroupes*2][nbCreneaux];
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbCreneaux; j++) {
				data[i][j] = resultat[i][j];
			}
		}
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbCreneaux; j++) {
				data[nbGroupes + i][j] = modele[i][j];
			}
		}

		CalculDifference calculDifference = new CalculDifference(data, nbGroupes, 2, instance.getCreneaux());
		calculDifference.calcul();
		calculDifference.print();
	}

	//	Cette fonction imprime les différences entre les solutions
	public void printDifferences(){
		if(nbSol > 1) {
			CalculDifference calculDifference = new CalculDifference(resultat, nbGroupes, nbSol, instance.getCreneaux());
			calculDifference.calcul();
			calculDifference.print();
		}
	}

	// Retourne l'indice d'une séquence d'un cour dans le tableau des matières.
	// Peut être rendu obsolète si la bdd utiliser des id unique pour les matières.
	private int getIndiceSeq(String matiereSubject, int nSeq, int groupe){
		for(int i = 0; i < this.nbActivites; i++){
			Matiere matiere = activitesMat[groupe][i].getMatiere();
			if(matiereSubject.equals(matiere.getSubject()) && matiere.getSequence() == nSeq){
				return i;
			}
		}
		return -1;
	}

	// Retourne la structure qui contient la solution.
	public SolutionEdt getSolutionEdt() {
		return solutionEdt;
	}

	// Sérialise une solution et l'enregistre au chemin précisé par path
	public static void serializaSolution(SolutionEdt solutionEdt, String path){
		File fichier = new File(path);
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(fichier));
			oos.writeObject(solutionEdt);
			System.out.println("solution ecrite");
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

