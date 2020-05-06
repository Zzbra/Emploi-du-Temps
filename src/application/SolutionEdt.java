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

    public void setHeures(int[][] heures) {
        this.heures = heures;
    }

    public void setEnseignants(int[][] enseignants) {
        this.enseignants = enseignants;
    }

    public void setSalles(int[][] salles) {
        this.salles = salles;
    }

    public void setNbGroupes(int nbGroupes) {
        this.nbGroupes = nbGroupes;
    }

    public void print(){
        Probleme probleme = new Probleme();
        CaseEdTGroupe[][] sol  = Solveur.fillModele(this, probleme);
        for (int i = 0; i < nbGroupes; i++) {
            System.out.println("Groupe: " + (i+1));
            for (int j = 0; j < 20; j++) {
                System.out.println(probleme.theCreneaux()[j] + " : "+ sol[i][j]);
            }
        }
    }
}
