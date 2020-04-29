package application;

import java.util.ArrayList;
import java.util.HashSet;

public class CalculDifference {
    private CaseEdTGroupe[][] edt;
    private int nbGroupes;
    private int nbSolutions;
    private ArrayList<HashSet<Difference>> resultat;
    private Creneau[] creneaux;

    public CalculDifference(CaseEdTGroupe[][] edt, int nbGroupes, int nbSolutions, Creneau[] creneaux) {
        this.edt = edt;
        this.creneaux = creneaux;
        this.nbGroupes = nbGroupes;
        this.nbSolutions = nbSolutions;
        resultat = new ArrayList<>(nbGroupes);
        for (int i = 0; i < nbGroupes; i++) {
            resultat.add(new HashSet<>());
        }
    }

    public void calcul(){
        for (int i = 0; i < 20; i++) {
            for(int l = 0; l < nbGroupes; l++) {
                for (int j = 0; j < nbSolutions-1; j++) {
                    for (int k = j + 1; k < nbSolutions; k++) {
                        if ( edt[j*nbGroupes + l][i] != null && edt[k*nbGroupes +l][i] != null) {
                            if (!edt[j * nbGroupes + l][i].equals(edt[k * nbGroupes + l][i])) {
                                Difference diff = new Difference(edt[j * nbGroupes + l][i], edt[k * nbGroupes + l][i], j + 1, k + 1, this.creneaux[i]);
                                resultat.get(l).add(diff);
                            }
                        }
                    }
                }
            }
        }
    }

    public void print(){
        int i = 1;
        for(HashSet<Difference> liste : resultat){
            System.out.println("Différence entre les solutions:");
            System.out.println("Groupe: " + i++);
            for(Difference diff : liste){
                System.out.println(diff);
            }
        }
    }
}
