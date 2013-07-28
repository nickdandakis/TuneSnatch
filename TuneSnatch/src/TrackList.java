import java.io.IOException;
import java.util.ArrayList;

public class TrackList {
	
	private ArrayList<Track> tracks = new ArrayList<Track>();
	private Scraper scraper;
	
	public TrackList() {
		scraper = new Scraper();
	}
	
	public Track getTrack(int i){
		return tracks.get(i);
	}
	
	public int getSize(){
		return tracks.size();
	}
	
	public void addTrack(Track track){
		tracks.add(track);
	}
	
	public void addTracks(TrackList tracklist){
		for(int i=0; i<tracklist.getSize(); i++){
			this.tracks.add(tracklist.getTrack(i));
		}
	}
	
	public void addTracks(HTML html) throws IOException{
		if(html.getClass() == HypeMachineHTML.class){
			addTracks(scraper.scrapeHypeMachineTracks((HypeMachineHTML) html));
		} else if(html.getClass() == SoundCloudHTML.class) {
			addTracks(scraper.scrapeSoundCloudTracks((SoundCloudHTML) html));
		} else if (html.getClass() == MixcloudHTML.class) {
			addTracks(scraper.scrapeMixcloudTracks((MixcloudHTML) html));
		}
	}
}
