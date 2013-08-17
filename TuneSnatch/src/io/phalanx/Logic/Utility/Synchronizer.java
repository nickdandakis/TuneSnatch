package io.phalanx.Logic.Utility;
import io.phalanx.Data.UserProfile;
import io.phalanx.Logic.Site;
import io.phalanx.Logic.Scraping.HTML.HTML;

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
			if(html.getArea().equalsIgnoreCase(syncdhtml.getArea()) && html.getPagenumber() == syncdhtml.getPagenumber() &&
					((syncdhtml.getSite() == Site.HypeMachine && html.getSite() == Site.HypeMachine) ||
							(syncdhtml.getSite() == Site.SoundCloud && html.getSite() == Site.SoundCloud) ||
								(syncdhtml.getSite() == Site.Mixcloud && html.getSite() == Site.Mixcloud))){
				exists = true;
				break;
			}
		}

		if(!exists){
			html.setDocument(null); // Because jsoup.org.Document is not serializable...
			data.add(html);
		}
		else
			System.out.println(html.toString() + "\nis already kept track of");
		
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
		if(data.size() == 0)
			System.out.println("Not keeping track of anything. ");
		else{
			int i=0;
			for(HTML html : data){
				System.out.printf("[%d] %s - %s - %d\n", ++i, html.getSite().toString(), html.getArea(), html.getPagenumber());
			}
		}
	}
	
	public static void clearData(){
		data.clear();
		UserProfile.clearData(dataFile);
	}
	
	public static void restoreData(){
		System.out.println("Restoring sync data");
		ArrayList<?> restoredObject = (ArrayList<?>) UserProfile.restoreData(dataFile);
		ArrayList<HTML> checked = new ArrayList<HTML>();
		data.clear();
		
		if(restoredObject != null) {
			for(Object obj : restoredObject){
				if(obj instanceof HTML && obj != null)
					checked.add((HTML) obj);
			}
			data = checked;
		}
	}
	
	private static void saveData(){
		UserProfile.saveData(data, dataFile);
	}
	
}
