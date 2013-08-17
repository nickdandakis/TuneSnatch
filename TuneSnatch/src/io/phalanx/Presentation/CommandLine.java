package io.phalanx.Presentation;

import io.phalanx.Logic.Site;
import io.phalanx.Logic.Scraping.HTML;
import io.phalanx.Logic.Utility.Processor;
import io.phalanx.Logic.Utility.Synchronizer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


public class CommandLine {
	// System Input scanner object
	Scanner in;
	Processor processor;
	
	/**
	 * Constructor for CommandLine object.
	 * Initializes Synchronizer object and restores SyncData from file
	 */
	public CommandLine(InputStream inputStream) {
		in = new Scanner(new InputStreamReader(inputStream));
		Synchronizer.restoreData();
		processor = new Processor();
	}
	
	private void prompt(){
		System.out.print("Ψ: ");
	}
	
	private void printHelp(){
		// Print out for help
		// TODO Make more user friendly and comprehensible
		System.out.println("List of commands:");
		System.out.println("download <SITE> <AREA/SUBAREA> <PAGES>");
		System.out.println("download <URL>");
		System.out.println("OR download <URL> <PAGES>");
		System.out.println("sync <SITE> <AREA/SUBAREA> <PAGES>");
		System.out.println("sync <URL>");
		System.out.println("OR sync <URL> <PAGES>");
		System.out.println("pull");
		System.out.println("unsync <SITE> <AREA/SUBAREA> <PAGES>");
		System.out.println("OR unsync <INDEX>");
		System.out.println("sync list");
		System.out.println("clear sync list");
		System.out.println("cls");
		System.out.println("config SIMULTANEOUS_DOWNLOADS <NUMBER>");
		System.out.println("config DOWNLOAD_DIRECTORY <PATH>");
		System.out.println("exit");
		System.out.println("\nNumber of pages == 0 means all pages.\n<SITE> == HypeMachine || SoundCloud || Mixcloud \n");
		System.out.println("For HypeMachine: <AREA/SUBAREA> == popular || <USERNAME> || artist/<ARTIST>");
		System.out.println("For SoundCloud: <AREA/SUBAREA> == <USERNAME>/[favorites || tracks]");
		System.out.println("For Mixcloud: <AREA/SUBAREA> == <USERNAME>/[activity || listens || favorites || null]");
		System.out.println("Examples:");
		System.out.println("download HypeMachine popular 3");
		System.out.println("download SoundCloud nnuages/tracks 0");
		System.out.println("download Mixcloud johndigweed/activity 1");
	}
	
	/*
	 * aka main() for this CommandLine object
	 */
	public void run(){
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
			switch (args.length) {
			case 1:
				if(args[0].equalsIgnoreCase("exit"))
					System.exit(0);
				else if(args[0].equalsIgnoreCase("help"))
					printHelp();
				else if(args[0].equalsIgnoreCase("cls"))
					processor.clear();
				else if(args[0].equalsIgnoreCase("pull"))
					processor.pull();
				else
					System.out.println("Need help? Just use the 'help' command!");
				break;
				
			case 2:
				if(args[0].equalsIgnoreCase("sync") && args[1].contains("list"))
					processor.printSyncList();
				else if(args[0].equalsIgnoreCase("download")){
					HTML html = new HTML(args[1]);
					html.setPagenumber(0);
					processor.download(html);
				}
				else if(args[0].equalsIgnoreCase("sync")){
					HTML html = new HTML(args[1]);
					html.setPagenumber(0);
					processor.sync(html);
				}
				else if(args[0].equalsIgnoreCase("ls") && args[1].contains("newtracks"))
					processor.printNewtracks();
				else if(args[0].equalsIgnoreCase("unsync"))
					processor.unsync(Integer.valueOf(args[1]));
				else if(args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("SIMULTANEOUS_DOWNLOADS"))
					processor.getSimultaneouDownloads();
				else if(args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("DOWNLOAD_DIRECTORY"))
					processor.getDownloadDirectory();
				else
					System.out.println("Need help? Just use the 'help' command!");
				break;
				
			case 3:
				if(args[0].equalsIgnoreCase("download")){
					HTML html = new HTML(args[1]);
					html.setPagenumber(Integer.parseInt(args[2]));
					processor.download(html);
				}
				else if(args[0].equalsIgnoreCase("sync")){
					HTML html = new HTML(args[1]);
					html.setPagenumber(Integer.parseInt(args[2]));
					processor.sync(html);
				}
				else if(args[0].equalsIgnoreCase("clear") && args[1].contains("sync") && args[2].contains("list"))
					processor.clearSyncList();
				else if(args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("DOWNLOAD_DIRECTORY"))
					processor.setDownloadDirectory(args[2]);
				else if(args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("SIMULTANEOUS_DOWNLOADS"))
					processor.setSimultaenousDownloads(args[2]);
				else
					System.out.println("Need help? Just use the 'help' command!");
				break;
				
			case 4:
				if(args[0].equalsIgnoreCase("download") && (Site.recognize(args[1]) != Site.Invalid))
					processor.download(Site.recognize(args[1]), args[2], args[3]);
				else if(args[0].equalsIgnoreCase("sync") && (Site.recognize(args[1]) != Site.Invalid))
					processor.sync(Site.recognize(args[1]), args[2], args[3]);
				else if(args[0].equalsIgnoreCase("unsync") && (Site.recognize(args[1]) != Site.Invalid))
					processor.unsync(Site.recognize(args[1]), args[2], args[3]);
				else if (Site.recognize(args[1]) == Site.Invalid)
					System.out.println("Unrecognized site. Go home. You're drunk");
				else
					System.out.println("Need help? Just use the 'help' command!");
				break;
				
			default:
				break;
			}
		}
	}
}
