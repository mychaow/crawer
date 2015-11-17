package one;

import java.io.IOException;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;

public class RetrievePage {
	public static void main(String[] argv) throws UnsupportedOperationException, IOException{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet get = new HttpGet("http://www.ctrip.com/");
		CloseableHttpResponse response1 = null;
		try {
			response1 = httpclient.execute(get);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(response1.toString());
		System.out.println(response1.getStatusLine());
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1,
				HttpStatus.SC_OK, "ok");
		response.addHeader("Set-cookie", "c1=a; path=/; domain=localhost");
		response.addHeader("Set-cookie", "c2=b; path=\"/\", b3=c; domain=\"localhost\"");
		
		HeaderElementIterator it = new BasicHeaderElementIterator(
				response.headerIterator("Set-cookie"));
		while(it.hasNext()){
			HeaderElement elem = it.nextElement();
			System.out.println(elem.getName() + " = " + elem.getValue());
			NameValuePair[] params = elem.getParameters();
			for(int i = 0;i < params.length;i++){
				System.out.println("$" + params[i]);
			}
		}
		
		
	}
}
