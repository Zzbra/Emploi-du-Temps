package interfaces.Param.removing;

import java.sql.SQLException;

import interfaces.Param.ParamPannel;
import interfaces.Param.FillTable.FillTableGrp;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class RemovingGroupe {

	private char grp;
	private char number;
	private ParamPannel param;
	
	public RemovingGroupe(FillTableGrp fillTable,ParamPannel param) {
		this.grp = fillTable.getGroupe().charAt(0);
		this.number=fillTable.getGroupe().charAt(1);
		System.out.println(number);
		this.param=param;
		this.initialize();
	}
	
	
	public void initialize() {
		System.out.println("Suppresion du groupe : " + grp);
		
		ButtonType allGroup = new ButtonType("Le groupe "+grp);
		ButtonType justSub = new ButtonType("Seulement le sous-groupe "+grp+""+number);
		
		Alert alert = new Alert(AlertType.INFORMATION, "",allGroup ,justSub, ButtonType.CANCEL);
		alert.setHeaderText("Que voulez-vous supprimer ?");
		alert.showAndWait();

		if (alert.getResult() == allGroup) {
			Alert alertAll = new Alert(AlertType.INFORMATION, "",ButtonType.YES , ButtonType.CANCEL);
			alertAll.setHeaderText("Supprimer le groupe "+grp+" ?");
			alertAll.showAndWait();
			
			if(alertAll.getResult()  == ButtonType.YES) {
				try {
					this.param.getParam().getDb().getModif().deleteGroupe(grp);
					new Alert(Alert.AlertType.CONFIRMATION,"Le groupe "+grp+" a bien été supprimé").show();
					this.param.getTableGrp().setAll();
					this.param.getTableEns().setAll();
				} catch (SQLException e) {
					new Alert(AlertType.ERROR,"Erreur : le groupe " + grp + " n'a pas pu etre supprimé").show();
				}
				
			}
			
		}
		else if (alert.getResult() == justSub) {
			Alert alertAll = new Alert(AlertType.INFORMATION, "",ButtonType.YES ,ButtonType.CANCEL);
			alertAll.setHeaderText("Supprimer seulement le sous-groupe "+grp+""+number+" ?");
			alertAll.showAndWait();
			
			if(alertAll.getResult()  == ButtonType.YES) {
				try {
					System.out.println(grp+""+number);
					this.param.getParam().getDb().getModif().deleteSubGroupe(grp, number);
					new Alert(Alert.AlertType.CONFIRMATION,"Le sous-groupe "+grp+""+number+" a bien été supprimé").show();
					this.param.getTableGrp().setAll();
					this.param.getTableEns().setAll();
				} catch (SQLException e) {
					new Alert(AlertType.ERROR,"Erreur : le sous-groupe " + grp+""+number+ " n'a pas pu etre supprimé").show();
				}
				
			}
		}
	}
	
	
	
}
