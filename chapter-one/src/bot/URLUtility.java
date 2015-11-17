package bot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class URLUtility {
	public static String indexFile="index.html";

	private URLUtility() {

	}

	static public URL stripQuery(URL url)
	throws MalformedURLException{
		String file = url.getFile();
		int i = file.indexOf('?');
		if(i == -1){
			return url;
		}
		file = file.substring(0, i);
		return new URL(url.getProtocol(), url.getHost(),
				url.getPort(), file);
	}
	
	static public URL stripAnchor(URL url)
	throws MalformedURLException{
		String file = url.getFile();
		return new URL(url.getProtocol(),url.getHost(),
				url.getPort(), file);
	}
	
	/**
	 * @param s
	 * @return
	 */
	static public String base64Encode(String s){
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		
		Base64OutputStream out = new Base64OutputStream(bout);
		
		try {
			out.write(s.getBytes());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bout.toString();
	}
	
	static public String ResolveBase(String base, String rel){
		String protocol;
		int i = base.indexOf(':');
		
		if(i != -1){
			protocol = base.substring(0, i+1);
			base = "http:" + base.substring(i+1);
		}else{
			protocol = null;
		}
		
		URL url;
		
		try {
			url = new URL(new URL(base), rel);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
		if(protocol != null){
			base = url.toString();
			i = base.indexOf(':');
			if(i != -1){
				base = base.substring(i+1);
			}
			base = protocol + base;
			return base;
		}
		return url.toString();
		
	}
	
	public static String convertFilename(String base, String path){
		return convertFilename(base, path, true);
	}
	
	public static String convertFilename(String base, String path, boolean mkdir){
		String result = base;
		
		int index1;
		int index2;
		
		if(result.charAt(result.length() - 1) != File.separatorChar){
			result += File.separator;
		}
		int queryIndex = path.indexOf("?");
		
		if(queryIndex != -1){
			path = path.substring(0, queryIndex);
		}
		
		int lastSlash = path.lastIndexOf(File.separatorChar);
		int lastDot = path.indexOf('.');
		
		if(path.charAt(path.length() - 1) != '/'){
			if(lastSlash > lastDot){
				path += "/" + indexFile;
			}
		}
		
		lastSlash = path.lastIndexOf('/');
		String filename = "";
		
		if(lastSlash != -1){
			filename = path.substring(1+lastSlash);
			path = path.substring(0, 1+lastSlash);
			
			if(filename == ""){
				filename = indexFile;
			}
		}
		
		index1 = 1;
		do{
			index2 = path.indexOf('/', index1);
			if(index2 != -1){
				String dirpart = path.substring(index1, index2);
				result += dirpart;
				result += File.separator;
				
				if(mkdir){
					File f = new File(result);
					f.mkdir();
				}
				index1 = index2 + 1;
			}
		}while(index2 != -1);
		
		result += filename;
		return result;
	}
}
