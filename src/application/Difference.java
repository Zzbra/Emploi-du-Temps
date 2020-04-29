package application;

public class Difference {
    private CaseEdTGroupe creneau1;
    private CaseEdTGroupe creneau2;
    private int numSol1;
    private int numSol2;
    private Creneau creneau;

    public Difference(CaseEdTGroupe creneau1, CaseEdTGroupe creneau2, int numSol1, int numSol2, Creneau creneau) {
        this.creneau1 = creneau1;
        this.creneau2 = creneau2;
        this.numSol1 = numSol1;
        this.numSol2 = numSol2;
        this.creneau = creneau;
    }

    @Override
    public String toString() {
        return  this.creneau +"\nSolution: " + numSol1 + "  - " +
                creneau1.getActivite().getMatiere().getSubject() + ": " + creneau1.getSalle().getNumber() + " avec " + creneau1.getEnseignant().getName() +
                "\nSolution: " + numSol2 + "  - " +
                creneau2.getActivite().getMatiere().getSubject() + ": " + creneau2.getSalle().getNumber() + " avec " + creneau2.getEnseignant().getName();
    }
}
