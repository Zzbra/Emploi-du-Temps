package interfaces;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mariaDB.DBConnection;

public class DatabaseQuery {
	
	private DBConnection db;
	private Connection connection;
	
	public DatabaseQuery(DBConnection db) {
		this.db=db;
		this.connection=db.getConnection();
	}
	
	
	public ResultSet selectAllEns() throws SQLException {
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT * from enseignants order by idEns");	
		return resultSet;
	}
	
	public ResultSet selectGrpForEns(int idEns) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT g.alphabet FROM ensgroupe e , groupes g WHERE e.idEns="+idEns+" AND g.idGroupe=e.idGroupe;");	
		return resultSet;
	}
	
	public ResultSet selectDiscicplineForEns(int idEns) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT * FROM disciplinesens WHERE idEns="+idEns+";");	
		return resultSet;
	}
	
	public ResultSet selectCreOffForEns(int idEns) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT * FROM creneauxoffens WHERE idEns="+idEns+";");	
		return resultSet;
	}
	
	public ResultSet selectAllSalle() throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT  * FROM salles order by idSalle;");	
		return resultSet;
	}
	
	
	public ResultSet selectNatureSalle(int idSalle) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT  * FROM naturesalle WHERE idSalle="+idSalle+";");	
		return resultSet;
	}
	
	public ResultSet selectGroupeSalle(int idSalle) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT  * FROM groupesalle WHERE idSalle="+idSalle+";");	
		return resultSet;
	}
	
	public ResultSet selectAllCreneaux() throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT  * FROM creneaux;");	
		return resultSet;
	}
	
	public ResultSet selectAllGroupe() throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT  s.idSGroupe , s.alphabet , s.num , s.capacity , g.specificity  FROM subgroupes s , groupes g WHERE g.alphabet=s.alphabet order BY s.idsGroupe;");	
		return resultSet;
	}
	
	public ResultSet selectAllMatiere() throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT  * FROM matieres ORDER BY idMatiere;");	
		return resultSet;
	}
	
	public ResultSet selectNatureMatiere(int idMat) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT  * FROM naturematiere where idMatiere ="+idMat+" ORDER BY idMatiere;");	
		return resultSet;
	}
	
	public ResultSet selectSeqMatiere(int idMat) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT a.idGroupe , m.subject, a.nbSeq  FROM matieres m,activites a WHERE a.idmatiere="+idMat+" and m.idMatiere=a.idMatiere AND nBSeq<>0;");	
		return resultSet;
	}
	
	
	public ResultSet getMatSize()throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT SUM(a.nbSeq) as somme FROM matieres m ,activites a WHERE a.idMatiere=m.idMatiere ;");	
		return resultSet;
		
	}
	
	public ResultSet selectAllMatFromGroup(int idGroupe)throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT a.idGroupe ,a.idMatiere, a.nbSeq , m.subject FROM activites a , matieres m WHERE m.idMatiere=a.idMatiere AND a.nbSeq<>0 AND a.idGroupe="+idGroupe+" order BY idgroupe;");	
		return resultSet;
		
	}
	
	public ResultSet selectCreMatiere(int idMat,int idGrp) throws SQLException{
		Statement statement = this.connection.createStatement();
		ResultSet resultSet =statement.executeQuery("SELECT a.idMatiere , a.idGroupe , c.idCreneau FROM creneauactivite c, activites a WHERE a.idActivite=c.idActivite AND a.idGroupe="+idGrp+" AND a.idMatiere="+idMat+" ;");	
		return resultSet;
	}
	
	
}
