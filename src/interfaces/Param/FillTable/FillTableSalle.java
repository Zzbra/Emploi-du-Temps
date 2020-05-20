package interfaces.Param.FillTable;

public class FillTableSalle {
	private int salle;
	private String groupe;
	private int capacite;
	
	
	public FillTableSalle(int salle, String groupe, int capacite) {
		this.setCapacite(capacite);
		this.setGroupe(groupe);
		this.setSalle(salle);
	}
	
	public int getSalle() {
		return salle;
	}
	public void setSalle(int salle) {
		this.salle = salle;
	}
	public int getCapacite() {
		return capacite;
	}
	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}
	public String getGroupe() {
		return groupe;
	}
	public void setGroupe(String groupe) {
		this.groupe = groupe;
	}
}
