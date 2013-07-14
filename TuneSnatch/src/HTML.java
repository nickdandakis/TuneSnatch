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
	private String SITE_URL;
	private String AREA;
	private int PAGENUM;
	private String COMPLETE_URL;
	
	public HTML(String AREA, int PAGENUM) {
		setAREA(AREA);
		setPAGENUM(PAGENUM);
	}

	public String getSITE_URL() {
		return SITE_URL;
	}

	public void setSITE_URL(String SITE_URL) {
		this.SITE_URL = SITE_URL;
	}

	public String getAREA() {
		return AREA;
	}

	public void setAREA(String AREA) {
		this.AREA = AREA;
	}

	public int getPAGENUM() {
		return PAGENUM;
	}

	public void setPAGENUM(int PAGENUM) {
		this.PAGENUM = PAGENUM;
	}

	public String getCOMPLETE_URL() {
		return COMPLETE_URL;
	}

	public void setCOMPLETE_URL(String COMPLETE_URL) {
		this.COMPLETE_URL = COMPLETE_URL;
	}

	public void saveDocument(Document document){
		File htmlFile = new File("." + File.separator + getAREA().replace("/", "-") + "-p" + getPAGENUM() + ".html");
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
}
