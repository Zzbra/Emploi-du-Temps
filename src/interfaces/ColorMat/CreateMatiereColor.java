package interfaces.ColorMat;


import application.Matiere;
import application.Probleme;
import application.Solveur;
import javafx.scene.paint.Color;

public class CreateMatiereColor {
	
	Solveur solveur;
	Probleme instance;
	MatiereColor[] matCol;
	
	public CreateMatiereColor(Solveur solveur) {
		this.solveur=solveur;
		this.instance = solveur.getInstance();
		matCol = new MatiereColor[instance.getMatieres().length];
		setTab();
	}
	
	
	public Color getColor(Matiere matiere) {
		for(MatiereColor col : matCol) {
			if(matiere.equals(col.getMatiere())) return col.getColor();
		}
		return null;
	}
	
	
	public void setTab(){
		matCol[0] = new MatiereColor(getMatiere("Temps referent",0),Color.RED);
		matCol[1] = new MatiereColor(getMatiere("Temps referent",1),Color.RED);
		matCol[2] = new MatiereColor(getMatiere("Temps referent",2),Color.RED);
		matCol[3] = new MatiereColor(getMatiere("Temps referent",3),Color.RED);
		
		matCol[4] = new MatiereColor(getMatiere("Projet pedagogique",0),Color.YELLOW);
		matCol[5] = new MatiereColor(getMatiere("Projet pedagogique",1),Color.YELLOW);
		
		matCol[6] = new MatiereColor(getMatiere("EDA francais",0),Color.MAGENTA);
		matCol[7] = new MatiereColor(getMatiere("Remediation francais",0),Color.HOTPINK);
		//matCol[3] = new MatiereColor(getMatiere("EDA francais - micro e/se",0),Color.PINK);
		
		matCol[8] = new MatiereColor(getMatiere("Francais",0),Color.DARKMAGENTA);
		matCol[9] = new MatiereColor(getMatiere("Atelier lecture/ecriture",0),Color.ORANGE);
		matCol[10] = new MatiereColor(getMatiere("EDA maths",0),Color.BLUE);
	//	matCol[8] = new MatiereColor(getMatiere("EDA maths - micro e/se",0),Color.LIGHTBLUE);
		matCol[11] = new MatiereColor(getMatiere("Remediation math",0),Color.STEELBLUE);
		matCol[12] = new MatiereColor(getMatiere("Mathematique",0),Color.SKYBLUE);
//		matCol[11] = new MatiereColor(getMatiere("ARL",0),Color.BURLYWOOD);
		matCol[13] = new MatiereColor(getMatiere("EDA bureautique",0),Color.GREEN);
	//	matCol[13] = new MatiereColor(getMatiere("EDA bureautique - micro e/se",0),Color.GREENYELLOW);
		matCol[14] = new MatiereColor(getMatiere("Remedition bureautique",0),Color.DARKSEAGREEN);
	//	matCol[15] = new MatiereColor(getMatiere("Bureautique",0),Color.MEDIUMSEAGREEN);
		matCol[15] = new MatiereColor(getMatiere("Anglais",0),Color.ROSYBROWN);
		matCol[16] = new MatiereColor(getMatiere("Sport",0),Color.OLIVE);
		matCol[17] = new MatiereColor(getMatiere("Sport",1),Color.OLIVE);
		
		matCol[18] = new MatiereColor(getMatiere("Demarches exterieures",0),Color.GOLD);
		matCol[19] = new MatiereColor(getMatiere("Demarches exterieures",1),Color.GOLD);
		
		
		
		
	}

	
	public Matiere getMatiere(String strMat,int strSeq) {
		Matiere[] matieres = instance.getMatieres();
		for(Matiere mat : matieres) {
			if(strMat.equals(mat.getSubject()) && strSeq==(mat.getSequence())) return mat;
		}
		return null;
	}
}
