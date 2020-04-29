package application;

public class Solution {
	private Probleme probleme;
	private Solveur solveur;
	

	public Solution() {
		this.probleme = new Probleme(4, 5);
		this.solveur = new Solveur(probleme);
		this.solveur.definirContraintes();
		this.solveur.solve();
	}
	
	public Probleme getProbleme() {
		return this.probleme;
	}
	
	public Solveur getSolveur() {
		return solveur;
	}
	
}
