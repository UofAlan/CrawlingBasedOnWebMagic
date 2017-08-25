package com.alan.webmagic.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class LinkedinCrawlingProcessor implements PageProcessor {
	
	private Site site;
	
	public LinkedinCrawlingProcessor(Site site){
		this.site = site;
	}
	public Site getSite() {
		// TODO Auto-generated method stub
		return this.site;
	}

	public void process(Page page) {
		
//		System.out.println("run2");
//		page.addTargetRequests(page.getHtml().links().regex("https://www\\.linkedin\\.com/.*").all());
//		page.putField("neenction NUm", page.getHtml().xpath("//span[@id='nav-item__title']/text()").toString());
//		System.out.println("run3");
		//page.addTargetRequests(page.getHtml().links().regex("(https://www\\.linkedin\\.com/in/\\w+)").all());
		page.putField("name", page.getHtml().xpath("//div[@id='mn-connections connections-container Elevation-2dp']/h3/text()"));
		System.out.println("run3");
		page.putField("occupation", page.getHtml().xpath("//h3[@id='pv-top-card-section__location Sans-17px-black-70% mb1 inline-block']/tidyText()").toString());
		
		if (page.getResultItems().get("name")==null){
            //skip this page
			System.out.println("run4");
           // page.setSkip(true);
            }
	}


}
