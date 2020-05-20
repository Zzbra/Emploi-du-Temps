package interfaces.Param.FillTable;


public class FillDispoEns {

	
	private String name;
	private boolean lundi;
	private boolean mardi;
	private boolean mercredi;
	private boolean jeudi;
	private boolean vendredi;
	
	public FillDispoEns(String name,boolean lundi,boolean mardi,boolean mercredi,boolean jeudi,boolean vendredi) {
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

	public boolean isLundi() {
		return lundi;
	}

	public void setLundi(boolean lundi) {
		this.lundi = lundi;
	}

	public boolean isMardi() {
		return mardi;
	}

	public void setMardi(boolean mardi) {
		this.mardi = mardi;
	}

	public boolean isMercredi() {
		return mercredi;
	}

	public void setMercredi(boolean mercredi) {
		this.mercredi = mercredi;
	}

	public boolean isJeudi() {
		return jeudi;
	}

	public void setJeudi(boolean jeudi) {
		this.jeudi = jeudi;
	}

	public boolean isVendredi() {
		return vendredi;
	}

	public void setVendredi(boolean vendredi) {
		this.vendredi = vendredi;
	}
}
