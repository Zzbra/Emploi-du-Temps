package interfaces.Param.removing;

import java.sql.ResultSet;
import java.sql.SQLException;

import interfaces.Param.ParamPannel;
import interfaces.Param.FillTable.FillTableSalle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class RemovingMatiere {


	private String subject;
	private ParamPannel param;

	public RemovingMatiere(String nature,ParamPannel param) {
		this.subject = nature;
		this.param=param;
		this.initialize();
	}


	public void initialize() {
		System.out.println("Suppresion de la matiere : " + subject);

		Alert alert = new Alert(AlertType.INFORMATION, "",ButtonType.YES ,ButtonType.CANCEL);
		alert.setHeaderText("Supprimer d�finitivement la matiere "+this.subject+" ?");
		alert.showAndWait();

		if(alert.getResult()==ButtonType.YES)
		{
			try {
				ResultSet getNbSeq = this.param.getParam().getDb().getModif().selectMatGetAct(subject);
				int nbSeq=0;
				while(getNbSeq.next()) nbSeq=getNbSeq.getInt("sommeSeq");
				
				if(nbSeq==0) {
					this.param.getParam().getDb().getModif().deleteMatiere(this.subject);
					new Alert(Alert.AlertType.CONFIRMATION,"La matiere "+this.subject+" a bien �t� supprim�").show();
					this.param.getTableSalle().setAll();
				}
				else {
					Alert alert2 = new Alert(AlertType.CONFIRMATION,"Des activites sont assign�es � cette mati�re. Supprimer cette mati�re entrainera la suppresion des activit�s associ�es. \n "
							+ "Supprimer d�finitivement la matiere "+this.subject+" ?",ButtonType.YES ,ButtonType.CANCEL);
					alert2.showAndWait();
					if(alert2.getResult()==ButtonType.YES) {
						this.param.getParam().getDb().getModif().deleteMatiere(this.subject);
						new Alert(Alert.AlertType.CONFIRMATION,"La matiere "+this.subject+" a bien �t� supprim�").show();
						this.param.getTableSalle().setAll();
					}
					else new Alert(AlertType.ERROR,"Erreur : La matiere "+subject+" n'a pas �t� supprim�e.").show();
				}
				
				
			} catch (SQLException e) {
				new Alert(AlertType.ERROR,"Erreur : La matiere "+subject+" n'a pas pu �tre supprim�e.").show();
			}
		}

		this.param.getTableMatiere().setAll();
	}



}
