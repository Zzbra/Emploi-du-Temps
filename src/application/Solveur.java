package application;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.tools.ArrayUtils;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

/*
	Contraintes à implémenter:
		- Le reste des contraintes liées au jours/activitées
		- Certains cours doivent être donnés par bloc de 2 (Temps referent)

	J'ai besoin de savoir si les discipline ayant pour salle x/y (ex cours/info)
	peuvent être donnés dans l'une ou l'autre en fonction des disponibilités ou
	doivent être donnés certaine semaine dans l'une et d'autres dans l'autre
	Pour l'instant c'est la première option qui est implémentée

	- Voir pour les domaines de définition de la structure enseignants[][]
	- Transformer le champ groupe en un ensemble pour les structure salle enseignant


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
					.mapToObj(j -> model.intVar("earliness #" + j, 0, 1, false))
					.toArray(IntVar[]::new);
		}

		// On récupère les dommaines de définitions des salles et enseignants
		// des différents groupes.
		ArrayList<int[]> indicesSallesGroupes = getIndiceSallesParGroupes();
		ArrayList<int[]> indicesEnseignantsGroupes = getIndiceEnseignantsParGroupes();

		// Ici, on instancie les variables avec les domaines de définitions qui leur correspond
		this.salles = new IntVar[nbGroupes][nbActivites];
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				this.salles[i][j] = model.intVar("salle", indicesSallesGroupes.get(i));
			}
		}

		this.enseignants = new IntVar[nbGroupes][nbActivites];
		for (int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				this.enseignants[i][j] = model.intVar("enseignants", indicesEnseignantsGroupes.get(i));
			}
		}

		this.heures = model.intVarMatrix("heures", nbGroupes, nbActivites, 0, nbCreneaux - 1);
		//this.enseignants = model.intVarMatrix("enseignants", nbGroupes, nbActivites, 0, nbEnseignants - 1);
		//this.salles = model.intVarMatrix("salles", nbGroupes, nbActivites, 0, nbSalles - 1);

		File fichier = new File("sol.ser");
		ObjectInputStream ois = null;
		this.solutionEdt = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fichier));
			this.solutionEdt = (SolutionEdt)ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

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

	private ArrayList<int[]> getIndiceSallesParGroupes(){
		ArrayList<int[]> resultat = new ArrayList<>();
		for (int i = 0; i < nbGroupes; i++) {
			ArrayList<Integer> ligne = new ArrayList<>();
			for (int j = 0; j < nbSalles; j++) {
				char groupe = this.instance.getGroupe()[i].getAlphabet();
				if(this.instance.getSalle(j).getGroupe().contains(groupe) ||
				   this.instance.getSalle(j).getGroupe().contains('I')){
					ligne.add(j);
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

	private ArrayList<int[]> getIndiceEnseignantsParGroupes(){
		ArrayList<int[]> resultat = new ArrayList<>();
		for (int i = 0; i < nbGroupes; i++) {
			ArrayList<Integer> ligne = new ArrayList<>();
			for (int j = 0; j < nbEnseignants; j++) {
				char groupe = this.instance.getGroupe()[i].getAlphabet();
				if(this.instance.getEnseignant(j).getGroupe().contains(groupe) ||
						this.instance.getEnseignant(j).getGroupe().contains('I')){
					ligne.add(j);
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

	// Deux activité ne peuvent avoir lieu dans la même salle au même moment
	private void contrainteActiviteTempsSalle(){
		for(int i = 0; i < nbActivites*nbGroupes; i++) {
			for(int j = 0; j < nbActivites*nbGroupes; j++) {
				ArrayList<String> natureI = activitesMat[i/nbActivites][i%nbActivites].getMatiere().getNature();
				ArrayList<String> natureJ = activitesMat[j/nbActivites][j%nbActivites].getMatiere().getNature();
				if(i != j && !natureI.contains("autre") && !natureJ.contains("autre")) {
					model.addClauses(LogOp.or(model.arithm(heures[i/nbActivites][i%nbActivites], "!=", heures[j/nbActivites][j%nbActivites]).reify(),
							(model.arithm(salles[i/nbActivites][i%nbActivites], "!=", salles[j/nbActivites][j%nbActivites]).reify())));
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
	// Le prof d'anglais ne donne que des cours d'anglais
	private void contrainteProfAnglais(){
		int indiceProfAnglais = 0;
		for(int i = 0; i < instance.theEnseignants().length; i++){
			if(instance.theEnseignants()[i].getDisciplines().contains("anglais"))
				indiceProfAnglais = i;
		}

		for(int i = 0; i < nbGroupes; i++) {
			for (int j = 0; j < nbActivites; j++) {
				// On s'assure que le prof d'anglais ne donne que le cour d'anglais
				if(!activitesMat[i][j].getMatiere().getSubject().equals("Anglais")){
					model.arithm(enseignants[i][j], "!=", indiceProfAnglais).post();
				}
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
			for(String key : instance.theSalles()[i].getNature()){
				if(mapSalles.containsKey(key)){
					mapSalles.get(key).add(i);
				}else{
					ArrayList<Integer> entry = new ArrayList<>();
					entry.add(i);
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
					for (int k = 0; k < mapSalles.get(s).size(); k++) {
						boolList.add(model.arithm(salles[i][j], "=", mapSalles.get(s).get(k)).reify());
					}
				}
				BoolVar[] boolVars = new BoolVar[boolList.size()];
				for (int k = 0; k < boolList.size(); k++) {
					boolVars[k] = boolList.get(k);
				}
				model.addClausesBoolOrArrayEqualTrue(boolVars);
			}
		}
	}
	// Deux Activit��s ne peuvent pas avoir un meme prof en meme temps.
	private void contrainteProfMemeTemps(){
		for(int i = 0; i < nbActivites*nbGroupes; i++) {
			for(int j = 0; j < nbActivites*nbGroupes; j++) {
				if(i != j) {
					model.addClauses(LogOp.or(model.arithm(heures[i/nbActivites][i%nbActivites], "!=", heures[j/nbActivites][j%nbActivites]).reify(),
							model.arithm(enseignants[i/nbActivites][i%nbActivites], "!=", enseignants[j/nbActivites][j%nbActivites]).reify()));
				}
			}
		}
	}
	// Un prof ne peut pas enseigner les cours qu'il n'est pas habilité à enseigner
	private void contrainteCourImpropre(){
		// On s'assure que les profs enseignent leurs matières exclusives
		// Pour chaque activité de chaque groupe
		for(int i = 0; i < nbActivites*nbGroupes; i++) {
			// On extrait le chaine qui décrit la matière
			String nature = activitesMat[i/nbActivites][i%nbActivites].getMatiere().getSubject();
			// Pour chaque enseignant
			for(int j = 0; j < nbEnseignants; j++){
				// On extrait une liste de matières qui lui sont exclusive
				ArrayList<String> matieresExclusives = instance.exclusiveDiscipline(j);
				// On vérifie qu'elle n'est pas précédée par EDA car tout les proffesseurs sont
				// Habilités à enseigner des matières dont l'intitulé commence par EDA
				if(!nature.startsWith("EDA"))
					// Pour charque matière exclusive de chaque proffesseur
					for(String matiere :matieresExclusives)
						if(nature.toLowerCase().contains(matiere.toLowerCase()))
							// On s'assure que l'activité qui correspond est bien enseignée par le proffesseur en question
							// Ici il faudra préciser la méthode avec laquelle on assicie les intitulés des matières
							// avec ceux des cours donné par les profs. Ils diffèrent, ce qui peux causer des problèmes
							model.arithm(enseignants[i/nbActivites][i%nbActivites], "=", j).post();
			}
		}
	}
	// Gestion des creneaux offs
	private void contrainteCreneauxOffs(){
		for (int i = 0; i < nbEnseignants; i++) {
			// Pour chaque activité de chaque groupe
			for(int j = 0; j < nbActivites*nbGroupes; j++){
				// Pour chaque creneau off (de chaque prof)
				for(Integer creneau : instance.theEnseignants()[i].getCreneauxOff()){
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

	// Contrainte symetrie classe
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

	private void contrainteSymetrieEnseignants(){
		// Pour chaque actvité (créneau)
		for (int i = 0; i < nbActivites; i++) {
			// Pour tout les groupes deux à deux
			for (int j = 0; j < nbGroupes-1; j++) {
				for (int k = j+1; k < nbGroupes; k++) {
					// Si les groupe sont de même nature, on définit un ordre sur les salles
					if(activitesMat[j][i].getMatiere().getNature().equals(activitesMat[k][i].getMatiere().getNature())){
						model.arithm(enseignants[j][i], "<=", enseignants[k][i]).post();
					}
				}
			}
		}
	}

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

		this.contrainteSymetrieClasses();

		this.contrainteSymetrieEnseignants();

//		this.contrainteSalleGroupe();
//
//		this.contrainteEnseignantGroupe();

		//this.contrainteCalculDeviation();
	}

	public void solveWithModel(){
		this.nbSol = 1;
		Solver solver = model.getSolver();
		solver.showShortStatistics();
		solver.findOptimalSolution(deviation_totale, false);
		resultat = new CaseEdTGroupe[nbGroupes][nbCreneaux];
		fillResultat(0);
	}

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

				fillResultat(offset);
				for (int i = 0; i < nbActivites * nbGroupes; i++) {
					heuresSol[i/nbActivites][i%nbActivites] = heures[i/nbActivites][i%nbActivites].getValue();
					enseignantsSol[i/nbActivites][i%nbActivites] = enseignants[i/nbActivites][i%nbActivites].getValue();
					sallesSol[i/nbActivites][i%nbActivites] = salles[i/nbActivites][i%nbActivites].getValue();

					//System.out.println(activite.getMatiere() + " groupe " + activite.getGroupe() + " : " + creneaux[heures[i].getValue()] + " salle " + lesSalles[salles[i].getValue()] + " avec " + lesEnseignants[enseignants[i].getValue()]);
				}

				SolutionEdt solutionEdt = new SolutionEdt(heuresSol, enseignantsSol, sallesSol, nbGroupes);
				serializaSolution(solutionEdt);


				k++;
			}
		}
	}

	private void fillResultat(int offset){
		Salle[] lesSalles = instance.getSalles();
		Enseignant[] lesEnseignants = instance.getEnseignants();
		for (int i = 0; i < nbActivites * nbGroupes; i++) {
			Activite activite = instance.getActivite(i / nbActivites, i % nbActivites);
			resultat[(i / nbActivites)+offset][heures[i / nbActivites][i % nbActivites].getValue()] =
					new CaseEdTGroupe(activite, lesSalles[salles[i / nbActivites][i % nbActivites].getValue()], lesEnseignants[enseignants[i / nbActivites][i % nbActivites].getValue()]);
			//System.out.println(activite.getMatiere() + " groupe " + activite.getGroupe() + " : " + creneaux[heures[i].getValue()] + " salle " + lesSalles[salles[i].getValue()] + " avec " + lesEnseignants[enseignants[i].getValue()]);
		}
	}

	private void fillModele(){
		modele = new CaseEdTGroupe[solutionEdt.getNbGroupes()][20];
		Salle[] lesSalles = instance.getSalles();
		Enseignant[] lesEnseignants = instance.getEnseignants();
		for (int i = 0; i < nbActivites * nbGroupes; i++) {
			Activite activite = instance.getActivite(i / nbActivites, i % nbActivites);
			modele[(i / nbActivites)][solutionEdt.getHeures()[i / nbActivites][i % nbActivites]] =
					new CaseEdTGroupe(activite, lesSalles[solutionEdt.getSalles()[i / nbActivites][i % nbActivites]],
					lesEnseignants[solutionEdt.getEnseignants()[i / nbActivites][i % nbActivites]]);
		}
	}

	public void printModele(){
		System.out.println("Impression du modele:");
		fillModele();
		for (int i = 0; i < solutionEdt.getNbGroupes(); i++) {
			System.out.println("Groupe " + (i+1));
			for (int j = 0; j < nbCreneaux; j++) {
				System.out.println(instance.getCreneaux()[j] + " : " + modele[i][j]);
			}
		}
	}

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

	public void printDifferences(){
		if(nbSol > 1) {
			CalculDifference calculDifference = new CalculDifference(resultat, nbGroupes, nbSol, instance.getCreneaux());
			calculDifference.calcul();
			calculDifference.print();
		}
	}

	private int getIndiceSeq(String matiereSubject, int nSeq, int groupe){
		for(int i = 0; i < this.nbActivites; i++){
			Matiere matiere = activitesMat[groupe][i].getMatiere();
			if(matiereSubject.equals(matiere.getSubject()) && matiere.getSequence() == nSeq){
				return i;
			}
		}
		return -1;
	}


	private void serializaSolution(SolutionEdt solutionEdt){
		File fichier = new File("sol.ser");
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

