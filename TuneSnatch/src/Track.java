
public class Track {

	private String ID;
	private String SONG;
	private String ARTIST;
	
	public Track(String ID, String SONG, String ARTIST) {
		setID(ID);
		setSONG(SONG);
		setARTIST(ARTIST);
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getSONG() {
		return SONG;
	}

	public void setSONG(String SONG) {
		this.SONG = SONG;
	}

	public String getARTIST() {
		return ARTIST;
	}

	public void setARTIST(String ARTIST) {
		this.ARTIST = ARTIST;
	}

}
