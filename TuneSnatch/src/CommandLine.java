import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class CommandLine {
	// System Input scanner object
	Scanner in = new Scanner(new InputStreamReader(System.in));
	private Synchronizer sz;
	private Downloader dw;
	
	/**
	 * Constructor for CommandLine object.
	 * Initializes Synchronizer object and restores SyncData from file
	 */
	public CommandLine() {
		sz = new Synchronizer();
		sz.restoreSyncData();
		dw = new Downloader();
	}
	
	private void prompt(){
		System.out.print("Ψ: ");
	}
	
	private void printHelp(){
		// Print out for help
		// TODO Make more user friendly and comprehensible
		System.out.println("List of commands:");
		System.out.println("download <SITE> <AREA/SUBAREA> <PAGES>");
		System.out.println("sync <SITE> <AREA/SUBAREA> <PAGES>");
		System.out.println("unsync <SITE> <AREA/SUBAREA> <PAGES>");
		System.out.println("ls -insync");
		System.out.println("ls -newtracks");
		System.out.println("clear insync");
		System.out.println("cls");
		System.out.println("exit");
		System.out.println("\nNumber of pages == 0 means all pages.\n<SITE> == HypeMachine || SoundCloud (case-sensitive) \n");
		System.out.println("For HypeMachine: <AREA/SUBAREA> == popular || <USERNAME>");
		System.out.println("For SoundCloud: <AREA/SUBAREA> == <USERNAME>/[favorites || tracks]");
		System.out.println("For Mixcloud: <AREA/SUBAREA> == <USERNAME>/[activity || listens || favorites || null]");
	}
	
	/*
	 * aka main() for this CommandLine object
	 */
	public void launch(){
		// Little launch message
		System.out.println("TuneSnatch - Grounding your favorite tunes in the cloud!");
		System.out.println("Synchronize HypeMachine and SoundCloud");
		System.out.println("(hint: Type help for a list of commands)");
		
		/*
		 * Main loop; Parses input from System.in after each line break (ie when user hits the return/enter key)
		 * Splits arguments with a single space character as a delimiter
		 * Arguments are an array of Strings
		 * Then checks number of arguments and executes recognized commandss
		 */
		for(prompt(); in.hasNextLine(); prompt()){
			String line = in.nextLine().replaceAll("\n", "");
			
			if(line.length() == 0)
				continue;
			
			String[] args = line.split(" ");
			
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("exit"))
					System.exit(0);
				if(args[0].equalsIgnoreCase("help"))
					printHelp();
				if(args[0].equalsIgnoreCase("cls"))
					clear();
				if(args[0].equalsIgnoreCase("pull"))
					pull();
			} else if(args.length == 2){
				if(args[0].equalsIgnoreCase("sync") && args[1].contains("list"))
					printSyncList();
				if(args[0].equalsIgnoreCase("ls") && args[1].contains("newtracks"))
					printNewtracks();
				if(args[0].equalsIgnoreCase("unsync"))
					unsync(Integer.valueOf(args[1]));
			} else if(args.length == 3){
				if(args[0].equalsIgnoreCase("clear") && args[1].contains("sync") && args[2].contains("list"))
					sz.clearSyncData();
			} else if(args.length == 4){
				if(args[0].equalsIgnoreCase("download"))
					download(args[1], args[2], args[3]);
				if(args[0].equalsIgnoreCase("sync"))
					sync(args[1], args[2], args[3]);
				if(args[0].equalsIgnoreCase("unsync"))
					unsync(args[1], args[2], args[3]);
			} 
		}
	}
	
	private void download (HTML html){
		if(html.getSite().equalsIgnoreCase("http://hypem.com/"))
			download("HypeMachine", html.getArea(), String.valueOf(html.getPagenumber()));
		else if(html.getSite().equalsIgnoreCase("https://soundcloud.com/"))
			download("SoundCloud", html.getArea(), String.valueOf(html.getPagenumber()));
		else if(html.getSite().equalsIgnoreCase("http://mixcloud.com/"))
			download("Mixcloud", html.getArea(), String.valueOf(html.getPagenumber()));
	}

	/*
	 * Downloads all tracks from site/area and page(s) passed through.
	 * 0 pages means to download all tracks from all pages.
	 */
	private void download(String site, String area, String pagenumber) {
		int pages = Integer.parseInt(pagenumber);
		TrackList tracklist = new TrackList();
		HTML html = null;
		
		if(pages != 0){
			try {
				for(int i=1; i<=pages; i++){
					if(site.equalsIgnoreCase("HypeMachine"))
						html = new HypeMachineHTML(area, i);
					else if(site.equalsIgnoreCase("SoundCloud"))
						html = new SoundCloudHTML(area, i);
					else if(site.equalsIgnoreCase("Mixcloud"))
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
					if(site.equalsIgnoreCase("HypeMachine"))
						html = new HypeMachineHTML(area, i);
					else if(site.equalsIgnoreCase("SoundCloud"))
						html = new SoundCloudHTML(area, i);
					else if(site.equalsIgnoreCase("Mixcloud"))
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
	private void sync(String site, String area, String page){
		int pages = Integer.valueOf(page);
		HTML html = null;
		
		if(pages != 0){
			for(int i=1; i<=pages; i++){
				if(site.equalsIgnoreCase("HypeMachine"))
					html = new HypeMachineHTML(area, i);
				else if(site.equalsIgnoreCase("SoundCloud"))
					html = new SoundCloudHTML(area, i);
				else if(site.equalsIgnoreCase("Mixcloud"))
					html = new MixcloudHTML(area, i);
				
				sz.addHTML(html);
			}
		} else {
			if(site.equalsIgnoreCase("HypeMachine"))
				html = new HypeMachineHTML(area, pages);
			else if(site.equalsIgnoreCase("SoundCloud"))
				html = new SoundCloudHTML(area, pages);
			else if(site.equalsIgnoreCase("Mixcloud"))
				html = new MixcloudHTML(area, pages);
			
			sz.addHTML(html);
		}
		
		printSyncList();
		System.out.println("Execute a 'pull' command if you want to start downloading tracks");
	}
	
	/*
	 * Removes site/area and pages from the sync list
	 */
	private void unsync(String site, String area, String page){
		int pages = Integer.parseInt(page);
		
		for(HTML html : sz.getSyncdata()){
			if(html.getArea().equalsIgnoreCase(area) && html.getPagenumber() == pages &&
					((site.equalsIgnoreCase("HypeMachine") && html instanceof HypeMachineHTML) ||
							(site.equalsIgnoreCase("SoundCloud") && html instanceof SoundCloudHTML) ||
								(site.equalsIgnoreCase("Mixcloud") && html instanceof MixcloudHTML))){
				System.out.println("Conditions met");
				sz.removeHTML(html);
				break;
			}
		}
		
		printSyncList();
	}
	
	private void unsync(int index){
		sz.removeHTML(--index); // Sync list is displayed to user with start index as 1 (not 0)
		printSyncList();
	}
	
	/*
	 * Downloads new tracks from site(s)/area(s) in sync list.
	 * Downloader object avoids downloading already downloaded tracks.
	 */
	private void pull(){
		for(HTML html : sz.getSyncdata()){
			download(html);
		}
	}
	
	private void printNewtracks() { // TODO
		System.out.println("IMMA PRINT ALL DA TRACKS!");
	}
	
	private void printSyncList() {
		sz.printSyncData();
	}
	
	// TODO Only works for UNIX 
	private void clear(){
		final String ESC = "\033[";
		System.out.print(ESC + "2J"); 
	}
}
