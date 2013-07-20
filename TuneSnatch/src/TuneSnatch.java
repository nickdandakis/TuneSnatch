import java.io.IOException;
import java.net.MalformedURLException;

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
		HypeMachineHTML mxcld = new HypeMachineHTML("http://www.hypem.com/nickdandakis");
		System.out.println(mxcld.toString());
		
		MixcloudHTML mxcld2 = new MixcloudHTML("http://www.mixcloud.com/ambientblog/favorites");
		System.out.println(mxcld2.toString());
		
//		CommandLine cmd = new CommandLine();	
//		cmd.launch();
//		try {
//			test();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}

}
