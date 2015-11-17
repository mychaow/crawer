package bot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class SocketFactory {
	private static String proxyHost;
	private static int proxyPort;
	private static String proxyUID;
	private static String proxyPWD;
	public static String getProxyHost() {
		return proxyHost;
	}
	public static void setProxyHost(String proxyHost) {
		SocketFactory.proxyHost = proxyHost;
	}
	public static int getProxyPort() {
		return proxyPort;
	}
	public static void setProxyPort(int proxyPort) {
		SocketFactory.proxyPort = proxyPort;
	}
	public static String getProxyUID() {
		return proxyUID;
	}
	public static void setProxyUID(String proxyUID) {
		SocketFactory.proxyUID = proxyUID;
	}
	public static String getProxyPWD() {
		return proxyPWD;
	}
	public static void setProxyPWD(String proxyPWD) {
		SocketFactory.proxyPWD = proxyPWD;
	}
	
	public static boolean useProxy(){
		return ((proxyHost != null) && (proxyHost.length() > 0));
	}
	
	public static void writeString(OutputStream out, String str) throws IOException{
		out.write(str.getBytes());
		out.write("\r\n".getBytes());
		Log.log(Log.LOG_LEVEL_TRACE, "Socket Out:" + str);
	}
	
	public static String receive(InputStream in) throws IOException{
		String result = "";
		boolean done = false;
		
		while(!done){
			int ch = in.read();
			switch(ch){
			case 13:
				break;
			case 10:
				done = true;
				break;
			default:
				result += (char)ch;	
			}
		}
		return result;
	}
	
	public static Socket getSocket(String host, int port, boolean https) throws IOException{
		Socket socket = null;
		
		if(useProxy()){
			Log.log(Log.LOG_LEVEL_NORMAL, "Connection to:" + proxyHost + "(" + proxyPort + ")");
			try {
				socket = new Socket(proxyHost, proxyPort);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(https){
				OutputStream out = socket.getOutputStream();
				InputStream in = socket.getInputStream();
				
				String str = "Connect " + host + ":" + port + " HTTP/1.0";
				Log.log(Log.LOG_LEVEL_NORMAL, "Tunnel: " + str);
				writeString(out, str);
				str = "User-Agent: Java Bot Package";
				writeString(out, str);
				writeString(out, "");
				out.flush();
				
				do{
					str = receive(in);
					Log.log(Log.LOG_LEVEL_TRACE, "Tunnel handshake: " + str);
					
				}while(str.length() > 0);
				socket = SSL.getSSLSocket(host, port);
			}
		}else{
			if(https){
				socket = SSL.getSSLSocket(host, port);
			}else{
				socket = new Socket(host, port);
			}
		}
		return socket;
	}
}
