package io.phalanx.Logic.Utility;
import io.phalanx.Data.UserProfile;
import io.phalanx.Logic.Site;
import io.phalanx.Logic.Scraping.HTML;
import io.phalanx.Logic.Scraping.HypeMachineHTML;
import io.phalanx.Logic.Scraping.MixcloudHTML;
import io.phalanx.Logic.Scraping.SoundCloudHTML;
import io.phalanx.Logic.Scraping.TrackList;
import io.phalanx.Logic.Utility.Multithreading.DownloadTask;
import io.phalanx.Logic.Utility.Multithreading.HTMLTask;
import io.phalanx.Logic.Utility.Multithreading.ProcessorTask;
import io.phalanx.Logic.Utility.Multithreading.ScrapeTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.HttpStatusException;

public class Processor {
	
	private static ExecutorService threadPool = Executors.newFixedThreadPool(UserProfile.getSimultaneousDownloads());
	private static CompletionService<TrackList> scrapePool = new ExecutorCompletionService<TrackList>(threadPool);
	private static CompletionService<HTML> htmlPool = new ExecutorCompletionService<HTML>(threadPool);
	private static CompletionService<Boolean> downloadPool = new ExecutorCompletionService<Boolean>(threadPool);
	private static CompletionService<Boolean> processorPool = new ExecutorCompletionService<Boolean>(threadPool);
			
	static{
		Synchronizer.restoreData();
	}
	private Processor() {
		
	}
	
	public static void download(String URL){
		download(URL, "0");
	}
	
	public static void download(String URL, String pagenumber){
		HTML html = new HTML(URL);
		html.setPagenumber(Integer.parseInt(pagenumber));
		download(html);
	}
	
	public static void download(HTML html){
		download(html.getSite(), html.getArea(), String.valueOf(html.getPagenumber()));
	}

	public static void download(String site, String area, String pagenumber){
		if(Site.recognize(site) != Site.Invalid)
			download(Site.recognize(site), area, pagenumber);
	}
	
	/*
	 * Downloads all tracks from site/area and page(s) passed through.
	 * 0 pages means to download all tracks from all pages.
	 */
	public static void download(Site site, String area, String pagenumber) {
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
			} catch (InterruptedException  e) {
				e.printStackTrace();
			} catch (ExecutionException e1){
                e1.printStackTrace();
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
				System.out.println("Finished");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e1){
                e1.printStackTrace();
            }
		}
	}
	
	public static void sync(String URL){
		sync(URL, "0");
	}
	
	public static void sync(String URL, String pagenumber){
		HTML html = new HTML(URL);
		html.setPagenumber(Integer.parseInt(pagenumber));
		sync(html);
	}
	
	public static void sync(HTML html){
		Synchronizer.addHTML(html);
		
		printSyncList();
		System.out.println("Execute a 'pull' command if you want to start downloading tracks");
	}
	
	public static void sync(String site, String area, String page){
		if(Site.recognize(site) != Site.Invalid)
			sync(Site.recognize(site), area, page);
	}
	
	/*
	 * Adds site/area and page(s) into the sync list.
	 */
	public static void sync(Site site, String area, String page){
		int pages = Integer.valueOf(page);
		HTML html;
		
		html = new HTML(area, pages);
		html.setSite(site);
		Synchronizer.addHTML(html);
		
		printSyncList();
		System.out.println("Execute a 'pull' command if you want to start downloading tracks");
	}
	
	public static void unsync(String site, String area, String page){
		if(Site.recognize(site) != Site.Invalid)
			unsync(Site.recognize(site), area, page);
	}
	
	/*
	 * Removes site/area and pages from the sync list
	 */
	public static void unsync(Site site, String area, String page){
		int pages = Integer.parseInt(page);
		
		for(HTML html : Synchronizer.getSyncdata()){
			if(html.getArea().equalsIgnoreCase(area) && html.getPagenumber() == pages &&
					((site == Site.HypeMachine && html.getSite() == Site.HypeMachine) ||
							(site == Site.SoundCloud && html.getSite() == Site.SoundCloud) ||
								(site == Site.Mixcloud && html.getSite() == Site.Mixcloud))){
				Synchronizer.removeHTML(html);
				break;
			}
		}
		
		printSyncList();
	}
	
	public static void unsync(int index){
		Synchronizer.removeHTML(--index); // Sync list is displayed to user with start index as 1 (not 0)
		printSyncList();
	}
	
	/*
	 * Downloads new tracks from site(s)/area(s) in sync list.
	 * Downloader object avoids downloading already downloaded tracks.
	 */
	public static void pull(){
		for(HTML html : Synchronizer.getSyncdata()){
			System.out.println("Submit");
			processorPool.submit(new ProcessorTask(html));
		}
		
		for(@SuppressWarnings("unused") HTML html : Synchronizer.getSyncdata()){
			try {
				processorPool.take().get();
			} catch (InterruptedException  e) {
				e.printStackTrace();
			} catch (ExecutionException e1){
                e1.printStackTrace();
            }
		}
	}
	
	public static ArrayList<HTML> getSyncList(){
		return Synchronizer.getSyncdata();
	}
	
	public static void printNewtracks() { // TODO
		System.out.println("IMMA PRINT ALL DA TRACKS!");
	}
	
	public static void printSyncList() {
		Synchronizer.printData();
	}
	
	public static void clearSyncList(){
		Synchronizer.clearData();
	}
	
	// TODO Only works for UNIX 
	public static void clear(){
		final String ESC = "\033[";
		System.out.print(ESC + "2J"); 
	}
	
	public static void setDownloadDirectory(String path){
		System.out.println("Setting new download directory path preference...");
		UserProfile.setDownloadDirectory(path);
	}

	public static void setSimultaneouslyDownloads(String number) {
		System.out.println("Setting new simultaneous downloads limit preference...");
		UserProfile.setSimultaneousDownloads(Integer.valueOf(number));
	}
	
	public static void getSimultaneouDownloads(){
		System.out.printf("SIMULTANEOUS_DOWNLOADS = %d\n", UserProfile.getSimultaneousDownloads());
	}
	
	public static void getDownloadDirectory(){
		System.out.printf("DOWNLOAD_DIRECTORY = %s\n", UserProfile.getDownloadDirectory());
	}
}
