import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class HypeMachineTrack extends Track implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3765563432705431846L;
	private static String HYPEM_SERVE_URL = "http://hypem.com/serve/source/";
	private String key;
	private String postURL;
	private Map<String, String> cookies;
	
	public HypeMachineTrack(String id, String key, String song, String artist, String postURL, Map<String, String> cookies) {
		super(id, song, artist);
		setKey(key);
		setPostURL(postURL);
		setCookies(cookies);
		
		try {
			this.setStreamURL(generateStreamURL());
		} catch (IOException e) {
			System.out.println("Failed to generate Hypemachine stream URL for track:");
			System.out.printf("%s - %s", this.getSong(), this.getArtist());
			System.out.println("HypeMachine probably doesn't serve this track anymore. :(");
		}
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

	/*
	 * HypeMachine serves download URLs via a JSON response to a HTTP request in the following format:
	 * http://hypem.com/serve/source/<TRACK_ID>/<TRACK_KEY>/, where <TRACK_ID> is the track ID and <TRACK_KEY> is the track key
	 * 
	 * This method constructs the correct URL and parses the response.
	 */
	private String generateStreamURL() throws IOException{
		String completeURL = HYPEM_SERVE_URL + this.getId() + "/" + this.getKey() + "/";
		Response res = Jsoup.connect(completeURL).ignoreContentType(true).cookies(cookies).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0").execute();
		
		String temp[] = res.parse().toString().split("&quot;");
		return temp[11].replaceAll("\\\\", "");
	}
	
	@Override
	public String toString(){
		return String.format("Id: %s\nKey: %s\nTitle: %s\nArtist: %s\nPostURL: %s", 
								getId(), getKey(), getSong(), getArtist(), getPostURL());
	}
	
}
