package application;

public class SolutionPbm {
	private Probleme probleme;
	private Solveur solveur;
	

	public SolutionPbm() {
		this.probleme = new Probleme(6, 1);
		this.solveur = new Solveur(probleme);
		this.solveur.definirContraintes();
		this.solveur.LDS();
	}
	
	public Probleme getProbleme() {
		return this.probleme;
	}
	
	public Solveur getSolveur() {
		return solveur;
	}
	
}
