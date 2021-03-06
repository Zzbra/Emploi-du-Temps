package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Enseignant implements Serializable {
	private String name;
	private ArrayList<Character> groupe;
	private ArrayList<String> disciplines;
	private ArrayList<Integer> creneauxOff;
	private int id;

	public Enseignant(String name,ArrayList<Character> groupe, String[] disciplines, int[] creneauxOff, int id) {
		super();
		this.id = id;
		this.setName(name);
		this.groupe = groupe;
		this.disciplines =new ArrayList<>(Arrays.asList(disciplines));

		this.creneauxOff = new ArrayList<>(creneauxOff.length);
		for (int i = 0; i < creneauxOff.length; i++) {
			this.creneauxOff.add(creneauxOff[i]);
		}
	}

	public Enseignant(int id, String name, ArrayList<Character> groupe, ArrayList<String> disciplines, ArrayList<Integer> creneauxOff){
		this.id = id;
		this.name = name;
		this.groupe = groupe;
		this.disciplines = disciplines;
		this.creneauxOff = creneauxOff;
	}

	public Enseignant(String name, ArrayList<Character> groupe, String[] disciplines, int id) {
		super();
		this.id = id;
		this.setName(name);
		this.groupe = groupe;
		this.disciplines =new ArrayList<>(Arrays.asList(disciplines));
		this.creneauxOff = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public ArrayList<Integer> getCreneauxOff() {
		return creneauxOff;
	}

	public ArrayList<String> getDisciplines() {
		return disciplines;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Character> getGroupe() {
		return groupe;
	}

	public void setGroupe(ArrayList<Character> groupe) {
		this.groupe = groupe;
	}
	
	public String toString() {
		return getName();
	}
}

