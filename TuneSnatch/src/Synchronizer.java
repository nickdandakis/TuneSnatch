import java.io.File;
import java.util.ArrayList;

public class Synchronizer {

	private static ArrayList<HTML> data = new ArrayList<HTML>();
	private static File dataFile;
	
	static{
		dataFile = new File(UserProfile.getDataDirectory() + File.separator + "sync.dat");
	}
	private Synchronizer() {
		
	}

	public static ArrayList<HTML> getSyncdata() {
		return data;
	}

	public static void setSyncdata(ArrayList<HTML> syncdata) {
		Synchronizer.data = syncdata;
	}

	public static void addHTML(HTML html){
		//Add validation
		boolean exists = false;
		for(HTML syncdhtml : data){
			if(syncdhtml.getCompleteURL().equalsIgnoreCase(html.getCompleteURL())){
				exists = true;
				break;
			}
		}

		if(!exists){
			html.setDocument(null); // Because jsoup.org.Document is not serializable...
			data.add(html);
		}
		else
			System.out.println(html.getCompleteURL() + " is already kept track of");
		
		saveData();
	}
	
	public static void removeHTML(HTML html){
		data.remove(html);
		saveData();
	}
	
	public static void removeHTML(int index){
		data.remove(index);
		saveData();
	}
	
	public static void printData(){
		int i=0;
		for(HTML html : data){
			System.out.print(String.format("[%d]: ", ++i));
			System.out.println(html.getCompleteURL());
		}
		if(data.size() == 0)
			System.out.println("Not keeping track of anything. ");
	}
	
	public static void clearData(){
		data = new ArrayList<HTML>();
		UserProfile.clearData(dataFile);
	}
	
	public static void restoreData(){
		System.out.println("Restoring sync data");
		ArrayList<?> restoredObject = (ArrayList<?>) UserProfile.restoreData(dataFile);
		ArrayList<HTML> checked = new ArrayList<HTML>();
		
		if(restoredObject != null) {
			for(Object obj : restoredObject){
				if(obj instanceof HTML)
					checked.add((HTML) obj);
			}
			data = checked;
		}
	}
	
	private static void saveData(){
		UserProfile.saveData(data, dataFile);
	}
	
}
