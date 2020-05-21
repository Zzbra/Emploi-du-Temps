package application;
/*
    TODO:
    -Implémenter prof référent pour sous groupe
    -Gérer le fait que pour l'instant si il n'y a pas de prof de sport, un prof qui n'est pas habilité a
    enseigner le sport est assigné a l'activité.
    -Démarches extérieures n'a pas besoin de prof
 */

import interfaces.MainInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Main {
    public static void main(String[] args) {

//        File file = new File("src/Solutions_Serialisees/probleme.ser");
//        ObjectInputStream ois = null;
//        Probleme probleme = null;
//        try{
//            ois = new ObjectInputStream(new FileInputStream(file));
//            probleme = (Probleme) ois.readObject();
//            ois.close();
//        }catch(IOException | ClassNotFoundException e){
//            e.printStackTrace();
//        }
//
//        probleme.printProbleme();
//        for (int i = 0; i < probleme.getNbGroupes(); i++) {
//            for (int j = 0; j < 20; j++) {
//                System.out.println(probleme.getActivite(i, j));
//            }
//        }
//
//        Solveur solveur = new Solveur(probleme);
//        solveur.definirContraintes();
//        solveur.LDS();
//        solveur.printSolution();


        Solveur solveur = new Solveur(new Probleme(10,   1));
        solveur.getInstance().printProbleme();
        solveur.definirContraintes();
        solveur.LDS();
        //solveur.printModele();
        solveur.printSolution();
        //solveur.printDifferenceAvecModele();
        if(solveur.getInstance().getNbSols() > 1)
            solveur.printDifferences();
    }
}
