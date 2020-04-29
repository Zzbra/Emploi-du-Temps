package interfaces.ColorMat;



import application.Matiere;
import javafx.scene.paint.Color;


public class MatiereColor {
	private Matiere matiere;
	private Color color;
	
	public MatiereColor(Matiere matiere,Color color) {
		
		this.matiere=matiere;
		this.color=color;
	}
	
	
	public Color getColor() {
		return this.color;
	}
	
	public Matiere getMatiere() {
		return this.matiere;
	}
	
	
	

}
