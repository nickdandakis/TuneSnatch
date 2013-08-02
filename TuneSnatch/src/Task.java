import java.util.concurrent.Callable;


public class Task implements Callable<HTML> {

	private Site site;
	private String area;
	private int pagenumber;
	
	public Task(Site site, String area, int pagenumber) {
		this.site = site;
		this.area = area;
		this.pagenumber = pagenumber;
	}

	@Override
	public HTML call() throws Exception {
		if(site == Site.HypeMachine)
			return new HypeMachineHTML(area, pagenumber);
		else if(site == Site.SoundCloud)
			return new SoundCloudHTML(area, pagenumber);
		else if(site == Site.Mixcloud)
			return new MixcloudHTML(area, pagenumber);
		else
			return null;
	}

}
