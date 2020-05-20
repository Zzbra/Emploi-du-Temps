package interfaces.Param.FillTable;

import java.util.ArrayList;

import interfaces.Param.ParamEDT;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FillTableActivite {
	
	private String groupe;
	private int nbSeq;
	private String matiere;
	private ComboBox<Integer> comboSeq = new ComboBox<Integer>();
	private VBox box;
	private ParamEDT param;
	private ArrayList<RadioButton> dispoArray = new ArrayList<RadioButton>();
	
	public FillTableActivite(String groupe, int nbSeq) {
		this.setNbSeq(nbSeq);
		this.setGroupe(groupe);
	}
	

	public FillTableActivite(String matiere,  int comboSeq, ParamEDT param) {
		this.param=param;
		this.setMatiere(matiere);
		setComboSeq(comboSeq);
		
		setBox(setDispo());
	}
	


	public int getNbSeq() {
		return nbSeq;
	}
	public VBox setDispo() {
		VBox box = new VBox();
		
		VBox boxDispo = new VBox();
		boxDispo.setPadding(new Insets(0,0,0,20));
		for(int i=1;i<5;i++) {
			RadioButton butt1 = new RadioButton("LUNDI Sequence "+i);
			RadioButton butt2 = new RadioButton("MARDI Sequence "+i);
			RadioButton butt3 = new RadioButton("MERCREDI Sequence "+i);
			RadioButton butt4 = new RadioButton("JEUDI Sequence "+i);
			RadioButton butt5 = new RadioButton("VENDREDI Sequence "+i);
			butt1.setId(String.valueOf(i));
			butt2.setId(String.valueOf(i+4));
			butt3.setId(String.valueOf(i+8));
			butt4.setId(String.valueOf(i+12));
			butt5.setId(String.valueOf(i+16));
			this.dispoArray.add(butt1);
			this.dispoArray.add(butt2);
			this.dispoArray.add(butt3);
			this.dispoArray.add(butt4);
			this.dispoArray.add(butt5);
			HBox tempH = new HBox(butt1,butt2,butt3,butt4,butt5);
			tempH.setSpacing(20);
			boxDispo.getChildren().add(tempH);
		}
		

		boxDispo.setSpacing(20);
		box.setSpacing(20);
		box.getChildren().addAll(new HBox(boxDispo));
		
		return box;
	}

	public void setNbSeq(int nbSeq) {
		
		this.nbSeq = nbSeq;
	}


	public String getGroupe() {
		return groupe;
	}


	public void setGroupe(String groupe) {
		this.groupe = groupe;
	}


	public String getMatiere() {
		return matiere;
	}


	public void setMatiere(String matiere) {
		this.matiere = matiere;
	}


	public ComboBox<Integer> getComboSeq() {
		return comboSeq;
	}


	public void setComboSeq(int comboSeq) {
		ObservableList<Integer> data = FXCollections.observableArrayList();
		for(int i=0;i<=20;i++){
			data.add(i);
		}
		this.comboSeq.setItems(data);
		this.comboSeq.getSelectionModel().select(comboSeq);
	}



	public VBox getBox() {
		return box;
	}


	public void setBox(VBox box) {
		this.box = box;
	}


	public ArrayList<Integer> getDispoArray() {
		ArrayList<Integer> array = new ArrayList<Integer>();
		for(RadioButton but  :dispoArray) {
			if(but.isSelected()) array.add(Integer.parseInt(but.getId()));
		}
		return array;
	}


	public void setDispoArray(ArrayList<RadioButton> dispoArray) {
		this.dispoArray = dispoArray;
	}
	
}
