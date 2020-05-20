package interfaces.Param.removing;

import java.sql.SQLException;

import interfaces.Param.ParamPannel;
import interfaces.Param.FillTable.FillTableEns;
import interfaces.Param.FillTable.FillTableGrp;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class RemovingEns {

	private String name;
	private ParamPannel param;
	private String groupe;
	
	public RemovingEns(FillTableEns ens,ParamPannel param) {
		this.name=ens.getName();
		this.param=param;
		this.groupe=ens.getGroupe();
		this.initialize();
	}
	
	
	public void initialize() {
		System.out.println("Suppresion de l'enseignant : " + name);
		
		Alert alert = new Alert(AlertType.INFORMATION, "",ButtonType.YES ,ButtonType.CANCEL);
		alert.setHeaderText("Supprimer définitivement l'enseignant "+this.name+" ?");
		alert.showAndWait();
		
		if(alert.getResult()==ButtonType.YES)
		{
			System.out.println(groupe);
			if(groupe.equals("-")) {
				try {
					this.param.getParam().getDb().getModif().deleteEns(this.name);
					new Alert(Alert.AlertType.CONFIRMATION,"L'enseignant "+this.name+" a bien été supprimé").show();
				} catch (SQLException e) {
					new Alert(AlertType.ERROR,"Erreur : L'enseignant " + name + " n'a pas pu etre supprimé.").show();
				}
			}
			else new Alert(AlertType.ERROR,"Erreur : L'enseignant " + name + " n'a pas pu etre supprimé. Le groupe "+groupe+" lui est assigné. \n Supprimer le groupe ou supprimer cet enseignant de ce groupe.").show();
			
		}
		
		this.param.getTableGrp().setAll();
		this.param.getTableEns().setAll();
	}
	
	
	
}
