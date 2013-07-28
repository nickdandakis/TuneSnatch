
public enum Site {
	HypeMachine, SoundCloud, Mixcloud, Invalid;
	
	static Site recognize(String string){
		if(string.equalsIgnoreCase("HypeMachine"))
			return HypeMachine;
		else if(string.equalsIgnoreCase("SoundCloud"))
			return SoundCloud;
		else if(string.equalsIgnoreCase("Mixcloud"))
			return Mixcloud;
		else
			return Invalid;
	}
}
