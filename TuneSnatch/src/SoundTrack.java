
public class SoundTrack extends Track {
	
	private String STREAMURL;
	private String WAVEFORMURL;
	
	public SoundTrack(String ID, String USERNAME, String SONG, String STREAMURL, String WAVEFORMURL) {
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
		return getID() + ", " + getARTIST() + ", " + getSONG() + ", " + getSTREAMURL() + ", " + getWAVEFORMURL();
	}
}
