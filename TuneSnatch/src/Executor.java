import java.io.IOException;

public class Executor {
	
	private Synchronizer sz;
	private Downloader dw;
	
	public Executor() {
		sz = new Synchronizer();
		sz.restoreSyncData();
		dw = new Downloader();
	}
	
	public void download (HTML html){
		if(html.getSite().equalsIgnoreCase("http://hypem.com/"))
			download(Site.HypeMachine, html.getArea(), String.valueOf(html.getPagenumber()));
		else if(html.getSite().equalsIgnoreCase("https://soundcloud.com/"))
			download(Site.SoundCloud, html.getArea(), String.valueOf(html.getPagenumber()));
		else if(html.getSite().equalsIgnoreCase("http://mixcloud.com/"))
			download(Site.Mixcloud, html.getArea(), String.valueOf(html.getPagenumber()));
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
				for(int i=1; i<=pages; i++){
					if(site == Site.HypeMachine)
						html = new HypeMachineHTML(area, i);
					else if(site == Site.SoundCloud)
						html = new SoundCloudHTML(area, i);
					else if(site == Site.Mixcloud)
						html = new MixcloudHTML(area, i);
					
					tracklist.addTracks(html);
					System.out.println("Tracks added from page " + i + " from " + " " + site + "/" + html.getArea());
				}
			} catch (IOException e) {
				System.out.println("Invalid HTTP request");
			}
		} else {
			try {
				for(int i=1; ; i++){
					if(site == Site.HypeMachine)
						html = new HypeMachineHTML(area, i);
					else if(site == Site.SoundCloud)
						html = new SoundCloudHTML(area, i);
					else if(site == Site.Mixcloud)
						html = new MixcloudHTML(area, i);
					
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
				if(site == Site.HypeMachine)
					html = new HypeMachineHTML(area, i);
				else if(site == Site.SoundCloud)
					html = new SoundCloudHTML(area, i);
				else if(site == Site.Mixcloud)
					html = new MixcloudHTML(area, i);
				
				sz.addHTML(html);
			}
		} else {
			if(site == Site.HypeMachine)
				html = new HypeMachineHTML(area, pages);
			else if(site == Site.SoundCloud)
				html = new SoundCloudHTML(area, pages);
			else if(site == Site.Mixcloud)
				html = new MixcloudHTML(area, pages);
			
			sz.addHTML(html);
		}
		
		printSyncList();
		System.out.println("Execute a 'pull' command if you want to start downloading tracks");
	}
	
	/*
	 * Removes site/area and pages from the sync list
	 */
	public void unsync(Site site, String area, String page){
		int pages = Integer.parseInt(page);
		
		for(HTML html : sz.getSyncdata()){
			if(html.getArea().equalsIgnoreCase(area) && html.getPagenumber() == pages &&
					((site == Site.HypeMachine && html instanceof HypeMachineHTML) ||
							(site == Site.SoundCloud && html instanceof SoundCloudHTML) ||
								(site == Site.SoundCloud && html instanceof MixcloudHTML))){
				System.out.println("Conditions met");
				sz.removeHTML(html);
				break;
			}
		}
		
		printSyncList();
	}
	
	public void unsync(int index){
		sz.removeHTML(--index); // Sync list is displayed to user with start index as 1 (not 0)
		printSyncList();
	}
	
	/*
	 * Downloads new tracks from site(s)/area(s) in sync list.
	 * Downloader object avoids downloading already downloaded tracks.
	 */
	public void pull(){
		for(HTML html : sz.getSyncdata()){
			download(html);
		}
	}
	
	public void printNewtracks() { // TODO
		System.out.println("IMMA PRINT ALL DA TRACKS!");
	}
	
	public void printSyncList() {
		sz.printSyncData();
	}
	
	public void clearSyncList(){
		sz.clearSyncData();
	}
	
	// TODO Only works for UNIX 
	public void clear(){
		final String ESC = "\033[";
		System.out.print(ESC + "2J"); 
	}
	
	public void changeDownloadDirectory(String path){
		UserProfile.setDownloadDirectory(path);
	}
}
