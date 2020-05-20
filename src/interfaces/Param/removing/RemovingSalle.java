package interfaces.Param.removing;

import java.sql.SQLException;

import interfaces.Param.ParamPannel;
import interfaces.Param.FillTable.FillTableSalle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class RemovingSalle {


	private int number;
	private ParamPannel param;

	public RemovingSalle(FillTableSalle fillTable,ParamPannel param) {
		this.number = fillTable.getSalle();
		this.param=param;
			this.initialize();
	}


	public void initialize() {
		System.out.println("Suppresion de la salle : " + number);

		Alert alert = new Alert(AlertType.INFORMATION, "",ButtonType.YES ,ButtonType.CANCEL);
		alert.setHeaderText("Supprimer définitivement la salle "+this.number+" ?");
		alert.showAndWait();

		if(alert.getResult()==ButtonType.YES)
		{
			try {
				this.param.getParam().getDb().getModif().deleteSalle(this.number);
				new Alert(Alert.AlertType.CONFIRMATION,"La salle "+this.number+" a bien été supprimé").show();
				this.param.getTableSalle().setAll();
			} catch (SQLException e) {
				new Alert(AlertType.ERROR,"Erreur : La salle "+number+" n'a pas pu être supprimée.").show();
			}
		}



		this.param.getTableGrp().setAll();
		this.param.getTableEns().setAll();
	}



}
