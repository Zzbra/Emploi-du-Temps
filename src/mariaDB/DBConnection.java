package mariaDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	private Connection connection;
	private FillDB fill;
	private ModificateDB modif;
	
	public DBConnection() throws ClassNotFoundException, SQLException {
		this.connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/e2c?user=root&password=laura");
		this.fill=new FillDB(this);
		this.setModif(new ModificateDB(this));
	}



	public Connection getConnection() {
		return connection;
	}



	public FillDB getFill() {
		return fill;
	}



	public ModificateDB getModif() {
		return modif;
	}



	public void setModif(ModificateDB modif) {
		this.modif = modif;
	}
	
	

}
