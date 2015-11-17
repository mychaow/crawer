package one;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class FileDownloader {
	public String getFileNameByUrl(String url, String contentType){
		url = url.substring(7);
		if(contentType.indexOf("html") != -1){
			url = url.replaceAll("[\\?/:*|.<>\"]", "_")+".html";
			return url;
		}else if(contentType.indexOf("plain") != -1){
			url = url.replaceAll("[\\?/:*|.<>\"]", "_")+".txt";
			return url;
		}else{
			return url.replaceAll("[\\?:*|.<>\"]", "_") + "." + 
					contentType.substring(contentType.lastIndexOf("/") + 1);
		}
	}
	
	private void saveToLocal(HttpEntity data, String filePath){
		try {
			DataOutputStream out = new DataOutputStream(new 
					FileOutputStream(new File(filePath)));
			DataInputStream in = new DataInputStream(data.getContent());
			byte[] c = new byte[200];
			while( (in.read(c)) != -1){
				out.write(c);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String downloadFile(String url) throws IOException{
		String filePath = null;
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpGet get = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(5000)
				.setConnectionRequestTimeout(5000)
				.setRedirectsEnabled(true)
				.build();
//		get.getParams().setParameter(Ht, arg1)
		try {
			response = client.execute(get);
			StatusLine statueline = response.getStatusLine();
			int statuecode = statueline.getStatusCode();
			if(statuecode != HttpStatus.SC_OK){
				System.err.println("Method failed:" + statueline);
				filePath =null;
			}
			HttpEntity responseBody = response.getEntity();
			filePath = "D:\\"+getFileNameByUrl(url, responseBody.getContentType().toString());
			saveToLocal(responseBody,filePath);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("please check your provided http address!");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			response.close();
		}
		return filePath;
	}
	
	public static void main(String[] argv) throws IOException{
		FileDownloader loader = new FileDownloader();
		loader.downloadFile("http://www.ibm.com/developerworks/cn/opensource/os-cn-crawler/");
	}
}
