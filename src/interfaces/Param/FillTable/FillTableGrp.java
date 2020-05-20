package interfaces.Param.FillTable;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class FillTableGrp {

	private String groupe;
	private String referent;
	private String specificite;
	
	public FillTableGrp(String groupe, String referent,  String specificite) {
		this.setGroupe(groupe);
		this.setReferent(referent);
		this.setSpecificite(specificite);
	}
	



	public String getGroupe() {
		return groupe;
	}



	public void setGroupe(String groupe) {
		this.groupe = groupe;
	}



	public String getSpecificite() {
		return specificite;
	}



	public void setSpecificite(String specificite) {
		this.specificite = specificite;
	}




	public String getReferent() {
		return referent;
	}




	public void setReferent(String referent) {
		this.referent = referent;
	}

	
}
