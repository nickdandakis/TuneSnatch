import java.io.File;
import java.util.ArrayList;


public class Synchronizer extends UserProfile{

	private ArrayList<HTML> syncdata = new ArrayList<HTML>();
	private File syncDataFile;
	private UserProfile USERPROFILE;
	
	public Synchronizer() {
		USERPROFILE = new UserProfile();
		syncDataFile = new File(USERPROFILE.getDATA_DIRECTORY() + File.separator + "sync.dat");
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
		USERPROFILE.clearData(syncDataFile);
	}
	
	public void restoreSyncData(){
		System.out.println("Restoring sync'd");
		ArrayList<?> restoredObject = (ArrayList<?>) USERPROFILE.restoreData(syncDataFile);
		ArrayList<HTML> checked = new ArrayList<HTML>();
		
		if(restoredObject != null) {
			for(Object obj : restoredObject){
				if(obj instanceof HTML)
					checked.add((HTML) obj);
			}
			syncdata = checked;
		}
	}
	
	private void saveSyncData(){
		USERPROFILE.saveData(syncdata, syncDataFile);
	}
	
}
