package io.phalanx.Logic.Scraping;


import java.io.IOException;
import java.util.ArrayList;

public class TrackList {
	
	private ArrayList<Track> tracks = new ArrayList<Track>();
	
	public TrackList() {
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
			addTracks(Scraper.scrapeHypeMachineTracks((HypeMachineHTML) html));
		} else if(html.getClass() == SoundCloudHTML.class) {
			addTracks(Scraper.scrapeSoundCloudTracks((SoundCloudHTML) html));
		} else if (html.getClass() == MixcloudHTML.class) {
			addTracks(Scraper.scrapeMixcloudTracks((MixcloudHTML) html));
		}
	}
}
