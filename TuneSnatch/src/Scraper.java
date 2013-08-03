import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

public class Scraper {

	private enum FilterKey{
		HypeMachine_id, HypeMachine_key, HypeMachine_artist, HypeMachine_song, HypeMachine_posturl,
		SoundCloud_id, SoundCloud_username, SoundCloud_song, SoundCloud_streamUrl, SoundCloud_waveformUrl,
		Mixcloud_song, Mixcloud_username, Mixcloud_previewUrl, Mixcloud_coverUrl
	}
	private static final boolean debug = false;	// Toggles ability to print out track fields
	
	private Scraper() {
	}
	
	private static String filter(Element unfiltered, FilterKey key){
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
	
	private static String filter(String unfiltered, FilterKey key){
		Scanner scanner = new Scanner(unfiltered);
		String filtered;
		
		switch (key) {
		case HypeMachine_id:
			filtered = scanner.findInLine("\"id\":\"[a-z0-9]+").replaceAll("\"id\":\"", "");
			break;
		case HypeMachine_key:
			filtered = scanner.findInLine("\"key\":\"[a-z0-9]+").replaceAll("\"key\":\"", "");
			break;
		case HypeMachine_artist:
			filtered = scanner.findInLine("\"artist\":\"[^\"]+").replaceAll("\"artist\":\"", "");
			break;
		case HypeMachine_song:
			filtered = scanner.findInLine("\"song\":\"[^\"]+").replaceAll("\"song\":\"", "");
			break;
		case HypeMachine_posturl:
			filtered = scanner.findInLine("\"posturl\":\".*?\"").replaceAll("\"posturl\":\"", "").replaceAll("\\\\", "").replaceAll("\"", "");
			break;
		case SoundCloud_id:
			filtered = scanner.findInLine("\"id\":[0-9]+").replaceAll("\"id\":", "");
			break;
		case SoundCloud_username:
			filtered = scanner.findInLine("\"username\":\"[^\"]+").replaceAll("\"username\":\"", "");
			break;
		case SoundCloud_song:
			filtered = scanner.findInLine("\"title\":\"[^\"]+").replaceAll("\"title\":\"", "");
			break;
		case SoundCloud_streamUrl:
			filtered = scanner.findInLine("\"streamUrl\":\".*?\"").replaceAll("\"streamUrl\":\"", "").replaceAll("\"", "");
			break;
		case SoundCloud_waveformUrl:
			filtered = scanner.findInLine("\"waveformUrl\":\".*?\"").replaceAll("\"waveformUrl\":\"", "").replaceAll("\"", "");
			break;
		default:
			filtered = null;
			break;
		}
		
		scanner.close();
		return filtered;
	}
	
	public static TrackList scrapeHypeMachineTracks(HypeMachineHTML html) throws IOException{
		TrackList tracks = new TrackList();
		Element trackList = html.getDocument().getElementById("displayList-data");
		
		String[] strs = trackList.toString().split("\\[\\{", 2);
		strs = strs[1].split("}]",2);
		Scanner lineScanner = new Scanner(strs[0]);
		lineScanner.useDelimiter("\\},\\{");
		
		String trackString, id, key, artist, song, posturl;
		
		while (lineScanner.hasNext()){
			trackString = StringEscapeUtils.unescapeJava(lineScanner.next()); // unescape unicode
			
			id = filter(trackString, FilterKey.HypeMachine_id);
			key = filter(trackString, FilterKey.HypeMachine_key);
			artist = filter(trackString, FilterKey.HypeMachine_artist);
			song = filter(trackString, FilterKey.HypeMachine_song);
			posturl = filter(trackString, FilterKey.HypeMachine_posturl);
			
			HypeMachineTrack track = new HypeMachineTrack(id, key, artist, song, posturl, ((HypeMachineHTML) html).getCookies());
			
			if(debug){
				System.out.println(trackString);
				System.out.println(track.toString());
			}
			
			tracks.addTrack(track);
		}
		
		lineScanner.close();
		return tracks;
	}
	
	public static TrackList scrapeSoundCloudTracks(SoundCloudHTML html) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		TrackList tracks = new TrackList();
		String htmlstring = html.getDocument().toString();
		Scanner scanner = new Scanner(htmlstring);
		String trackString, username, song, streamUrl, waveformUrl, id;
		
		for(int i = countSubstring("window.SC.bufferTracks.push", htmlstring); i>0; i--){
			trackString = StringEscapeUtils.unescapeJava(scanner.findWithinHorizon("window.SC.bufferTracks.push\\(\\{[^<]+", 0)); // unescape unicode
			
			id = filter(trackString, FilterKey.SoundCloud_id);
			song = filter(trackString, FilterKey.SoundCloud_song);
			username = filter(trackString, FilterKey.SoundCloud_username);
			streamUrl = filter(trackString, FilterKey.SoundCloud_streamUrl);
			waveformUrl = filter(trackString, FilterKey.SoundCloud_waveformUrl);
			
			SoundCloudTrack track = new SoundCloudTrack(id, song, username, streamUrl, waveformUrl);
			
			if(debug){
				System.out.println(trackString);
				System.out.println(track.toString());
			}
			
			tracks.addTrack(track);
		}
		
		scanner.close();
		return tracks;
	}
	
	public static TrackList scrapeMixcloudTracks(MixcloudHTML html) throws IOException{
		TrackList tracks = new TrackList();
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
			song = StringEscapeUtils.unescapeJava(filter(songs.get(i), FilterKey.Mixcloud_song));
			username = StringEscapeUtils.unescapeJava(filter(usernames.get(i), FilterKey.Mixcloud_username)); 
			previewUrl = filter(previewURLs.get(i), FilterKey.Mixcloud_previewUrl);
			id = previewUrl.replaceAll(".+\\/.+\\/", "").replaceAll(".mp3", ""); // filename is track ID because Mixcloud doesn't do IDs.
			coverUrl = filter(covers.get(i), FilterKey.Mixcloud_coverUrl);
			MixcloudTrack track = new MixcloudTrack(id, song, username, previewUrl, coverUrl);
			
			if(debug)
				System.out.println(track.toString());
			
			tracks.addTrack(track);
		}
		
		return tracks;
	}
	
	/*
	 * Helper method for SoundCloud tracks filtering
	 */
	private static int countSubstring(String subStr, String str){
		return (str.length() - str.replace(subStr, "").length()) / subStr.length();
	}
}
