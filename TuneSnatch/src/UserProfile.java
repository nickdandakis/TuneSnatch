import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.prefs.Preferences;

public class UserProfile {
	private static String dataDirectory;
	private static Preferences preferences;
	
	static { // Static 'constructor'
		File datadir = new File(defaultDataDirectory() + File.separator + ".TuneSnatch");
		if(!datadir.exists()){
			datadir.mkdir();
		}
		setDataDirectory(datadir.getPath());
		
	    setPreferences(Preferences.userRoot().node(UserProfile.class.getName()));
	}
	
	private UserProfile(){
	}
	
	public static String getDataDirectory() {
		return dataDirectory;
	}

	public static void setDataDirectory(String dataDirectory) {
		UserProfile.dataDirectory = dataDirectory;
	}
	
	public static Preferences getPreferences() {
		return preferences;
	}

	public static void setPreferences(Preferences preferences) {
		UserProfile.preferences = preferences;
	}
	
	public static String getDownloadDirectory(){
		return UserProfile.preferences.get("DOWNLOAD_DIRECTORY", System.getProperty("user.home") + File.separator + "music" + File.separator + "TuneSnatch");
	}
	
	public static void setDownloadDirectory(String path){
		UserProfile.preferences.put("DOWNLOAD_DIRECTORY", path);
	}
	
	public static int getSimultaneousDownloads(){
		return UserProfile.preferences.getInt("SIMULTANEOUS_DOWNLOADS", 5);
	}
	
	public static void setSimultaneousDownloads(int limit){
		UserProfile.preferences.putInt("SIMULTANEOUS_DOWNLOADS", limit);
	}
	
	static String defaultDataDirectory() {
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
	
	public static void saveData(Object obj, File file){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(obj);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Object restoreData(File file){
		Object obj = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			obj = ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found, making it now.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public static void clearData(File file){
		saveData((Object) null, file);
	}

	
}
