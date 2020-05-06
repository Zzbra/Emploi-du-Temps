package application;
import java.util.ArrayList;
import java.util.Arrays;

public class Salle {
	private ArrayList<Character> groupe;
	private int number;
	private ArrayList<String> nature;
	private int capacity, id;
	
	public Salle(char[] groupe, int number, String[] nature, int capacity, int id) {
		super();
		this.id = id;
		this.groupe = new ArrayList<>(groupe.length);
		for (int i = 0; i < groupe.length; i++) {
			this.groupe.add(groupe[i]);
		}
		this.setNumber(number);
		this.nature = new ArrayList<>();
		this.nature.addAll(Arrays.asList(nature));
		this.setCapacity(capacity);
	}

	public int getId() {
		return id;
	}

	public ArrayList<Character> getGroupe() {
		return groupe;
	}

	public void setGroupe(ArrayList groupe) {
		this.groupe = groupe;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ArrayList<String> getNature() {
		return nature;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public String toString() {
		return "" + (char)getGroupe().get(0) + getNumber();
	}
}
