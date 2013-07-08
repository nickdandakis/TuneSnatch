import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class CommandLine {
	// System Input Scanner object
	Scanner in = new Scanner(new InputStreamReader(System.in));
	private Synchronizer sz;
	
	/**
	 * Constructor for CommandLine object.
	 * Initializes Synchronizer object and restores SyncData from file
	 */
	public CommandLine() {
		sz = new Synchronizer();
		sz.restoreSyncData();
	}
	
	private void prompt(){
		System.out.print("Ψ: ");
	}
	
	private void printHelp(){
		// Print out for help
		// [FIX]: Make more user friendly and comprehensible
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
		 * Then checks number of arguments and executes recognized commands
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
				if(args[0].equalsIgnoreCase("ls") && args[1].contains("insync"))
					printInsync();
				if(args[0].equalsIgnoreCase("ls") && args[1].contains("newtracks"))
					printNewtracks();
				if(args[0].equalsIgnoreCase("clear") && args[1].contains("insync"))
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

	/*
	 * Downloads all tracks from site/area and page(s) passed through.
	 * 0 pages means to download all tracks from all pages.
	 */
	private void download(String site, String area, String page) {
		int pages = Integer.parseInt(page);
		TrackList tracklist = new TrackList();
		Downloader dw = new Downloader();
		HTML html = null;
		
		if(pages != 0){
			try {
				for(int i=1; i<=pages; i++){
					if(site.equalsIgnoreCase("HypeMachine"))
						html = new HypeHTML(area, i);
					else if(site.equalsIgnoreCase("SoundCloud"))
						html = new SoundHTML(area, i);
					
					tracklist.addTracks(html);
					System.out.println("Tracks added from page " + i + " from " + " " + site + "/" + html.getAREA());
				}
			} catch (IOException e) {
				System.out.println("Invalid HTTP request");
			}
		} else {
			try {
				for(int i=1; ; i++){
					if(site.equalsIgnoreCase("HypeMachine"))
						html = new HypeHTML(area, i);
					else if(site.equalsIgnoreCase("SoundCloud"))
						html = new SoundHTML(area, i);

					if(html.getDoc().toString().length() < 35000) // [FIX]: Doesn't work for SoundCloud anymore. Check for same tracks on next page as well.
						break;
					tracklist.addTracks(html);
					System.out.println("Tracks added from page " + i + " from " + " " + site + "/" + html.getAREA());
				}
			} catch (IOException e) {
				System.out.println("Invalid HTTP request");
			}
		}

		dw.downloadTracklist(tracklist);
	}
	/*
	 * Adds site/area and page(s) into the sync list.
	 */
	private void sync(String site, String area, String page){
		int pages = Integer.parseInt(page);
		HTML html = null;
		
		if(pages != 0){
			for(int i=1; i<=pages; i++){
				if(site.equalsIgnoreCase("HypeMachine"))
					html = new HypeHTML(area, i);
				else if(site.equalsIgnoreCase("SoundCloud"))
					html = new SoundHTML(area, i);
				sz.addHTML(html);
			}
		} else {
			if(site.equalsIgnoreCase("HypeMachine"))
				html = new HypeHTML(area, pages);
			else if(site.equalsIgnoreCase("SoundCloud"))
				html = new SoundHTML(area, pages);
			
			sz.addHTML(html);
		}
	}
	
	/*
	 * Removes site/area and pages from the sync list
	 */
	private void unsync(String site, String area, String page){
		int pages = Integer.parseInt(page);
		
		for(HTML html : sz.getSyncdata()){
			if(html.getAREA().equalsIgnoreCase(area) && html.getPAGENUM() == pages &&
					((site.equalsIgnoreCase("HypeMachine") && html instanceof HypeHTML) ||
							(site.equalsIgnoreCase("SoundCloud") && html instanceof SoundHTML))){
				sz.removeHTML(html);
				break;
			}
				
		}
	}
	
	/*
	 * Downloads new tracks from site(s)/area(s) in sync list.
	 * Downloader object avoids downloading already downloaded tracks.
	 */
	private void pull(){
		Downloader dw = new Downloader();
		TrackList tracklist = new TrackList();
		
		for(HTML html : sz.getSyncdata()){
			try {
				tracklist.addTracks(html);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		dw.downloadTracklist(tracklist);
	}
	
	/*
	 * NOT YET IMPLEMENTED!
	 */
	private void printNewtracks() {
		System.out.println("IMMA PRINT ALL DA TRACKS!");
	}
	
	private void printInsync() {
		sz.printSyncData();
	}
	
	// [FIX]: Doesn't work for Windows! 
	private void clear(){
		final String ESC = "\033[";
		System.out.print(ESC + "2J"); 
	}
}
