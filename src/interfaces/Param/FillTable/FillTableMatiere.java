package interfaces.Param.FillTable;

import java.util.ArrayList;

public class FillTableMatiere {

	private String subject;
	private String natures ;
	private String groupes;
	
	public FillTableMatiere(String subject, ArrayList<String> natures,ArrayList<Character> groupes) {
		this.setGroupes(groupes);
		this.setNatures(natures);
		this.setSubject(subject);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getNatures() {
		return natures;
	}

	public void setNatures(ArrayList<String> natures) {
		String str="";
		for(String nat : natures) {
			str=str+""+nat+" , ";
		}
		if(str.length()>2) this.natures= str.substring(0,str.length()-2);
		else this.natures = "-";
	}

	public String getGroupes() {
		return groupes;
	}

	public void setGroupes(ArrayList<Character> groupes) {
		String str="";
		for(Character nat : groupes) {
			str=str+""+nat+" , ";
		}
		if(str.length()>2) this.groupes= str.substring(0,str.length()-2);
		else this.groupes = "-";
	}

	
}
