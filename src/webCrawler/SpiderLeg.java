package webCrawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg {
	private List<String> links = new LinkedList<String>(); // Just a list of URLs
	private Document htmlDocument; // This is our web page, or in other words, our document
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";;

	public boolean crawl(String url) { // Give it a URL and it makes an HTTP request for a web page
		try {
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;

			if (connection.response().statusCode() == 200) {// 200 is the HTTP OK status code
															// indicating that everything is great.
				SpiderTest.setResults("\n**Visiting** Received web page at " + url);
			}
			if (!connection.response().contentType().contains("text/html")) {
				SpiderTest.setResults("**Failure** Retrieved something other than HTML");
				return false;
			}
			Elements linksOnPage = htmlDocument.select("a[href]");
			SpiderTest.setResults("Found (" + linksOnPage.size() + ") links");
			SpiderTest.setResults("These are the links found\n" + linksOnPage);
			for (Element link : linksOnPage) {
				this.links.add(link.absUrl("href"));
			}
			for (int i=0; i < links.size(); i++) {
				SpiderTest.setResults("\n" + links.get(i));
			}
			return true;
		} catch (IOException ioe) {
			SpiderTest.setResults("HTTP request error" + ioe);
			return false;
		}
	}

	public boolean searchForWord(String searchWord) {
		if (this.htmlDocument == null) {
			SpiderTest.setResults("ERROR! Call crawl() before performing analysis on the document");
			return false;
		}
		SpiderTest.setResults("Searching for the word " + searchWord + "...");
		String bodyText = this.htmlDocument.body().text();
		return bodyText.toLowerCase().contains(searchWord.toLowerCase());
	}

	public List<String> getLinks() {
		return this.links;
	}
}
