package com.cisco;
import java.net.URI;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;



public class HttpServer {
	
	private SSLContext sslcontext;
	private SSLConnectionSocketFactory sslsf;
	private  CloseableHttpClient httpclient;
	private String cookie_all;
	
	public boolean open(String sslfile) {
		try {
			ssl(sslfile);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public void close() {
		
		try {
			this.httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public boolean login() throws Exception {
		 BasicCookieStore cookieStore = new BasicCookieStore();
		   
	     httpclient = HttpClients.custom()
	                .setDefaultCookieStore(cookieStore)
	                .setSSLSocketFactory(sslsf)
	                .build();
	        try {
	        	
	            HttpUriRequest login = RequestBuilder.post()
	                    .setUri(new URI("https://10.74.87.60/webacs/j_spring_security_check"))
	                    .addParameter("username", "root")
	                    .addParameter("password", "default1A")
	                    .addParameter("j_username", "root")
	                    .addParameter("j_password","default1A")
	                    .addParameter("spring-security-redirect","https://10.74.87.60/webacs/pages/common/index.jsp")
	                    .addParameter("action","login")
	                    .addParameter("flashVersion","9.0.124.0")
	                    .addParameter("hasCorrectFlashVersion","true")
	                    .build();
	            CloseableHttpResponse response2 = httpclient.execute(login);
	            String Location="";
	            String cookie="";
	            try {
	            	
	            	Header[] headers = response2.getAllHeaders();
	            	
	            	for(int i=0 ; i<headers.length ; i++) {
	            		
	            		Header header= headers[i];
	            		System.out.println("header: name : " + header.getName() + " value: " + header.getValue() );
	            		if(header.getName().compareTo("Location") == 0) 
	            			Location= header.getValue();
	            		if(header.getName().compareTo("Set-Cookie")==0) {
	            			cookie=header.getValue();
	            			String[] cookies = cookie.split(";");
	            			cookie=cookies[0];
	            			System.out.println("cookie:" + cookie);
	            		}
	            	}
	            	System.out.println("status code: " + response2.getStatusLine().getStatusCode());
	            	login.abort();
	            	System.out.println("======================j_security_check end ============================");
	                
	            	
	            	HttpGet httpget = new HttpGet("https://10.74.87.60/webacs/pages/common/index.jsp?&flashVersion=9.0.124.0&hasCorrectFlashVersion=true");
	                
	            	httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	            	httpget.setHeader("Accept-Encoding", "gzip, deflate, sdch");
	            	httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
	            	httpget.setHeader("Cache-Control", "max-age=0");
	            	httpget.setHeader("Connection", "keep-alive");
	            	httpget.setHeader("Host", "10.74.87.60");
	            	httpget.setHeader("Referer", "https://10.74.87.60/webacs/pages/common/login.jsp");
	            	httpget.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
	            	cookie = cookie + ";doNotShowStartupInfoLicense=true; mapsDivSaveStateCookie=90090; dontShowRemoveConfirmDialog=true; ciscoDashboardContainerncsHomePage_selectedChild=dashboardTab4553565; ciscoDashboardContainerdetailDashboardHomePage_selectedChild=dashboardTab2975976; username=root; doNotShowStartupOnLoad=true";
	            	httpget.setHeader("Cookie", cookie);
	            	
	            	response2 = httpclient.execute(httpget);
	       
	                headers = response2.getAllHeaders();
	                
	                for(int i=0 ; i<headers.length ; i++) {
	            		
	            		Header header= headers[i];
	            		System.out.println("header: name : " + header.getName() + " value: " + header.getValue() );
	            		if(header.getName().compareTo("Location") == 0) 
	            			Location= header.getValue();
	            	}
	                System.out.println("status code: " + response2.getStatusLine().getStatusCode());
	            	httpget.abort();
	                System.out.println("======================login end ============================");
	                
	            	
	                httpget = new HttpGet("https://10.74.87.60/webacs/loginAction.do?action=login&flashVersion=9.0.124.0&hasCorrectFlashVersion=true&product=wcs");
	                
	            	httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	            	httpget.setHeader("Accept-Encoding", "gzip, deflate, sdch");
	            	httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4");
	            	httpget.setHeader("Cache-Control", "max-age=0");
	            	httpget.setHeader("Connection", "keep-alive");
	            	httpget.setHeader("Host", "10.74.87.60");
	            	httpget.setHeader("Referer", "https://10.74.87.60/webacs/pages/common/login.jsp");
	            	httpget.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
	            	cookie = cookie + ";doNotShowStartupInfoLicense=true; mapsDivSaveStateCookie=90090; dontShowRemoveConfirmDialog=true; ciscoDashboardContainerncsHomePage_selectedChild=dashboardTab4553565; ciscoDashboardContainerdetailDashboardHomePage_selectedChild=dashboardTab2975976; username=root; doNotShowStartupOnLoad=true";
	            	this.cookie_all = cookie;
	            	httpget.setHeader("Cookie", cookie);
	            	
	            	response2 = httpclient.execute(httpget);
	       
	                headers = response2.getAllHeaders();
	                
	                for(int i=0 ; i<headers.length ; i++) {
	            		
	            		Header header= headers[i];
	            		System.out.println("header: name : " + header.getName() + " value: " + header.getValue() );
	            		if(header.getName().compareTo("Location") == 0) 
	            			Location= header.getValue();
	            	}
	                System.out.println("status code: " + response2.getStatusLine().getStatusCode());
	                httpget.abort();
	                
	                
	            
	            } finally {
	                response2.close();
	            }
	        } finally {
	            //httpclient.close();
	        }
	        
	        return true;
	}
	
	public String getData(String url) {
		
		 BasicCookieStore cookieStore = new BasicCookieStore();
		   
	     httpclient = HttpClients.custom()
	                .setDefaultCookieStore(cookieStore)
	                //.setSSLSocketFactory(sslsf)
	                .build();
	     
		HttpGet httpget = new HttpGet(url);
     	httpget.setHeader("Content-Type", "application/json; charset=utf-8");
     	httpget.setHeader("X-Cloupia-Request-Key", "4F0F24EC40E64C76B6A397ECD0ECBE5E");
     	//httpget.setHeader("Cookie", this.cookie_all);
     	
     	CloseableHttpResponse response2;
		try {
			response2 = httpclient.execute(httpget);
			System.out.println("status code: " + response2.getStatusLine().getStatusCode());
			InputStream inputstream = response2.getEntity().getContent();
			
			ByteArrayOutputStream   baos   =   new   ByteArrayOutputStream(); 
		    int   i=-1; 
		    while((i=inputstream.read())!=-1){ 
		        baos.write(i); 
		        } 
		    System.out.println(baos.toString());   
		    return   baos.toString("utf-8"); 
		       
		
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         httpget.abort();
		return null;
         
		
	}
	
	private void ssl(String sslfile) throws Exception {
		
		KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
        
		FileInputStream instream = new FileInputStream(new File(sslfile));
        try {
            trustStore.load(instream, "123456".toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        sslcontext = SSLContexts.custom()
                .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                .build();
        // Allow TLSv1 protocol only
        sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        //org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
		
	}
}
