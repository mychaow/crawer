package bot;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Provider;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSL {
	private SSL(){
		
	}
	
	protected static SSLSocketFactory factory = null;
	
	static public Socket getSSLSocket(String host, int port) throws UnknownHostException, IOException{
		obtainFactory();
		SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
		return socket;
	}
	
	static private void obtainFactory(){
		if(factory == null){
			factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
		}
	}
	
	static public Socket getSSLSocket(Socket s, String host, int port) throws IOException{
		obtainFactory();
		SSLSocket socket = (SSLSocket)factory.createSocket(s, host, port, true);
		return socket;
	}
}	
