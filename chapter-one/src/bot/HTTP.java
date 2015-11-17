package bot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.zip.GZIPInputStream;

import javax.xml.ws.http.HTTPException;

import org.omg.CORBA.portable.UnknownException;

public abstract class HTTP {
	protected byte body[];
	protected StringBuffer header = new StringBuffer();
	protected String url;
	protected boolean autoRedirect = true;
	protected AttributeList cookieStore = new AttributeList();
	protected AttributeList clientHeaders = new AttributeList();
	protected AttributeList serverHeaders = new AttributeList();
	
	protected boolean useCookies = false;
	protected boolean usePermCookies = false;
	
	protected String response;
	protected int timeout = 30000;
	protected String referrer = null;
	
	protected String agent = "Mozilla/4.0";
	
	protected String user = "";
	protected String password = "";
	protected int maxBodySize = -1;
	
	protected String rootURL;
	protected int err;
	
	abstract HTTP copy();
	
	/**
	 * @param url
	 * @param post
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	abstract protected void lowLevelSend(String url, String post)
		throws UnknownHostException, IOException;
	
	public void clearCookies(){
		cookieStore.clear();
	}
	
	protected void addCookieHeader(){
		int i = 0;
		String str = "";
		
		if(cookieStore.get(0) == null){
			return;
		}
		
		while(cookieStore.get(i) != null){
			CookieParse cookie;
			if(str.length() != 0){
				str += "; ";
			}
			cookie = (CookieParse)cookieStore.get(i);
			str += cookie.toString();
			i++;
		}
	}
	
	protected void addAuthHeader(){
		if((user.length() == 0) || (password.length() == 0)){
			return;
		}
		
		String hdr = user + ':' + password;
		String encode = URLUtility.base64Encode(hdr);
		
		clientHeaders.set("Authorization", "Basic " + encode);
	}
	
	public void send(String requestedURL, String post)
	throws HTTPException, UnknownHostException, IOException{
		int rtn;
		
		if(post == null){
			Log.log(Log.LOG_LEVEL_NORMAL, "HTTP GET " + requestedURL);
		}else{
			Log.log(Log.LOG_LEVEL_NORMAL, "HTTP POST " + requestedURL);
		}
		
		rootURL = requestedURL;
		setUrl(requestedURL);
		
		if(clientHeaders.get("referrer") == null){
			if(referrer != null){
				clientHeaders.set("referrer", referrer);
			}
		}
		
		if(clientHeaders.get("Accept") != null){
			clientHeaders.set("Accept", "image/gif," 
					+ "image/x-xbitmap, image/jpeg, "
					+ "image/pjpeg, application/vnd.ms-excel,"
					+ "application/msword,"
					+ "application/vnd.ms-powerpoint, */*");
		}
		
		if(clientHeaders.get("Accept-Language") == null){
			clientHeaders.set("Accept-Language", "en-us");
		}
		if(clientHeaders.get("User-Agent") == null){
			clientHeaders.set("User-Agent", agent);
		}
		
		while(true){
			if(useCookies){
				addCookieHeader();
			}
			addAuthHeader();
			lowLevelSend(this.url, post);
			
			if(useCookies){
				parseCookies();
			}
			
			Attribute encoding = serverHeaders.get("Content-Encoding");
			
			if(encoding != null && "gzip".equalsIgnoreCase(encoding.getValue())){
				processGZIP();
			}
			
			Attribute a = serverHeaders.get("Location");
			
			if((a == null) || !autoRedirect){
				referrer = getUrl();
				return;
			}
			
			URL u = new URL(new URL(url), a.getValue());
			
			Log.log(Log.LOG_LEVEL_NORMAL, "HTTP REDIRECT to " + u.toString());
			setUrl(u.toString());
		}
	}
	
	public String getBody(){
		return new String(body);
	}
	
	public String getBody(String enc) throws UnsupportedEncodingException{
		return new String(body, enc);
	}
	
	public byte[] getBodyBytes(){
		return body;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getMaxBodySize() {
		return maxBodySize;
	}

	public void setMaxBodySize(int maxBodySize) {
		this.maxBodySize = maxBodySize;
	}
	
	public void SetAutoRedirect(boolean b){
		autoRedirect = b;
	}
	
	public AttributeList getClientHeaders(){
		return clientHeaders;
	}
	
	public AttributeList getServerHeaders(){
		return serverHeaders;
	}
	
	protected void parseCookies(){
		Attribute a;
		int i = 0;
		
		while((a = serverHeaders.get(i++)) != null){
			if(a.getName().equalsIgnoreCase("set-cookie")){
				CookieParse cookie = new CookieParse();
				cookie.source = new StringBuffer(a.getValue());
				
				cookie.get();
				cookie.setName(cookie.get(0).getName());
				
				if(cookieStore.get(cookie.get(0).getName()) == null){
					if(cookie.get("expires") == null ||
							(cookie.get("expires") != null) && useCookies){
						cookieStore.add(cookie);
					}
				}
				Log.log(Log.LOG_LEVEL_NORMAL, "Got cookie: " + cookie.toString());
			}
		}
	}
	
	protected void processGZIP() throws IOException{
		ByteArrayInputStream bis = new ByteArrayInputStream(body);
		GZIPInputStream is = new GZIPInputStream(bis);
		
		ByteList bl = new ByteList();
		bl.read(is, -1);
		body = bl.detach();
	}
	
	public CookieParse getCookie(String name){
		return (CookieParse)cookieStore.get(name);
	}
	
	public void setUseCookies(boolean session, boolean perm){
		useCookies = session;
		usePermCookies = perm;
	}
	
	public boolean getUseCookies(){
		return useCookies;
	}
	
	public boolean getPerminantCookies(){
		return usePermCookies;
	}
	
	protected void processResponse(String name){
		int l1, l2;
		
		response = name;
		l1 = response.indexOf(' ');
		if(l1 != -1){
			l2 = response.indexOf(' ');
			if(l2 != -1){
				try {
					err = Integer.parseInt(response.substring(l1+1, l2));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Log.log(Log.LOG_LEVEL_TRACE, "Response : " + name);
	}
	
	protected void parseHeaders(){
		boolean first;
		String name, value;
		int l;
		first = true;
		
		serverHeaders.clear();
		name = "";
		
		String parse = new String(header);
		
		for(l = 0;l < parse.length();l++){
			if(parse.charAt(l) == '\n'){
				if(name.length() == 0){
					return;
				}else{
					if(first){
						first = false;
						processResponse(name);
					}else{
						int ptr = name.indexOf(':');
						if(ptr != -1){
							value = name.substring(ptr+1).trim();
							name = name.substring(0, ptr);
							Attribute a = new Attribute(name, value);
							serverHeaders.add(a);
							Log.log(Log.LOG_LEVEL_TRACE, "ServerHeader:" +
							name + '=' + value);
						}
					}
				}
				name = "";
			}else if(parse.charAt(l) != '\r'){
				name += parse.charAt(l);
			}
		}
		
		
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public void setUseCookies(boolean useCookies) {
		this.useCookies = useCookies;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public AttributeList getCookies(){
		return cookieStore;
	}
	
	public String getRootUrl(){
		return rootURL;
	}
	
	public String getContentType(){
		Attribute a = serverHeaders.get("Content-Type");
		if(a == null){
			return null;
		}else{
			return a.getValue();
		}
	}
	
}
