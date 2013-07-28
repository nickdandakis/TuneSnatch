import java.io.InputStreamReader;
import java.util.Scanner;


public class CommandLine {
	// System Input scanner object
	Scanner in = new Scanner(new InputStreamReader(System.in));
	Executor exe;
	
	/**
	 * Constructor for CommandLine object.
	 * Initializes Synchronizer object and restores SyncData from file
	 */
	public CommandLine() {
		exe = new Executor();
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
		System.out.println("OR unsync <INDEX>");
		System.out.println("sync list");
		System.out.println("ls newtracks");
		System.out.println("clear sync list");
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
					exe.clear();
				if(args[0].equalsIgnoreCase("pull"))
					exe.pull();
			} else if(args.length == 2){
				if(args[0].equalsIgnoreCase("sync") && args[1].contains("list"))
					exe.printSyncList();
				if(args[0].equalsIgnoreCase("ls") && args[1].contains("newtracks"))
					exe.printNewtracks();
				if(args[0].equalsIgnoreCase("unsync"))
					exe.unsync(Integer.valueOf(args[1]));
			} else if(args.length == 3){
				if(args[0].equalsIgnoreCase("clear") && args[1].contains("sync") && args[2].contains("list"))
					exe.clearSyncList();
			} else if(args.length == 4){
				if(args[0].equalsIgnoreCase("download"))
					exe.download(args[1], args[2], args[3]);
				if(args[0].equalsIgnoreCase("sync"))
					exe.sync(args[1], args[2], args[3]);
				if(args[0].equalsIgnoreCase("unsync"))
					exe.unsync(args[1], args[2], args[3]);
			}
		}
	}
}
