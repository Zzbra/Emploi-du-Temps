package application;
/*
    TODO:
    -Implémenter prof référent pour sous groupe
    -Gérer le fait que pour l'instant si il n'y a pas de prof de sport, un prof qui n'est pas habilité a
    enseigner le sport est assigné a l'activité.
    -Démarches extérieures n'a pas besoin de prof

    Chrono: nb Sous groupes     temps en sec
            2                   0.682
            4                   3.895
            6                   15.797
            8                   66.705
    ajout du deuxième prof d'anglais
            10                  75.999
            12                  194.853
            14                  306.767
            16                  407.145
            18
 */

import interfaces.MainInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;

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


        Solveur solveur = new Solveur(new Probleme(18,     1));
        solveur.getInstance().printProbleme();
        solveur.definirContraintes();
        Date tps1 = new Date(System.currentTimeMillis());
        solveur.LDS();
        //solveur.printModele();

        solveur.printSolution();
        System.out.println((new Date(System.currentTimeMillis()).getTime()-tps1.getTime())/1000.0 + " secondes");
        //solveur.printDifferenceAvecModele();
        if(solveur.getInstance().getNbSols() > 1)
            solveur.printDifferences();
    }
}
