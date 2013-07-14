import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HypeMachineHTML extends HTML {

	private static final long serialVersionUID = 106075426457023929L;
	private Map<String, String> COOKIES;
	
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
	
	public void saveDoc(){
		File htmlFile = new File("." + File.separator + "hypeMTest.html");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFile.getAbsoluteFile()));
			bw.write(getDoc().toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Document getDoc() throws IOException{
		System.out.println("Scraping... (SoundCloud takes a while)");
		Response res = Jsoup.connect(getCOMPLETE_URL()).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0").execute();
		setCOOKIES(res.cookies());
		
		return res.parse();
	}

}
