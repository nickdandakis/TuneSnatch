package io.phalanx.Logic;
import io.phalanx.Data.UserProfile;
import io.phalanx.Logic.Scraping.HTML.HTML;
import io.phalanx.Logic.Scraping.HTML.HypeMachineHTML;
import io.phalanx.Logic.Scraping.HTML.MixcloudHTML;
import io.phalanx.Logic.Scraping.HTML.SoundCloudHTML;
import io.phalanx.Logic.Scraping.Track.TrackList;
import io.phalanx.Logic.Utility.Synchronizer;

import java.io.IOException;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.HttpStatusException;

public class Processor {
	
	ExecutorService threadPool = Executors.newFixedThreadPool(UserProfile.getSimultaneousDownloads());
	CompletionService<TrackList> scrapePool = new ExecutorCompletionService<TrackList>(threadPool);
	CompletionService<HTML> htmlPool = new ExecutorCompletionService<HTML>(threadPool);
	CompletionService<Boolean> downloadPool = new ExecutorCompletionService<Boolean>(threadPool);
	CompletionService<Boolean> processorPool = new ExecutorCompletionService<Boolean>(threadPool);
			
	public Processor() {
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
			} catch (InterruptedException | ExecutionException e) {
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
		
		for(int i=0; i<tracklist.getSize(); i++){
			downloadPool.submit(new DownloadTask(tracklist.getTrack(i)));
		}
		
		for(int i=0; i<tracklist.getSize(); i++){
			try {
				downloadPool.take().get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sync(HTML html){
		Synchronizer.addHTML(html);
		
		printSyncList();
		System.out.println("Execute a 'pull' command if you want to start downloading tracks");
	}
	
	/*
	 * Adds site/area and page(s) into the sync list.
	 */
	public void sync(Site site, String area, String page){
		int pages = Integer.valueOf(page);
		HTML html = null;
		
		html = new HTML(area, pages);
		html.setSite(site);
		Synchronizer.addHTML(html);
		
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
			System.out.println("Submit");
			processorPool.submit(new ProcessorTask(html));
		}
		
		for(@SuppressWarnings("unused") HTML html : Synchronizer.getSyncdata()){
			try {
				processorPool.take().get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
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
	
	public void setDownloadDirectory(String path){
		System.out.println("Setting new download directory path preference...");
		UserProfile.setDownloadDirectory(path);
	}

	public void setSimultaenousDownloads(String number) {
		System.out.println("Setting new simultaneous downloads limit preference...");
		UserProfile.setSimultaneousDownloads(Integer.valueOf(number));
	}
	
	public void getSimultaneouDownloads(){
		System.out.printf("SIMULTANEOUS_DOWNLOADS = %d\n", UserProfile.getSimultaneousDownloads());
	}
	
	public void getDownloadDirectory(){
		System.out.printf("DOWNLOAD_DIRECTORY = %s\n", UserProfile.getDownloadDirectory());
	}
}
