package io.phalanx.Presentation;

import io.phalanx.Logic.Utility.Processor;

import java.util.Scanner;


public class PreProcessor implements Runnable {
	// System Input scanner object
	Scanner in = new Scanner(System.in);
	
	/**
	 * Constructor for CommandLine object.
	 * Initializes Synchronizer object and restores SyncData from file
	 */
	public PreProcessor() {
	}
	
	private void prompt(){
		System.out.print(">> ");
	}
	
	private void printHelp(){
		// Print System.out for help
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
		for(prompt(); in.hasNext(); prompt()){
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
					Processor.clear();
				else if(args[0].equalsIgnoreCase("pull"))
					Processor.pull();
				else
					System.out.println("Need help? Just use the 'help' command!");
				break;
				
			case 2:
				if(args[0].equalsIgnoreCase("sync") && args[1].contains("list"))
					Processor.printSyncList();
				else if(args[0].equalsIgnoreCase("download"))
					Processor.download(args[1]);
				else if(args[0].equalsIgnoreCase("sync"))
					Processor.sync(args[1]);
				else if(args[0].equalsIgnoreCase("ls") && args[1].contains("newtracks"))
					Processor.printNewtracks();
				else if(args[0].equalsIgnoreCase("unsync"))
					Processor.unsync(Integer.valueOf(args[1]));
				else if(args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("SIMULTANEOUS_DOWNLOADS"))
					Processor.getSimultaneouDownloads();
				else if(args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("DOWNLOAD_DIRECTORY"))
					Processor.getDownloadDirectory();
				else
					System.out.println("Need help? Just use the 'help' command!");
				break;
				
			case 3:
				if(args[0].equalsIgnoreCase("download"))
					Processor.download(args[1], args[2]);
				else if(args[0].equalsIgnoreCase("sync"))
					Processor.sync(args[1], args[2]);
				else if(args[0].equalsIgnoreCase("clear") && args[1].contains("sync") && args[2].contains("list"))
					Processor.clearSyncList();
				else if(args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("DOWNLOAD_DIRECTORY"))
					Processor.setDownloadDirectory(args[2]);
				else if(args[0].equalsIgnoreCase("config") && args[1].equalsIgnoreCase("SIMULTANEOUS_DOWNLOADS"))
					Processor.setSimultaneouslyDownloads(args[2]);
				else
					System.out.println("Need help? Just use the 'help' command!");
				break;
				
			case 4:
				if(args[0].equalsIgnoreCase("download"))
					Processor.download(args[1], args[2], args[3]);
				else if(args[0].equalsIgnoreCase("sync"))
					Processor.sync(args[1], args[2], args[3]);
				else if(args[0].equalsIgnoreCase("unsync"))
					Processor.unsync(args[1], args[2], args[3]);
				else
					System.out.println("Need help? Just use the 'help' command!");
				break;
				
			default:
				break;
			}
		}
	}
}
