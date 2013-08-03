import java.io.IOException;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.HttpStatusException;

public class Processor {
	
	private Downloader dw;
	ExecutorService threadPool = Executors.newFixedThreadPool(UserProfile.getSimultaneousDownloads());
	CompletionService<TrackList> scrapePool = new ExecutorCompletionService<TrackList>(threadPool);
	CompletionService<HTML> htmlPool = new ExecutorCompletionService<HTML>(threadPool);
			
	public Processor() {
		Synchronizer.restoreData();
		dw = new Downloader();
	}
	
	public void download (HTML html){
		download(html.getSite(), html.getArea(), String.valueOf(html.getPagenumber()));
	}

	/*
	 * Downloads all tracks from site/area and page(s) passed through.
	 * 0 pages means to download all tracks from all pages.
	 */
	public void download(Site site, String area, String pagenumber) {
		int pages = Integer.parseInt(pagenumber);
		TrackList tracklist = new TrackList();
		HTML html = null;
		
		if(pages != 0){
			try {
				for(int i=1; i<=pages; i++)
					htmlPool.submit(new HTMLTask(site, area, i));
				
				for(int i=1; i<=pages; i++){
					html = htmlPool.take().get();
					scrapePool.submit(new ScrapeTask(html));
				}
				
				for(int i=1; i<=pages; i++){
					tracklist.addTracks(scrapePool.take().get());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			try {
				for(int i=1; ; i++){
					if(site == Site.HypeMachine)
						try{
							html = new HypeMachineHTML(area, i);
						} catch (HttpStatusException e){ // Hit 404
							System.out.println("Hit a wall!");
							break;
						}
					else if(site == Site.SoundCloud)
						html = new SoundCloudHTML(area, i);
					else if(site == Site.Mixcloud)
						html = new MixcloudHTML(area, i);
					
					if(html.getDocument() == null)
						break;
					if(html.getDocument().toString().length() < 40000)
						break;
					tracklist.addTracks(html);
					System.out.println("Tracks added from page " + i + " from " + " " + site + "/" + html.getArea());
				}
			} catch (IOException e) {
				System.out.println("Invalid HTTP request");
			}
		}

		try {
			dw.downloadTracks(tracklist);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Adds site/area and page(s) into the sync list.
	 */
	public void sync(Site site, String area, String page){
		int pages = Integer.valueOf(page);
		HTML html = null;
		
		if(pages != 0){
			for(int i=1; i<=pages; i++){
				html = new HTML(area, i);
				html.setSite(site);
				Synchronizer.addHTML(html);
			}
		} else {
			html = new HTML(area, pages);
			html.setSite(site);
			Synchronizer.addHTML(html);
		}
		
		printSyncList();
		System.out.println("Execute a 'pull' command if you want to start downloading tracks");
	}
	
	/*
	 * Removes site/area and pages from the sync list
	 */
	public void unsync(Site site, String area, String page){
		int pages = Integer.parseInt(page);
		
		for(HTML html : Synchronizer.getSyncdata()){
			if(html.getArea().equalsIgnoreCase(area) && html.getPagenumber() == pages &&
					((site == Site.HypeMachine && html.getSite() == Site.HypeMachine) ||
							(site == Site.SoundCloud && html.getSite() == Site.SoundCloud) ||
								(site == Site.Mixcloud && html.getSite() == Site.Mixcloud))){
				System.out.println("Conditions met");
				Synchronizer.removeHTML(html);
				break;
			}
		}
		
		printSyncList();
	}
	
	public void unsync(int index){
		Synchronizer.removeHTML(--index); // Sync list is displayed to user with start index as 1 (not 0)
		printSyncList();
	}
	
	/*
	 * Downloads new tracks from site(s)/area(s) in sync list.
	 * Downloader object avoids downloading already downloaded tracks.
	 */
	public void pull(){
		for(HTML html : Synchronizer.getSyncdata()){
			download(html);
			// TODO Multithread this bitch
		}
	}
	
	public void printNewtracks() { // TODO
		System.out.println("IMMA PRINT ALL DA TRACKS!");
	}
	
	public void printSyncList() {
		Synchronizer.printData();
	}
	
	public void clearSyncList(){
		Synchronizer.clearData();
	}
	
	// TODO Only works for UNIX 
	public void clear(){
		final String ESC = "\033[";
		System.out.print(ESC + "2J"); 
	}
	
	public void changeDownloadDirectory(String path){
		System.out.println("Setting new download directory path preference...");
		UserProfile.setDownloadDirectory(path);
	}

	public void changeSimultaenousDownloads(String number) {
		System.out.println("Setting new simultaneous downloads limit preference...");
		UserProfile.setSimultaneousDownloads(Integer.valueOf(number));
	}
}
