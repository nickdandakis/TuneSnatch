import java.util.Map;

public class HypeMachineTrack extends Track {

	private String key;
	private String postURL;
	private Map<String, String> cookies;
	
	public HypeMachineTrack(String id, String key, String song, String artist, String postURL, Map<String, String> cookies) {
		super(id, song, artist);
		setKey(key);
		setPostURL(postURL);
		setCookies(cookies);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPostURL() {
		return postURL;
	}

	public void setPostURL(String postURL) {
		this.postURL = postURL;
	}
	
	public Map<String, String> getCookies() {
		return cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	@Override
	public String toString(){
		return String.format("Id: %s\nKey: %s\nTitle: %s\nArtist: %s\nPostURL: %s", 
								getId(), getKey(), getSong(), getArtist(), getPostURL());
	}
}
