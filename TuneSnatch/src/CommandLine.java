import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class CommandLine {
	
	Scanner in = new Scanner(new InputStreamReader(System.in));
	private Synchronizer sz;
	
	public CommandLine() {
		sz = new Synchronizer();
		sz.restoreSyncData();
	}
	
	private void prompt(){
		System.out.print("Ψ: ");
	}
	
	private void printHelp(){
		System.out.println("List of commands:");
		System.out.println("download <SITE> <AREA/SUBAREA> <PAGES>");
		System.out.println("sync <SITE> <AREA/SUBAREA> <PAGES>");
		System.out.println("ls -insync");
		System.out.println("ls -newtracks");
		System.out.println("clear insync");
		System.out.println("clear");
		System.out.println("exit");
		System.out.println("\nNumber of pages == 0 means all pages.\n<SITE> == HypeMachine || SoundCloud (case-sensitive) \n");
		System.out.println("For HypeMachine: <AREA/SUBAREA> == popular || <USERNAME>");
		System.out.println("For SoundCloud: <AREA/SUBAREA> == <USERNAME>/[favorites || tracks]");
	}
	
	public void launch(){
		System.out.println("TuneSnatch - Grounding your favorite tunes in the cloud!");
		System.out.println("Synchronize HypeMachine and SoundCloud");
		System.out.println("(hint: Type help for a list of commands)");
		
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
				if(args[0].equalsIgnoreCase("clear"))
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
				if(args[0].equalsIgnoreCase("download") && args[1].equalsIgnoreCase("HypeMachine"))
					download("HypeMachine", args[2], args[3]);
				if(args[0].equalsIgnoreCase("download") && args[1].equalsIgnoreCase("SoundCloud"))
					download("SoundCloud", args[2], args[3]);
				if(args[0].equalsIgnoreCase("sync") && args[1].equalsIgnoreCase("HypeMachine"))
					sync("HypeMachine", args[2], args[3]);
				if(args[0].equalsIgnoreCase("sync") && args[1].equalsIgnoreCase("SoundCloud"))
					sync("SoundCloud", args[2], args[3]);
			} 
		}
	}

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

					if(html.getDoc().toString().length() < 35000) 
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
			try {
				for(int i=1; ; i++){
					if(site.equalsIgnoreCase("HypeMachine"))
						html = new HypeHTML(area, i);
					else if(site.equalsIgnoreCase("SoundCloud"))
						html = new SoundHTML(area, i);
					
					if(html.getDoc().toString().length() < 35000) 
						break;
					sz.addHTML(html);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
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
	
	private void printNewtracks() {
		System.out.println("IMMA PRINT ALL DA TRACKS!");
	}

	private void printInsync() {
		sz.printSyncData();
	}
	
	private void clear(){
		final String ESC = "\033[";
		System.out.print(ESC + "2J"); 
	}
}
