package application;

public class Groupe {
	private char alphabet;
	private int number;
	private int capacity;
	private String specificity;
	
	public Groupe(char alphabet, int number, int capacity, String specificity) {
		super();
		this.setAlphabet(alphabet);
		this.setNumber(number);
		this.setCapacity(capacity);
		this.setSpecificity(specificity);
	}

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
		return "" + getAlphabet() + getNumber();
	}
}
