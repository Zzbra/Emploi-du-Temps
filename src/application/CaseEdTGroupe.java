package application;

public class CaseEdTGroupe {
	private Activite activite;
	private Salle salle;
	private Enseignant enseignant;
	
	public CaseEdTGroupe(Activite activite, Salle salle, Enseignant enseignant) {
		super();
		this.activite = activite;
		this.salle = salle;
		this.enseignant = enseignant;
	}
	
	public Activite getActivite() {
		return activite;
	}
	public void setActivite(Activite activite) {
		this.activite = activite;
	}
	public Salle getSalle() {
		return salle;
	}
	public void setSalle(Salle salle) {
		this.salle = salle;
	}
	public Enseignant getEnseignant() {
		return enseignant;
	}
	public void setEnseignant(Enseignant enseignant) {
		this.enseignant = enseignant;
	}
	
	public String toString() {
		return activite.getMatiere() + " en " + getSalle() + " avec " + getEnseignant();
	}
	public boolean equals(CaseEdTGroupe autre){
		return (this.activite.getMatiere().getSubject().equals(autre.activite.getMatiere().getSubject())
				&& this.salle.getNumber() == autre.getSalle().getNumber()
				&& this.enseignant.getName().equals(autre.getEnseignant().getName()));
	}
}
