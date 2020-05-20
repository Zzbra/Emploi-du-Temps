package application;

import java.time.*;
import java.time.temporal.*;

import mariaDB.DBConnection;

public class Solution {
	private Probleme probleme;
	private Solveur solveur;


	public Solution(DBConnection dbConnection) {
		this.probleme = new Probleme();
		this.solveur = new Solveur(probleme);  
		
		Instant beforeContrainte = Instant.now();
	//	this.solveur.definirContraintes();
		Instant afterContrainte = Instant.now();
		System.out.println("Durée definirContrainte = "+Duration.between(beforeContrainte, afterContrainte).toMinutes() + " : " +Duration.between(beforeContrainte, afterContrainte).getSeconds() + " : " + Duration.between(beforeContrainte, afterContrainte).getNano() );
		
		Instant beforeSolve = Instant.now();
	//	this.solveur.solve();
		Instant afterSolve = Instant.now();
		System.out.println("Durée solve = "+Duration.between(beforeSolve, afterSolve).toMinutes() + " : " +Duration.between(beforeSolve, afterSolve).getSeconds() + " : " + Duration.between(beforeSolve, afterSolve).getNano() );
		
		//		
	}

	public Probleme getProbleme() {
		return this.probleme;
	}

	public Solveur getSolveur() {
		return solveur;
	}

}
