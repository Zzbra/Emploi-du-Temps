package mariaDB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class FillDB {

	private DBConnection db;
	private Connection connection;

	public FillDB(DBConnection db) {
		this.db=db;
		this.connection=db.getConnection();
	}

	public ResultSet selectAllGroupe() throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT s.alphabet,s.num, e.name , g.specificity FROM subgroupes s, groupes g ,enseignants e WHERE g.alphabet = s.alphabet AND e.idens=s.referent;");	
		return resultSet;
	}

	public ResultSet selectDiscicplineForEns(int idEns) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT * FROM disciplinesens WHERE idEns="+idEns+";");	
		return resultSet;
	}

	public ResultSet selectAllEns() throws SQLException {
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT e.idEns, e.name, g.alphabet FROM enseignants e, groupes g, ensgroupe eg WHERE eg.idGroupe=g.idGroupe AND eg.idEns=e.idEns order by e.name;");	
		return resultSet;
	}

	public ResultSet selectAllSalle() throws SQLException {
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT s.idSalle, s.num, s.capacity ,g.groupeSalle  FROM salles s , groupesalle g WHERE g.idsalle=s.idsalle ORDER BY s.num;");	
		return resultSet;
	}



	public ResultSet selectAllEnsWithoutGrp() throws SQLException {
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT *  FROM enseignants WHERE idEns NOT IN (SELECT idEns FROM ensgroupe )order by name; ");	
		return resultSet;
	}

	public void insertIntoGroupe(String lettre,String spec, ArrayList<String> enseignants) throws SQLException{
		Statement statement = this.connection.createStatement();
		statement.executeQuery("INSERT INTO GROUPES(alphabet,specificity) values('"+lettre+"','"+spec+"');");	
		for(String ens : enseignants) {

			statement.executeQuery("INSERT INTO ENSGROUPE SELECT g.idgroupe, e.idens FROM groupes g, enseignants e WHERE g.alphabet='"+lettre+"' AND e.NAME='"+ens+"';");
		}
	}


	public void insertIntoSubGroupe(String lettre, TextField[] capacity, ComboBox[] referent) throws SQLException {
		Statement statement = this.connection.createStatement();
		for(int i=0;i<3;i++) {
			
			int grp = i+1;
			statement.executeQuery("INSERT INTO subgroupes(alphabet,num,referent,capacity) VALUES ('"+lettre+"',"+grp+",(SELECT idens FROM enseignants WHERE NAME='"+referent[i].getSelectionModel().getSelectedItem().toString()+"'),"+capacity[i].getText()+");");	
		}
	}
	
	
	public ResultSet selectCapacityGrp(String grp) throws SQLException {
		char alpha = grp.charAt(0);
		char num = grp.charAt(1);
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT * FROM subgroupes WHERE alphabet='"+alpha+"' AND num="+num+";");	
		return resultSet;
	}
	
	public ResultSet selectEnsWORef(String grp) throws SQLException {
		char alpha = grp.charAt(0);
		char num = grp.charAt(1);
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT e.name FROM ENSGROUPE eg , subgroupes s , groupes g , enseignants e WHERE s.num="+num+" and s.alphabet='"+alpha+"' AND g.idGroupe=eg.idgroupe AND s.alphabet=g.alphabet AND e.idens=eg.idens AND e.idens <> s.referent;");	
		return resultSet;
	}
	
	public ResultSet selectEnsJustRef(String grp) throws SQLException {
		char alpha = grp.charAt(0);
		char num = grp.charAt(1);
		System.out.println(alpha+""+num);
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT e.name FROM ENSGROUPE eg , subgroupes s , groupes g , enseignants e WHERE s.num="+num+" and s.alphabet='"+alpha+"' AND g.idGroupe=eg.idgroupe AND s.alphabet=g.alphabet AND e.idens=eg.idens AND e.idens = s.referent;");	
		return resultSet;
	}
	
	public ResultSet selectAllMatiere() throws SQLException {
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT * FROM matieres m order by subject;");	
		return resultSet;
	}
	
	public ResultSet selectNatureMatiere(int idMatiere) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT * FROM naturematiere WHERE idMatiere="+idMatiere+" ;");	
		return resultSet;
	}
	
	public ResultSet selectGroupesMatiere(int idMatiere) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT distinct g.alphabet FROM activites m, groupes g , subgroupes s WHERE m.idMatiere="+idMatiere+" AND s.idsGroupe=m.idGroupe AND s.alphabet=g.alphabet;");	
		return resultSet;
	}
	
	
	public ResultSet selectAllActivite() throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT DISTINCT s.alphabet , s.num ,SUM(g.nbSeq) AS NbSeq FROM  activites g , subgroupes s WHERE g.idGroupe=s.idSGroupe GROUP BY s.alphabet , s.num;");	
		return resultSet;
	}
	
	public ResultSet selectAllActiviteWOSeq() throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT * FROM subgroupes WHERE idSGroupe NOT IN (SELECT distinct idGroupe FROM activites );");	
		return resultSet;
	}
	
	public ResultSet selecActiviteSub(char alpha, int num) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT DISTINCT m.subject FROM  activites gm , subgroupes s , matieres m WHERE  s.alphabet='"+alpha+"' AND s.num="+num+" AND m.idMatiere=gm.idMatiere AND gm.idGroupe=s.idsGroupe;");	
		return resultSet;
	}
	
	
	public ResultSet selectSeqAct(char alpha, int num , String act) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT SUM(gm.nbSeq) AS NbSeq FROM activites gm , subgroupes s, matieres m WHERE gm.idMatiere=m.idMatiere AND gm.idGroupe=s.idSGroupe AND s.alphabet='"+alpha+"' AND s.num="+num+" and m.subject='"+act+"' GROUP BY s.alphabet,s.num");	
		return resultSet;
	}	
	

}
