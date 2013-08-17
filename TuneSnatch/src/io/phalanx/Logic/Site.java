package io.phalanx.Logic;

public enum Site {
	HypeMachine("http://hypem.com/", "HypeMachine"),
	SoundCloud("https://soundcloud.com/", "SoundCloud"),
	Mixcloud("http://mixcloud.com/", "Mixcloud"),
	Invalid("", "");
	
	private final String URL;
	private final String name;
	
	public static Site recognize(String string){
		if(string.equalsIgnoreCase("HypeMachine"))
			return HypeMachine;
		else if(string.equalsIgnoreCase("SoundCloud"))
			return SoundCloud;
		else if(string.equalsIgnoreCase("Mixcloud"))
			return Mixcloud;
		else
			return Invalid;
	}
	
	private Site(String URL, String name){
		this.URL = URL;
		this.name= name;
	}
	
	public String getURL(){
		return URL;
	}
	
	public String toString(){
		return name;
	}
}
