package bot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

public class Log {
	public final static int LOG_LEVEL_DUMP = 1;
	public final static int LOG_LEVEL_TRACE = 2;
	public final static int LOG_LEVEL_NORMAL = 3;
	public final static int LOG_LEVEL_ERROR = 4;
	public final static int LOG_LEVEL_NONE = 5;
	
	protected static boolean log2Console = true;
	protected static boolean log2File = false;
	
	protected static String path = "." + File.pathSeparator + "log.txt";
	
	protected static int level = LOG_LEVEL_NONE;
	
	private Log(){};
	
	static public void setLevel(int lev){
		switch(lev){
		case LOG_LEVEL_DUMP:
		case LOG_LEVEL_TRACE:
		case LOG_LEVEL_NORMAL:
		case LOG_LEVEL_ERROR:
		case LOG_LEVEL_NONE:
			level = lev;break;
		default:
			level = LOG_LEVEL_NORMAL;
		}
	}
	
	static public int getLevel(){
		return level;
	}

	public static boolean isLog2Console() {
		return log2Console;
	}

	public static void setLog2Console(boolean log2Console) {
		Log.log2Console = log2Console;
	}

	public static boolean isLog2File() {
		return log2File;
	}

	public static void setLog2File(boolean log2File) {
		Log.log2File = log2File;
	}

	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		Log.path = path;
	}
	
	
	static public void logException(String event, Exception e){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bos);
		e.printStackTrace(ps);
		ps.close();
		log(LOG_LEVEL_ERROR, event + e + ":" + bos);
		
		try {
			bos.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	synchronized static public void log(int level, String event){
		if(level == LOG_LEVEL_NONE){
			return;
		}
		if(level < Log.level){
			return;
		}
		
		Date dt = new Date();
		String log = "[" + dt.toString() + "] [";
		
		switch(level){
		case LOG_LEVEL_DUMP: log+= "DUMP";break;
		case LOG_LEVEL_ERROR: log += "ERROR";break;
		case LOG_LEVEL_NONE: log += "NONE?";break;
		case LOG_LEVEL_NORMAL: log += "NORMAL";break;
		case LOG_LEVEL_TRACE: log += "TRACE";break;
		}
		log += "][" + Thread.currentThread().getName() + "]" + event;
		
		if(log2Console){
			System.out.println(log);
		}
		if(log2File){
			try {
				FileOutputStream fw = new FileOutputStream(path, true);
				PrintStream ps = new PrintStream(fw);
				ps.println(log);
				ps.close();
				fw.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
