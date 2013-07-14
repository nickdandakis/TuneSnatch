import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;


public class TrackList {
	
	private enum FilterKey{
		HypeMachine_id, HypeMachine_key, HypeMachine_artist, HypeMachine_song, HypeMachine_posturl,
		SoundCloud_id, SoundCloud_username, SoundCloud_song, SoundCloud_streamUrl, SoundCloud_waveformUrl,
		Mixcloud_song, Mixcloud_username, Mixcloud_previewUrl, Mixcloud_coverUrl
	}
	private ArrayList<Track> tracks = new ArrayList<Track>();
	private static final boolean DEBUG = false;	// Toggles ability to print out track fields
	
	public TrackList() {
	}
	
	public Track getTrack(int i){
		return tracks.get(i);
	}
	
	public int getSize(){
		return tracks.size();
	}
	
	private String filter(Element unfiltered, FilterKey key){
		switch (key){
		case Mixcloud_song:
			return unfiltered.attr("title");
		case Mixcloud_username:
			return unfiltered.attr("title");
		case Mixcloud_previewUrl:
			return unfiltered.attr("data-preview-url");
		case Mixcloud_coverUrl:
			return "http:" + unfiltered.attr("src");
		default:
			return null;
		}
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
	
	public void addTracks(HTML html) throws IOException{
		if(html.getClass() == HypeMachineHTML.class){
			addHypeMachineTracks((HypeMachineHTML) html);		
		} else if(html.getClass() == SoundCloudHTML.class) {
			addSoundCloudTracks((SoundCloudHTML) html); 
		} else if (html.getClass() == MixcloudHTML.class) {
			addMixcloudTracks((MixcloudHTML) html);
		}
	}
	
	private void addHypeMachineTracks(HypeMachineHTML html) throws IOException{
		Element trackList = html.getDocument().getElementById("displayList-data");
		
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
			
			HypeMachineTrack track = new HypeMachineTrack(id, key, artist, song, posturl, ((HypeMachineHTML) html).getCOOKIES());
			
			if(DEBUG){
				System.out.println(trackString);
				System.out.println(track.toString());
			}
			
			tracks.add(track);
		}
	}
	
	private void addSoundCloudTracks(SoundCloudHTML html) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String htmlstring = html.getDocument().toString();
		Scanner scanner = new Scanner(htmlstring);
		String trackString, username, song, streamUrl, waveformUrl, id;
		
		for(int i = countSubstring("window.SC.bufferTracks.push", htmlstring); i>0; i--){
			trackString = scanner.findWithinHorizon("window.SC.bufferTracks.push\\(\\{[^<]+", 0);
			
			id = filter(trackString, FilterKey.SoundCloud_id);
			song = filter(trackString, FilterKey.SoundCloud_song);
			username = filter(trackString, FilterKey.SoundCloud_username);
			streamUrl = filter(trackString, FilterKey.SoundCloud_streamUrl);
			waveformUrl = filter(trackString, FilterKey.SoundCloud_waveformUrl);
			
			SoundCloudTrack track = new SoundCloudTrack(id, song, username, streamUrl, waveformUrl);
			
			if(DEBUG){
				System.out.println(trackString);
				System.out.println(track.toString());
			}
			
			tracks.add(track);
		} 
	}
	
	private void addMixcloudTracks(MixcloudHTML html) throws IOException{
		Document doc = html.getDocument();
		Elements previewURLs = doc.getElementsByAttribute("data-preview-url");
		Elements songs = doc.select("div.mx-cc-item-image");
		Elements usernames = doc.select("a.mx-cc-item-creator");
		Elements altimages = doc.select("img[alt]");
		Elements covers = new Elements();
				
		for(Element img : altimages){
			for(Element title : songs){
				if(img.attr("alt").equalsIgnoreCase(title.attr("title"))){
					covers.add(img);
					break;
				}
			}
		}
		
		String song, username, previewUrl, coverUrl, id; 
		for(int i=0; i<previewURLs.size(); i++){
			song = filter(songs.get(i), FilterKey.Mixcloud_song);
			username = filter(usernames.get(i), FilterKey.Mixcloud_username); 
			previewUrl = filter(previewURLs.get(i), FilterKey.Mixcloud_previewUrl);
			id = previewUrl.replaceAll(".+\\/.+\\/", "").replaceAll(".mp3", ""); // filename is track ID because Mixcloud doesn't do IDs.
			coverUrl = filter(covers.get(i), FilterKey.Mixcloud_coverUrl);
			MixcloudTrack track = new MixcloudTrack(id, song, username, previewUrl, coverUrl);
			
			if(DEBUG)
				System.out.println(track.toString());
			
			tracks.add(track);
		}
	}
	
	/*
	 * Helper method for SoundCloud tracks filtering
	 */
	private int countSubstring(String subStr, String str){
		return (str.length() - str.replace(subStr, "").length()) / subStr.length();
	}
}
