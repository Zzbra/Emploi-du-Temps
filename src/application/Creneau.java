package application;

public class Creneau {
	private Jour jour;
	private int starttime;
	private int endtime;
	
	public Creneau(Jour jour, int starttime, int endtime) {
		super();
		this.setJour(jour);
		this.setStarttime(starttime);
		this.setEndtime(endtime);
	}

	public Jour getJour() {
		return jour;
	}

	public void setJour(Jour jour) {
		this.jour = jour;
	}

	public int getStarttime() {
		return starttime;
	}

	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	
	public String toString() {
		return "" + getJour() + " " + getStarttime() / 100 + "h" + getStarttime() % 100;
	}
}
