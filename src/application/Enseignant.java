package application;

import java.util.ArrayList;
import java.util.Arrays;

public class Enseignant {
	private String name;
	private ArrayList<Character> groupe;
	private ArrayList<String> disciplines;
	private ArrayList<Integer> creneauxOff;
	private int id;

	public Enseignant(String name, char[] groupe, String[] disciplines, int[] creneauxOff, int id) {
		super();
		this.id = id;
		this.setName(name);
		this.groupe = new ArrayList<>(groupe.length);
		for (int i = 0; i < groupe.length; i++) {
			this.groupe.add(groupe[i]);
		}
		this.disciplines =new ArrayList<>(Arrays.asList(disciplines));

		this.creneauxOff = new ArrayList<>(creneauxOff.length);
		for (int i = 0; i < creneauxOff.length; i++) {
			this.creneauxOff.add(creneauxOff[i]);
		}
	}

	public Enseignant(String name, char[] groupe, String[] disciplines, int id) {
		super();
		this.id = id;
		this.setName(name);
		this.groupe = new ArrayList<>(groupe.length);
		for (int i = 0; i < groupe.length; i++) {
			this.groupe.add(groupe[i]);
		}
		this.disciplines =new ArrayList<>(Arrays.asList(disciplines));
		this.creneauxOff = new ArrayList<>();
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

