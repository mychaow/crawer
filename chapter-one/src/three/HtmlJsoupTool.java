package three;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import one.LinkFilter;

public class HtmlJsoupTool {
	public static Set<String> extractLinks(String url, LinkFilter filter){
		Set<String> links = new HashSet<String>();
		
		try {
			Document doc = Jsoup.connect(url).get();
//			doc.charset(Charset.forName("gb2312"));
//			Element content = doc.getElementById("content");
//			Elements alinkURLs = doc.select("a[href]");
//			Elements frameLinkURLs = doc.select("frame[src]");
			Elements alinkURLs = doc.getElementsByTag("a");
			
			String urlstring = null;
			for(Element e : alinkURLs){
				urlstring = e.attr("abs:href");
				if(filter.accept(urlstring)){
					links.add(urlstring);
				}
			}
			
//			for(Element e : frameLinkURLs){
//				urlstring = e.attr("abs:src");
//				if(filter.accept(urlstring)){
//					links.add(urlstring);
//				}
//			}
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return links;
	}
}
