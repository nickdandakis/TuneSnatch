import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.nodes.Element;


public class TrackList {
	
	private enum FilterKey{
		HypeMachine_id, HypeMachine_key, HypeMachine_artist, HypeMachine_song, HypeMachine_posturl,
		SoundCloud_id, SoundCloud_username, SoundCloud_song, SoundCloud_streamUrl, SoundCloud_waveformUrl
	}
	private ArrayList<Track> tracks = new ArrayList<Track>();
	
	public TrackList() {
	}
	
	private String filter(String unfiltered, FilterKey key){
		Scanner scanner = new Scanner(unfiltered);
		
		switch (key) {
		case HypeMachine_id:
			return scanner.findInLine("\"id\":\"[a-z0-9]+").replaceAll("\"id\":\"", "");
		case HypeMachine_key:
			return scanner.findInLine("\"key\":\"[a-z0-9]+").replaceAll("\"key\":\"", "");
		case HypeMachine_artist:
			return scanner.findInLine("\"artist\":\"[^\"]+").replaceAll("\"artist\":\"", "");
		case HypeMachine_song:
			return scanner.findInLine("\"song\":\"[^\"]+").replaceAll("\"song\":\"", "");
		case HypeMachine_posturl:
			return scanner.findInLine("\"posturl\":\".*?\"").replaceAll("\"posturl\":\"", "").replaceAll("\\\\", "").replaceAll("\"", "");
		case SoundCloud_id:
			return scanner.findInLine("\"id\":[0-9]+").replaceAll("\"id\":", "");
		case SoundCloud_username:
			return scanner.findInLine("\"username\":\"[^\"]+").replaceAll("\"username\":\"", "");
		case SoundCloud_song:
			return scanner.findInLine("\"title\":\"[^\"]+").replaceAll("\"title\":\"", "");
		case SoundCloud_streamUrl:
			return scanner.findInLine("\"streamUrl\":\".*?\"").replaceAll("\"streamUrl\":\"", "").replaceAll("\"", "");
		case SoundCloud_waveformUrl:
			return scanner.findInLine("\"waveformUrl\":\".*?\"").replaceAll("\"waveformUrl\":\"", "").replaceAll("\"", "");
		default:
			return null;
		}
	}
	
	public Track getTrack(int i){
		return tracks.get(i);
	}
	
	public int getSize(){
		return tracks.size();
	}
	
	private int countSubstring(String subStr, String str){
		return (str.length() - str.replace(subStr, "").length()) / subStr.length();
	}
	
	public void addTracks(HTML html) throws IOException{
		if(html.getClass() == HypeMachineHTML.class){
			Element trackList = ((HypeMachineHTML) html).getDoc().getElementById("displayList-data");
			
			String[] strs = trackList.toString().split("\\[\\{", 2);
			strs = strs[1].split("}]",2);
			Scanner lineScanner = new Scanner(strs[0]).useDelimiter("\\},\\{");
			String trackString, id, key, artist, song, posturl;
			
			while (lineScanner.hasNext()){
				trackString = lineScanner.next();
				
				id = filter(trackString, FilterKey.HypeMachine_id);
				key = filter(trackString, FilterKey.HypeMachine_key);
				artist = filter(trackString, FilterKey.HypeMachine_artist);
				song = filter(trackString, FilterKey.HypeMachine_song);
				posturl = filter(trackString, FilterKey.HypeMachine_posturl);
				
				tracks.add(new HypeMachineTrack(id, key, artist, song, posturl, ((HypeMachineHTML) html).getCOOKIES()));
			}		
		} else if(html.getClass() == SoundCloudHTML.class){
			String htmlstring = ((SoundCloudHTML) html).getDoc();
			
			Scanner scanner = new Scanner(htmlstring);
			String trackString, username, song, streamUrl, waveformUrl, id;
			
			for(int i = countSubstring("window.SC.bufferTracks.push", htmlstring); i>0; i--){
				trackString = scanner.findWithinHorizon("window.SC.bufferTracks.push\\(\\{[^<]+", 0);
//				System.out.println(trackString);
				
				id = filter(trackString, FilterKey.SoundCloud_id);
//				System.out.println("id: " + id);
				username = filter(trackString, FilterKey.SoundCloud_username);
//				System.out.println("username: " + username);
				song = filter(trackString, FilterKey.SoundCloud_song);
//				System.out.println("song: " + song);
				streamUrl = filter(trackString, FilterKey.SoundCloud_streamUrl);
//				System.out.println("streamurl: " + streamUrl);
				waveformUrl = filter(trackString, FilterKey.SoundCloud_waveformUrl);
//				System.out.println("waveformurl: " + waveformUrl);
				
				tracks.add(new SoundCloudTrack(id, username, song, streamUrl, waveformUrl));
			}
		}
		
		
	}
}
