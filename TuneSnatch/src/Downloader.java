import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Downloader extends Thread {
	
    private static boolean VERBOSE = false; // Set to true to print out detailed download info (speed, percentage etc)
    
	private Downloader() {           
	}
        
	private static long getFilesize(Track track) throws IOException {
		String url = track.getStreamURL();
		URLConnection conn = new URL(url).openConnection();
		return conn.getContentLength();
	}
	
	private static String generateFilename(Track track){
		String illegalFilename = (track.getArtist() + " - " + track.getSong());
		
		// Escape illegal Windows and UNIX characters
		String legitFilename = illegalFilename.replaceAll("[.\\\\/:*?\"<>|]?[\\\\/:*?\"<>|]*", "");
		
		return legitFilename + ".mp3";
	}
	
	/*
	 * Checks for filename and filesize currently.
	 * Might need to make it more robust.
	 */
	private static boolean isDownloaded(Track track){
		boolean emptyDir = false;
		File downloadDirectory = new File(UserProfile.getDownloadDirectory());
		if(!downloadDirectory.exists()){
			downloadDirectory.mkdir();
			emptyDir = true;
		}
		
		File dir = new File(UserProfile.getDownloadDirectory());
		File[] files = dir.listFiles();
		
		if(!emptyDir){
			for(File file : files){
				try {
					if(file.getName().equalsIgnoreCase(generateFilename(track)) && 
							file.length() == getFilesize(track))
								return true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return false;
	}
	
	public static void downloadTrack(Track track) throws IOException {
		if(!isDownloaded(track)){
			String url = track.getStreamURL();
	        URLConnection conn = null;
	        boolean malformedURL = false;
	        
	        try{
	        	conn = new URL(url).openConnection();
	        } catch (MalformedURLException e){
	        	malformedURL = true; 
	        }
	        
	        if(!malformedURL){
	        	InputStream is = conn.getInputStream();
	    	    
	        	int filesize = conn.getContentLength();
	    	    long startTime = System.nanoTime();
	    	    final double NANOS_PER_SECOND = 1000000000.0;
	    	    final double BYTES_PER_MIB = 1024;
	    	    double speed = 0;
	    	    float totalDataWritten = 0;
	    	    float percentage = 0;
	    	    
	    	    String filename = generateFilename(track);
	    	    int exp = (int) (Math.log(conn.getContentLength()) / Math.log(1000));
	    	    String tracksize = String.format("%.2f", conn.getContentLength() / Math.pow(1000, exp));
	    	    
	    	    File downloadDirectory = new File(UserProfile.getDownloadDirectory());
	    	    File trackFile = new File(downloadDirectory.getAbsolutePath() + File.separator + filename);
	    	    
	    	    OutputStream outstream = new FileOutputStream(trackFile);
	    	    byte[] buffer = new byte[8192];
	    	    int len;
	    	    System.out.println("Downloading " + filename + " [" + (tracksize) + " MB]");
	    	    if(VERBOSE)
	    	    	System.out.print("0%");
	    	    while ((len = is.read(buffer)) > 0) {
	    	    	outstream.write(buffer, 0, len);
	    	    	
	    	    	if(VERBOSE){
	    	    		totalDataWritten += len;
	    		        percentage = (totalDataWritten * 100) / filesize;
	    		        speed = NANOS_PER_SECOND / BYTES_PER_MIB * totalDataWritten / (System.nanoTime() - startTime + 1);
	    		        String speedString = String.format("%.2f", speed); 
	    		        
	    		        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"); // Back, back, back dat ass up
	    		        System.out.print((int) percentage);
	    		        System.out.print("% ");
	    		        System.out.print(speedString);
	    		        System.out.print("KB/s ");
	    	    	}
	    	    }
	    	    if(VERBOSE)
	    	    	System.out.println("");
	    	    
	    	    outstream.close();
	        }
		}
	}
}
