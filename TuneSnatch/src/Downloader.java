import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.ArrayList;

import org.apache.commons.lang3.StringEscapeUtils;
//import org.farng.mp3.MP3File;
//import org.farng.mp3.TagException;
//import org.farng.mp3.id3.ID3v1;
import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;


public class Downloader extends Thread {
	
	private static String HYPEM_SERVE_URL = "http://hypem.com/serve/source/";
	private Map<String, String> COOKIES;
        private Track track;
        private ArrayList<Thread> downloaderArrayList = new ArrayList<Thread>();
	
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
	private long getFilesize(Track track) throws IOException{
		String url = null;
		Object trackClass = track.getClass();
                
		if(trackClass == HypeMachineTrack.class){
			COOKIES = ((HypeMachineTrack) track).getCookies();
			url = getHypeMachineTrackDownloadURL((HypeMachineTrack) track);
		} else if(trackClass == SoundCloudTrack.class){
			url = ((SoundCloudTrack) track).getStreamURL();
		} else if(trackClass == MixcloudTrack.class){
			url = getMixcloudTrackDownloadURL((MixcloudTrack) track);
		}
		
		URLConnection conn = new URL(url).openConnection();
	    
	    return conn.getContentLength();
	}
	
	/*
	 * Checks for filename and filesize currently.
	 * Might need to make it more robust.
	 */
	private boolean isDownloaded(Track track){
		File dir = new File(".");
		File[] files = dir.listFiles();
		
		for(File file : files){
			try {
				if(file.getName().equalsIgnoreCase(track.getArtist() + " - " + track.getSong() + ".mp3") && 
						file.length() == getFilesize(track))
					return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/*
	 * Loops through each Track in a TrackList and downloads it
	 * (if it hasn't been downloaded already)  
	 */
	public void downloadTracks(TrackList tracklist) {
            int j = 0;
            for(int i=0; i<tracklist.getSize(); i++){         
                    if(!isDownloaded(tracklist.getTrack(i))) {                                    
                            Thread trackDownloadThread = new Thread(new Downloader (tracklist.getTrack(i)));
                            downloaderArrayList.add(trackDownloadThread);
                            downloaderArrayList.get(j).start();
                            System.out.println("Thread "+j+ " running");
                            j++;
                    } else {
                            System.out.println(tracklist.getTrack(i).getArtist() + " - " + tracklist.getTrack(i).getSong() + " already downloaded");                    
                    }
            }
	}

	public void downloadTrack(Track track) throws IOException{
		String url = null;
		Object trackClass = track.getClass();
		/*
		 * HypeMachine won't serve the download URL if the request doesn't contain the same
		 * cookies as the initial request.
		 * Mixcloud needs some bruteforcing. Check getMixcloudTrackDownloadURL for more info.
		 */
		if(trackClass == HypeMachineTrack.class){
			COOKIES = ((HypeMachineTrack) track).getCookies();
			url = getHypeMachineTrackDownloadURL((HypeMachineTrack) track);
		} else if(trackClass == SoundCloudTrack.class){
			url = ((SoundCloudTrack) track).getStreamURL();
		} else if(trackClass == MixcloudTrack.class){
			url = getMixcloudTrackDownloadURL((MixcloudTrack) track);
		}
		
            URLConnection conn = new URL(url).openConnection();
	    InputStream is = conn.getInputStream();
	    
	    int filesize = conn.getContentLength();
	    long startTime = System.nanoTime();
	    final double NANOS_PER_SECOND = 1000000000.0;
	    final double BYTES_PER_MIB = 1024;
	    double speed = 0;
	    float totalDataWritten = 0;
	    float percentage = 0;
	    String unicodeFilename = track.getArtist() + " - " + track.getSong() + ".mp3"; // TODO Fix unicode filenames
//	    String normalizedFilename = Normalizer.normalize(unicodeFilename, Normalizer.Form.NFKD);					NONE
//	    String unicodeRegex = Pattern.quote("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");				OF THIS SHIT
//	    String filename = new String(normalizedFilename.replaceAll(unicodeRegex, "").getBytes("ascii"), "ascii");	WORKS.
	    String filename = StringEscapeUtils.unescapeHtml4(unicodeFilename).replaceAll("[^\\x20-\\x7e]", "");
	    
	    int exp = (int) (Math.log(conn.getContentLength()) / Math.log(1000));
	    String tracksize = String.format("%.2f", conn.getContentLength() / Math.pow(1000, exp));
	    File trackFile = new File("." + File.separator + StringEscapeUtils.unescapeHtml4(unicodeFilename).replaceAll("[^\\x20-\\x7e]", ""));
	    
	    OutputStream outstream = new FileOutputStream(trackFile);
	    byte[] buffer = new byte[8192];
	    int len;
	    System.out.println("Downloading " + filename + " [" + (tracksize) + " MB]");
	    System.out.print("0%");
	    while ((len = is.read(buffer)) > 0) {
	    	totalDataWritten += len;
	        outstream.write(buffer, 0, len);
	        percentage = (totalDataWritten * 100) / filesize;
	        speed = NANOS_PER_SECOND / BYTES_PER_MIB * totalDataWritten / (System.nanoTime() - startTime + 1);
	        String speedString = String.format("%.2f", speed); 
	        
	        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"); // Back, back, back dat ass up
	        System.out.print((int) percentage);
	        System.out.print("% ");
	        System.out.print(speedString);
	        System.out.print("KB/s ");
	    }
	    System.out.println("");
	    
	    
	    // TODO Proper ID3 tags for downloaded files
//	    try {
//			MP3File trackMP3File = new MP3File(trackFile);
//			if(trackMP3File.hasID3v1Tag()){
//				ID3v1 id3v1Tag = trackMP3File.getID3v1Tag();
//				System.out.println(id3v1Tag.toString());
//			} else {
//				System.out.println("No ID3v1 tag");
//			}
//		} catch (TagException e) {
//			e.printStackTrace();
//		}
	    
	    outstream.close();
	}
	/** END OF MAIN METHODS **/
	
	/** HELPER METHODS **/
	/*
	 * HypeMachine serves download URLs via a JSON response to a HTTP request in the following format:
	 * http://hypem.com/serve/source/<TRACK_ID>/<TRACK_KEY>/, where <TRACK_ID> is the track ID and <TRACK_KEY> is the track key
	 * 
	 * This method constructs the correct URL and parses the response.
	 */
	private String getHypeMachineTrackDownloadURL(HypeMachineTrack track) throws IOException{
		String COMPLETE_URL = HYPEM_SERVE_URL + track.getId() + "/" + track.getKey() + "/";
		Response res = Jsoup.connect(COMPLETE_URL).ignoreContentType(true).cookies(COOKIES).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0").execute();
		
		String strs[] = res.parse().toString().split("&quot;");
		return strs[11].replaceAll("\\\\", "");
	}
	
	/*
	 * I haven't found a direct way of extracting the download URL of a Mixcloud track.
	 * Mixcloud's track preview URLs and full download URLs are similar. The preview URL for 
	 * a Mixcloud track is simple to extract.
	 * 
	 * This method replaces the "previews" part of the preview URL with "cloudcasts/originals" and then
	 * cycles through all of Mixcloud's stream servers until the download URL is found.
	 * 
	 * Similarity between Mixcloud preview URL and full download URL:
	 * http://stream8.mxcdn.com/previews/9/6/a/e/93a8-2d77-4573-85c5-68bfb679d9bc.mp3 - preview URL
	 * http://stream11.mxcdn.com/cloudcasts/originals/9/6/a/e/93a8-2d77-4573-85c5-68bfb679d9bc.mp3 - download URL
	 */
	private String getMixcloudTrackDownloadURL(MixcloudTrack track) throws IOException {
		String downloadUrl = track.getPreviewURL().replaceAll("previews", "cloudcasts/originals");
		
		try {
			@SuppressWarnings("unused")
			Response res = Jsoup.connect(downloadUrl).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0").execute();
			return downloadUrl;
		} catch (HttpStatusException firstAttempt) {
			int serversToCycle = 30;
			for(int i=1; i<= serversToCycle; ){
				try{
					String cycledUrl = downloadUrl.replaceAll("stream[0-9]+", ("stream" + i));
					
					Response res = Jsoup.connect(cycledUrl).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0").execute();
					if(res.parse().toString().length() < 2000)
						i++;
					else 
						return cycledUrl;
				} catch (HttpStatusException cycledAttempt){
					i++;
				}
			}
		}
		
		return null;
	}
	/** END OF HELPER METHODS **/
}
