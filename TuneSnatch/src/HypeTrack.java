import java.util.Map;

public class HypeTrack extends Track {

	private String KEY;
	private String POSTURL;
	private Map<String, String> COOKIES;
	
	public HypeTrack(String ID, String KEY, String ARTIST, String SONG, String POSTURL, Map<String, String> COOKIES) {
		super(ID, ARTIST, SONG);
		setKEY(KEY);
		setPOSTURL(POSTURL);
		setCOOKIES(COOKIES);
	}

	public String getKEY() {
		return KEY;
	}

	public void setKEY(String KEY) {
		this.KEY = KEY;
	}

	public String getPOSTURL() {
		return POSTURL;
	}

	public void setPOSTURL(String POSTURL) {
		this.POSTURL = POSTURL;
	}
	
	public Map<String, String> getCOOKIES() {
		return COOKIES;
	}

	public void setCOOKIES(Map<String, String> COOKIES) {
		this.COOKIES = COOKIES;
	}

	@Override
	public String toString(){
		return getID() + ", " + getKEY() + ", " + getARTIST() + ", " + getSONG() + ", " + getPOSTURL();
	}
}
