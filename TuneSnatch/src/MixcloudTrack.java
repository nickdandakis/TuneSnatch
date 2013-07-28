import java.io.IOException;

import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;


public class MixcloudTrack extends Track {
	
	private String previewURL;
	private String coverURL;

	public MixcloudTrack(String id, String song, String username, String previewURL, String coverURL) {
		super(id, song, username);
		setPreviewURL(previewURL);
		setCoverURL(coverURL);
		
		try {
			this.setStreamURL(generateStreamURL());
		} catch (IOException e) {
			System.out.println("Failed to generate Hypemachine stream URL.");
			e.printStackTrace();
		}
	}

	public String getPreviewURL() {
		return previewURL;
	}

	public void setPreviewURL(String previewURL) {
		this.previewURL = previewURL;
	}

	public String getCoverURL() {
		return coverURL;
	}

	public void setCoverURL(String coverURL) {
		this.coverURL = coverURL;
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
	private String generateStreamURL() throws IOException {
		String downloadUrl = this.getPreviewURL().replaceAll("previews", "cloudcasts/originals");
		
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
	
	@Override
	public String toString(){
		return String.format("Id: %s\nTitle: %s\nArtist: %s\nPreviewURL: %s\nCoverURL: %s", 
								getId(), getSong(), getArtist(), getPreviewURL(), getCoverURL()); 
	}

}
