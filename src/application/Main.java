package application;

public class Main {
    public static void main(String[] args) {
        Solveur solveur = new Solveur(new Probleme(2,   1));
        solveur.getInstance().printProbleme();
        solveur.definirContraintes();
        solveur.LNS();
        solveur.printSolution();
        if(solveur.getInstance().getNbSols() > 1)
            solveur.printDifferences();
    }

}
/*
Yang Zheng; Thierry Mathias; Christine Caldentey; Slimane Sadelli; Lionel Silvy; Diane Pietrini; Florence Exbrayat;
I504; I0; I1; A513; A611; B612; B502;
A1; A2; B3; B4;
 */