package bot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;


public class HTTPSocket extends HTTP{

	@Override
	HTTP copy() {
		// TODO Auto-generated method stub
		return new HTTPSocket();
	}

	@Override
	synchronized public void lowLevelSend(String url, String post) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		String command;
		StringBuffer headers;
		byte buffer[] = new byte[1024];
		int l, i;
		int port = 80;
		Attribute a;
		boolean https = false;
		URL u;
		Socket socket = null;
		OutputStream out = null;
		InputStream in = null;
		
		try {
			if(url.toLowerCase().startsWith("https")){
				url = "http" + url.substring(5);
				u = new URL(url);
				if(u.getPort() == -1){
					port = 443;
				}
				https = true;
			}else{
				u = new URL(url);
			}
			
			if(u.getPort() != -1){
				port = u.getPort();
			}
			
			socket = SocketFactory.getSocket(u.getHost(), port, https);
			
			socket.setSoTimeout(timeout);
			out = socket.getOutputStream();
			in = socket.getInputStream();
			
			if(post == null){
				command = "GET ";
			}else{
				command = "POST ";
			}
			
			String file = u.getFile();
			if(file.length() == 0){
				file = "/";
			}
			
			if(SocketFactory.useProxy()){
				addProxyAuthHeader();
				if(port != 80){
					file = "http://" + u.getHost() + ":" + port + file;
				}else{
					file = "http://" + u.getHost() + file;
				}
			}
			
			command = command + file + " HTTP/1.0";
			SocketFactory.writeString(out, command);
			Log.log(Log.LOG_LEVEL_NORMAL, "Request: " + command);
			
			if(post != null){
				clientHeaders.set("Content-Length", "" + post.length());
			}
			
			clientHeaders.set("Host", u.getHost());
			
			i = 0;
			headers = new StringBuffer();
			do{
				a = clientHeaders.get(i++);
				if(a != null){
					headers.append(a.getName());
					headers.append(": ");
					headers.append(a.getValue());
					headers.append("\r\n");
					Log.log(Log.LOG_LEVEL_TRACE, "Client Header:" + a.getName() + "=" + a.getValue());
				}
			}while(a != null);
			Log.log(Log.LOG_LEVEL_DUMP, "Writing client header:" + headers.toString());
			if(headers.length() > 0){
				out.write(headers.toString().getBytes());
			}
			
			SocketFactory.writeString(out, "");
			if(post != null){
				Log.log(Log.LOG_LEVEL_TRACE, "Socket post(" + post.length() + " bytes):" + new String(post));
				out.write(post.getBytes());
			}
			
			header.setLength(0);
			int chars = 0;
			boolean done = false;
			
			while(!done){
				int ch;
				ch = in.read();
				if(ch == -1){
					done = true;
				}
				
				switch(ch){
				case '\r':
					break;
				case '\n':
					if(chars == 0){
						done = true;
					}
					chars = 0;
					break;
				default:
					chars++;
					break;
				}
				header.append((char)ch);
			}
			parseHeaders();
			Attribute acl = serverHeaders.get("Content-length");
			int contentLength = 0;
			try {
				if(acl != null){
					contentLength = Integer.parseInt(acl.getValue());
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.logException("Bad value for content-length:", e);
			}
			
			int max;
			if(maxBodySize != -1){
				max = Math.min(maxBodySize, contentLength);
			}else{
				max = contentLength;
			}
			
			if(max < 1){
				max = -1;
			}
			
			ByteList byteList = new ByteList();
			byteList.read(in, max);
			body = byteList.detach();
			Log.log(Log.LOG_LEVEL_DUMP, "Socket Page Back:" + new String(body) + "\r\n");
			if((err>=400) && (err <= 599)){
				Log.log(Log.LOG_LEVEL_ERROR, "HTTP Exception:" + response);
				throw new HTTPException(response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(in != null){
				try {
					in.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(socket != null){
				try {
					socket.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		
	}

	protected void addProxyAuthHeader(){
		if((SocketFactory.getProxyUID() != null) && (SocketFactory.getProxyUID().length() > 0)){
			String hdr = SocketFactory.getProxyUID() + ":" + SocketFactory.getProxyPWD()==null?"":SocketFactory.getProxyPWD();
			String encode = URLUtility.base64Encode(hdr);
			clientHeaders.set("Proxy-Authorization", "Basic " + encode);
		}
	}
}
