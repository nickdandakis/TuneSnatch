import java.io.IOException;
import java.io.Serializable;

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
public class MixcloudHTML extends HTML implements Serializable{

	private static final long serialVersionUID = 5416179481396296124L;
	private static final boolean debug = false;	// Toggles ability to save HTML document
	
	public MixcloudHTML(String completeURL){
		super(completeURL);
		
		try {
			this.setDocument(createDocument());
		} catch (IOException e) {
			System.out.println("Failed to parse HTML.");
			e.printStackTrace();
		}
	}
	
	public MixcloudHTML(String area, int pagenumber) {
		super(area, pagenumber);
		setSite("http://mixcloud.com/");
		setCompleteURL(getSite() + getArea() + "?page=" + getPagenumber() + "/");
		
		try {
			this.setDocument(createDocument());
		} catch (IOException e) {
			System.out.println("Failed to parse HTML.");
			e.printStackTrace();
		}
	}
	
	private Document createDocument() throws IOException{
		System.out.println("Working on Mixcloud...");
		Response res = Jsoup.connect(getCompleteURL()).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0").execute();
		Document doc = res.parse();
		
		if(debug)
			saveDocument(doc);
		
		return doc;
	}
	
}