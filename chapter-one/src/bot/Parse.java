package bot;

public class Parse extends AttributeList{
	public StringBuffer source;
	protected int idx;
	protected char parseDelim;
	protected String parseName;
	protected String parseValue;
	
	public String tag;
	
	public static boolean isWhiteSpace(char ch){
		return ("\t\n\r ".indexOf(ch) != -1);
	}
	
	public void eatWhiteSpace(){
		while(!eof()){
			if(!isWhiteSpace(source.charAt(idx))){
				return;
			}
			idx++;
		}
	}
	
	public boolean eof(){
		return (idx > source.length());
	}
	
	public void parseAttributeName(){
		eatWhiteSpace();
		
		if(source.charAt(idx) == '\'' || source.charAt(idx) == '\"'){
			parseDelim = source.charAt(idx);
			idx++;
			while(source.charAt(idx) != parseDelim){
				parseName += source.charAt(idx);
				idx++;
			}
			idx++;
		}else{
			while(!eof()){
				if(isWhiteSpace(source.charAt(idx)) ||
					(source.charAt(idx) == '=') ||
					(source.charAt(idx) == '>')){
					break;
				}
				parseName += source.charAt(idx);
				idx++;
			}
		}
		eatWhiteSpace();
	}
	
	public void parseAttributeValue(){
		if(parseDelim != 0){
			return;
		}
		
		if(source.charAt(idx) == '='){
			idx++;
			eatWhiteSpace();
			if(source.charAt(idx) == '\'' || source.charAt(idx) == '\"'){
				parseDelim = source.charAt(idx);
				idx++;
				while(source.charAt(idx) != parseDelim){
					parseValue += source.charAt(idx);
					idx++;
				}
				idx++;
			}else{
				while(!eof() &&
						!isWhiteSpace(source.charAt(idx)) &&
						(source.charAt(idx) != '>')){
					parseValue += source.charAt(idx);
					idx++;
				}
			}
			eatWhiteSpace();
		}
	}
	
	protected void addAttribute(){
		Attribute a = new Attribute(parseName, parseValue, parseDelim);
		add(a);
	}

	public char getParseDelim() {
		return parseDelim;
	}

	public void setParseDelim(char parseDelim) {
		this.parseDelim = parseDelim;
	}

	public String getParseName() {
		return parseName;
	}

	public void setParseName(String parseName) {
		this.parseName = parseName;
	}

	public String getParseValue() {
		return parseValue;
	}

	public void setParseValue(String parseValue) {
		this.parseValue = parseValue;
	}
	
	
}
