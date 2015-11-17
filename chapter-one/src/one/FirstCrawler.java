package one;

import java.io.IOException;

public class FirstCrawler {
	public static void main(String[] argv) throws IOException{
		Crawler craw = new Crawler();
		String[] seeds = {"http://localhost:8080/"};
		craw.crawling(seeds);
	}
}
