package application;
import java.util.ArrayList;

public class Matiere {
	private String subject;
	private ArrayList<String> nature;
	private int sequence;
	private ArrayList<Integer> creneaux;
	private boolean hasCreneau;


	public Matiere(String subject, ArrayList<String > nature, int sequence) {
		super();
		this.setSubject(subject);
		this.nature = nature;
		this.setSequence(sequence);
		this.creneaux = new ArrayList<>();
		this.hasCreneau = false;
	}
	public Matiere(String subject, ArrayList<String> nature, int sequence,int[] creneaux) {
		super();
		this.setSubject(subject);
		this.nature = nature;
		this.setSequence(sequence);
		this.creneaux = new ArrayList<Integer>(){{
			for (int creneau : creneaux) {
				add(creneau);
			}
		}};
		this.hasCreneau = true;
	}

	public ArrayList<Integer> getCreneaux() {
		return creneaux;
	}

	public boolean hasCreneau(){return this.hasCreneau;}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public ArrayList<String> getNature() {
		return nature;
	}


	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public String toString() {
		return getSubject() + " (" + getNature() + ") seq num :" + getSequence();
	}
}
