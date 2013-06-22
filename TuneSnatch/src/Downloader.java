import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;


public class Downloader {

	private static String HYPEM_SERVE_URL = "http://hypem.com/serve/source/";
	private Map<String, String> COOKIES;
	
	public Downloader() {
		
	}
	
	private String computeDownloadURL(HypeTrack track) throws IOException{
		String COMPLETE_URL = HYPEM_SERVE_URL + track.getID() + "/" + track.getKEY() + "/";
		Response res = Jsoup.connect(COMPLETE_URL).ignoreContentType(true).cookies(COOKIES).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0").execute();
		
		String strs[] = res.parse().toString().split("&quot;");
		return strs[11].replaceAll("\\\\", "");
	}

	public void download(Track track) throws IOException{
		String url = null;
		
		if(track.getClass() == HypeTrack.class){
			COOKIES = ((HypeTrack) track).getCOOKIES();
			url = computeDownloadURL((HypeTrack) track);
			
		} else if(track.getClass() == SoundTrack.class){
			url = ((SoundTrack) track).getSTREAMURL();
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
	    String filename = track.getARTIST() + " - " + track.getSONG() + ".mp3";
	    int exp = (int) (Math.log(conn.getContentLength()) / Math.log(1000));
	    String tracksize = String.format("%.2f", conn.getContentLength() / Math.pow(1000, exp));
	    
	    OutputStream outstream = new FileOutputStream(new File("." + File.separator + filename));
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
	        
	        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
	        System.out.print((int) percentage);
	        System.out.print("% ");
	        System.out.print(speedString);
	        System.out.print("KB/s ");
	    }
	    System.out.println("");
	    outstream.close();
	}

	
	
}
