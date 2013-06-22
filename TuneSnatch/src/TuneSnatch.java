import java.io.IOException;
import java.net.MalformedURLException;

public class TuneSnatch {
	
	public static void test() throws MalformedURLException, IOException{
		SoundHTML testHtml = new SoundHTML("nick-dandakis/favorites", 1);
		HypeHTML popular1HTML = new HypeHTML("popular", 1);
		HypeHTML popular2HTML = new HypeHTML("popular", 2);
		TrackList tracklist = new TrackList();
		Downloader dw = new Downloader();
		
		tracklist.addTracks(testHtml);
		tracklist.addTracks(popular1HTML);
		tracklist.addTracks(popular2HTML);
		
		dw.download(tracklist.getTrack(0));
		dw.download(tracklist.getTrack(1));
		dw.download(tracklist.getTrack(4));
		dw.download(tracklist.getTrack(10));
		dw.download(tracklist.getTrack(11));
		dw.download(tracklist.getTrack(35));
		dw.download(tracklist.getTrack(40));
	}
	
	public static void main(String[] args) throws IOException {
		CommandLine cmd = new CommandLine();	
		cmd.launch();
//		try {
//			test();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
