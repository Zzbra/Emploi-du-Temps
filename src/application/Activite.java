package application;

import java.io.Serializable;

public class Activite implements Serializable {
	private Groupe groupe;
	private Matiere matiere;
	
	public Activite(Groupe groupe, Matiere matiere) {
		super();
		this.setGroupe(groupe);
		this.setMatiere(matiere);
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

	public Matiere getMatiere() {
		return matiere;
	}

	public void setMatiere(Matiere matiere) {
		this.matiere = matiere;
	}

	@Override
	public String toString() {
		return "Activite{" +
				"groupe=" + groupe +
				", matiere=" + matiere +
				'}';
	}
}
