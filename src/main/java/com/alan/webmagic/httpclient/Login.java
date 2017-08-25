package com.alan.webmagic.httpclient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;
public class Login {
private static final Logger log = getLogger(Login.class);
	
	private static final String LOGIN_PAGE = "https://www.linkedin.com";
	
	private static final String LOGIN_POST_URL = "https://www.linkedin.com/uas/login-submit";
	
	private static final String EMAIL = "jianshawn@email.arizona.edu";
	
	private static final String PASSWORD = "asdewq123";
	
	private CloseableHttpClient httpClient = HttpClients.createDefault();
	
	private HttpContext httpContext = new BasicHttpContext();
	
	private HttpClientContext context = HttpClientContext.adapt(httpContext);
	
	public List<Cookie> login() {
		
		try {
			HttpPost httpPost = new HttpPost(LOGIN_POST_URL);
			 
//			httpPost.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");  
//			httpPost.setHeader("accept-encoding","gzip, deflate, br");  
//			httpPost.setHeader("accept-language:en-US","en;q=0.8");  
//			httpPost.setHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
//			
			httpPost.setHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
			httpPost.setHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			httpPost.setHeader("accept-encoding","gzip, deflate, br");
			httpPost.setHeader("accept-language:en-US","en;q=0.8");
			httpPost.setHeader("cache-control","max-age=0");
//			httpPost.setHeader("content-length","138");
			httpPost.setHeader("content-type","application/x-www-form-urlencoded");
			httpPost.setHeader("origin","https://www.linkedin.com");
			httpPost.setHeader("referer","https://www.linkedin.com/");
			httpPost.setHeader("upgrade-insecure-requests","1");
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("isJsEnabled","false"));
			//nameValuePairs.add(new BasicNameValuePair("loginCsrfParam","d2115cf0-88a6-415a-8a0b-27e56fef9e39"));
			nameValuePairs.add(new BasicNameValuePair("loginCsrfParam", getTokenStr()));
			nameValuePairs.add(new BasicNameValuePair("session_key", EMAIL));
			nameValuePairs.add(new BasicNameValuePair("session_password", PASSWORD));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			HttpResponse response = httpClient.execute(httpPost, context);
			int status = response.getStatusLine().getStatusCode();
			
			log.info("Login Github Status is ： "+status);//302
			
			Header[] headers = response.getHeaders("Location");
			
			log.info("Redirect Location : "+headers[0].getValue());
			
			return displayCookies();
			
			//displayCookies();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private List<Cookie> displayCookies() {
		CookieStore cookieStore = context.getCookieStore();
		List<Cookie> cookies = cookieStore.getCookies();
		
		return cookies;
	}
	
	
	
	private String getTokenStr() {
		
		Document document = Jsoup.parse(getLoginPage());
		System.out.println(document.toString());
		String token = document.select("input[name=loginCsrfParam]").first().attr("value");
		log.info("Linkedin login page current authenticity loginCsrfParam is : "+token);
		return token;
	}
	
	
	private String getLoginPage() {
		CloseableHttpResponse response = null;
		String content = null;
		try {
			HttpGet get = new HttpGet(LOGIN_PAGE);
			
		   
//			get.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");  
//			get.setHeader("accept-encoding","gzip, deflate, br");  
//			get.setHeader("accept-language:en-US","en;q=0.8");  
//			get.setHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
//			
			get.setHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
			get.setHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			get.setHeader("accept-encoding","gzip, deflate, br");
			get.setHeader("accept-language:en-US","en;q=0.8");
			get.setHeader("cache-control","max-age=0");
//			get.setHeader("content-length","138");
			get.setHeader("content-type","application/x-www-form-urlencoded");
			get.setHeader("origin","https://www.linkedin.com");
			get.setHeader("referer","https://www.linkedin.com/");
			get.setHeader("upgrade-insecure-requests","1");
			
			response = httpClient.execute(get, context);
			
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);
			
			EntityUtils.consume(entity);//close
			
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			if (response != null) {
				try {
					response.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return content;
	}
	
	public static void main(String[] args) {
		Login login = new Login();
		System.out.println(login.login());
	}
}
