import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Downloader extends Thread {
	
    private Track track;
    private ArrayList<Thread> downloaderArrayList = new ArrayList<Thread>();
    
    private long beginTime;
    private long endTime;
    
    private static boolean VERBOSE = false; // Set to true to print out detailed download info (speed, percentage etc)
    
	public Downloader() {           
	}
        
    public Downloader(Track track) {
        this.track = track;
    }
    
    public void run() {
        try {
            downloadTrack(track);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
	
	/** MAIN METHODS **/
//	private long getFilesize(Track track) throws IOException {
//            String url = track.getStreamURL();
//            URLConnection conn = new URL(url).openConnection();
//            return conn.getContentLength();
//	}
	
	private String generateFilename(Track track){
		String illegalFilename = (track.getArtist() + " - " + track.getSong());
		
		// Escape illegal Windows and UNIX characters
		String legitFilename = illegalFilename.replaceAll("[.\\\\/:*?\"<>|]?[\\\\/:*?\"<>|]*", "");
		
		return legitFilename + ".mp3";
	}
	
	/*
	 * Checks for filename and filesize currently.
	 * Might need to make it more robust.
	 */
	private boolean isDownloaded(Track track){
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
//				try {
					if(file.getName().equalsIgnoreCase(generateFilename(track))) //&& 
//	                                    file.length() == getFilesize(track))
	                                    return true;
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}
		}
		
		return false;
	}
	
	/*
	 * Loops through each Track in a TrackList and downloads it
	 * (if it hasn't been downloaded already)  
	 */
	public void downloadTracks(TrackList tracklist) throws InterruptedException, IOException {
        int j = 0;
        beginTime = System.currentTimeMillis();
        
        for(int i=0; i<tracklist.getSize(); i++){         
                if(!isDownloaded(tracklist.getTrack(i))) {                                    
                        Thread trackDownloadThread = new Thread(new Downloader (tracklist.getTrack(i)));
                        downloaderArrayList.add(trackDownloadThread);
//                        System.out.println("Array size: "+downloaderArrayList.size());
//                        System.out.println("Thread "+j+ " About to start");
                        downloaderArrayList.get(j).start();
//                        System.out.println("Thread "+j+ " running");
                        j++;
                } else {
                        System.out.println(generateFilename(tracklist.getTrack(i)) + " already downloaded");                    
                }
        }
        for(int k=0; k<downloaderArrayList.size(); k++) {
            downloaderArrayList.get(k).join();
//            System.out.println("*****Thread "+k+" completed*****");
        }
        
        endTime = System.currentTimeMillis();
        System.out.println("****Downloads Complete!****");
        System.out.println("Time take = "+(endTime - beginTime)+" ms");
    }

	public void downloadTrack(Track track) throws IOException {
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
	/** END OF MAIN METHODS **/
}
