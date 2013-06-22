import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SoundHTML extends HTML {
	
	public SoundHTML(String AREA, int PAGENUM) {
		super(AREA, PAGENUM);
		setSITE_URL("https://soundcloud.com/");
		setCOMPLETE_URL(getSITE_URL() + getAREA() + "?format=html&page=" + getPAGENUM() + "/");
	}
	
	public String getDoc() throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		System.out.println("Scraping... (SoundCloud takes a while)");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        
        HtmlPage page = webClient.getPage(getCOMPLETE_URL());
		String doc = page.getWebResponse().getContentAsString();
		webClient.closeAllWindows();
		
		return doc;
	}
	
}
