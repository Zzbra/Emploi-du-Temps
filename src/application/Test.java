package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Test {
    public static void main(String[] args) {
//        File sol1File = new File("src/Solutions_Serialisees/solG.ser");
//        ObjectInputStream ois1 = null;
//        SolutionEdt sol1 = null;
//        try{
//            ois1 = new ObjectInputStream(new FileInputStream(sol1File));
//            sol1 = (SolutionEdt)ois1.readObject();
//            ois1.close();
//        }catch (IOException | ClassNotFoundException e){
//            e.printStackTrace();
//        }
//
//        sol1.print();

        genererSolution2();
    }

    public static void genererSolution2(){
        File sol1File = new File("src/Solutions_Serialisees/sol10-3.ser");
        File sol2File = new File("src/Solutions_Serialisees/sol10_a_18.ser");
        ObjectInputStream ois1 = null;
        ObjectInputStream ois2 = null;
        SolutionEdt sol1 = null;
        SolutionEdt sol2 = null;

        try{
            ois1 = new ObjectInputStream(new FileInputStream(sol1File));
            sol1 = (SolutionEdt)ois1.readObject();
            ois1.close();

            ois2 = new ObjectInputStream(new FileInputStream(sol2File));
            sol2 = (SolutionEdt)ois2.readObject();
            ois2.close();

        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

//        sol1.print();
//        sol2.print();

        int nbAjout = 8;

        int[][] heurNouvSol = new int[10+nbAjout][20];
        int[][] enseignantsNouvSol = new int[10+nbAjout][20];
        int[][] sallesNouvSol = new int[10+nbAjout][20];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                heurNouvSol[i][j] = sol1.getHeures()[i][j];
                enseignantsNouvSol[i][j] = sol1.getEnseignants()[i][j];
                sallesNouvSol[i][j] = sol1.getSalles()[i][j];
            }
        }

        for (int i = 0; i < nbAjout; i++) {
            for (int j = 0; j < 20; j++) {
                heurNouvSol[10+i][j] = sol2.getHeures()[i][j];
                enseignantsNouvSol[10+i][j] = sol2.getEnseignants()[i][j] ;
                sallesNouvSol[10+i][j] = sol2.getSalles()[i][j] ;
            }
        }

        SolutionEdt solutionEdt = new SolutionEdt(heurNouvSol, enseignantsNouvSol, sallesNouvSol, 10);
        solutionEdt.setNbGroupes(10+nbAjout);

        System.out.println(solutionEdt.getNbGroupes());

        Solveur.serializaSolution(solutionEdt, "src/Solutions_Serialisees/sol18.ser");

    }

    public static void genererSolution1(){
        File sol1File = new File("src/Solutions_Serialisees/solAB.ser");
        File sol2File = new File("src/Solutions_Serialisees/solCE.ser");
        File sol3File = new File("src/Solutions_Serialisees/solG.ser");

        ObjectInputStream ois1 = null;
        ObjectInputStream ois2 = null;
        ObjectInputStream ois3 = null;

        SolutionEdt sol1 = null;
        SolutionEdt sol2 = null;
        SolutionEdt sol3 = null;
        try{
            ois1 = new ObjectInputStream(new FileInputStream(sol1File));
            sol1 = (SolutionEdt)ois1.readObject();
            ois1.close();

            ois2 = new ObjectInputStream(new FileInputStream(sol2File));
            sol2 = (SolutionEdt)ois2.readObject();
            ois2.close();

            ois3 = new ObjectInputStream(new FileInputStream(sol3File));
            sol3 = (SolutionEdt)ois3.readObject();
            ois3.close();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        int[][] heurNouvSol = new int[6][20];
        int[][] enseignantsNouvSol = new int[6][20];
        int[][] sallesNouvSol = new int[6][20];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 20; j++) {
                heurNouvSol[i][j] = sol1.getHeures()[i][j];
                enseignantsNouvSol[i][j] = sol1.getEnseignants()[i][j];
                sallesNouvSol[i][j] = sol1.getSalles()[i][j];
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 20; j++) {
                heurNouvSol[4+i][j] = sol2.getHeures()[i][j];
                enseignantsNouvSol[4+i][j] = sol2.getEnseignants()[i][j] > 0 ? sol2.getEnseignants()[i][j]+6 : 0;
                sallesNouvSol[4+i][j] = sol2.getSalles()[i][j] > 2 ? sol2.getSalles()[i][j]+4 : sol2.getSalles()[i][j];
            }
        }
//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 20; j++) {
//                heurNouvSol[8+i][j] = sol3.getHeures()[i][j];
//                enseignantsNouvSol[8+i][j] = sol3.getEnseignants()[i][j];
//                sallesNouvSol[8+i][j] = sol3.getSalles()[i][j];
//            }
//        }

        SolutionEdt solutionEdt = new SolutionEdt(heurNouvSol, enseignantsNouvSol, sallesNouvSol, 6);
        Solveur.serializaSolution(solutionEdt, "src/Solutions_Serialisees/solABC.ser");
    }

}
