package one;

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

public class HtmlParserTool {
	public static Set<String> extracLinks(String url, LinkFilter filter) {
		Set<String> links = new HashSet<String>();

		try {
			Parser parser = new Parser(url);
			parser.setEncoding("gb2312");

			NodeFilter frameFilter = new NodeFilter() {
				public boolean accept(Node node) {
					if (node.getText().startsWith("frame src=")) {
						return true;
					}
					return false;
				}
			};

			OrFilter linkFilter = new OrFilter(new NodeClassFilter(LinkTag.class), frameFilter);
			NodeList list = parser.extractAllNodesThatMatch(linkFilter);
			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				if (tag instanceof LinkTag) {
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink();
					if (filter.accept(linkUrl)) {
						links.add(linkUrl);
					}
				} else {
					String frame = tag.getText();
					int start = frame.indexOf("src=");
					frame = frame.substring(start);
					int end = frame.indexOf(" ");
					if (end == -1) {
						end = frame.indexOf(">");
					}
					String frameUrl = frame.substring(5, end - 1);
					if (filter.accept(frameUrl)) {
						links.add(frameUrl);
					}
				}
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return links;

	}

	public static void main(String[] argv){
		Set<String> links = HtmlParserTool.extracLinks("http://localhost", 
				new LinkFilter(){
			public boolean accept(String url){
				if(url.startsWith("http://localhost") || 
					url.startsWith("http://127.0.0.1")){
			return true;
		}else{
			return false;
		}
			}
		});
		
		for(String link : links){
			System.out.println(link);
		}
	}
}
