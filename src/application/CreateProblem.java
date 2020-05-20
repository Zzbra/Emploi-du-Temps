package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import mariaDB.DBConnection;
import interfaces.DatabaseQuery;

public class CreateProblem {
	private Probleme probleme;
	private Solveur solveur;
	private DatabaseQuery db;
	

	public CreateProblem(DBConnection connection) {
		this.db=new DatabaseQuery(connection);
		this.probleme = new Probleme();
		init();
		this.solveur = new Solveur(probleme);  
		
	
	//	print();
		Instant beforeContrainte = Instant.now();
		this.solveur.definirContraintes();
		Instant afterContrainte = Instant.now();
		System.out.println("Durée definirContrainte = "+Duration.between(beforeContrainte, afterContrainte).toMinutes() + " : " +Duration.between(beforeContrainte, afterContrainte).getSeconds() + " : " + Duration.between(beforeContrainte, afterContrainte).getNano() );
		
		Instant beforeSolve = Instant.now();
		this.solveur.solve();
		Instant afterSolve = Instant.now();
		System.out.println("Durée solve = "+Duration.between(beforeSolve, afterSolve).toMinutes() + " : " +Duration.between(beforeSolve, afterSolve).getSeconds() + " : " + Duration.between(beforeSolve, afterSolve).getNano() );
		
	//	this.solveur.getSolutionEdt().print();
		//		
	}
	
	public void init() {
		probleme.setEnseignant(theEnseignants());
		probleme.setSalle(theSalles());
		probleme.setCreneaux(theCreneaux());
		probleme.setGroupe(theGroupes());
		probleme.setMatiere(theMatieres());
		probleme.setActivites(theActivites());
	}
	
	public void print() {
		for(Enseignant ens : probleme.getEnseignants()) System.out.println(ens.getName()+" : " + ens.getCreneauxOff() + ens.getDisciplines() + ens.getGroupe());
		System.out.println();
		for(Salle sall : probleme.getSalles()) System.out.println(sall.getId() + " " + sall.getNumber() + " : " + sall.getCapacity() + sall.getGroupe() + sall.getNature());
		System.out.println();
		for(Creneau cre : probleme.getCreneaux())System.out.println(cre.getJour() + " "+cre.getStarttime()+"-"+cre.getEndtime());
		System.out.println();
		for(Groupe grp : probleme.getGroupe())System.out.println(grp.getNumber() + " " + grp.getAlphabet()+""+" : " +grp.getCapacity()+grp.getSpecificity());
		System.out.println();
		for(Matiere mt : probleme.getMatieres())System.out.println(" " + mt.getSubject() + " : "+mt.getSequence()+mt.getCreneaux()+mt.getNature());
		System.out.println();
		for(int grp=0;grp<probleme.nbGroupes();grp++)
			for(int mat=0;mat<20;mat++)
				System.out.println(probleme.getActivite(grp, mat).getGroupe()+" : " + probleme.getActivite(grp, mat).getMatiere());
	}
	
	public Probleme getProbleme() {
		return this.probleme;
	}
	
	public Solveur getSolveur() {
		return solveur;
	}
	
	
	public Enseignant [] theEnseignants() {

		ResultSet resultEns;
		Enseignant [] enseignants;
		try {
			resultEns = this.db.selectAllEns();
			enseignants = new Enseignant [getResultSize(resultEns)];
			int count=0;
			while(resultEns.next()) {
				int idEns = resultEns.getInt("idEns");
				String ensName = resultEns.getString("name");

				ResultSet grpRes = this.db.selectGrpForEns(idEns);
				ArrayList<Character> groupe = new ArrayList<>();
				while(grpRes.next()) groupe.add(grpRes.getString("alphabet").charAt(0));



				ResultSet discRes = this.db.selectDiscicplineForEns(idEns);
				String[] disciplines = new String[this.getResultSize(discRes)];
				int counter=0;
				while(discRes.next()) disciplines[counter]=discRes.getString("discipline");

				ResultSet creOffRes = this.db.selectCreOffForEns(idEns);
				if(getResultSize(creOffRes)==0)enseignants[count]=new Enseignant(ensName,groupe,disciplines,idEns);
				else {
					int[] creneauxOff = new int[this.getResultSize(creOffRes)];
					int counterC=0;
					while(creOffRes.next()) creneauxOff[counterC]=creOffRes.getInt("idCreneau");
					enseignants[count]=new Enseignant(ensName,groupe,disciplines,creneauxOff,idEns);

				}

				count++;
			}
			return enseignants;
		} catch (SQLException e) {
			System.out.println("Erreur : Chargement enseignants");
		}

		return null;
	}

	public ArrayList<String> exclusiveDiscipline(int indexEnseignant){

		ArrayList<String> classesList = theEnseignants()[indexEnseignant].getDisciplines();
		for (int i = 0; i < theEnseignants().length; i++)
			if(i!= indexEnseignant)
				for(String s : theEnseignants()[indexEnseignant].getDisciplines()) {
					if(theEnseignants()[i].getDisciplines().contains(s)) {
						classesList.remove(s);
					}

				}
		return classesList;
	}

