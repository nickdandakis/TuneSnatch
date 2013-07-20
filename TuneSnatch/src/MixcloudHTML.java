import java.io.IOException;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/*
 * Mixcloud area definition:
 * just <USERNAME> for cloudcasts (uploaded tracks)
 * OR
 * <USERNAME>/<SUB_AREA>
 * where <SUB_AREA> == (activity || listens || favorites)
 */
public class MixcloudHTML extends HTML {

	private static final long serialVersionUID = 5416179481396296124L;
	private static final boolean debug = false;	// Toggles ability to save HTML document
	
	public MixcloudHTML(String complete_url){
		super(complete_url);
	}
	
	public MixcloudHTML(String AREA, int PAGENUM) {
		super(AREA, PAGENUM);
		setSITE_URL("http://mixcloud.com/");
		setCOMPLETE_URL(getSITE_URL() + getAREA() + "?page=" + getPAGENUM() + "/");
	}
	
	public Document getDocument() throws IOException{
		System.out.println("Scraping Mixcloud...");
		Response res = Jsoup.connect(getCOMPLETE_URL()).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0").execute();
		Document doc = res.parse();
		
		if(debug)
			saveDocument(doc);
		
		return doc;
	}
	
}