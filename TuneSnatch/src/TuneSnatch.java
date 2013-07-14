//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;

//import org.jsoup.Connection.Response;
//import org.jsoup.Jsoup;
//import org.jsoup.select.Elements;

public class TuneSnatch {
	
	public static void test() throws MalformedURLException, IOException{
//		SoundHTML testHtml = new SoundHTML("nick-dandakis/favorites", 1);
		HypeMachineHTML popular1HTML = new HypeMachineHTML("popular", 1);
		HypeMachineHTML popular2HTML = new HypeMachineHTML("popular", 2);
		TrackList tracklist = new TrackList();
//		Downloader dw = new Downloader();
		Synchronizer sz = new Synchronizer();
		
//		tracklist.addTracks(testHtml);
		tracklist.addTracks(popular1HTML);
		tracklist.addTracks(popular2HTML);
		
//		dw.download(tracklist.getTrack(0));
//		dw.download(tracklist.getTrack(1));
//		dw.download(tracklist.getTrack(4));
//		dw.download(tracklist.getTrack(10));
//		dw.download(tracklist.getTrack(11));
//		dw.download(tracklist.getTrack(35));
//		dw.download(tracklist.getTrack(40));
		
		sz.addHTML(popular1HTML);
//		sz.addHTML(testHtml);
		sz.addHTML(popular2HTML);
	}
	
	public static void main(String[] args) throws IOException {
		CommandLine cmd = new CommandLine();	
		cmd.launch();
//		try {
//			test();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		/*
		 * Mixcloud HTML scraping. Need to actually fully implement this.
		 */
//		Response res = Jsoup.connect("http://www.mixcloud.com/tag/guaba-beach-bar/").userAgent("Mozilla/5.0 (Windows NT 6.1; rv:17.0) Gecko/20100101 Firefox/17.0").execute();
//		File htmlFile = new File("." + File.separator + "mixcloudTest.html");
//		BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFile.getAbsoluteFile()));
//		bw.write(res.parse().toString());
//		bw.close();
//		
//		Elements playerPlay = res.parse().getElementsByAttribute("data-preview-url");
//		for(int i=0; i<playerPlay.size(); i++){
//			System.out.println(playerPlay.get(i).attr("data-preview-url"));
//		}
		
	}

}
