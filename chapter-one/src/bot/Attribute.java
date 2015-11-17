package bot;

public class Attribute implements Cloneable{
	private String name;
	private String value;
	private char delim;
	
	public Object clone(){
		return new Attribute(name, value, delim);
	}
	
	public Attribute(String name, String value, char delim){
		this.name = name;
		this.value = value;
		this.delim = delim;
	}
	
	public Attribute(){
		this("", "", (char)0);
	}
	
	public Attribute(String name, String value){
		this("", "", (char)0);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public char getDelim() {
		return delim;
	}

	public void setDelim(char delim) {
		this.delim = delim;
	}
	
	
}
