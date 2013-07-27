
public class SoundCloudTrack extends Track {
	
	private String waveformURL;
	
	public SoundCloudTrack(String id, String song, String username, String streamURL, String waveformURL) {
		super(id, username, song);
		setStreamURL(streamURL);
		setWaveformURL(waveformURL);
	}

	public String getWaveformURL() {
		return waveformURL;
	}

	public void setWaveformURL(String waveformURL) {
		this.waveformURL = waveformURL;
	}
	
	@Override
	public String toString(){
		return String.format("id: %s\ntitle: %s\nartist: %s\nstreamurl: %s\nwaveformurl: %s", 
								getId(), getSong(), getArtist(), getStreamURL(), getWaveformURL()); 
	}
}
