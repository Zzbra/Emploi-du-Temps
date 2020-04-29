package application;

public class Horaire {
	
	private String name;
	private int starttime;
	private int endtime;
	
	public Horaire(String name, int start, int end) {
		this.setName(name);
		this.setStarttime(start);
		this.setEndtime(end);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStarttime() {
		return starttime;
	}

	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	

}
