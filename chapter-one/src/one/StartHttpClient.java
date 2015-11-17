package one;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class StartHttpClient {
	public static void main(String[] argv) throws IOException{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet("http://localhost:8080");
		CloseableHttpResponse response = null;
		try {
			response = client.execute(get);
			System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			EntityUtils.consume(entity);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			response.close();
		}
		
		HttpPost post = new HttpPost("http://localhost:8080/manager");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		nvps.add(new BasicNameValuePair("username", "admin"));
		nvps.add(new BasicNameValuePair("password", "123"));
		post.setEntity(new UrlEncodedFormEntity(nvps));
		
		CloseableHttpResponse response1 = client.execute(post);
		
		try{
			System.out.println(response1.getStatusLine());
			HttpEntity entity1 = response1.getEntity();
			EntityUtils.consume(entity1);
		}finally{
			response1.close();
		}
		client.close();
	}
}
