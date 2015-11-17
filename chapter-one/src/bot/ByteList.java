package bot;

import java.io.IOException;
import java.io.InputStream;



public class ByteList {
	public static final int INITIAL_SIZE = 32768;
	
	private byte buffer[] = new byte[INITIAL_SIZE];
	
	private int index = 0;
	
	public byte[] detach(){
		byte result[] = new byte[index];
		System.arraycopy(buffer, 0, result, 0, index);
		buffer = null;
		index = 0;
		return result;
	}
	
	private int capacity(){
		return (buffer.length - index);
	}
	
	private void extend(){
		byte newBuffer[] = new byte[buffer.length * 2];
		System.arraycopy(buffer, 0, newBuffer, 0, index);
		buffer = newBuffer;
	}
	
	public void append(byte buffer[]){
		System.arraycopy(buffer, 0, this.buffer, index, buffer.length);
	}
	
	public void read(InputStream in, int max) throws IOException{
		long l = 0;
		do{
			int size;
			if(max != -1){
				size = Math.min(max, capacity());
			}else{
				size = capacity();
			}
			
			l = in.read(buffer, index, size);
			
			if(l < 0){
				break;
			}
			
			index += l;
			
			if(capacity() < 10){
				extend();
			}
			
			if(max != -1){
				max -= l;
				if(max <= 0){
					break;
				}
			}
		}while(l != 0);
	}
}
