
public class SoundCloudTrack extends Track {
	
	private String STREAMURL;
	private String WAVEFORMURL;
	
	public SoundCloudTrack(String ID, String SONG, String USERNAME, String STREAMURL, String WAVEFORMURL) {
		super(ID, USERNAME, SONG);
		setSTREAMURL(STREAMURL);
		setWAVEFORMURL(WAVEFORMURL);
	}

	public String getSTREAMURL() {
		return STREAMURL;
	}

	public void setSTREAMURL(String STREAMURL) {
		this.STREAMURL = STREAMURL;
	}

	public String getWAVEFORMURL() {
		return WAVEFORMURL;
	}

	public void setWAVEFORMURL(String WAVEFORMURL) {
		this.WAVEFORMURL = WAVEFORMURL;
	}
	
	@Override
	public String toString(){
		return String.format("id: %s\ntitle: %s\nartist: %s\nstreamurl: %s\nwaveformurl: %s", 
								getID(), getSONG(), getARTIST(), getSTREAMURL(), getWAVEFORMURL()); 
	}
}
