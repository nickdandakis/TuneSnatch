package io.phalanx.Logic.Scraping;


import io.phalanx.Logic.Site;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HypeMachineHTML extends HTML {

	private static final long serialVersionUID = 106075426457023929L;
	private Map<String, String> cookies;
	private static final boolean debug = false; // Toggles ability to save HTML document
	
	public HypeMachineHTML(String completeURL) throws HttpStatusException{
		super(completeURL);
		
		try {
			this.setDocument(createDocument());
		} catch (IOException e) {
			System.out.println("Failed to parse HTML.");
			e.printStackTrace();
		}
	}
	
	public HypeMachineHTML(String area, int pagenumber) throws HttpStatusException{
		super(area, pagenumber);
		setSite(Site.HypeMachine);
		setCompleteURL(getSite().getURL() + getArea() + "/" + getPagenumber() + "/");
		
		try {
			this.setDocument(createDocument());
		} catch (IOException e) {
			System.out.println("Failed to parse HTML.");
			e.printStackTrace();
		}
	}
	
	public void setCookies(Map<String, String> cookies){
		this.cookies = cookies;
	}
	
	public Map<String,String> getCookies(){
		return cookies;
	}
	
	public Document createDocument() throws IOException, HttpStatusException{
		System.out.println("Working on HypeMachine...");
		Response res = Jsoup.connect(getCompleteURL()).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0").execute();
		setCookies(res.cookies());
		Document doc = res.parse();
		if(debug)
			saveDocument(doc);
		
		return doc;
	}

}
