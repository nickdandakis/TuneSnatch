import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class CommandLine {
	
	Scanner in = new Scanner(new InputStreamReader(System.in));
	
	public CommandLine() {
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
		System.out.println("clear");
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
			} else if(args.length == 2){
				if(args[0].equalsIgnoreCase("ls") && args[1].contains("insync"))
					printInsync();
				if(args[0].equalsIgnoreCase("ls") && args[1].contains("newtracks"))
					printNewtracks();
			} else if(args.length == 4){
				if(args[0].equalsIgnoreCase("download") && args[1].contains("HypeMachine"))
					download("HypeMachine", args[2], args[3]);
				if(args[0].equalsIgnoreCase("download") && args[1].contains("SoundCloud"))
					download("SoundCloud", args[2], args[3]);
			}
		}
	}

	private void download(String site, String area, String page) {
		int pages = Integer.parseInt(page);
		TrackList tracklist = new TrackList();
		Downloader dw = new Downloader();
		
		if(site.equalsIgnoreCase("HypeMachine")){
			HypeHTML html = null;
			
			if(pages != 0){
				try {
					for(int i=1; i<=pages; i++){
						html = new HypeHTML(area, i);
						tracklist.addTracks(html);
						System.out.println("Tracks added from page " + i + " from " + " HypeMachine/" + html.getAREA());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					for(int i=1; ; i++){
						html = new HypeHTML(area, i);
						if(html.getDoc().toString().length() < 35000) 
							break;
						tracklist.addTracks(html);
						System.out.println("Tracks added from page " + i + " from " + " HypeMachine/" + html.getAREA());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			for(int i=0; i<tracklist.getSize(); i++){
				try {
					dw.download(tracklist.getTrack(i));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if(site.equalsIgnoreCase("SoundCloud")){
			SoundHTML html = null;
			
			if(pages != 0){
				try {
					for(int i=1; i<=pages; i++){
						html = new SoundHTML(area, i);
						tracklist.addTracks(html);
						System.out.println("Tracks added from page " + i + " SoundCloud/" + html.getAREA());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					for(int i=1; ; i++){
						html = new SoundHTML(area, i);
						if(html.getDoc().toString().length() < 35000) 
							break;
						tracklist.addTracks(html);
						System.out.println("Tracks added from page " + i + " SoundMachine/" + html.getAREA());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			for(int i=0; i<tracklist.getSize(); i++){
				try {
					dw.download(tracklist.getTrack(i));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void printNewtracks() {
		System.out.println("IMMA PRINT ALL DA TRACKS!");
	}

	private void printInsync() {
		System.out.println("IMMA PRINT ALL DA AREAS!");
	}
	
	private void clear(){
		final String ESC = "\033[";
		System.out.print(ESC + "2J"); 
	}
}
