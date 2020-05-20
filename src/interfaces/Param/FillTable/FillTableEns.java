package interfaces.Param.FillTable;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

public class FillTableEns {
	private String name;
	private String groupe;
	private ArrayList<String> disciplines;
	private RadioButton button;
	private String fonction;

	public FillTableEns(String name, String groupe, ArrayList<String> disciplines) {
		this.setDisciplines(disciplines);
		this.setGroupe(groupe);
		this.setName(name);
	}
	
	public FillTableEns(String name) {
		
	}
	
	
	public FillTableEns(String name, RadioButton button) {
		this.setName(name);
		this.setButton(button);
	}
	
	public FillTableEns(String name, String fonction) {
		this.setName(name);
		this.setFonction(fonction);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisciplines() {
		String str ="";
		for(String disc : this.disciplines) {
			str=str+""+disc+", ";
		}
		if(str.length()<2) return str;
		return str.substring(0,str.length()-2);
	}

	public void setDisciplines(ArrayList<String> disciplines) {
		this.disciplines = disciplines;
	}

	public String getGroupe() {
		return groupe;
	}

	public void setGroupe(String groupe) {
		this.groupe = groupe;
	}


	public RadioButton getButton() {
		return button;
	}


	public void setButton(RadioButton button) {
		this.button = button;
	}


	public String getFonction() {
		return fonction;
	}


	public void setFonction(String fonction) {
		this.fonction = fonction;
	}
	
	
	
	

}
