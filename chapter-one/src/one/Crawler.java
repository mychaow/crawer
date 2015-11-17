package one;

import java.io.IOException;
import java.util.Set;

import three.HtmlJsoupTool;

public class Crawler {
	private void initCrawlerWithSeeds(String[] seeds){
		for(int i = 0;i < seeds.length;i++){
			LinkDB.addUnVisitedUrl(seeds[i]);
		}
	}
	
	public void crawling(String[] seeds) throws IOException{
		LinkFilter filter = new LinkFilter(){
			public boolean accept(String url){
				if(url.startsWith("http://localhost") ||
				   url.startsWith("http://127.0.0.1")){
					return true;
				}else{
					return false;
				}
			}
		};
		initCrawlerWithSeeds(seeds);
		
		while(!LinkDB.unVisitedUrlsEmpty() && LinkDB.getVisitedUrlNum() <= 1000){
			String visitUrl = LinkDB.unVisitedUrlDeQueue();
			if(visitUrl == null){
				continue;
			}
			FileDownloader downLoader = new FileDownloader();
			downLoader.downloadFile(visitUrl);
			LinkDB.addVisitedUrl(visitUrl);
			Set<String> links = HtmlJsoupTool.extractLinks(visitUrl, filter);
					//HtmlParserTool.extracLinks(visitUrl, filter);
			for(String link : links){
				LinkDB.addUnVisitedUrl(link);
			}
		}
	}
	
	public static void main(String[] argv) throws IOException{
		Crawler crawler = new Crawler();
		String[] seeds = {"http://localhost:8080/"};
		crawler.crawling(seeds);
	}
}
