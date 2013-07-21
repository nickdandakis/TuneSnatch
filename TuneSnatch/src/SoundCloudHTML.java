import java.io.IOException;
import java.net.MalformedURLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SoundCloudHTML extends HTML {

	private static final long serialVersionUID = 5589093030331078854L;
	private static final boolean debug = false; // Toggles ability to save HTML document

	public SoundCloudHTML(String completeURL){
		super(completeURL);
	}
	
	public SoundCloudHTML(String area, int pagenumber) {
		super(area, pagenumber);
		setSite("https://soundcloud.com/");
		setCompleteURL(getSite() + getArea() + "?format=html&page=" + getPagenumber() + "/");
	}
	
	public Document getDocument() throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		System.out.println("Scraping SoundCloud... (SoundCloud takes a while)");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        
        HtmlPage page = webClient.getPage(getCompleteURL());
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		webClient.closeAllWindows();
		
		if(debug)
			saveDocument(doc);
		
		return doc;
	}
	
}
