package interfaces;

import application.Solveur;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ResourcePanel {

	public ResourcePanel(Group root,MainInterface main,Solveur solveur) {
		Rectangle rect = new Rectangle(main.getWIDTH()/7,main.getHEIGHT()-60);
		rect.setFill(Color.rgb(61,105,126));
		

		Label ressource = new Label("LISTE DES RESSOURCES");
		AnchorPane.setLeftAnchor(ressource, 0.0);
		AnchorPane.setRightAnchor(ressource, 0.0);
		ressource.setAlignment(Pos.CENTER);
		ressource.setTextFill(Color.WHITE);
		
		
		new SizeDisplayOnScene(main,rect,ressource);
		
		
		AnchorPane anchor = new AnchorPane(rect,ressource);
		root.getChildren().add(anchor);
		
		
	}

}
