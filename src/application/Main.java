package application;

public class Main {
    public static void main(String[] args) {
        Solveur solveur = new Solveur(new Probleme(2, 1));
        solveur.definirContraintes();
        solveur.solve();
        solveur.printModele();
        solveur.printSolution();
        solveur.printDifferenceAvecModele();
//        solveur.printDifferences();
    }

}
