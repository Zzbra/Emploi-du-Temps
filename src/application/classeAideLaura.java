package application;

import java.util.ArrayList;

public class classeAideLaura {
    public static void main(String[] args) {
        Enseignant [] enseignants = new Enseignant [7];
        enseignants[0] = new Enseignant("Yang Zheng", new ArrayList<Character>(){{add('I');}},new String[] {"anglais"}, 0);

        enseignants[1] = new Enseignant("Thierry Mathias", new ArrayList<Character>(){{add('A');}},
                new String[] {"referent", "maths", "bureautique"}, 1);
        enseignants[2] = new Enseignant("Christine Caldentey", new ArrayList<Character>(){{add('A');}},
                new String[] {"referent", "sport", "arl"}, new int[]{8, 9, 10, 11}, 2);
        enseignants[3] = new Enseignant("Slimane Sadelli", new ArrayList<Character>(){{add('A');}},
                new String[] {"referent", "francais"}, new int[]{12, 13, 14, 15}, 3);

        enseignants[4] = new Enseignant("Lionel Silvy", new ArrayList<Character>(){{add('B');}},
                new String[]{"referent", "sport", "bureautique"}, new int[]{10, 11}, 4);
        enseignants[5] = new Enseignant("Diane Pietrini", new ArrayList<Character>(){{add('B');}},
                new String[]{"referent", "maths", "francais"}, 5);
        enseignants[6] = new Enseignant("Florence Exbrayat", new ArrayList<Character>(){{add('B');}},
                new String[]{"referent", "francais"}, 6);


        Salle [] salles = new Salle [7];
        salles[0] = new Salle (new ArrayList<Character>(){{add('I');}}, 504, new ArrayList<String>(){{add("anglais");}}, 15, 0);
        salles[1] = new Salle(new ArrayList<Character>(){{add('I');}}, 0, new ArrayList<String>(){{add("sport");}}, 500, 1); //'I' indique que c'est pour tout le monde
        salles[2] = new Salle(new ArrayList<Character>(){{add('I');}}, 1, new ArrayList<String>(){{add("autre");}}, 500, 2);
        salles[3] = new Salle(new ArrayList<Character>(){{add('A');}}, 513, new ArrayList<String>(){{add("info");}}, 15, 3);
        salles[4] = new Salle(new ArrayList<Character>(){{add('A');}}, 611, new ArrayList<String>(){{add("cours");add("info");}}, 15, 4);
        salles[5] = new Salle(new ArrayList<Character>(){{add('B');}}, 612, new ArrayList<String>(){{add("info");}}, 15, 5);
        salles[6] = new Salle(new ArrayList<Character>(){{add('B');}}, 502, new ArrayList<String>(){{add("cours");}}, 15, 6);

        Groupe [] groupes = new Groupe [18];
        groupes[0] = new Groupe('A', 1, 15, "generique", 1);
        groupes[1] = new Groupe('A', 2, 15, "generique", 2);
        //groupes[2] = new Groupe('A', 3, 15, "generique");
        groupes[2] = new Groupe( 'B', 3, 15, "generique", 1);
        groupes[3] = new Groupe( 'B', 4, 15, "generique", 2);

        Matiere [] matieres = new Matiere [20];
        matieres[0] = new Matiere("Temps referent", new ArrayList<String>(){{add("cours");add("info");}}, 0,new int[]{0, 1, 18, 19});
        matieres[1] = new Matiere("Temps referent", new ArrayList<String>(){{add("cours");add("info");}}, 1, new int[]{0, 1, 18, 19});
        matieres[2] = new Matiere("Temps referent", new ArrayList<String>(){{add("cours");add("info");}}, 2,new int[]{0, 1, 18, 19});
        matieres[3] = new Matiere("Temps referent", new ArrayList<String>(){{add("cours");add("info");}}, 3, new int[]{0, 1, 18, 19});

//		matieres[4] = new Matiere("Projet pedagogique", new String[]{"cours", "autre"}, 0, Color.WHITE);
//		matieres[5] = new Matiere("Projet pedagogique", new String[]{"cours", "autre"}, 1, Color.WHITE);
        matieres[4] = new Matiere("Projet pedagogique", new ArrayList<String>(){{add("autre");}}, 0);
        matieres[5] = new Matiere("Projet pedagogique", new ArrayList<String>(){{add("autre");}}, 1);

        matieres[6] = new Matiere("EDA francais", new ArrayList<String>(){{add("info");}}, 0);

        matieres[7] = new Matiere("Remediation francais", new ArrayList<String>(){{add("cours");}}, 0);
        //matieres[3] = new Matiere("EDA francais - micro e/se", new String[]{"info"}, 0);

        matieres[8] = new Matiere("Francais", new ArrayList<String>(){{add("cours");}}, 0);

        matieres[9] = new Matiere("Atelier lecture/ecriture", new ArrayList<String>(){{add("cours");}}, 0);
        matieres[10] = new Matiere("EDA maths", new ArrayList<String>(){{add("info");}}, 0);
        //matieres[8] = new Matiere("EDA maths - micro e/se", "info", 0);
        matieres[11] = new Matiere("Remediation math", new ArrayList<String>(){{add("cours");}}, 0);
        matieres[12] = new Matiere("Mathematique", new ArrayList<String>(){{add("cours");}}, 0);
        //matieres[13] = new Matiere("ARL", new String[]{"cours"}, 0);
        matieres[13] = new Matiere("EDA bureautique", new ArrayList<String>(){{add("info");}}, 0);
        //matieres[13] = new Matiere("EDA bureautique - micro e/se", "info", 0);
        matieres[14] = new Matiere("Remedition bureautique", new ArrayList<String>(){{add("cours");}}, 0);
        //matieres[15] = new Matiere("Bureautique", "info", 0);
        matieres[15] = new Matiere("Anglais", new ArrayList<String>(){{add("anglais");}}, 0);
        matieres[16] = new Matiere("Sport", new ArrayList<String>(){{add("sport");}}, 0, new int[]{3, 10, 9, 12, 13, 14, 15});
        matieres[17] = new Matiere("Sport", new ArrayList<String>(){{add("sport");}}, 1, new int[]{2, 3, 10, 11, 12, 13, 14, 15});
        matieres[18] = new Matiere("Demarches exterieures", new ArrayList<String>(){{add("autre");}}, 0, new int[]{6, 7});
        //matieres[19] = new Matiere("Null", "null", 0);
        matieres[19] = new Matiere("Demarches exterieures", new ArrayList<String>(){{add("autre");}}, 1, new int[]{6, 7});

        Activite [][] activites = new Activite [4][20];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 20; j++) {
                activites[i][j] = new Activite(groupes[i], matieres[j]);
            }
        }
        Creneau [] creneaux = new Creneau [20];
        for(Jour jour : Jour.values()) {
            creneaux[jour.ordinal() * 4] = new Creneau(jour, 840, 1010);
            creneaux[jour.ordinal() * 4 + 1] = new Creneau(jour, 1030, 1200);
            creneaux[jour.ordinal() * 4 + 2] = new Creneau(jour, 1340, 1510);
            creneaux[jour.ordinal() * 4 + 3] = new Creneau(jour, 1530, 1700);
        }



        Probleme probleme = new Probleme();
        probleme.setEnseignant(enseignants);
        probleme.setSalle(salles);
        probleme.setGroupe(groupes);
        probleme.setCreneaux(creneaux);
        probleme.setActivites(activites);
        probleme.setNbGroupes(4);

        Solveur solveur = new Solveur(probleme);
        solveur.definirContraintes();
        solveur.solve();
        solveur.printSolution();
    }
}
