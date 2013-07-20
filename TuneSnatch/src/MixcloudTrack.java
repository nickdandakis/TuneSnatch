
public class MixcloudTrack extends Track {
	
	private String previewURL;
	private String coverURL;

	public MixcloudTrack(String id, String song, String username, String previewURL, String coverURL) {
		super(id, song, username);
		setPreviewURL(previewURL);
		setCoverURL(coverURL);
	}

	public String getPreviewURL() {
		return previewURL;
	}

	public void setPreviewURL(String previewURL) {
		this.previewURL = previewURL;
	}

	public String getCoverURL() {
		return coverURL;
	}

	public void setCoverURL(String coverURL) {
		this.coverURL = coverURL;
	}
	
	@Override
	public String toString(){
		return String.format("Id: %s\nTitle: %s\nArtist: %s\nPreviewURL: %s\nCoverURL: %s", 
								getId(), getSong(), getArtist(), getPreviewURL(), getCoverURL()); 
	}

}
