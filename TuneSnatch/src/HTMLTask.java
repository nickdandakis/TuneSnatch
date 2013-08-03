import java.util.concurrent.Callable;


public class HTMLTask implements Callable<HTML> {

	private Site site;
	private String area;
	private int pagenumber;
	
	public HTMLTask(Site site, String area, int pagenumber) {
		this.site = site;
		this.area = area;
		this.pagenumber = pagenumber;
	}

	@Override
	public HTML call() throws Exception {
		HTML html = null;
		if(site == Site.HypeMachine){
			html = new HypeMachineHTML(area, pagenumber);
		}
		else if(site == Site.SoundCloud){
			html = new SoundCloudHTML(area, pagenumber);
		}
		else if(site == Site.Mixcloud){
			html = new MixcloudHTML(area, pagenumber);
		}
		
		return html;
	}

}
