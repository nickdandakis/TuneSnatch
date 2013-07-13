import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UserProfile {
	private String DATA_DIRECTORY;
	
	public UserProfile(){
		File datadir = new File(defaultDirectory() + File.separator + ".TuneSnatch");
		if(!datadir.exists()){
			datadir.mkdir();
		}
		
		setDATA_DIRECTORY(datadir.getPath());
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
	
	public Object restoreData(File file){
		Object obj = null;
		try {
			@SuppressWarnings("resource")
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			obj = ois.readObject();
		} catch (FileNotFoundException e) {
			System.out.println("File not found, making it now.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public void saveData(Object obj, File file){
		try {
			@SuppressWarnings("resource")
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(obj);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clearData(File file){
		saveData((Object) null, file);
	}

	public String getDATA_DIRECTORY() {
		return DATA_DIRECTORY;
	}

	public void setDATA_DIRECTORY(String dATA_DIRECTORY) {
		DATA_DIRECTORY = dATA_DIRECTORY;
	}
}
