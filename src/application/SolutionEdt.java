package application;

import java.io.Serializable;

public class SolutionEdt implements Serializable {

    private  static  final  long serialVersionUID =  1350092881346723535L;

    private int[][] heures, enseignants, salles;
    private int nbGroupes;

    public SolutionEdt(int[][] heures, int[][] enseignants, int[][] salles, int nbGroupes) {
        this.heures = heures;
        this.enseignants = enseignants;
        this.salles = salles;
        this.nbGroupes = nbGroupes;
    }

    public int[][] getHeures() {
        return heures;
    }

    public int[][] getEnseignants() {
        return enseignants;
    }

    public int[][] getSalles() {
        return salles;
    }

    public int getNbGroupes() {
        return nbGroupes;
    }
}