	public Salle [] theSalles() {

		ResultSet resultSalle;
		Salle [] salles;
		int count=0;
		try {
			resultSalle=this.db.selectAllSalle();
			salles = new Salle[getResultSize(resultSalle)];
			while(resultSalle.next()) {
				int idSalle = resultSalle.getInt("idSalle");
				int numSalle = resultSalle.getInt("num");
				int capaSalle = resultSalle.getInt("capacity");

				ArrayList<Character> grpSalle = new ArrayList<Character>();
				ResultSet grpRes  = this.db.selectGroupeSalle(idSalle);
				while(grpRes.next()) grpSalle.add(grpRes.getString("groupeSalle").charAt(0));


				ArrayList<String> natureSalle = new ArrayList<String>();
				ResultSet natureRes  = this.db.selectNatureSalle(idSalle);
				while(natureRes.next()) natureSalle.add(natureRes.getString("nature"));


				salles[count]=new Salle(grpSalle,numSalle,natureSalle,capaSalle,idSalle);
				count++;
			}
			return salles;
		} catch (SQLException e) {
			System.out.println("Erreur : Chargement salles");
		}
		return null;

	}

	public Creneau [] theCreneaux() {
		Creneau [] creneaux;
		ResultSet creneauxResult;
		int count=0;
		try {
			creneauxResult=this.db.selectAllCreneaux();
			creneaux = new Creneau[getResultSize(creneauxResult)];
			while(creneauxResult.next()) {
				int idCre = creneauxResult.getInt("idCreneau");
				String jour = creneauxResult.getString("jour");
				int startTime =creneauxResult.getInt("startTime");
				int endTime = creneauxResult.getInt("endTime");
				
				creneaux[count]= new Creneau(Jour.valueOf(jour),startTime,endTime);
				count++;
			}
			return creneaux;

		} catch (SQLException e) {
			System.out.println("Erreur : Chargement creneaux");
		}
		return null;

	}

	public Groupe [] theGroupes() {
		Groupe [] groupe = null;
		ResultSet groupeResult;
		int count=0;
		try {
			groupeResult=this.db.selectAllGroupe();
			groupe = new Groupe[getResultSize(groupeResult)];
			while(groupeResult.next()) {
				int idGrp =groupeResult.getInt("idsGroupe");
				char alphabet=groupeResult.getString("alphabet").charAt(0);
				int number=groupeResult.getInt("num");
				int capacity=groupeResult.getInt("capacity");
				String specificity=groupeResult.getString("specificity");
				groupe[count] = new Groupe(alphabet,idGrp,capacity,specificity,number);
				count++;
			}
			return groupe;

		} catch (SQLException e) {
			System.out.println("Erreur : Chargement groupes");
		}
		return null;
	}



	public Matiere [] theMatieres() {
		ResultSet result; 
		Matiere [] matiere;
		int count=1;
		try {
			result=this.db.selectAllMatiere();
			ResultSet sizeRes = this.db.getMatSize();
			int size=0;
			while(sizeRes.next()) size=sizeRes.getInt("somme");
			matiere= new Matiere[size+1];
			matiere[0]=new Matiere(" ", new ArrayList<String>(),0);
			while(result.next()) {
				int idMat = result.getInt("idMatiere");
				String subject = result.getString("subject");
				int sequence = 0;


				ArrayList<String> natMat = new ArrayList<String>();
				ResultSet natureRes = this.db.selectNatureMatiere(idMat);
				while(natureRes.next()) natMat.add(natureRes.getString("nature"));
				ResultSet seqRes = this.db.selectSeqMatiere(idMat);
				while(seqRes.next()) {
					int grp =seqRes.getInt("idGroupe");
					int maximum=seqRes.getInt("nbSeq");

					ResultSet creRes = this.db.selectCreMatiere(idMat, grp);
					if(this.getResultSize(creRes)==0) {
						for(int i=0;i<maximum;i++) {

							matiere[count] = new Matiere(subject,natMat,i+1);
							count++;
						}
					}
					else {
						int[] creMat = new int[this.getResultSize(creRes)];
						int countC=0;
						while(creRes.next()) creMat[countC]=creRes.getInt("idCreneau");
						for(int i=0;i<maximum;i++) {
							matiere[count] = new Matiere(subject,natMat,i+1,creMat);
							count++;
						}
					}
				}
			}
			//		System.out.println(count);
			return matiere;
		} catch (SQLException e) {
			System.out.println("Erreur : Chargement matieres");
		}
		return null;


	}

	public Activite [][] theActivites() {
		Activite [][] activites = new Activite [probleme.nbGroupes()][20];
		for (int i = 0; i < probleme.nbGroupes(); i++) {
			try {
				ResultSet result =this.db.selectAllMatFromGroup(i+1);
				int matCount=0;
				while(result.next()) {
					int nbSeq = result.getInt("nbSeq");
					int idMat = result.getInt("idMatiere");
					ResultSet creRes = this.db.selectCreMatiere(idMat, i+1);
					int countSeq=0;
					ArrayList<Integer> creMat = new ArrayList<Integer>();
					while(creRes.next()) creMat.add(creRes.getInt("idCreneau"));
					
					for(Matiere mat : probleme.getMatieres()) {
						if(mat.getSubject().equals(result.getString("subject")) && creMat.equals(mat.getCreneaux())&& countSeq<nbSeq) {
					//		System.out.println(matCount + " : " + probleme.getGroupe(i).getAlphabet()+""+probleme.getGroupe(i).getNumSousGroupe()+" = " + mat.toString());
							activites[i][matCount]=new Activite(probleme.getGroupe()[i],mat);
					//		System.out.println(activites[i][matCount].getGroupe() + " : " + activites[i][matCount].getMatiere());
							countSeq++;
							matCount++;
						}
					}
				}
				while(matCount<20) {
					activites[i][matCount]= new Activite(probleme.getGroupe()[i],probleme.getMatieres()[0]);
					matCount++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	
		
		return activites;
	}
	
	
	public int getResultSize(ResultSet result) {
		int number=0;
		try {
			result.last();
			number=result.getRow();
			result.beforeFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return number;
	}

	
}
