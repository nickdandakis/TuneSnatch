
public class MixcloudTrack extends Track {
	
	private String PREVIEW_URL;
	private String COVER_URL;

	public MixcloudTrack(String ID, String SONG, String USERNAME, String PREVIEW_URL, String COVER_URL) {
		super(ID, SONG, USERNAME);
		setPREVIEW_URL(PREVIEW_URL);
		setCOVER_URL(COVER_URL);
	}

	public String getPREVIEW_URL() {
		return PREVIEW_URL;
	}

	public void setPREVIEW_URL(String PREVIEW_URL) {
		this.PREVIEW_URL = PREVIEW_URL;
	}

	public String getCOVER_URL() {
		return COVER_URL;
	}

	public void setCOVER_URL(String COVER_URL) {
		this.COVER_URL = COVER_URL;
	}
	
	@Override
	public String toString(){
		return String.format("id: %s\ntitle: %s\nartist: %s\npreviewurl: %s\ncoverurl: %s", 
								getID(), getSONG(), getARTIST(), getPREVIEW_URL(), getCOVER_URL()); 
	}

}
