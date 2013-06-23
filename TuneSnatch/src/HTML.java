import java.io.IOException;


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

	public Object getDoc() throws IOException {
		return null;
	}
}
