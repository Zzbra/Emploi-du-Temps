package application;
/*
    TODO:
    -Implémenter prof référent pour sous groupe
    -Gérer le fait que pour l'instant si il n'y a pas de prof de sport, un prof qui n'est pas habilité a
    enseigner le sport est assigné a l'activité.
    -Démarches extérieures n'a pas besoin de prof
 */

import interfaces.MainInterface;

public class Main {
    public static void main(String[] args) {

        Solveur solveur = new Solveur(new Probleme(4,   1));
        solveur.getInstance().printProbleme();
        solveur.definirContraintes();
        solveur.solve();
        //solveur.printModele();
        solveur.printSolution();
        //solveur.printDifferenceAvecModele();
        if(solveur.getInstance().getNbSols() > 1)
            solveur.printDifferences();
    }

}
