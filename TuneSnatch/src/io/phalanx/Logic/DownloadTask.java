package io.phalanx.Logic;
import io.phalanx.Logic.Scraping.Track.Track;
import io.phalanx.Logic.Utility.Downloader;

import java.io.IOException;
import java.util.concurrent.Callable;

public class DownloadTask implements Callable<Boolean> {
	
	private Track track;
	
	public DownloadTask(Track track) {
		this.track = track;
	}

	@Override
	public Boolean call() throws Exception {
		try{
			Downloader.downloadTrack(track);
		} catch (IOException e){
			return false;
		}
		
		return true;
	}

}
