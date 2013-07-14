import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
//import org.farng.mp3.MP3File;
//import org.farng.mp3.TagException;
//import org.farng.mp3.id3.ID3v1;
import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;


public class Downloader {

	private static String HYPEM_SERVE_URL = "http://hypem.com/serve/source/";
	private Map<String, String> COOKIES;
	
	public Downloader() {
	}
	
	private String getHypeMachineTrackDownloadURL(HypeMachineTrack track) throws IOException{
		String COMPLETE_URL = HYPEM_SERVE_URL + track.getID() + "/" + track.getKEY() + "/";
		Response res = Jsoup.connect(COMPLETE_URL).ignoreContentType(true).cookies(COOKIES).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0").execute();
		
		String strs[] = res.parse().toString().split("&quot;");
		return strs[11].replaceAll("\\\\", "");
	}
	
	private String getMixcloudTrackDownloadURL(MixcloudTrack track) throws IOException {
		String downloadUrl = track.getPREVIEW_URL().replaceAll("previews", "cloudcasts/originals");
		
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
	
	private long getFilesize(Track track) throws IOException{
		String url = null;
		
		if(track.getClass() == HypeMachineTrack.class){
			COOKIES = ((HypeMachineTrack) track).getCOOKIES();
			url = getHypeMachineTrackDownloadURL((HypeMachineTrack) track);
			
		} else if(track.getClass() == SoundCloudTrack.class){
			url = ((SoundCloudTrack) track).getSTREAMURL();
		} else if(track.getClass() == MixcloudTrack.class){
			url = getMixcloudTrackDownloadURL((MixcloudTrack) track);
		}
		
		URLConnection conn = new URL(url).openConnection();
	    
	    return conn.getContentLength();
	}
	
	private boolean isDownloaded(Track track){
		File dir = new File(".");
		File[] files = dir.listFiles();
		
		for(File file : files){
			try {
				if(file.getName().equalsIgnoreCase(track.getARTIST() + " - " + track.getSONG() + ".mp3") && 
						file.length() == getFilesize(track))
					return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public void downloadTracklist(TrackList tracklist){
		for(int i=0; i<tracklist.getSize(); i++){
			try {
				if(!isDownloaded(tracklist.getTrack(i)))
					downloadTrack(tracklist.getTrack(i));
				else
					System.out.println(tracklist.getTrack(i).getARTIST() + " - " + tracklist.getTrack(i).getSONG() + " already downloaded");
			} catch (IOException e) {
				System.out.println("Invalid HTTP request");
			}
		}
	}

	public void downloadTrack(Track track) throws IOException{
		String url = null;
		
		if(track.getClass() == HypeMachineTrack.class){
			COOKIES = ((HypeMachineTrack) track).getCOOKIES();
			url = getHypeMachineTrackDownloadURL((HypeMachineTrack) track);
			
		} else if(track.getClass() == SoundCloudTrack.class){
			url = ((SoundCloudTrack) track).getSTREAMURL();
		} else if(track.getClass() == MixcloudTrack.class){
			url = getMixcloudTrackDownloadURL((MixcloudTrack) track);
			System.out.println(url);
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
	    String unicodeFilename = track.getARTIST() + " - " + track.getSONG() + ".mp3";
//	    String normalizedFilename = Normalizer.normalize(unicodeFilename, Normalizer.Form.NFKD);					NONE
//	    String unicodeRegex = Pattern.quote("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");				OF THIS SHIT
//	    String filename = new String(normalizedFilename.replaceAll(unicodeRegex, "").getBytes("ascii"), "ascii");	WORKS.
	    String filename = StringEscapeUtils.unescapeHtml4(unicodeFilename).replaceAll("[^\\x20-\\x7e]", "");
	    
//	    System.out.println(unicodeFilename);
//	    System.out.println(normalizedFilename);
	    System.out.println(StringEscapeUtils.unescapeHtml4(unicodeFilename).replaceAll("[^\\x20-\\x7e]", ""));		// OR THIS SHIT.
	    
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
}
