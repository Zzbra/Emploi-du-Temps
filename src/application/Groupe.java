package application;

import java.io.Serializable;

public class Groupe implements Serializable {
	private char alphabet;
	private int number;
	private int capacity;
	private String specificity;
	private int numSousGroupe;
	public Groupe(char alphabet, int number, int capacity, String specificity, int numSousGroupe) {
		super();
		this.numSousGroupe = numSousGroupe;
		this.setAlphabet(alphabet);
		this.setNumber(number);
		this.setCapacity(capacity);
		this.setSpecificity(specificity);
	}


	public int getNumSousGroupe() { return numSousGroupe;}

	public char getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(char alphabet) {
		this.alphabet = alphabet;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getSpecificity() {
		return specificity;
	}

	public void setSpecificity(String specificity) {
		this.specificity = specificity;
	}
	
	public String toString() {
		return "" + getAlphabet() + getNumSousGroupe();
	}
}
