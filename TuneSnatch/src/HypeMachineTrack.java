import java.util.Map;

public class HypeMachineTrack extends Track {

	private String KEY;
	private String POSTURL;
	private Map<String, String> COOKIES;
	
	public HypeMachineTrack(String ID, String KEY, String SONG, String ARTIST, String POSTURL, Map<String, String> COOKIES) {
		super(ID, SONG, ARTIST);
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
		return String.format("id: %s\nkey: %s\ntitle: %s\nartist: %s\nposturl: %s", 
								getID(), getKEY(), getSONG(), getARTIST(), getPOSTURL());
	}
}
