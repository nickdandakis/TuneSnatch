package io.phalanx.Logic.Utility.Multithreading;
import io.phalanx.Logic.Scraping.HTML;
import io.phalanx.Logic.Scraping.HypeMachineHTML;
import io.phalanx.Logic.Scraping.MixcloudHTML;
import io.phalanx.Logic.Scraping.Scraper;
import io.phalanx.Logic.Scraping.SoundCloudHTML;
import io.phalanx.Logic.Scraping.TrackList;

import java.util.concurrent.Callable;


public class ScrapeTask implements Callable<TrackList> {

	private HTML html;
	
	public ScrapeTask(HTML html) {
		this.html = html;
	}

	@Override
	public TrackList call() throws Exception {
		if(html.getClass() == HypeMachineHTML.class){
			return Scraper.scrapeHypeMachineTracks((HypeMachineHTML) html);
		} else if(html.getClass() == SoundCloudHTML.class) {
			return Scraper.scrapeSoundCloudTracks((SoundCloudHTML) html);
		} else if (html.getClass() == MixcloudHTML.class) {
			return Scraper.scrapeMixcloudTracks((MixcloudHTML) html);
		}
		return null;
	}

}
