package com.alan.webmagic.shan;

import java.util.List;

import org.apache.http.cookie.Cookie;

import com.alan.webmagic.httpclient.Login;
import com.alan.webmagic.processor.LinkedinCrawlingProcessor;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;



public class App
{
	public static final String LINKEDIN_USER_NETWORK_PAGE = "https://www.linkedin.com/mynetwork/invite-connect/connections/";
	
	private Spider spider = null;
	public App(List<Cookie> cookies){
		Site site = Site.me()
                .setRetryTimes(10)
                .setSleepTime(200)
                .setTimeOut(5000);
		for(Cookie cookie : cookies) {
			site.addCookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
		}
		
		this.spider = Spider.create(new LinkedinCrawlingProcessor(site)).addUrl(LINKEDIN_USER_NETWORK_PAGE).thread(5);
	}
	public void startCrawler() {
		this.spider.run();
	}
//	public static void main(String[] args) {
//		Login login = new Login();
//		new App(login.login()).startCrawler();
//	}
}
