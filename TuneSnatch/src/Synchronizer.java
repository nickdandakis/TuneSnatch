import java.io.File;
import java.util.ArrayList;


public class Synchronizer {

	private ArrayList<HTML> syncdata = new ArrayList<HTML>();
	private File syncDataFile;
	private UserProfile userProfile;
	
	public Synchronizer() {
		userProfile = new UserProfile();
		syncDataFile = new File(userProfile.getDataDirectory() + File.separator + "sync.dat");
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
			if(syncdhtml.getCompleteURL().equalsIgnoreCase(html.getCompleteURL())){
				exists = true;
				break;
			}
		}

		if(!exists)
			syncdata.add(html);
		else
			System.out.println(html.getCompleteURL() + " is already kept track of");
		
		saveSyncData();
	}
	
	public void removeHTML(HTML html){
		syncdata.remove(html);
		saveSyncData();
	}
	
	public void removeHTML(int index){
		syncdata.remove(index);
		saveSyncData();
	}
	
	public void printSyncData(){
		int i=0;
		for(HTML html : syncdata){
			System.out.print(String.format("[%d]: ", ++i));
			System.out.println(html.getCompleteURL());
		}
		if(syncdata.size() == 0)
			System.out.println("Not keeping track of anything. ");
	}
	
	public void clearSyncData(){
		syncdata = new ArrayList<HTML>();
		userProfile.clearData(syncDataFile);
	}
	
	public void restoreSyncData(){
		System.out.println("Restoring sync data");
		ArrayList<?> restoredObject = (ArrayList<?>) userProfile.restoreData(syncDataFile);
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
		userProfile.saveData(syncdata, syncDataFile);
	}
	
}
