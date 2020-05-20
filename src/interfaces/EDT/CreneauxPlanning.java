package interfaces.EDT;

import application.CaseEdTGroupe;

public class CreneauxPlanning {
	
	private String name;
	private CaseEdTGroupe lundi;
	private CaseEdTGroupe mardi;
	private CaseEdTGroupe mercredi;
	private CaseEdTGroupe jeudi;
	private CaseEdTGroupe vendredi;
	
	public CreneauxPlanning(String name,CaseEdTGroupe lundi,CaseEdTGroupe mardi,CaseEdTGroupe mercredi,CaseEdTGroupe jeudi,CaseEdTGroupe vendredi) {
		this.setName(name);
		this.setLundi(lundi);
		this.setMardi(mardi);
		this.setMercredi(mercredi);
		this.setJeudi(jeudi);
		this.setVendredi(vendredi);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CaseEdTGroupe getLundi() {
		return lundi;
	}

	public void setLundi(CaseEdTGroupe lundi) {
		this.lundi = lundi;
	}


	public CaseEdTGroupe getMardi() {
		return mardi;
	}

	public void setMardi(CaseEdTGroupe mardi) {
		this.mardi = mardi;
	}


	public CaseEdTGroupe getMercredi() {
		return mercredi;
	}


	public void setMercredi(CaseEdTGroupe mercredi) {
		this.mercredi = mercredi;
	}


	public CaseEdTGroupe getJeudi() {
		return jeudi;
	}


	public void setJeudi(CaseEdTGroupe jeudi) {
		this.jeudi = jeudi;
	}


	public CaseEdTGroupe getVendredi() {
		return vendredi;
	}


	public void setVendredi(CaseEdTGroupe vendredi) {
		this.vendredi = vendredi;
	}

}
