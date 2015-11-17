package bot;

public class CookieParse extends Parse{
	
	public void parseAttributeValue(){
		eatWhiteSpace();
		
		if(eof()){
			return;
		}
		
		if(source.charAt(idx) == '='){
			idx++;
			eatWhiteSpace();
			if((source.charAt(idx) == '\'') ||
				(source.charAt(idx) == '\"')){
					parseDelim = source.charAt(idx);
					idx++;
					while(source.charAt(idx) != parseDelim){
						parseValue += source.charAt(idx);
						idx++;
					}
					idx++;
			}else{
				while(!eof() && (source.charAt(idx) != ';')){
					parseValue += source.charAt(idx);
					idx++;
				}
			}
			eatWhiteSpace();
		}
	}
	
	public boolean get(){
		while(!eof()){
			parseName = "";
			parseValue = "";
			
			parseAttributeName();
			
			if( !eof() && (source.charAt(idx) == ';')){
				addAttribute();
				break;
			}
			
			parseAttributeValue();
			addAttribute();
			
			eatWhiteSpace();
			
			while(!eof()){
				if(source.charAt(idx++) == ';'){
					break;
				}
			}
		}
		idx++;
		return false;
	}
	
	public String toString(){
		String str;
		str = get(0).getName();
		str += "=";
		str += get(0).getValue();
		return str;
	}
}
