package bot;

import java.io.IOException;

public class HTTPException extends IOException{
	
	public HTTPException(){
		
	}
	
	public HTTPException(String msg){
		super(msg);
	}
}
