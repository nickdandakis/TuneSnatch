import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HypeMachineHTML extends HTML {

	private static final long serialVersionUID = 106075426457023929L;
	private Map<String, String> COOKIES;
	private static final boolean DEBUG = true; // Toggles ability to save HTML document
	
	public HypeMachineHTML(String complete_url){
		super(complete_url);
	}
	
	public HypeMachineHTML(String area, int pagenum){
		super(area, pagenum);
		setSITE_URL("http://hypem.com/");
		setCOMPLETE_URL(getSITE_URL() + getAREA() + "/" + getPAGENUM() + "/");
	}
	
	public void setCOOKIES(Map<String, String> COOKIES){
		this.COOKIES = COOKIES;
	}
	
	public Map<String,String> getCOOKIES(){
		return COOKIES;
	}
	
	public Document getDocument() throws IOException{
		System.out.println("Scraping HypeMachine...");
		Response res = Jsoup.connect(getCOMPLETE_URL()).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0").execute();
		setCOOKIES(res.cookies());
		Document doc = res.parse();
		
		if(DEBUG)
			saveDocument(doc);
		
		return doc;
	}

}
