package mariaDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import interfaces.Param.FillTable.FillTableEns;
import javafx.collections.ObservableList;

public class ModificateDB {

	private DBConnection db;
	private Connection connection;

	public ModificateDB(DBConnection db) {
		this.db=db;
		this.connection=db.getConnection();
	}

	public ResultSet selectAllInGroupe(char lettre,char num) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT g.specificity, s.capacity FROM groupes g,subgroupes s WHERE s.num="+num+" and g.alphabet='"+lettre+"'AND g.alphabet=s.alphabet ;");	
		return resultSet;
	}


	public ResultSet selectEnsNotInGroupe(char lettre) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT e.name FROM enseignants e WHERE e.idEns NOT IN (SELECT e.idEns FROM enseignants e , groupes g, ensgroupe eg WHERE e.idens=eg.idEns AND g.idGroupe=eg.idGroupe AND g.alphabet='"+lettre+"');");	
		return resultSet;
	}

	public void setAll(String groupe, int capacity, String specification, ObservableList<FillTableEns> dataEns) throws SQLException {
		System.out.println(groupe);
		char alpha = groupe.charAt(7);
		char num = groupe.charAt(8);
		System.out.println(alpha + " : " + specification);
		Statement statement = this.connection.createStatement();
		statement.executeQuery("UPDATE GROUPES SET specificity='"+specification+"' WHERE alphabet='"+alpha+"';");	
		statement.executeQuery("UPDATE subgroupes SET capacity="+capacity+" WHERE alphabet='"+alpha+"' AND num="+num+";");	
		statement.executeQuery("delete FROM ensgroupe  WHERE idGroupe = (SELECT idgroupe from groupes WHERE alphabet='"+alpha+"');");
		for(FillTableEns ens : dataEns) {
			statement.executeQuery("INSERT INTO ENSGROUPE SELECT g.idgroupe , e.idens FROM groupes g, enseignants e WHERE g.alphabet='"+alpha+"' AND e.NAME='"+ens.getName()+"' ");
			if(ens.getFonction().equals("referent")) 	statement.executeQuery("UPDATE subgroupes SET referent = (SELECT idEns FROM enseignants WHERE NAME='"+ens.getName()+"') WHERE alphabet='"+alpha+"' AND num="+num+";");
		}

	}

	public void deleteGroupe(char lettre) throws SQLException{
		Statement statement = this.connection.createStatement();
		statement.executeQuery("DELETE FROM subgroupes WHERE alphabet='"+lettre+"';");	
		statement.executeQuery("DELETE FROM ensgroupe WHERE idgroupe = (SELECT idgroupe FROM groupes WHERE alphabet='"+lettre+"');");	
		statement.executeQuery("DELETE FROM groupes WHERE alphabet='"+lettre+"';");	

	}

	public void deleteSubGroupe(char lettre,char num) throws SQLException{
		Statement statement = this.connection.createStatement();
		System.out.println(lettre + " " + num);
		statement.executeQuery("DELETE FROM subgroupes WHERE alphabet='"+lettre+"' and num="+num+";");	
	}

	public ResultSet selectDispoEns(String name) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT  c.idCreneau FROM creneaux c, enseignants e, creneauxoffens o WHERE c.idCreneau=o.idCreneau AND o.idEns=e.idEns and e.NAME='"+name+"';");	
		return resultSet;
	}

	public ResultSet selectSubjectEns(String name) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT s.discipline FROM disciplinesens s, enseignants e WHERE s.idEns=e.idEns and e.NAME='"+name+"';");	
		return resultSet;
	}

	public ResultSet selectAllSubject() throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT DISTINCT discipline FROM subjectdiscipline WHERE discipline<>'demarches' AND discipline<>'exterieures'");	
		return resultSet;
	}

	public void deleteInfoEns(String name) throws SQLException{
		Statement statement = this.connection.createStatement();
		statement.executeQuery("DELETE FROM creneauxoffens WHERE idEns=(SELECT idens FROM enseignants WHERE NAME='"+name+"');");	
		statement.executeQuery("DELETE FROM disciplinesens WHERE idens=(SELECT idens FROM enseignants WHERE name='"+name+"')");	

	}

	public void addInfoEns(String name, ArrayList<Integer> dispo, ArrayList<String> subject)throws SQLException{
		Statement statement = this.connection.createStatement();
		if(!dispo.isEmpty()) {
			for(Integer dispoInt : dispo) {
				statement.executeQuery("INSERT INTO creneauxoffens SELECT e.idens, c.idCreneau FROM creneaux c, enseignants e WHERE c.idcreneau="+dispoInt+" AND e.name='"+name+"';");	
			}
		}

		if(!subject.isEmpty()) {
			for(String str : subject) {
				statement.executeQuery("INSERT INTO disciplinesEns SELECT DISTINCT e.idEns, s.discipline FROM subjectdiscipline s, enseignants e WHERE s.discipline='"+str+"' AND e.name='"+name+"';	");	
			}
		}
	}

	public void insertEns(String name,ArrayList<Integer> dispoArray,ArrayList<String> subjectArray) throws SQLException {
		Statement statement = this.connection.createStatement();
		statement.executeQuery("INSERT INTO enseignants(name) VALUES('"+name+"');");

		if(!dispoArray.isEmpty()) {
			for(Integer dispo : dispoArray)
				statement.executeQuery("INSERT INTO creneauxoffens SELECT e.idens , c.idcreneau FROM creneaux c, enseignants e WHERE e.NAME='"+name+"' AND c.idCreneau="+dispo+";");
		}

		if(!subjectArray.isEmpty()) {
			for(String subject : subjectArray) {
				statement.executeQuery("INSERT INTO disciplinesEns SELECT DISTINCT e.idEns, s.discipline FROM subjectdiscipline s, enseignants e WHERE s.discipline='"+subject+"' AND e.name='"+name+"';	");	
			}
		}

	}

	public void deleteEns(String name) throws SQLException {
		Statement statement = this.connection.createStatement();
		statement.executeQuery("DELETE FROM subjectens WHERE idens =(select idens from enseignants where NAME='"+name+"');");	
		statement.executeQuery("DELETE FROM disciplinesEns WHERE idens =(select idens from enseignants where NAME='"+name+"');");
		statement.executeQuery("DELETE FROM creneauxoffens WHERE idens =(select idens from enseignants where NAME='"+name+"');");	
		statement.executeQuery("DELETE FROM enseignants WHERE NAME='"+name+"';");	



	}

	public ResultSet selectCapInSalle(String numSalle) throws SQLException {
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT capacity FROM salles WHERE num="+Integer.parseInt(numSalle)+";");	
		return resultSet;
	}


	public ResultSet selectNatureSalle(String numSalle) throws SQLException {
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT n.nature FROM natureSalle n , salles s WHERE n.idsalle = s.idsalle AND  s.num="+Integer.parseInt(numSalle)+";");	
		return resultSet;
	}


	public ResultSet selectAllAlphabet() throws SQLException {
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT alphabet FROM groupes;");	
		return resultSet;
	}

	public ResultSet selectGrpSalle(int salle) throws SQLException {
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT g.groupeSalle FROM groupesalle g, salles s WHERE g.idSalle=s.idSalle AND s.num="+salle+" ;");	
		return resultSet;
	}

	public void updateSalle(int salle,int capacity,ArrayList<String> nature,char groupe) throws SQLException {
		Statement statement = this.connection.createStatement();
		ResultSet result =statement.executeQuery("SELECT IDSALLE FROM SALLES WHERE NUM="+salle+";");
		int idSalle=0;
		while(result.next())idSalle=result.getInt("idSalle");
		statement.executeQuery("delete from naturesalle where idsalle="+idSalle+";");	
		if(capacity>=0)statement.executeQuery("UPDATE salles SET capacity="+capacity+" WHERE idSalle=(SELECT idSalle FROM salles WHERE num="+salle+");");	
		if(!nature.isEmpty()) {
			for(String nat : nature) {
				statement.executeQuery("insert into naturesalle values("+idSalle+",'"+nat+"');");	
			}
		}
		statement.executeQuery("update groupesalle set groupesalle='"+groupe+"' where idsalle="+idSalle+";");	
	}

	public void insertSalle(int salle,int capacity,ArrayList<String> natures, char groupe) throws SQLException{
		Statement statement = this.connection.createStatement();
		int idSalle = 0;
		statement.executeQuery("INSERT INTO salles(num,capacity) VALUES("+salle+","+capacity+");");	
		ResultSet resultID =statement.executeQuery("select idsalle from salles where num="+salle+";");
		while(resultID.next()) idSalle=resultID.getInt("idSalle");
		statement.executeQuery("INSERT INTO groupesalle VALUES("+idSalle+",'"+groupe+"');");	

		if(!natures.isEmpty()) {
			for(String nat : natures) {
				statement.executeQuery("insert into naturesalle values("+idSalle+",'"+nat+"');");	
			}
		}

	}

	public void deleteSalle(int number) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet idResult = statement.executeQuery("SELECT IDSALLE FROM SALLES WHERE NUM="+number+";");
		int idSalle=0;
		while(idResult.next()) idSalle=idResult.getInt("idSalle");
		System.out.println(idSalle);
		statement.executeQuery("DELETE FROM groupesalle where idSalle="+idSalle+";");
		statement.executeQuery("DELETE FROM naturesalle where idSalle="+idSalle+";");
		statement.executeQuery("DELETE FROM salles where idSalle="+idSalle+";");

	}

	public ResultSet selectNatureMatiere(String subject) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT n.nature FROM naturematiere n , matieres m WHERE m.idMatiere=n.idMatiere AND m.subject='"+subject+"';");	
		return resultSet;
	}

	public ResultSet selectGrpMatiere(String subject) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT distinct s.alphabet FROM subgroupes s, matieres m, activites gm WHERE s.idsGroupe=gm.idGroupe AND m.idMatiere=gm.idMatiere AND m.subject='"+subject+"';");	
		return resultSet;
	}

	public ResultSet selectNOTGrpMatiere(String subject) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT distinct alphabet FROM groupes WHERE alphabet NOT IN (SELECT g.alphabet FROM subgroupes g, matieres m, activites gm WHERE g.idsGroupe=gm.idGroupe AND m.idMatiere=gm.idMatiere AND m.subject='"+subject+"');");	
		return resultSet;
	}

	public void updateMatiere(String subject,ArrayList<Character> groupes,ArrayList<String> natures) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet idResult = statement.executeQuery("SELECT idmatiere FROM matieres WHERE subject='"+subject+"';");
		int idMatiere=0;
		while(idResult.next()) idMatiere=idResult.getInt("idMatiere");
		System.out.println(idMatiere);
		statement.executeQuery("DELETE FROM activites WHERE idmatiere="+idMatiere+";");
		for(Character grp : groupes) statement.executeQuery("INSERT INTO activites(idGroupe,idMatiere,nbSeq) SELECT s.idSGroupe,m.idMatiere ,0 FROM matieres m, subgroupes s WHERE m.subject='"+subject+"' AND s.alphabet='"+grp+"';");

		statement.executeQuery("DELETE FROM naturematiere WHERE idmatiere="+idMatiere+";");
		for(String str : natures) statement.executeQuery("INSERT INTO naturematiere SELECT m.idMatiere, '"+str+"'  FROM matieres m WHERE m.idMatiere="+idMatiere+";");
	}


	public void insertMatiere(String subject,ArrayList<Character> groupes,ArrayList<String> natures) throws SQLException{
		System.out.println("INSERTION : " + subject + " : " + groupes + " " + natures);
		Statement statement = this.connection.createStatement();
		statement.executeQuery("INSERT INTO MATIERES(subject) VALUES ('"+subject+"');");


		ResultSet idResult = statement.executeQuery("SELECT idmatiere FROM matieres WHERE subject='"+subject+"';");
		int idMatiere=0;
		while(idResult.next()) idMatiere=idResult.getInt("idMatiere");
		if(!natures.isEmpty())
			for(String str : natures) statement.executeQuery("INSERT INTO naturematiere SELECT m.idMatiere, '"+str+"'  FROM matieres m WHERE m.idMatiere="+idMatiere+";");

		if(!groupes.isEmpty())
			for(Character grp : groupes) statement.executeQuery("INSERT INTO activites(idGroupe,idMatiere,nbSeq) SELECT s.idsgroupe ,m.idMatiere, 0 FROM subgroupes s, matieres m WHERE m.idMatiere="+idMatiere+" AND s.alphabet='"+grp+"';");
	}

	public void deleteMatiere(String subject) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet idResult = statement.executeQuery("SELECT idmatiere FROM matieres WHERE subject='"+subject+"';");
		int idMatiere=0;
		while(idResult.next()) idMatiere=idResult.getInt("idMatiere");

		statement.executeQuery("DELETE FROM activites WHERE idmatiere="+idMatiere+";");
		statement.executeQuery("DELETE FROM naturematiere WHERE idmatiere="+idMatiere+";");
		statement.executeQuery("DELETE FROM matieres WHERE idmatiere="+idMatiere+";");
	}

	public ResultSet selectMatGetAct(String subject) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet idResult = statement.executeQuery("SELECT idmatiere FROM matieres WHERE subject='"+subject+"';");
		int idMatiere=0;
		while(idResult.next()) idMatiere=idResult.getInt("idMatiere");

		ResultSet resultSet =statement.executeQuery("SELECT SUM(nbSeq) AS sommeSeq FROM activites WHERE idMatiere="+idMatiere+" GROUP BY idmatiere;");	
		return resultSet;
	}

	public void updateSeq(String subject, char alpha, int num,int seq) throws SQLException{
		Statement statement = this.connection.createStatement();
		statement.executeQuery("UPDATE activites SET nbSeq="+seq+" WHERE idmatiere=(SELECT idMatiere FROM matieres WHERE subject='"+subject+"') AND idgroupe=(SELECT idsgroupe FROM subgroupes WHERE alphabet='"+alpha+"' AND num="+num+");");

	}

	public void updateCreAct(String subject,char alpha,int num, ArrayList<Integer> dispoArray) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet idResult = statement.executeQuery("SELECT a.idactivite FROM activites a , matieres m , subgroupes s WHERE a.idMatiere=m.idMatiere AND m.subject='"+subject+"' AND s.idSGroupe=a.idGroupe AND s.alphabet='"+alpha+"' AND s.num="+num+"");
		int idActivite=0;
		while(idResult.next()) idActivite=idResult.getInt("idactivite");

		statement.executeQuery("DELETE FROM creneauactivite WHERE idActivite="+idActivite+";");
		
		for(Integer dispo : dispoArray) statement.executeQuery("INSERT INTO creneauactivite VALUES("+idActivite+","+dispo+");");
	}
	

	public ResultSet selectAllNatureSalle() throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT DISTINCT n.nature from naturesalle n;");	
		return resultSet;
	}
	
	public ResultSet selectAllGrpWOI() throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT DISTINCT alphabet from groupes where alphabet<>'I';");	
		return resultSet;
	}


}
