package two;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ClientWithResponseHandler {
	public static void main(String []argv) throws IOException{
		CloseableHttpClient client = HttpClients.createDefault();
		try{
			HttpGet get = new HttpGet("http://localhost:8080");
			System.out.println("Execute request: " + get.getRequestLine());
			
//			ResponseHandler<String> responseHandler = new ResponseHandler<String>(){
//				public String handleResponse(final HttpResponse response) throws IOException{
//					int statuscode = response.getStatusLine().getStatusCode();
//					if(statuscode >= 200 && statuscode < 300){
//						HttpEntity entity = response.getEntity();
//						return entity != null ? EntityUtils.toString(entity) : null;
//					}else {
//						throw new ClientProtocolException("unexcepted response status: " + statuscode);
//					}
//				}
//			};
//			String responseBody = client.execute(get, responseHandler);
			CloseableHttpResponse response = client.execute(get);
			
			try {
				System.out.println("-----------");
				System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				
				if(entity != null){
					InputStream instream = entity.getContent();
					try{
						instream.read();
					}finally{
						instream.close();
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				response.close();
			}
//			System.out.println(responseBody);
		}finally{
			client.close();
		}
	}
}
