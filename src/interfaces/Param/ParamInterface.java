package interfaces.Param;

import java.sql.SQLException;

import application.Solveur;
import interfaces.MainInterface;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import mariaDB.DBConnection;

public class ParamInterface {
	private Group root;
	private MainInterface main;
	private Solveur solveur;
	private ParamMenu paramMenu;
	private DBConnection db;
	private ParamPannel param;



	public ParamInterface(Group rootParam, MainInterface mainInterface, Solveur solveur) throws ClassNotFoundException, SQLException {
		this.setDb(new DBConnection());
		this.root= rootParam;
		this.main = mainInterface;
		this.solveur = solveur;
		this.main.getSceneParam().setFill(Color.rgb(76, 130, 154));
		
		this.paramMenu = new ParamMenu(this);
	//	new IDPannel(this);
		setParam(new ParamPannel(this));
		setRoot();
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
	
	
	public void setRoot() {
	//	root.getChildren().remove(paramMenu.getTreeView());
		//root.getChildren().remove(paramMenu.getMenu());
	//	root.getChildren().add(paramMenu.getMenu());
//		root.getChildren().add(paramMenu.getTreeView());
		
	}

	/**
	 * @return the db
	 */
	public DBConnection getDb() {
		return db;
	}

	/**
	 * @param db the db to set
	 */
	public void setDb(DBConnection db) {
		this.db = db;
	}

	public ParamPannel getParam() {
		return param;
	}

	public void setParam(ParamPannel param) {
		this.param = param;
	}
}
