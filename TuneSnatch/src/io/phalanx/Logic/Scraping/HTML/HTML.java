package io.phalanx.Logic.Scraping.HTML;


import io.phalanx.Logic.Site;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

import org.jsoup.nodes.Document;


public class HTML implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7381759251822881043L;
	private Site site;
	private String area;
	private int pagenumber;
	private String completeURL;
	private Document document;
	
	public HTML(String area, int pagenumber) {
		setArea(area);
		setPagenumber(pagenumber);
	}
	
	public HTML(String completeURL){
		setCompleteURL(completeURL);
		breakdownCompleteURL(this.completeURL);
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public int getPagenumber() {
		return pagenumber;
	}

	public void setPagenumber(int pagenumber) {
		this.pagenumber = pagenumber;
	}

	public String getCompleteURL() {
		return completeURL;
	}

	public void setCompleteURL(String completeURL) {
		this.completeURL = completeURL;
	}

	private void breakdownCompleteURL(String completeURL) {
		if(completeURL.contains(Site.HypeMachine.getURL()))
			setSite(Site.HypeMachine);
		else if(completeURL.contains(Site.SoundCloud.getURL()))
			setSite(Site.SoundCloud);
		else if(completeURL.contains(Site.Mixcloud.getURL()))
			setSite(Site.Mixcloud);
		
		setArea(completeURL.replaceAll(".+.com/", ""));
		
		if(area.endsWith("/"))
			area = area.substring(0, area.length()-1);
	}

	public void saveDocument(Document document){
		File htmlFile = new File("." + File.separator + getArea().replace("/", "-") + "-p" + getPagenumber() + ".html");
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFile.getAbsoluteFile()));
			bw.write(document.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Document getDocument() {
		return document;
	}
	
	@Override
	public String toString(){
		return String.format("Site: %s\nArea: %s\nPagenumber: %d",
								this.site, this.area, this.pagenumber);
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
