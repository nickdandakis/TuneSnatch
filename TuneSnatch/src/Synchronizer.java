import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class Synchronizer {

	private ArrayList<HTML> syncdata = new ArrayList<HTML>();
	private String DIR;
	private File syncDataFile;
	
	public Synchronizer() {
		File datadir = new File(defaultDirectory() + File.separator + ".TuneSnatch");
		if(!datadir.exists()){
			datadir.mkdir();
		}
		
		DIR = datadir.getPath();
		syncDataFile = new File(DIR + File.separator + "sync.dat");
	}

	public ArrayList<HTML> getSyncdata() {
		return syncdata;
	}

	public void setSyncdata(ArrayList<HTML> syncdata) {
		this.syncdata = syncdata;
	}

	public void addHTML(HTML html){
		//Add validation
		boolean exists = false;
		for(HTML syncdhtml : syncdata){
			if(syncdhtml.getCOMPLETE_URL().equalsIgnoreCase(html.getCOMPLETE_URL())){
				exists = true;
				break;
			}
		}

		if(!exists)
			syncdata.add(html);
		else
			System.out.println(html.getCOMPLETE_URL() + " is already kept track of");
		
		saveSyncData();
	}
	
	public void removeHTML(HTML html){
		syncdata.remove(html);
	}
	
	public void printSyncData(){
		for(HTML html : syncdata){
			System.out.println(html.getCOMPLETE_URL());
		}
		if(syncdata.size() == 0)
			System.out.println("Not keeping track of anything. ");
	}
	
	public void clearSyncData(){
		syncdata = new ArrayList<HTML>();
	}
	
	private static String defaultDirectory() {
	    String OS = System.getProperty("os.name").toUpperCase();
	    if (OS.contains("WIN"))
	        return System.getenv("APPDATA");
	    else if (OS.contains("MAC"))
	        return System.getProperty("user.home") + "/Library/Application "
	                + "Support";
	    else if (OS.contains("NUX"))
	        return System.getProperty("user.home");
	    return System.getProperty("user.dir");
	}

	@SuppressWarnings("unchecked")
	public void restoreSyncData(){
		System.out.println("Restoring sync'd");
		
		try {
			@SuppressWarnings("resource")
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(syncDataFile));
			Object obj = ois.readObject();
			
			if(obj instanceof ArrayList<?>){
				syncdata = (ArrayList<HTML>) obj;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found, making it now.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void saveSyncData(){
		try {
			@SuppressWarnings("resource")
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(syncDataFile));
			oos.writeObject(syncdata);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
