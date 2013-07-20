import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.nodes.Document;


public class HTML implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7381759251822881043L;
	private String site;
	private String area;
	private int pagenumber;
	private String completeURL;
	
	public HTML(String area, int pagenumber) {
		setArea(area);
		setPagenumber(pagenumber);
	}
	
	public HTML(String completeURL){
		setCompleteURL(completeURL);
		breakdownCompleteURL(this.completeURL);
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getPagenumber() {
		return pagenumber;
	}

	public void setPagenumber(int pagenumber) {
		this.pagenumber = pagenumber;
	}

	public String getCompleteURL() {
		return completeURL;
	}

	public void setCompleteURL(String completeURL) {
		this.completeURL = completeURL;
	}

	private void breakdownCompleteURL(String completeURL) {
		setSite(completeURL.replaceAll(".com/.+", ".com/"));
		setArea(completeURL.replaceAll(".+.com/", ""));
		if(area.endsWith("/"))
			area = area.substring(0, area.length()-1);
	}

	public void saveDocument(Document document){
		File htmlFile = new File("." + File.separator + getArea().replace("/", "-") + "-p" + getPagenumber() + ".html");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFile.getAbsoluteFile()));
			bw.write(document.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Object getDocument() throws IOException {
		return null;
	}
	
	@Override
	public String toString(){
		return String.format("Site: %s\nArea: %s\nPagenumber: %d",
								this.site, this.area, this.pagenumber);
	}
}
