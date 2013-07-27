
public class Track {

	private String id;
	private String song;
	private String artist;
	private String streamURL;
	
	public Track(String id, String song, String artist) {
		setId(id);
		setSong(song);
		setArtist(artist);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSong() {
		return song;
	}

	public void setSong(String song) {
		this.song = song;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getStreamURL() {
		return streamURL;
	}

	public void setStreamURL(String streamURL) {
		this.streamURL = streamURL;
	}
	
}
