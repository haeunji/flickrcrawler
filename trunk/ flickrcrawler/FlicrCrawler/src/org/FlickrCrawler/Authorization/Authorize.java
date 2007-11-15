package org.FlickrCrawler.Authorization;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.util.IOUtilities;
/*
 * Authorize.java
 *
 * Created on November 3, 2007, 12:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class Authorize {
        static String restHost = "www.flickr.com";
        Auth auth;
	Flickr f;
	REST rest;
	RequestContext requestContext;
        String apiKey = "778b0fbe7fff7b24c9d7e97a11add8f5";
        String secret = "ba20e9e81829dde5";
	String frob = "";
	String token = "";
    
    /** Creates a new instance of Authorize */
    public Authorize() throws ParserConfigurationException {
	rest = new REST();
	rest.setHost(restHost);
	f = new Flickr(apiKey,rest);
	Flickr.debugStream = false;

    }
    
    public Authorize(String MODE){
        
    }
    
    public Flickr getFlickr(){
        return f;
    }
    
    public String getFrob(){
        return frob;
    }
    
    public String getToken(){
        return token;
    }
}
