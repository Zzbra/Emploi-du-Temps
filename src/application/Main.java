package application;


public class Main {
    public static void main(String[] args) {
        Solveur solveur = new Solveur(new Probleme(10,   1));
        solveur.getInstance().printProbleme();
        solveur.definirContraintes();
        solveur.LDS();
        solveur.printModele();
        solveur.printSolution();
        solveur.printDifferenceAvecModele();
        if(solveur.getInstance().getNbSols() > 1)
            solveur.printDifferences();
    }

}
